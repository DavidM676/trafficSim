import javax.swing.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;

public class TrafficSimulator {
    // change game settings here
    private static final int SCREEN_WIDTH = 950;
    private static final int SCREEN_HEIGHT = 950;
    private static final int CELL_SIZE = 25;
    private ArrayList<Save> saves;
    private Save currentSave;
    private Cell[][] grid;
    private ArrayList<Integer[]> entryPoints;
    private MultiRectangleDrawer drawer;
    private boolean simulationRunning;
    private ArrayList<Car> cars;
    private int targetVolume;
    private int currentVolume;
    private int[] settings;
    private int[] config;

    public TrafficSimulator() {
        saves = new ArrayList<>();
        saves.add(createEmptySave()); // create empty save

        drawer = new MultiRectangleDrawer(this, SCREEN_WIDTH, SCREEN_HEIGHT, CELL_SIZE);
        drawer.start();
    }

    public static Save createEmptySave() {
        return new Save(SCREEN_WIDTH/CELL_SIZE, SCREEN_HEIGHT/CELL_SIZE);
    }

    public ArrayList<Save> getSaves() {
        return saves;
    }

    public Save getSave(int num) {
        return saves.get(num);
    }

    public void addSave(Save save) {
        saves.add(save);
    }

    public void simulate() {
        SwingWorker<Void, String> Worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                simulationRunning = true;
                while (simulationRunning) {
                    System.out.println("Simulation running");
                    currentSave = drawer.getMap();
                    grid = currentSave.getGrid();
                    entryPoints = currentSave.getEntryPoints();
                    cars = currentSave.getCars();
                    targetVolume = (int) (roadCount() * (currentSave.getTrafficVolume() / 100.0));
                    currentVolume = cars.size();
                    settings = currentSave.getSettings();
                    config = settingsToConfig();
                    System.out.println("Road count: " + roadCount());
                    System.out.println("Traffic volume: " + currentSave.getTrafficVolume());
                    System.out.println("Target volume: " + targetVolume);
                    System.out.println("Current volume: " + currentVolume);
                    simulationStep(currentVolume >= targetVolume);
                }
                return null;
            }
        };
        Worker.execute();
    }

    public void stopSimulation() {
        simulationRunning = false;
    }

    public boolean saveExists(String name) {
        for (Save save : saves) {
            if (save.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void simulationStep(boolean targetReached) {
        System.out.println("Simulation step reached");
        Cell[][] gridBefore = currentSave.cloneGrid();
        System.out.println(cars.size());
        for (Car car : cars) {
            System.out.println(car);
            System.out.println("going through sim");
            if (car instanceof Collision) {
                cars.remove(car);
                ((Road) grid[car.getY()][car.getX()]).removeOccupant();
            } else {
                System.out.println("Car is NOT a collision, moving on");
                Move nextMove = car.nextMove(gridBefore);
                System.out.println("Car's next move is: " + nextMove);
                if (nextMove == Move.FORWARD) {
                    System.out.println("Moving Forward");
                    int x = car.getX();
                    int y = car.getY();
                    ((Road) grid[y][x]).removeOccupant();
                    System.out.println("Occupant successfully removed");
                    switch(car.getDirection()) {
                        case NORTH -> x--;
                        case EAST -> y++;
                        case SOUTH -> x++;
                        case WEST -> y--;
                    }
                    System.out.println("Car coords updated");
                    System.out.println("New coordinates: x = " + x + ", y = " + y );
                    if (x < 0 || y < 0 || x >= currentSave.getWidth() || y >= currentSave.getHeight()) { // car made it out successfully
                        System.out.println("Car has escaped");
                        Car slowest = slowestCar();
                        System.out.println("Slowest car is: " + slowest);
                        cars.remove(slowest);
                        ((Road) grid[slowest.getY()][slowest.getX()]).removeOccupant();
                        for (int i = 0; i < 2; i ++) { // add two cars of successful type (or attempt to)
                            // ADD MORE TYPES AS TYPES ARE ADDED
                            Car newCar = null;
                            if (car instanceof BasicDriver) {
                                newCar = new BasicDriver();
                            }
                            addCar(newCar);
                            System.out.println("Added " + newCar);
                        }
                    } else {
                        System.out.println("Car has not escaped");
                        System.out.println(((Road) grid[y][x]));
                        if (((Road) grid[y][x]).isOccupied()) { // if the car has moved into an occupied space, i.e. crashed into another car
                            System.out.println("Car has crashed");
                            cars.remove(car);
                            System.out.println("Removed " + car);
                            Car otherCar = ((Road) grid[y][x]).getOccupant();
                            cars.remove(otherCar);
                            System.out.println("Removed " + otherCar + " from " + (Road) grid[y][x]);
                            ((Road) grid[y][x]).removeOccupant();
                            System.out.println("Removed occupant from " + ((Road) grid[y][x]));
//                            Collision c = new Collision(x, y);
//                            System.out.println("Created " + c);
//                            ((Road) grid[y][x]).setOccupant(c);
//                            System.out.println("Made " + c + " occupant of " + ((Road) grid[y][x]));
//                            cars.add(c);
//                            System.out.println("Added " + c + " to cars list");
                        } else {
                            System.out.println("Setting new occupant");
                            ((Road) grid[y][x]).setOccupant(car);
                            car.setX(x);
                            car.setY(y);
                        }
                    }
                }
            }
        }
        System.out.println(!targetReached);
        if (!targetReached) { // max cars not reached; add more cars based on user pref.
            System.out.println("adding cars");
            int randType = (int) (Math.random() * 100) + 1;
            int total = 0;
            int type = 0;
            for (int i = 0; i < config.length; i++) {
                if (randType >= total && randType <= config[i]) {
                    type = i;
                }
                total += config[i];
            }
            Car newCar = switch (type) { // UPDATE AS MORE TYPES ARE ADDED
                case 0 -> new BasicDriver();
                default -> null;
            };
            addCar(newCar);
        }
        drawer.repaintAll();
    }

    private int roadCount() {
        int count = 0;
        for (Cell[] row : grid) {
            for (Cell c : row) {
                if (c instanceof Road) {
                    count++;
                }
            }
        }
        return count;
    }

    private Car slowestCar() { // returns the car that has been on the map the longest, i.e. the slowest one (to be purged)
        Car slowest = cars.get(0);
        int mostMoves = Integer.MIN_VALUE;
        for (Car car : cars) {
            if (car.getMoveNum() > mostMoves) {
                slowest = car;
                mostMoves = car.getMoveNum();
            }
        }
        return slowest;
    }

    private void addCar(Car c) {
        if (entryPoints.size() > 0) {
            cars.add(c);
            int randIndex = (int) (Math.random() * entryPoints.size());
            Integer[] coords = entryPoints.get(randIndex);
            entryPoints.remove(randIndex);
            ((Road) grid[coords[1]][coords[0]]).setOccupant(c);
            c.setX(coords[0]);
            c.setY(coords[1]);
            c.setDirection(startingPositionToDirection(coords));
        }
    }

    private Orientation startingPositionToDirection(Integer[] coords) {
        if (coords[0] == 0) { // first row
            return Orientation.SOUTH;
        } else if (coords[0] == grid.length) { // last row
            return Orientation.NORTH;
        } else if (coords[1] == 0) { // first column
            return Orientation.EAST;
        }
        return Orientation.WEST; // last column
    }

    private int[] settingsToConfig() {
        currentSave.setTrafficVolume(settings[settings.length - 2]);
        int[] result = new int[settings.length - 1];
        int total = 0;
        for (int i = 0; i < settings.length - 1; i++) { // skips the last element (traffic config)
            total += settings[i];
        }
        double conversionFactor = (double) total / 100;
        for (int j = 0; j < settings.length - 1; j++) {
            result[j] = (int) (settings[j] / conversionFactor);
        }
        return result;
    }
}
