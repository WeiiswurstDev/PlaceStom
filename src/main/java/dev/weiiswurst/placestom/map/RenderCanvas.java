package dev.weiiswurst.placestom.map;

import dev.weiiswurst.placestom.world.PlaceBlocks;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

public class RenderCanvas {

    private static final int OPAQUE_ALPHA = 0xFF000000;
    private static final int SIZE = 64;

    private final Instance instance;

    public RenderCanvas(Instance instance) {
        this.instance = instance;
    }

    public BufferedImage render(int centerX, int centerZ) {
        int topLeftX = centerX - SIZE/2;
        int topLeftZ = centerZ - SIZE/2;
        BufferedImage image = new BufferedImage(SIZE, SIZE, IndexColorModel.TRANSLUCENT);
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                Block block = instance.getBlock(topLeftX + x, 40, topLeftZ + z);
                Integer color = PlaceBlocks.COLOR_MAP.get(block.registry().material());
                if (color != null) {
                    image.setRGB(x,z,color | OPAQUE_ALPHA);
                }
            }
        }
        return image;
    }
}
