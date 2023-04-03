import java.util.ArrayList;
import java.util.Arrays;

public class Car implements Cloneable {
    private DriverType type;
    private int x;
    private int y;
    private Orientation direction;
    private String image;
    private ArrayList<Move> moves;


    public Car(DriverType type, int x, int y, Orientation direction, String image) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.image = image;
        moves = new ArrayList<>();
    }

    public Orientation getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getImage() {
        return image;
    }

    public void setX(int newX) {
        x = newX;
    }

    public void setY(int newY) {
        y = newY;
    }

    public void setDirection(Orientation newDirection) {
        direction = newDirection;
    }

    public int getMoveNum() {
        return moves.size();
    }

    public void addMove(Move newMove) {
        moves.add(newMove);
    }

    public Move turnLeft() {
        direction = Orientation.rotateLeft(direction);
        moves.add(Move.LEFT);
        return Move.LEFT;
    }

    public Move turnRight() {
        direction = Orientation.rotateRight(direction);
        moves.add(Move.RIGHT);
        return Move.RIGHT;
    }

    public Move nextMove(Cell[][] grid) {
        // to be overridden by subclasses
        moves.add(Move.NONE);
        return Move.NONE;
    }

    @Override
    public Car clone() {
        try {
            Car clone = (Car) super.clone();
            clone.type = type;
            clone.x = x;
            clone.y = y;
            clone.direction = direction;
            clone.image = image;
            clone.moves = moves;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return getClass() + " at x = " + x + ", y = " + y + ", facing " + direction + " and having done: " + moves;
    }
}
