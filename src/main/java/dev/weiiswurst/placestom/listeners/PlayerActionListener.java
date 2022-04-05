package dev.weiiswurst.placestom.listeners;

import com.j256.ormlite.dao.Dao;
import dev.weiiswurst.placestom.commands.CooldownCommand;
import dev.weiiswurst.placestom.world.PlaceBlocks;
import dev.weiiswurst.placestom.util.ProtectedLocations;
import dev.weiiswurst.placestom.world.ChunkData;
import dev.weiiswurst.placestom.util.PlayerActionCoolDown;
import dev.weiiswurst.placestom.world.PlayerPlacementLog;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;

import java.sql.SQLException;

public interface PlayerActionListener {

    default void tryPlaceBlock(Player player, Point location, PlayerActionCoolDown coolDown, Block block,
                               Dao<ChunkData, Integer> chunkDao, Dao<PlayerPlacementLog, Long> playerPlacementLogs) {
        if (ProtectedLocations.isProtected(location)) {
            player.sendActionBar(Component.text("Protected location.").color(TextColor.color(255,50,50)));
            return;
        }
        if (!player.hasPermission(CooldownCommand.NO_COOLDOWN_PERMISSION) && !coolDown.performAction(player.getUuid())) {
            player.sendActionBar(Component.text("You are on cooldown.").color(TextColor.color(255,50,50)));
            return;
        }
        coolDown.applyAnimation(player);

        try {
            ChunkData chunkData = chunkDao.queryForId(ChunkData.toDatabaseIndex(location.chunkX(), location.chunkZ()));
            chunkData.setBlockAt(ChunkData.worldCoordsToLocalIndex(location),
                    (byte) PlaceBlocks.ALLOWED_BLOCKS.indexOf(block.registry().material())
                    );
            chunkDao.update(chunkData);
            PlayerPlacementLog logEntry = new PlayerPlacementLog(location.blockX(), location.blockZ(), player);
            playerPlacementLogs.createOrUpdate(logEntry);
            player.getInstance().setBlock(location, block);
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("Error while updating the database. Your edit was not saved.");
        }
    }

}
