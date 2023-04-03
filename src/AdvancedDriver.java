public class AdvancedDriver extends Car {
    public AdvancedDriver() {
        super(DriverType.AdvancedDriver, -1, -1, null, "src/advancedDriver.png");
    }

    @Override
    public Move nextMove(Cell[][] grid) {
        System.out.println("Calculating next move...");
        Road r = (Road) grid[getY()][getX()];
        if (r.canMoveForward(getDirection())) { // && clearAhead(grid)) {
            addMove(Move.FORWARD);
            System.out.println("Moving forward!");
            return Move.FORWARD;
        } else if (r.canTurnLeft(getDirection())) {
            System.out.println("Turning left");
            return turnLeft();
        } else if (r.canTurnRight(getDirection())) {
            System.out.println("Turning right");
            return turnRight();
        }
        System.out.println("No moves");
        addMove(Move.NONE);
        return Move.NONE;
    }

    private boolean clearAhead(Cell[][] grid) {
        Cell cellAhead = switch (getDirection()) {
            case NORTH -> grid[getY() + 1][getX()];
            case EAST -> grid[getY()][getX() + 1];
            case SOUTH -> grid[getY() - 1][getX()];
            case WEST -> grid[getY()][getX() - 1];
        };
        return !((Road) cellAhead).isOccupied();
    }
}
