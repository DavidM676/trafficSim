import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class MultiRectangleDrawer extends JPanel {

    private Element[][] map;
    private JFrame frame;

    private int screenWidth;
    private int screenHeight;
    private int cellSize;





    public MultiRectangleDrawer(Element[][] map, int screenWidth, int screenHeight, int cellSize) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                getCell(e.getX(), e.getY());

            }

            }
        );

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
//                System.out.println(e.getX()+" "+e.getY());
                getCell(e.getX(), e.getY());
            }
        });



        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.cellSize = cellSize;

        setPreferredSize(new Dimension(screenWidth, screenHeight));

        this.map = map;
        frame = new JFrame("Multi-Rectangle Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    private void getCell(int x, int y) {
        int newX = (int)((((double)x)/screenWidth)*(screenWidth/cellSize));
        int newY = (int)((((double)y)/screenHeight)*(screenHeight/cellSize));
        map[newX][newY] = new Road("up");
        repaint();
        JFrame newWindow = new JFrame();
        newWindow.setSize(800, 1089);
        newWindow.setLocation(150, 50);
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon("src\\arrow.png"));
        newWindow.add(label);
        newWindow.setVisible(true);

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
                g.setColor(new Color(0x0000ff));
            }
        }
    }



}
