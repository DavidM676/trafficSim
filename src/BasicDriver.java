public class BasicDriver extends Car {
    public BasicDriver() {
        super(DriverType.BasicDriver, -1, -1, null, "src/basicDriver.png");
    }

    @Override
    public Move nextMove(Cell[][] grid) {
        Road r = (Road) grid[getY()][getX()];
        if (r.canMoveForward(getDirection())) {
            addMove(Move.FORWARD);
            return Move.FORWARD;
        } else if (r.canTurnLeft(getDirection())) {
            return turnLeft();
        } else {
            return turnRight();
        }
    }
}
