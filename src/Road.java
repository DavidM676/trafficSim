import java.util.ArrayList;
import java.util.Arrays;

public class Road extends Cell {
    private final static String[] IMAGES = {"src/road.png", "src/leftTurn.png", "src/rightTurn.png", "src/bothTurns.png", "src/leftTurnOnly.png", "src/rightTurnOnly.png", "src/bothTurnsOnly.png"};
    private int type;
    private Orientation direction;
    private boolean canMoveForward;
    private boolean canTurnLeft;
    private boolean canTurnRight;
    private boolean occupied;
    private Car occupant;

    public Road(Orientation direction) {
        super("src/road.png");
        type = 0;
        this.direction = direction;
        canMoveForward = true;
        canTurnLeft = false;
        canTurnRight = false;
    }

    public int getType() {
        return type;
    }

    public Orientation getDirection() {
        return direction;
    }

    public boolean canMoveForward() {
        return canMoveForward;
    }

    public boolean canTurnLeft() {
        return canTurnLeft;
    }

    public boolean canTurnRight() {
        return canTurnRight;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Car getOccupant() {
        return occupant;
    }

    public void setType(int newType) {
        type = newType;
    }

    public void setDirection(Orientation newDirection) {
        direction = newDirection;
    }

    public void setCanMoveForward(boolean newCanMoveForward) {
        canMoveForward = newCanMoveForward;
    }

    public void setCanTurnLeft(boolean newCanTurnLeft) {
        canTurnLeft = newCanTurnLeft;
    }

    public void setCanTurnRight(boolean newCanTurnRight) {
        canTurnRight = newCanTurnRight;
    }

    public void setOccupant(Car newOccupant) {
        occupant = newOccupant;
        occupied = true;
    }

    public void removeOccupant() {
        occupant = null;
        occupied = false;
    }

    public void changeType() {
        type++;
        if (type >= IMAGES.length) {
            type = 0;
        }
        setImage(IMAGES[type]);

        if (type < 4) { // first 4 types, moveForward is always true
            canMoveForward = true;
            canTurnRight = type > 2;
            canTurnLeft = type % 2 == 1;
        } else { // last 3 types, moveForward is always false
            canMoveForward = false;
            canTurnRight = type > 4;
            canTurnLeft = type % 2 == 0;
        }
    }

    @Override
    public Road clone() {
        Road clone = (Road) super.clone();
        // TODO: copy mutable state here, so the clone can't change the internals of the original
        clone.type = type;
        clone.direction = direction;
        clone.canMoveForward = canMoveForward;
        clone.canTurnLeft = canTurnLeft;
        clone.canTurnRight = canTurnRight;
        clone.occupied = occupied;
        if (occupied) {
            clone.occupant = occupant.clone();
        } else {
            clone.occupant = null;
        }
        return clone;
    }
}
