package dev.weiiswurst.placestom.world;

import com.j256.ormlite.dao.Dao;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public record PlaceLoader(
        Dao<ChunkData, Integer> chunkDao) implements Generator {

    public static final DimensionType PLACE_DIMENSION = DimensionType.builder(NamespaceID.from("placestom:world"))
            .ultrawarm(false)
            .natural(false)
            .piglinSafe(false)
            .respawnAnchorSafe(false)
            .bedSafe(true)
            .raidCapable(true)
            .skylightEnabled(true)
            .ceilingEnabled(false)
            .fixedTime(null)
            // We do a little lighting (fix #3)
            .ambientLight(2.0f)
            .height(384)
            .minY(-64)
            .logicalHeight(384)
            .build();


    @Override
    public void generate(@NotNull GenerationUnit unit) {
        unit.subdivide().forEach(subUnit -> {
            if (subUnit.absoluteStart().blockY() != 32) {
                // Only generate blocks in one subchunk
                return;
            }
            final int chunkX = subUnit.absoluteStart().chunkX();
            final int chunkZ = subUnit.absoluteStart().chunkZ();
            setChunkData(subUnit.modifier(), chunkX, chunkZ);
            if (chunkX == 0 && chunkZ == 0) {
                subUnit.modifier().setBlock(0, 40, 0, PlaceBlocks.SPAWN_BLOCK);
            }
        });
    }

    private void setChunkData(Block.Setter blockSetter, int chunkX, int chunkZ) {
        ChunkData chunkData = loadData(chunkX, chunkZ);
        if (chunkData == null) {
            MinecraftServer.LOGGER.error("The chunk at x={} | z={} could not be loaded.", chunkX, chunkZ);
            return;
        }
        for (byte x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
            for (byte z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                byte type = chunkData.getBlockAt(x, z);
                blockSetter.setBlock(x, 40, z, PlaceBlocks.ALLOWED_BLOCKS.get(type).block());
                blockSetter.setBlock(x, 39, z, Block.WHITE_CONCRETE);
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
}
