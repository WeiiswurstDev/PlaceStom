package dev.weiiswurst.placestom.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

public class SpawnCommand extends Command {

    private static final Pos spawnPosition = new Pos(0.5, 41.1, 0.5);

    public SpawnCommand() {
        super("spawn");
        setCondition(Conditions::playerOnly);
        setDefaultExecutor((sender, context) -> ((Player)sender).teleport(spawnPosition));
    }

}
