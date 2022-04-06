package dev.weiiswurst.placestom.commands;

import net.minestom.server.command.builder.Command;

public class VersionCommand extends Command {

    // Get replaced by blossom at build time
    private static final String NAME = "&implementationName";
    private static final String VERSION = "&version";

    public VersionCommand() {
        super("version", "ver", "placestom");
        setDefaultExecutor((sender, context) -> sender.sendMessage(String.format("The server is running %s %s", NAME, VERSION)));
    }

}
