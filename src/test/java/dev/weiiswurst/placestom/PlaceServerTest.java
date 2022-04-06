package dev.weiiswurst.placestom;

import com.j256.ormlite.dao.DaoManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.extras.MojangAuth;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class PlaceServerTest {

    @Test
    public void runMainMethod() {
        assertDoesNotThrow(() -> PlaceServer.main(new String[]{}));
        assertTrue(MinecraftServer.isStarted());
        assertTrue(MojangAuth.isEnabled());
        // Make sure that the correct server.properties file is loaded, with the right flags!
        assertFalse(Boolean.getBoolean("placestom.check-for-updates"));

        // Untested: Database connection, world generation start

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        assertTrue(globalEventHandler.hasListener(PlayerLoginEvent.class));
        assertTrue(globalEventHandler.hasListener(PlayerBlockPlaceEvent.class));
        assertTrue(globalEventHandler.hasListener(PlayerStartDiggingEvent.class));
        assertTrue(globalEventHandler.hasListener(ServerListPingEvent.class));
        assertTrue(globalEventHandler.hasListener(ItemDropEvent.class));

        CommandManager commandManager = MinecraftServer.getCommandManager();
        assertTrue(commandManager.commandExists("blame"));
        assertTrue(commandManager.commandExists("clearchunk"));
        assertTrue(commandManager.commandExists("cooldown"));
        assertTrue(commandManager.commandExists("spawn"));
        assertTrue(commandManager.commandExists("stop"));
        assertTrue(commandManager.commandExists("teleport"));
        assertTrue(commandManager.commandExists("version"));
        assertTrue(commandManager.commandExists("updateservericon"));

        // Will add further tests once the internal Minestom testing api becomes public
    }

}
