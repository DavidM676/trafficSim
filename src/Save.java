import java.lang.reflect.Array;
import java.util.ArrayList;

public class Save {
    private static int ID = 1;
    private String name;
    private int width;
    private int height;
    private Cell[][] grid;
    private int trafficVolume;
    private ArrayList<Car> cars;
    private int[] settings;

    // clone old save, but rename it. used for "Save" menu button
    public Save(Save oldSave, String newName) {
        name = newName;
        width = oldSave.width;
        height = oldSave.height;
        grid = oldSave.cloneGrid();
        trafficVolume = oldSave.trafficVolume;
        System.out.println("Creating " + this + " with traffic volume " + trafficVolume);
        cars = new ArrayList<>();
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
        settings = new int[oldSave.settings.length];
        for (int i = 0; i < oldSave.settings.length; i++) {
            settings[i] = oldSave.settings[i];
        }
        ID++;
    }

    public Save(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        grid = new Cell[width][height];
        trafficVolume = 50;
        cars = new ArrayList<>();
        settings = new int[DriverType.values().length + 1];
        ID++;
    }

    public Save(int width, int height) {
        this.name = "Save " + ID; // ensures a unique name for every save
        this.width = width;
        this.height = height;
        grid = new Cell[width][height];
        trafficVolume = 50;
        System.out.println("Creating " + this + " with traffic volume " + trafficVolume);
        cars = new ArrayList<>();
        settings = new int[DriverType.values().length + 1];
        for (int i = 0; i < settings.length - 1; i++) {
            settings[i] = 100;
        }
        settings[settings.length - 1] = trafficVolume;
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

    public int getTrafficVolume() {
        System.out.println("Returning " + trafficVolume);
        return trafficVolume;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public int[] getSettings() {
        return settings;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setSettings(int[] newSettings) {
        settings = newSettings;
    }

    public void setTrafficVolume(int newTrafficVolume) {
        trafficVolume = newTrafficVolume;
    }

    public Cell[][] cloneGrid() {
        Cell[][] newGrid = new Cell[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newGrid[i][j] = grid[i][j].clone();
            }
        }
        return newGrid;
    }

    public ArrayList<Integer[]> getEntryPoints() { // returns coordinates of points of entry from outside the map in x y order (column-row)
        ArrayList<Integer[]> result = new ArrayList<>();
        for (int i = 0; i < width; i++) { // traverse top row
            if (grid[0][i] instanceof Road) {
                Road r = (Road) grid[0][i];
                if (r.getType() == 0 && r.getDirection().equals(Orientation.EAST)) {
                    result.add(new Integer[] {i, 0});
                }
            }
        }
        for (int i = 0; i < width; i++) { // traverse bottom row
            if (grid[height - 1][i] instanceof Road) {
                Road r = (Road) grid[height - 1][i];
                if (r.getType() == 0 && r.getDirection().equals(Orientation.WEST)) {
                    result.add(new Integer[] {i, height - 1});
                }
            }
        }
        for (int i = 1; i < height - 1; i++) { // traverse first column
            if (grid[i][0] instanceof Road) {
                Road r = (Road) grid[i][0];
                if (r.getType() == 0 && r.getDirection().equals(Orientation.SOUTH)) {
                    result.add(new Integer[] {0, i});
                }
            }
        }
        for (int i = 1; i < height - 1; i++) { // traverse last column
            if (grid[i][width - 1] instanceof Road) {
                Road r = (Road) grid[i][width - 1];
                if (r.getType() == 0 && r.getDirection().equals(Orientation.NORTH)) {
                    result.add(new Integer[] {0, width - 1});
                }
            }
        }
        return result;
    }
}
