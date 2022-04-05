package dev.weiiswurst.placestom.util;

import net.minestom.server.coordinate.Point;

public class ProtectedLocations {

    private ProtectedLocations() {
    }

    public static boolean isProtected(Point point) {
        return point.blockY() != 40 || point.blockX() == 0 && point.blockZ() == 0;
    }
}
