package dev.weiiswurst.placestom.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

public class TeleportCommand extends Command {

    private static final int MAX_VALUE = Integer.getInteger("placestom.worldborder-size");

    public TeleportCommand() {
        super("teleport", "tp");
        ArgumentNumber<Integer> x = ArgumentType.Integer("x").max(MAX_VALUE).min(-MAX_VALUE);
        ArgumentNumber<Integer> z = ArgumentType.Integer("z").max(MAX_VALUE).min(-MAX_VALUE);
        setCondition(Conditions::playerOnly);
        CommandExecutor commandExecutor = (sender, context) ->
                ((Player)sender).teleport(new Pos(context.get("x"), 40.1, context.get("z")));
        addSyntax(commandExecutor, x, z);
    }
}
