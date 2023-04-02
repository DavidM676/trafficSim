import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuListener;
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


    private JMenuBar mb;

    private JButton start;



    public MultiRectangleDrawer(Element[][] map, int screenWidth, int screenHeight, int cellSize) {
        mb = new JMenuBar();
        JMenu fileTab= new JMenu("File");

        // file menu--------------------------------------
        JMenuItem save = new JMenuItem("save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fileName = saveWindow();
                System.out.println("FILENAME: "+fileName);
            }
        });

        JMenuItem load = new JMenuItem("load map");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String option = loadWindow();;
            }
        });

        JMenuItem quit = new JMenuItem("quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        fileTab.add(save);
        fileTab.add(load);
        fileTab.add(quit);
        //--------------------------------------

        JMenu configTab = new JMenu("Configure");
        //config tab -----------------------------------
        JMenuItem settings = new JMenuItem("settings");
        settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                settingsWindow();
            }
        });

        configTab.add(settings);
        //--------------------------------------


        start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (start.getText().equals("Start")) {
                    start.setText("Stop");
                    for (int i = 0; i<map.length; i++) {
                        for (int j = 0; j<map[i].length; j++) {
                            if (map[i][j] instanceof Road) {
                                System.out.println("yes");
                                repaint(i * cellSize, j * cellSize, cellSize, cellSize);
                            }
                        }
                    }

                    // code to start the task
                } else {
                    start.setText("Start");

                    // code to stop the task
                }
            }
        });


        mb.add(fileTab);
        mb.add(configTab);
        mb.add(start);



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
        frame.setJMenuBar(mb);


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





    public String loadWindow() {
        String[] options = {"Option 1", "Option 2", "Option 3"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        int result = JOptionPane.showConfirmDialog(null, comboBox, "Select an option", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String selectedOption = comboBox.getSelectedItem().toString();
            System.out.println("Selected option: " + selectedOption);
            return selectedOption;
        }
        return null;
    }

    public String saveWindow() {
        String input = JOptionPane.showInputDialog("Enter File Name:");
        return input;
    }

    public int[] settingsWindow() {
        JSlider slider1 = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        JSlider slider2 = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        JSlider slider3 = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);

        JPanel panel = new JPanel(new GridLayout(3,2));
        panel.add(new JLabel("Slider 1:"));
        panel.add(slider1);
        panel.add(new JLabel("Slider 2:"));
        panel.add(slider2);
        panel.add(new JLabel("Slider 3:"));
        panel.add(slider3);

        int result = JOptionPane.showConfirmDialog(null, panel, "Sliders", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int value1 = slider1.getValue();
            int value2 = slider2.getValue();
            int value3 = slider3.getValue();
            System.out.println("Slider 1 value: " + value1);
            System.out.println("Slider 2 value: " + value2);
            System.out.println("Slider 3 value: " + value3);
            int[] vals = {value1, value2, value3};
            return vals;
        }
        int[] def = new int[3];
        return def;
    }
    public void drawImage(Graphics g, int i, int j) {
        try {
            g.drawImage(ImageIO.read(new File("src/" + map[i][j].getImage())), i * cellSize, j * cellSize, cellSize, cellSize, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(start.getText());
        if (map[i][j] instanceof Road && start.getText().equals("Start")) {
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
