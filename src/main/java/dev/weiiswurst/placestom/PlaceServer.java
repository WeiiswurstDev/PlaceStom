package dev.weiiswurst.placestom;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.weiiswurst.placestom.commands.BlameCommand;
import dev.weiiswurst.placestom.commands.ClearChunkCommand;
import dev.weiiswurst.placestom.commands.CooldownCommand;
import dev.weiiswurst.placestom.commands.SpawnCommand;
import dev.weiiswurst.placestom.commands.StopCommand;
import dev.weiiswurst.placestom.commands.TeleportCommand;
import dev.weiiswurst.placestom.commands.VersionCommand;
import dev.weiiswurst.placestom.listeners.MotdListener;
import dev.weiiswurst.placestom.listeners.PlayerBreakBlockListener;
import dev.weiiswurst.placestom.listeners.PlayerJoinListener;
import dev.weiiswurst.placestom.listeners.PlayerPlaceBlockListener;
import dev.weiiswurst.placestom.util.PropertyLoader;
import dev.weiiswurst.placestom.util.UpdateChecker;
import dev.weiiswurst.placestom.world.ChunkData;
import dev.weiiswurst.placestom.world.PlaceLoader;
import dev.weiiswurst.placestom.util.PlayerActionCoolDown;
import dev.weiiswurst.placestom.world.PlayerPlacementLog;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.*;
import net.minestom.server.timer.TaskSchedule;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class PlaceServer {

    public static void main(String[] args) throws SQLException, IOException, URISyntaxException {
        // Loading properties
        PropertyLoader.loadProperties();

        // Check for updates
        if (Boolean.getBoolean("placestom.check-for-updates")) {
            UpdateChecker.checkForUpdates();
        }

        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();
        MojangAuth.init();

        // Database connection
        ConnectionSource connectionSource = new JdbcConnectionSource(
                System.getProperty("placestom.database-url", "jdbc:h2:./database"));
        Dao<ChunkData, Integer> chunkDao = DaoManager.createDao(connectionSource, ChunkData.class);
        // Enable caching, because we don't support multiple servers on one database anyways
        chunkDao.setObjectCache(true);
        TableUtils.createTableIfNotExists(connectionSource, ChunkData.class);

        Dao<PlayerPlacementLog, Long> playerDao = DaoManager.createDao(connectionSource, PlayerPlacementLog.class);
        playerDao.setObjectCache(true);
        TableUtils.createTableIfNotExists(connectionSource, PlayerPlacementLog.class);

        // Create the instance (=world)
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        // Setup of the instance
        instanceContainer.setChunkGenerator(new PlaceLoader(chunkDao));
        instanceContainer.getWorldBorder().setCenter(0, 0);
        instanceContainer.getWorldBorder().setDiameter(Integer.getInteger("placestom.worldborder-size", 500));
        instanceContainer.setTime(6000);

        // Instantiate the action cooldown
        PlayerActionCoolDown cooldown = new PlayerActionCoolDown();

        MinecraftServer.getSchedulerManager().scheduleTask(
                () -> instanceContainer.getPlayers().forEach(cooldown::applyAnimation),
                TaskSchedule.tick(1), TaskSchedule.tick(1));

        // Register commands
        registerCommands(cooldown, chunkDao, playerDao);

        // Register event listeners
        registerListeners(instanceContainer, chunkDao, playerDao, cooldown);

        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", Integer.getInteger("placestom.port", 25565));
    }

    private static void registerCommands(PlayerActionCoolDown cooldown, Dao<ChunkData, Integer> chunkDao, Dao<PlayerPlacementLog, Long> playerDao) {
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new StopCommand());
        commandManager.register(new VersionCommand());
        commandManager.register(new CooldownCommand(cooldown));
        commandManager.register(new SpawnCommand());
        commandManager.register(new TeleportCommand());
        commandManager.register(new ClearChunkCommand(chunkDao));
        commandManager.register(new BlameCommand(playerDao));
    }

    private static void registerListeners(InstanceContainer instanceContainer, Dao<ChunkData, Integer> chunkDao,
                                          Dao<PlayerPlacementLog, Long> playerDao, PlayerActionCoolDown cooldown) {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, new PlayerJoinListener(instanceContainer));
        globalEventHandler.addListener(PlayerBlockPlaceEvent.class, new PlayerPlaceBlockListener(cooldown, chunkDao, playerDao));
        globalEventHandler.addListener(PlayerStartDiggingEvent.class, new PlayerBreakBlockListener(cooldown, chunkDao, playerDao));
        globalEventHandler.addListener(ServerListPingEvent.class, new MotdListener());
        globalEventHandler.addListener(ItemDropEvent.class, event -> event.setCancelled(true));
    }

}
