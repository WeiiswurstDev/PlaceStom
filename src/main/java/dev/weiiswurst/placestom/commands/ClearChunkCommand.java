package dev.weiiswurst.placestom.commands;

import com.j256.ormlite.dao.Dao;
import dev.weiiswurst.placestom.world.ChunkData;
import dev.weiiswurst.placestom.world.PlaceBlocks;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Chunk;

import java.sql.SQLException;

public class ClearChunkCommand extends Command {

    public ClearChunkCommand(Dao<ChunkData, Integer> chunkDao) {
        super("clearchunk");
        setCondition((sender, commandString) -> sender instanceof Player
                && sender.hasPermission("placestom.clearchunk"));
        setDefaultExecutor((sender, context) -> {
            Player player = (Player) sender;
            Pos position = player.getPosition();
            try {
                ChunkData chunkData = chunkDao.queryForId(ChunkData.toDatabaseIndex(position.chunkX(), position.chunkZ()));
                chunkData.clear();
                chunkDao.update(chunkData);
                Chunk chunk = player.getChunk();
                for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
                    for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                        if (z == 0 && x == 0 && chunk.getChunkX() == 0 && chunk.getChunkZ() == 0) continue;
                        player.getInstance().setBlock(x + chunk.getChunkX() * 16, 40, z + chunk.getChunkZ() * 16, PlaceBlocks.DEFAULT_BLOCK);
                    }
                }
            } catch(SQLException e) {
                sender.sendMessage("Error while clearing the chunk.");
            }

        });
    }

}
