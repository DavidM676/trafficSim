import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class MultiRectangleDrawer extends JPanel {

    private Element[][] map;
    private JFrame frame;

    private int screenWidth;
    private int screenHeight;
    private int cellSize;


    public MultiRectangleDrawer(Element[][] map, int screenWidth, int screenHeight, int cellSize) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.cellSize = cellSize;

        setPreferredSize(new Dimension(screenWidth, screenHeight));

        this.map = map;
        frame = new JFrame("Multi-Rectangle Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void start() {
        MultiRectangleDrawer drawer = new MultiRectangleDrawer(map, screenWidth, screenHeight, cellSize);
        drawer.addRectangles();
        frame.add(drawer);
        frame.pack();
        frame.setVisible(true);
    }

    public void addRectangles() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i<map.length; i++) {
            for (int j = 0; j<map[i].length; j++) {
                g.setColor(new Color(map[i][j].getColor()));
                g.fillRect(i*cellSize, j*cellSize, cellSize, cellSize);
            }
        }
    }


}
