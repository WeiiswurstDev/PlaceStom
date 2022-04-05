package dev.weiiswurst.placestom.listeners;

import com.j256.ormlite.dao.Dao;
import dev.weiiswurst.placestom.world.PlaceBlocks;
import dev.weiiswurst.placestom.world.ChunkData;
import dev.weiiswurst.placestom.util.PlayerActionCoolDown;
import dev.weiiswurst.placestom.world.PlayerPlacementLog;
import net.minestom.server.event.player.PlayerStartDiggingEvent;

import java.util.function.Consumer;

public record PlayerBreakBlockListener(PlayerActionCoolDown coolDown, Dao<ChunkData, Integer> chunkDao,
                                       Dao<PlayerPlacementLog, Long> playerDao) implements Consumer<PlayerStartDiggingEvent>,
        PlayerActionListener {

    @Override
    public void accept(PlayerStartDiggingEvent event) {
        if (event.getPlayer().isCreative()) {
            // Ignore creative players (admins)
            return;
        }
        event.setCancelled(true);
        tryPlaceBlock(event.getPlayer(), event.getBlockPosition(), coolDown, PlaceBlocks.DEFAULT_BLOCK, chunkDao, playerDao);
    }
}
