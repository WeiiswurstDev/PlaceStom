package dev.weiiswurst.placestom.world;

import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.minestom.server.item.Material.*;

public class PlaceBlocks {

    public static final Block DEFAULT_BLOCK = Block.fromNamespaceId(System.getProperty("placestom.blocks.default", "barrier"));
    public static final Block SPAWN_BLOCK = Block.fromNamespaceId(System.getProperty("placestom.blocks.spawn", "bedrock"));

    public static final List<Material> ALLOWED_BLOCKS;

    static {
        List<Material> configMaterials = Arrays.stream(System.getProperty("placestom.blocks.allowed-blocks", "").split(","))
                .map(Material::fromNamespaceId).collect(Collectors.toCollection(ArrayList::new));

        if (configMaterials.isEmpty() || configMaterials.size() >= Byte.MAX_VALUE) {
            ALLOWED_BLOCKS = List.of(
                    DEFAULT_BLOCK.registry().material(),
                    WHITE_CONCRETE,
                    RED_CONCRETE,
                    ORANGE_CONCRETE,
                    YELLOW_CONCRETE,
                    GREEN_CONCRETE,
                    LIME_CONCRETE,
                    LIGHT_BLUE_CONCRETE,
                    BLUE_CONCRETE,
                    MAGENTA_CONCRETE,
                    PINK_CONCRETE,
                    BLACK_CONCRETE,
                    GRAY_CONCRETE,
                    LIGHT_GRAY_CONCRETE
            );
        } else {
            configMaterials.add(0, DEFAULT_BLOCK.registry().material());
            ALLOWED_BLOCKS = Collections.unmodifiableList(configMaterials);
        }
    }

    private PlaceBlocks() {
    }

}
