public class Intersection extends Road {
    private final static String[] IMAGES = {"src/intersection.png",
            "src/intersectionPrimaryTurn.png",
            "src/intersectionSecondaryTurn.png",
            "src/intersectionBothTurns.png",
            "src/intersectionRightTurnOnly.png",
            "src/intersectionLeftTurnOnly.png"};
    private Orientation direction;
    private boolean canMoveForward;
    private boolean canTurnLeft;
    private boolean canTurnRight;

    public Intersection(Orientation direction1) {
        super(direction1, IMAGES[0]);
        direction = Orientation.rotateLeft(direction1);
        canMoveForward = true;
        canTurnLeft = false;
        canTurnRight = false;
    }

    @Override
    public boolean canMoveForward(Orientation startingDirection) {
        if (startingDirection == getDirection()) {
            return super.canMoveForward(startingDirection);
        } else { // assumes startingDirection is either super.direction or this.direction
            if (startingDirection == direction) {
                return canMoveForward;
            } else if (startingDirection == Orientation.rotateLeft(direction)) {
                return canTurnLeft;
            } else if (startingDirection == Orientation.rotateRight(direction)) {
                return canTurnRight;
            }
            return false;
        }
    }

    @Override
    public boolean canTurnLeft(Orientation startingDirection) {
        if (startingDirection == getDirection()) {
            return super.canTurnLeft(startingDirection);
        } else { // assumes startingDirection is either super.direction or this.direction
            if (startingDirection == direction) {
                return canTurnLeft;
            } else if (startingDirection == Orientation.rotateLeft(direction)) {
                return false;
            } else if (startingDirection == Orientation.rotateRight(direction)) {
                return canMoveForward;
            }
            return canTurnRight;
        }
    }

    @Override
    public boolean canTurnRight(Orientation startingDirection) {
        if (startingDirection == getDirection()) {
            return super.canTurnRight(startingDirection);
        } else { // assumes startingDirection is either super.direction or this.direction
            if (startingDirection == direction) {
                return canTurnRight;
            } else if (startingDirection == Orientation.rotateLeft(direction)) {
                return canMoveForward;
            } else if (startingDirection == Orientation.rotateRight(direction)) {
                return false;
            }
            return canTurnLeft;
        }
    }

    @Override
    public void changeType() {
        int type = getType() + 1;
        setType(type);
        if (type >= IMAGES.length) {
            type = 0;
            setType(0);
        }
        setArrowImage(IMAGES[type]);

        setCanMoveForward(type < 5);
        canMoveForward = type != 4;
        // right turn from primary always false
        canTurnRight = type >= 2 && type <= 4;
        setCanTurnLeft(type % 2 == 1);
        // left turn from secondary always false
    }

    @Override
    public Intersection clone() {
        Intersection clone = (Intersection) super.clone();
        clone.direction = direction;
        clone.canMoveForward = canMoveForward;
        clone.canTurnLeft = canTurnLeft;
        clone.canTurnRight = canTurnRight;
        return clone;
    }
}
