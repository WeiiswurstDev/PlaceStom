package dev.weiiswurst.placestom.listeners;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.ResponseData;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class MotdListener implements Consumer<ServerListPingEvent> {

    private static final Logger LOGGER = Logger.getLogger("ServerIcon");

    private final String serverIcon;

    public MotdListener() {
        String iconBase64;
        try {
            BufferedImage image = ImageIO.read(new File("./server-icon.png"));
            ByteArrayOutputStream iconOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", iconOutputStream);
            iconBase64 = Base64.getEncoder().encodeToString(iconOutputStream.toByteArray());
        } catch (IOException e) {
            LOGGER.info("No server icon found. Place a server icon at ./server-icon.png (dimensions: 64x64)");
            iconBase64 = "";
        }
        this.serverIcon = "data:image/png;base64," + iconBase64;
    }

    @Override
    public void accept(ServerListPingEvent event) {
        ResponseData response = event.getResponseData();
        response.setDescription(Component.text("Minecraft Place"));
        response.setFavicon(serverIcon);
        event.setResponseData(response);
    }
}
