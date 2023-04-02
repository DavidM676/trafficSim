public class Intersection extends Road {
    private final static String[] IMAGES = {"src/intersection.png",
            "src/intersectionPrimaryTurn.png",
            "src/intersectionSecondaryTurn.png",
            "src/intersectionBothTurns.png",
            "src/intersectionRightTurnOnly",
            "src/intersectionLeftTurnOnly"};
    private Orientation direction;
    private boolean canMoveForward;
    private boolean canTurnLeft;
    private boolean canTurnRight;

    public Intersection(Orientation direction1) {
        super(direction1);
        direction = Orientation.rotateLeft(direction1);
        canMoveForward = true;
        canTurnLeft = false;
        canTurnRight = false;
    }

    public boolean canMoveForward(Orientation startingDirection) {
        if (startingDirection == getDirection()) {
            return canMoveForward();
        } else { // assumes startingDirection is either super.direction or this.direction
            return canMoveForward;
        }
    }

    public boolean canTurnLeft(Orientation startingDirection) {
        if (startingDirection == getDirection()) {
            return canTurnLeft();
        } else { // assumes startingDirection is either super.direction or this.direction
            return canTurnLeft;
        }
    }

    public boolean canTurnRight(Orientation startingDirection) {
        if (startingDirection == getDirection()) {
            return canTurnRight();
        } else { // assumes startingDirection is either super.direction or this.direction
            return canTurnRight;
        }
    }

    @Override
    public void changeType() {
        int type = getType() + 1;
        setType(type);
        if (type >= IMAGES.length) {
            type = 0;
        }
        setImage(IMAGES[type]);

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
        // TODO: copy mutable state here, so the clone can't change the internals of the original
        clone.direction = direction;
        clone.canMoveForward = canMoveForward;
        clone.canTurnLeft = canTurnLeft;
        clone.canTurnRight = canTurnRight;
        return clone;
    }
}
