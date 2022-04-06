package dev.weiiswurst.placestom.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class UpdateChecker {

    // Gets replaced by blossom at build time
    private static final String CURRENT_VERSION = "&version";
    private static final String UPDATE_URI = "&updateUri";

    private static final Logger LOGGER = LoggerFactory.getLogger("Update Checker");

    private UpdateChecker() {
    }

    @ApiStatus.Internal
    public static void checkForUpdates() {
        LOGGER.info("Checking for updates...");
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET().timeout(Duration.ofSeconds(10)).version(HttpClient.Version.HTTP_2)
                    .uri(new URI(UPDATE_URI)).build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(JsonParser::parseString)
                    .thenAccept(jsonElement -> {
                        JsonObject response = jsonElement.getAsJsonObject();
                        if (response.has("name")) {
                            String newVersion = response.get("name").getAsString();
                            if (CURRENT_VERSION.equals(newVersion)) {
                                LOGGER.info("You are up to date!");
                            } else {
                                LOGGER.info("There is a new version available. \nYou are on {} and should update to {}!",
                                        CURRENT_VERSION, newVersion);
                            }
                        } else if (response.has("message")) {
                            LOGGER.error("Unable to get the newest version. Message: {}", response.get("message").getAsString());
                        }
                    });
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to get the newest version. Message: {}", e.getMessage());
        }

    }


}
