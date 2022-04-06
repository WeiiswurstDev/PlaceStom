package dev.weiiswurst.placestom.commands;

import dev.weiiswurst.placestom.map.RenderCanvas;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

public class UpdateServerIconCommand extends Command {

    public UpdateServerIconCommand() {
        super("generateservericon", "updateservericon");
        setCondition((sender, commandString) -> sender instanceof Player &&
                sender.hasPermission("placestom.servericon"));
        setDefaultExecutor((sender, context) -> {
            Player player = (Player) sender;
            player.sendMessage("Starting to render..");
            RenderCanvas renderer = new RenderCanvas(player.getInstance());
            BufferedImage image = renderer.render(player.getPosition().blockX(), player.getPosition().blockZ());
            player.sendMessage("Writing to server-icon.png..");
            try (FileOutputStream outputStream = new FileOutputStream("server-icon.png")){
                ImageIO.write(image, "png", outputStream);
                player.sendMessage("The new server icon was written and will be reloaded on restart.");
            } catch (IOException e) {
                player.sendMessage("Failed to write the file.");
            }
        });
    }
}
