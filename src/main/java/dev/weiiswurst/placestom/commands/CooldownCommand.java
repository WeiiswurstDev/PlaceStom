package dev.weiiswurst.placestom.commands;

import dev.weiiswurst.placestom.util.PlayerActionCoolDown;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.permission.Permission;

public class CooldownCommand extends Command {

    public static final Permission NO_COOLDOWN_PERMISSION = new Permission("placestom.noCooldown");
    public static final Permission SET_COOLDOWN_PERMISSION = new Permission("placestom.setCooldown");

    private final PlayerActionCoolDown coolDown;

    public CooldownCommand(PlayerActionCoolDown coolDown) {
        super("cooldown");
        this.coolDown = coolDown;
        // setCondition((sender, commandString) -> sender instanceof Player);
        /*setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player player) {
                player.sendMessage("Remaining: "+coolDown.getCoolDownTime(player.getUuid()) + "ms");
            }
        });*/
        addSubcommand(new NoCooldown());
        addSubcommand(new SetCooldownOnce());
    }

    private static class NoCooldown extends Command {
        public NoCooldown() {
            super("remove");
            setCondition((sender, commandString) -> sender.hasPermission(SET_COOLDOWN_PERMISSION));
            final ArgumentEntity targetArgument = ArgumentType.Entity("target").onlyPlayers(true);
            CommandExecutor executor = (sender, context) ->
                    context.get(targetArgument).find(sender).stream()
                            // The argument only accepts players, so we can safely convert to players here
                            .map(entity -> (Player) entity)
                            .forEach(player -> player.addPermission(NO_COOLDOWN_PERMISSION));
            addSyntax(executor, targetArgument);
        }
    }

    private class SetCooldownOnce extends Command {
        public SetCooldownOnce() {
            super("setonce");
            setCondition((sender, commandString) -> sender.hasPermission(SET_COOLDOWN_PERMISSION));
            final ArgumentEntity targetArgument = ArgumentType.Entity("target").onlyPlayers(true);
            final ArgumentInteger cooldownArgument = ArgumentType.Integer("cooldown");
            CommandExecutor executor = (sender, context) ->
                context.get(targetArgument).find(sender).stream()
                        .map(Entity::getUuid)
                        .forEach(uuid -> coolDown.setCooldownMillis(uuid, context.get(cooldownArgument) * 1000));

            addSyntax(executor, targetArgument, cooldownArgument);
        }
    }
}
