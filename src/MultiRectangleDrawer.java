import javax.swing.JFrame;
import javax.swing.JPanel;
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
//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                mousePt = e.getPoint();
//
//            }
//
//            }
//        );

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
        System.out.println((double)(x/screenWidth)*(screenWidth/cellSize));
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
    public void mouseClicked(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        System.out.println(x+","+y);//these co-ords are relative to the component
    }


}
