import java.util.ArrayList;
import java.util.Arrays;

public class Road extends Cell {
    private final static String[] IMAGES = {"src/arrow.png", "src/leftTurn.png", "src/rightTurn.png", "src/bothTurns.png", "src/leftTurnOnly.png", "src/rightTurnOnly.png", "src/bothTurnsOnly.png"};
    private int type;
    private String arrowImage;
    private Orientation direction;
    private boolean canMoveForward;
    private boolean canTurnLeft;
    private boolean canTurnRight;
    private boolean occupied;
    private Car occupant;
    private boolean modified;

    public Road(Orientation direction) {
        super("src/road.png");
        type = 0;
        arrowImage = IMAGES[0];
        this.direction = direction;
        canMoveForward = true;
        canTurnLeft = false;
        canTurnRight = false;
    }

    public Road(Orientation direction, String arrowImage) {
        super("src/road.png");
        type = 0;
        this.arrowImage = arrowImage;
        this.direction = direction;
        canMoveForward = true;
        canTurnLeft = false;
        canTurnRight = false;
    }

    public int getType() {
        return type;
    }

    public String getArrowImage() {
        return arrowImage;
    }

    public Orientation getDirection() {
        return direction;
    }

    public boolean canMoveForward(Orientation startingDirection) {
        if (startingDirection == direction) {
            return canMoveForward;
        } else if (startingDirection == Orientation.rotateLeft(direction)) {
            return canTurnLeft;
        } else if (startingDirection == Orientation.rotateRight(direction)) {
            return canTurnRight;
        }
        return false;
    }

    public boolean canTurnLeft(Orientation startingDirection) {
        if (startingDirection == direction) {
            return canTurnLeft;
        } else if (startingDirection == Orientation.rotateLeft(direction)) {
            return false;
        } else if (startingDirection == Orientation.rotateRight(direction)) {
            return canMoveForward;
        }
        return canTurnRight;
    }

    public boolean canTurnRight(Orientation startingDirection) {
        if (startingDirection == direction) {
            return canTurnRight;
        } else if (startingDirection == Orientation.rotateLeft(direction)) {
            return canMoveForward;
        } else if (startingDirection == Orientation.rotateRight(direction)) {
            return false;
        }
        return canTurnLeft;
    }

    public boolean isOccupied() {
        System.out.println("Returning that I am occupied? " + occupied);
        return occupied;
    }

    public Car getOccupant() {
        System.out.println("Returning car " + occupant);
        return occupant;
    }

    public void setType(int newType) {
        type = newType;
    }

    public void setArrowImage(String newArrowImage) {
        arrowImage = newArrowImage;
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
        arrowImage = IMAGES[type];

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
        clone.type = type;
        clone.arrowImage = arrowImage;
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

    @Override
    public String toString() {
        return getClass() + " with occupant " + occupant;
    }
}
