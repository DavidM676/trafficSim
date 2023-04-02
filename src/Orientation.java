public enum Orientation {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public static Orientation rotateLeft(Orientation o) {
        return switch (o) {
            case NORTH -> WEST;
            case EAST -> NORTH;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
        };
    }

    public static Orientation rotateRight(Orientation o) {
        return switch (o) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public static int toDegrees(Orientation o) {
        return switch (o) {
            case NORTH -> 0;
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
        };
    }
}
