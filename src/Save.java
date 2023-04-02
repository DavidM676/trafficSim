import java.util.ArrayList;

public class Save {
    private int width;
    private int height;
    private Cell[][] grid;
    private int trafficVolume;
    private ArrayList<Car> cars;

    public Save(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Cell[width][height];
        trafficVolume = 0;
        cars = new ArrayList<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell[][] getGrid() {
        return grid;
    }
}
