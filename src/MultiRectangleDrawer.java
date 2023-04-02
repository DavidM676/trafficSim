import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public class MultiRectangleDrawer extends JPanel {



    private Element[][] map;
    private JFrame frame;

    private int screenWidth;
    private int screenHeight;
    private int cellSize;

    private Point changed;





    public MultiRectangleDrawer(Element[][] map, int screenWidth, int screenHeight, int cellSize) {



        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // Left mouse button was clicked
                    changeCell(e.getX(), e.getY(), true);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    changeAngle(e.getX(), e.getY());
                    // Right mouse button was clicked
                    // do something else
                }
            }

            }
        );

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
//                System.out.println(e.getX()+" "+e.getY());

                changeCell(e.getX(), e.getY(), false);
            }
        });

        changed = null;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.cellSize = cellSize;

        setPreferredSize(new Dimension(screenWidth, screenHeight));

        this.map = map;
        frame = new JFrame("Multi-Rectangle Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private Point getCell(int x, int y) {
        int newX = (int)((((double)x)/screenWidth)*(screenWidth/cellSize));
        int newY = (int)((((double)y)/screenHeight)*(screenHeight/cellSize));
        return new Point(newX, newY);
    }
    private void changeCell(int x, int y, boolean clickAgain) {

        Point newPnt = getCell(x, y);
        if ((!(newPnt.equals(changed))) || clickAgain) {

            if (map[newPnt.x][newPnt.y] instanceof Grass) {
                int angle = 0;
                if (changed!=null) {
                    if (newPnt.x < changed.x) {
                        angle = 270;
                    } else if (newPnt.x>changed.x) {
                        angle = 90;
                    } else if (newPnt.y>changed.y) {
                        angle=180;
                    }
                }
                map[newPnt.x][newPnt.y] = new Road(angle);
            } else if (map[newPnt.x][newPnt.y] instanceof Road) {
                map[newPnt.x][newPnt.y] = new Grass();
            }
            changed = new Point(newPnt.x, newPnt.y);
            repaint(newPnt.x * cellSize, newPnt.y * cellSize, cellSize, cellSize);
        }
    }

    private void changeAngle(int x, int y) {
        Point changePnt = getCell(x, y);
        if (map[changePnt.x][changePnt.y] instanceof Road) {
            Road r = (Road) map[changePnt.x][changePnt.y];
            r.setAngle(r.getAngle()+90);
            changed = new Point(changePnt.x, changePnt.y);
            repaint(changePnt.x * cellSize, changePnt.y * cellSize, cellSize, cellSize);
        }
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


    public void drawImage(Graphics g, int i, int j) {
        try {
            g.drawImage(ImageIO.read(new File("src/" + map[i][j].getImage())), i * cellSize, j * cellSize, cellSize, cellSize, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        if (map[i][j] instanceof Road) {
            try {
                BufferedImage image = ImageIO.read(new File("src/arrow.png"));
                BufferedImage rotatedImage = new BufferedImage(cellSize, cellSize, image.getType());
                Graphics2D g2d = rotatedImage.createGraphics();
                g2d.rotate(Math.toRadians(((Road) map[i][j]).getAngle()), cellSize/2, cellSize/2);
                g2d.drawImage(image, 0, 0, cellSize, cellSize, null);
                g2d.dispose();
                g.drawImage(rotatedImage, i*cellSize, j*cellSize, null);
//                g.drawImage(ImageIO.read(new File("src/arrow.png")), i * cellSize, j * cellSize, cellSize, cellSize, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (changed == null) {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    //                g.setColor(new Color(map[i][j].getColor()));
                    //                g.fillRect(i*cellSize, j*cellSize, cellSize, cellSize);

                    drawImage(g, i, j);
                }
            }
        } else {
            int i = changed.x;
            int j = changed.y;
            drawImage(g, i, j);
        }
    }



}
