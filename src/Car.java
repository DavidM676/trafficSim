public class Car implements Cloneable {
    private int x;
    private int y;

    private Orientation direction;

    private String image;


    public Car(int x, int y, Orientation direction, String image) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.image = image;
    }

    public void move() {
        switch (direction) {
            case NORTH -> y++;
            case EAST -> x++;
            case SOUTH -> y--;
            case WEST -> x--;
        }
    }

    public void turnLeft() {
        direction = Orientation.rotateLeft(direction);
    }

    public void turnRight() {
        direction = Orientation.rotateRight(direction);
    }

    @Override
    public Car clone() {
        try {
            Car clone = (Car) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            clone.x = x;
            clone.y = y;
            clone.direction = direction;
            clone.image = image;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
