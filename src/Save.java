import java.util.ArrayList;

public class Save {
    private static int ID = 1;
    private String name;
    private int width;
    private int height;
    private Cell[][] grid;
    private int trafficVolume;
    private ArrayList<Car> cars;

    // clone old save, but rename it. used for "Save" menu button
    public Save(Save oldSave, String newName) {
        name = newName;
        width = oldSave.width;
        height = oldSave.height;
        grid = new Cell[width][height];
        for (int i = 0; i < oldSave.getHeight(); i++) {
            for (int j = 0; j < oldSave.getWidth(); j++) {
                grid[i][j] = oldSave.getGrid()[i][j].clone();
                System.out.println(grid[i][j].getClass() + " at x = " + j + ", y = " + i);
            }
        }
        trafficVolume = oldSave.trafficVolume;
        for (Cell[] row : grid) {
            for (Cell c : row) {
                if (c instanceof Road) {
                    Road r = (Road) c;
                    if (r.isOccupied()) {
                        cars.add(r.getOccupant());
                    }
                }
            }
        }
        ID++;
    }

    public Save(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        grid = new Cell[width][height];
        trafficVolume = 0;
        cars = new ArrayList<>();
        ID++;
    }

    public Save(int width, int height) {
        this.name = "Save " + ID; // ensures a unique name for every save
        this.width = width;
        this.height = height;
        grid = new Cell[width][height];
        trafficVolume = 0;
        cars = new ArrayList<>();
        ID++;
    }

    public String getName() {
        return name;
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

    public void setName(String newName) {
        name = newName;
    }
}
