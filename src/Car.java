public class Car {
    private int x;
    private int y;

    private Orientation direction;

    private int speed;

    private String image;


    public Car(int x, int y, Orientation direction, int speed, String image) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
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
}
