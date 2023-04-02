import java.util.ArrayList;

public class TrafficSimulator {
    // change game settings here
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;
    private static final int CELL_SIZE = 25;
    private ArrayList<Save> saves;
    private MultiRectangleDrawer drawer;

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

    public boolean saveExists(String name) {
        for (Save save : saves) {
            if (save.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
