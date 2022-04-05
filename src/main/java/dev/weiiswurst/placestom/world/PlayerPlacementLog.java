package dev.weiiswurst.placestom.world;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;

@DatabaseTable(tableName = "placement-log")
public class PlayerPlacementLog {

    @DatabaseField(id = true, canBeNull = false, index = true)
    private long blockId;

    @DatabaseField(index = true, canBeNull = false)
    private UUID player;

    @DatabaseField(canBeNull = false)
    private String playerName;

    @ApiStatus.Internal
    public PlayerPlacementLog() {

    }

    public PlayerPlacementLog(int x, int z, Player player) {
        this.blockId = toDatabaseId(x,z);
        this.player = player.getUuid();
        this.playerName = player.getUsername();
    }

    public long getBlockId() {
        return blockId;
    }

    public UUID getPlayer() {
        return player;
    }

    public String getPlayerName() {
        return playerName;
    }

    public static long toDatabaseId(int x, int z) {
        return (((long)x) << 32) | (z & 0xffffffffL);
    }
}
