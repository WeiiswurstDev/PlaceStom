package dev.weiiswurst.placestom.world;

import com.j256.ormlite.dao.Dao;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

public record PlaceLoader(
        Dao<ChunkData, Integer> chunkDao) implements ChunkGenerator {

    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
        ChunkData chunkData = loadData(chunkX, chunkZ);
        for (byte x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
            for (byte z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                if (chunkX == 0 && chunkZ == 0 && x == 0 && z == 0) {
                    batch.setBlock(x, 40, z, PlaceBlocks.SPAWN_BLOCK);
                } else {
                    byte type = chunkData.getBlockAt(x, z);
                    batch.setBlock(x, 40, z, PlaceBlocks.ALLOWED_BLOCKS.get(type).block());
                }
                batch.setBlock(x, 39, z, Block.WHITE_CONCRETE);
            }
        }
    }


    private ChunkData loadData(int chunkX, int chunkZ) {
        try {
            ChunkData chunkData = chunkDao.queryForId(ChunkData.toDatabaseIndex(chunkX, chunkZ));
            if (chunkData == null) {
                chunkData = new ChunkData(chunkX, chunkZ);
                chunkDao.createIfNotExists(chunkData);
            }
            return chunkData;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public @Nullable
    List<ChunkPopulator> getPopulators() {
        return null;
    }
}
