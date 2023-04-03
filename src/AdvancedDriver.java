public class AdvancedDriver extends Car {
    public AdvancedDriver() {
        super(DriverType.AdvancedDriver, -1, -1, null, "src/advancedDriver.png");
    }

    @Override
    public Move nextMove(Cell[][] grid) {
        Road r = (Road) grid[getY()][getX()];
        if (r.canMoveForward(getDirection()) && r.) {
            addMove(Move.FORWARD);
            return Move.FORWARD;
        } else if (r.canTurnLeft(getDirection())) {
            return turnLeft();
        } else {
            return turnRight();
        }
    }

    private boolean clearAhead(Cell[][] grid) {
        Cell cellAhead = switch (getDirection()) {
            case NORTH -> grid[getY() + 1][getX()];
            case EAST -> grid[getY()][getX() + 1];
            case SOUTH -> grid[getY() - 1][getX()];
            case WEST -> grid[getY()][getX() - 1];
        };
        return false;
    }
}
