package dev.weiiswurst.placestom.util;

import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;

public class PropertyLoader {

    private PropertyLoader() {
    }

    @ApiStatus.Internal
    public static void loadProperties() throws IOException, URISyntaxException {
        File propertiesFile = new File("./server.properties");
        if (!propertiesFile.exists()) {
            URI defaultPropertiesUrl = PropertyLoader.class.getClassLoader().getResource("server.properties").toURI();
            fixPathFromJar(defaultPropertiesUrl);
            Files.copy(Paths.get(defaultPropertiesUrl), new FileOutputStream(propertiesFile));
        }
        try (FileReader reader = new FileReader(propertiesFile)) {
            System.getProperties().load(reader);
        }
    }

    private static void fixPathFromJar(URI uri) throws IOException {
        // this function is a hack to enable reading modules from within a JAR file
        // see https://stackoverflow.com/a/48298758
        if ("jar".equals(uri.getScheme())) {
            for (FileSystemProvider provider: FileSystemProvider.installedProviders()) {
                if (provider.getScheme().equalsIgnoreCase("jar")) {
                    try {
                        provider.getFileSystem(uri);
                    } catch (FileSystemNotFoundException e) {
                        // in this case we need to initialize it first:
                        provider.newFileSystem(uri, Collections.emptyMap());
                    }
                }
            }
        }
    }

}
