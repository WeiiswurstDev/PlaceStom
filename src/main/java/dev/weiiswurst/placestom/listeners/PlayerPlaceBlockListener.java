package dev.weiiswurst.placestom.listeners;

import com.j256.ormlite.dao.Dao;
import dev.weiiswurst.placestom.world.ChunkData;
import dev.weiiswurst.placestom.util.PlayerActionCoolDown;
import dev.weiiswurst.placestom.world.PlayerPlacementLog;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;

import java.util.function.Consumer;

public record PlayerPlaceBlockListener(PlayerActionCoolDown coolDown, Dao<ChunkData, Integer> chunkDao,
                                       Dao<PlayerPlacementLog, Long> playerDao)
        implements Consumer<PlayerBlockPlaceEvent>,
        PlayerActionListener{

    @Override
    public void accept(PlayerBlockPlaceEvent event) {
        if (event.getPlayer().isCreative()) {
            // Ignore creative players (admins)
            return;
        }
        event.setCancelled(true);
        tryPlaceBlock(event.getPlayer(), event.getBlockPosition().sub(0,1,0), coolDown, event.getBlock(), chunkDao, playerDao);
    }
}
