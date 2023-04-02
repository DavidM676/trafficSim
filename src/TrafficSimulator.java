public class TrafficSimulator {
    private MultiRectangleDrawer drawer;

    public TrafficSimulator() {
        int screenWidth  = 1000;
        int screenHeight = 1000;
        int cellSize     = 25;

        drawer = new MultiRectangleDrawer(screenWidth, screenHeight, cellSize);

        drawer.start();
    }
}
