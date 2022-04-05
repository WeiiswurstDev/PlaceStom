package dev.weiiswurst.placestom.world;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import net.minestom.server.coordinate.Point;

@DatabaseTable(tableName = "chunkdata")
public class ChunkData {

    private static final int CHUNK_X_FACTOR = 0xFFFF;

    @DatabaseField(index = true, canBeNull = false, id = true)
    private int chunkCoords;

    @DatabaseField(canBeNull = false, dataType=DataType.BYTE_ARRAY, columnName = "chunkData")
    private byte[] serializedBlocks;

    private final int chunkX;
    private final int chunkZ;

    // Constructor required by ORMLite
    public ChunkData() {
        this(0,0);
    }

    public ChunkData(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        clear();
        chunkCoords = toDatabaseIndex(chunkX, chunkZ);
    }

    public void clear() {
        serializedBlocks = new byte[256];
    }

    public byte getBlockAt(byte x, byte z) {
        return serializedBlocks[x*16+z];
    }

    public void setBlockAt(int index, byte block) {
        serializedBlocks[index] = block;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public static int toDatabaseIndex(int chunkX, int chunkZ) {
        return chunkX * CHUNK_X_FACTOR + chunkZ;
    }

    public static int worldCoordsToLocalIndex(Point location) {
        int localX = location.blockX() % 16;
        if (localX < 0) localX += 16;
        int localZ = location.blockZ() % 16;
        if (localZ < 0) localZ += 16;
        return localX * 16 + localZ;
    }
}
