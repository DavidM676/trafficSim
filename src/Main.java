
public class Main {
    public static void main(String[] args) {
        int screenWidth  = 1000;
        int screenHeight = 1000;
        int cellSize     = 25;



        Element[][] map = new Element[screenWidth/cellSize][screenHeight/cellSize];

        //fill with grass
        for(int i = 0; i<map.length;i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = new Grass();
            }
        }

        //draw simple road
        for(int i = 0; i<map.length;i++) {

                map[i][16] = new Road("up");
        }

        MultiRectangleDrawer mr = new MultiRectangleDrawer(map, screenWidth, screenHeight, cellSize);

        mr.start();

    }
}
