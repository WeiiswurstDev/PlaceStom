package dev.weiiswurst.placestom.commands;

import com.j256.ormlite.dao.Dao;
import dev.weiiswurst.placestom.world.PlayerPlacementLog;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

import java.sql.SQLException;

public class BlameCommand extends Command {

    public BlameCommand(Dao<PlayerPlacementLog, Long> playerDao) {
        super("blame", "whodidthis");
        setCondition(Conditions::playerOnly);
        setDefaultExecutor((sender, context) -> {
            Player player = (Player) sender;
            Pos position = player.getPosition();
            try {
                PlayerPlacementLog log = playerDao.queryForId(PlayerPlacementLog.toDatabaseId(position.blockX(), position.blockZ()));
                if (log == null) {
                    player.sendMessage("This block was not placed by a player.");
                } else {
                    player.sendMessage("You can blame "+log.getPlayerName()+" for the block you are standing on.");
                }
            } catch (SQLException e) {
                player.sendMessage("Error while looking up who is to blame.");
            }
        });
    }

}
