import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class MultiRectangleDrawer extends JPanel {
    private TrafficSimulator sim;
    private Save map;
    private JFrame frame;
    private int screenWidth;
    private int screenHeight;
    private int cellSize;
    private Point changed;

    private JMenuBar mb;
    private JButton start;

    private JLabel loadingLabel;
    private JMenuItem settings;

    private boolean startButtonPressed;
    private boolean mouse1Down;

    private Image backgroundImage;

    public MultiRectangleDrawer(TrafficSimulator sim, int screenWidth, int screenHeight, int cellSize) {
        this.sim = sim;
        startButtonPressed = false;
        mb = new JMenuBar();
        JMenu fileTab= new JMenu("File");

        // file menu--------------------------------------
        JMenuItem save = new JMenuItem("save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newName = nameWindow();
                if (newName != null) {
                    map.setName(newName);
                    frame.setTitle(map.getName());
                }
            }
        });

        JMenuItem saveAs = new JMenuItem("save as...");
        saveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fileName = nameWindow();
                if (fileName != null) {
                    if (!sim.saveExists(fileName)) { // if the user intends to make a new save off the current one ("save as"): saving using the original name would in theory save to the same file but saving doesn't do anything
                        map = new Save(map, fileName);
                        frame.setTitle(map.getName());
                        sim.addSave(map);
                    }
                }
            }
        });

        JMenuItem load = new JMenuItem("load");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Save toBeLoaded = loadWindow();
                if (toBeLoaded != null) {
                    map = toBeLoaded;
                    frame.setTitle(map.getName());
                    repaintAll();
                }
            }
        });

        JMenuItem quit = new JMenuItem("quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        fileTab.add(save);
        fileTab.add(saveAs);
        fileTab.add(load);
        fileTab.add(quit);
        //--------------------------------------

        JMenu configTab = new JMenu("Configure");
        //config tab -----------------------------------
        settings = new JMenuItem("settings");
        settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (settings.getText().equals("settings")) {//you can also use run and !run boolean
                    map.setSettings(settingsWindow());
                }
//                } else if (settings.getText().equals("stats")) {
//                    statsWindow();
//                }
            }
        });

        configTab.add(settings);
        //--------------------------------------

        start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButtonPressed = !startButtonPressed;
                if (start.getText().equals("Start")) {
                    start.setText("Stop");
//                    settings.setText("stats");
                    changed=null;
                    repaint();
                    sim.simulate(); // start sim
                } else {
                    sim.stopSimulation(); // stop sim
                    start.setText("Start");
                    settings.setText("settings");
                    changed=null;
                    repaint();
                }
            }
        });

        mb.add(fileTab);
        mb.add(configTab);
        mb.add(start);

        //MOUSE EVENTS -------------------------------
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!startButtonPressed) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        // Left mouse button was clicked -> change the cell (swap between grass, road and intersection)
                        mouse1Down = true;
                        changeCell(e.getX(), e.getY(), true);
                    } else if (e.getButton() == MouseEvent.BUTTON2) {
                        // Middle mouse button was clicked -> change turning abilities (swap between no turn, left turn, right turn, and both turns, or for an intersection, no turns, turn from path 1, turn from path 2, or turns from both)
                        changeRules(e.getX(), e.getY());
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        changeAngle(e.getX(), e.getY());
                        // Right mouse button was clicked
                        // do something else
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!startButtonPressed && e.getButton() == MouseEvent.BUTTON1) {
                    mouse1Down = false;
                }
            }

            }
        );

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!startButtonPressed && mouse1Down)
                    changeCell(e.getX(), e.getY(), false);
            }
        });

        // --------------------------------------------------------

        changed = null;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.cellSize = cellSize;

        map = sim.getSave(0); // get first save

        setPreferredSize(new Dimension(screenWidth, screenHeight));

        //fill with grass
        for(int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                map.getGrid()[i][j] = new Grass();
            }
        }
        try {
            backgroundImage = ImageIO.read(new File("src/wallpaper.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        frame = new JFrame(map.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setJMenuBar(mb);
    }

    public Save getMap() {
        return map;
    }

    public void repaintRoad() {
        for (int i = 0; i<map.getHeight(); i++) {
            for (int j = 0; j<map.getWidth(); j++) {
                if (map.getGrid()[i][j] instanceof Road) {
                    changed = new Point(i, j);
                    paintImmediately(i * cellSize, j * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    private void repaintAll() {
        for (int i = 0; i<map.getHeight(); i++) {
            for (int j = 0; j<map.getWidth(); j++) {
                changed = new Point(i, j);
                paintImmediately(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }
    }

    private Point getCell(int x, int y) {
        int newX = (int)((((double)x)/screenWidth)*(screenWidth/cellSize));
        int newY = (int)((((double)y)/screenHeight)*(screenHeight/cellSize));
        return new Point(newX, newY);
    }

    private void changeCell(int x, int y, boolean clickAgain) {
        Point newPnt = getCell(x, y);
        if ((!(newPnt.equals(changed))) || clickAgain) {
            if (map.getGrid()[newPnt.x][newPnt.y] instanceof Grass) { // grass -> road
                Orientation angle = Orientation.NORTH;
                if (changed!=null) {
                    if (newPnt.x < changed.x) {
                        angle = Orientation.WEST;
                    } else if (newPnt.x>changed.x) {
                        angle = Orientation.EAST;
                    } else if (newPnt.y>changed.y) {
                        angle = Orientation.SOUTH;
                    }
                }
                map.getGrid()[newPnt.x][newPnt.y] = new Road(angle);
            } else if (map.getGrid()[newPnt.x][newPnt.y] instanceof Road) {
                if (map.getGrid()[newPnt.x][newPnt.y] instanceof Intersection) { // intersection -> grass
                    map.getGrid()[newPnt.x][newPnt.y] = new Grass();
                } else { // road -> intersection
                    Orientation angle = Orientation.NORTH;
                    if (changed!=null) {
                        if (newPnt.x < changed.x) {
                            angle = Orientation.WEST;
                        } else if (newPnt.x>changed.x) {
                            angle = Orientation.EAST;
                        } else if (newPnt.y>changed.y) {
                            angle = Orientation.SOUTH;
                        }
                    }
                    map.getGrid()[newPnt.x][newPnt.y] = new Intersection(angle);
                }
            }
            changed = new Point(newPnt.x, newPnt.y);
            repaint(newPnt.x * cellSize, newPnt.y * cellSize, cellSize, cellSize);
        }
    }

    private void changeRules(int x, int y) {
        Point changePnt = getCell(x, y);
        if (map.getGrid()[changePnt.x][changePnt.y] instanceof Road) {
            Road r = (Road) map.getGrid()[changePnt.x][changePnt.y];
            r.changeType();
        }
        changed = new Point(changePnt.x, changePnt.y);
        repaint(changePnt.x * cellSize, changePnt.y * cellSize, cellSize, cellSize);
    }

    private void changeAngle(int x, int y) {
        Point changePnt = getCell(x, y);
        if (map.getGrid()[changePnt.x][changePnt.y] instanceof Road) {
            Road r = (Road) map.getGrid()[changePnt.x][changePnt.y];
            r.setDirection(Orientation.rotateRight(r.getDirection()));
            changed = new Point(changePnt.x, changePnt.y);
            repaint(changePnt.x * cellSize, changePnt.y * cellSize, cellSize, cellSize);
        }
    }
    public void start() {
        addRectangles();
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }

    public void addRectangles() {
        repaint();
    }

    public Save loadWindow() {
        ArrayList<Save> saves = sim.getSaves();
        String[] options = new String[saves.size()];
        for (int i = 0; i < options.length; i++) {
            options[i] = saves.get(i).getName();
        }
        JComboBox<String> comboBox = new JComboBox<>(options);
        int result = JOptionPane.showConfirmDialog(null, comboBox, "Select an option", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String selectedOption = comboBox.getSelectedItem().toString();
            return saves.get(Arrays.asList(options).indexOf(selectedOption));
        }
        return null;
    }

    public String nameWindow() {
        String input = JOptionPane.showInputDialog("Enter New Name:");
        return input;
    }

    public int[] settingsWindow() {
        int[] currentSettings = map.getSettings();
        JSlider[] sliders = new JSlider[DriverType.values().length + 1];
        JPanel panel = new JPanel(new GridLayout(sliders.length, 2));
        int count = 0;
        for (DriverType type : DriverType.values()) {
            sliders[count] = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
            panel.add(new JLabel(type.toString()));
            panel.add(sliders[count]);
            sliders[count].setValue(currentSettings[count]);
            count++;
        }

        sliders[sliders.length - 1] = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        panel.add(new JLabel("Traffic Concentration"));
        panel.add(sliders[sliders.length - 1]);
        sliders[sliders.length - 1].setValue(currentSettings[sliders.length - 1]);

        int result = JOptionPane.showConfirmDialog(null, panel, "Configure", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int[] values = new int[DriverType.values().length + 1];
            for (int i = 0; i < sliders.length; i++) {
                values[i] = sliders[i].getValue();
            }
            return values;
        }
        return new int[3];
    }

    public void statsWindow() {
        JFrame frame = new JFrame("Table Example");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Driver", "#", "Collisions", "Trips without accident"};
        Object[][] data = {
                {"jojodoss", "1", 2345, 123},
                {"hansy_boi", "2", 2345, 5432},
                {"java joe", "3", 6354, 897},
                {"mr miller", "4", 7152435, 78},
                {"sichang", "5", 123412, 49785}
        };

        JTable table = new JTable(data, columnNames);
        table.setEnabled(false); // disable the entire table
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.add(closeButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    public void drawImage(Graphics g, int i, int j) {
        if (map.getGrid()[i][j] instanceof Road) { //only draw Roads
            Road r = (Road) map.getGrid()[i][j];
            try {
                g.drawImage(ImageIO.read(new File(r.getImage())), i * cellSize, j * cellSize, cellSize, cellSize, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (!startButtonPressed) {
                try {
                    BufferedImage image = ImageIO.read(new File(r.getArrowImage()));
                    BufferedImage rotatedImage = new BufferedImage(cellSize, cellSize, image.getType());
                    Graphics2D g2d = rotatedImage.createGraphics();
                    g2d.rotate(Math.toRadians(Orientation.toDegrees(((Road) map.getGrid()[i][j]).getDirection())), cellSize / 2, cellSize / 2);
                    g2d.drawImage(image, 0, 0, cellSize, cellSize, null);
                    g2d.dispose();
                    g.drawImage(rotatedImage, i * cellSize, j * cellSize, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else { // draw cars/collisions
                if (r.isOccupied()) { // if road has occupant
                    try {
                        BufferedImage image = ImageIO.read(new File(r.getOccupant().getImage()));
                        BufferedImage rotatedImage = new BufferedImage(cellSize, cellSize, image.getType());
                        Graphics2D g2d = rotatedImage.createGraphics();
                        g2d.rotate(Math.toRadians(Orientation.toDegrees(r.getOccupant().getDirection())), cellSize / 2, cellSize / 2);
                        g2d.drawImage(image, 0, 0, cellSize, cellSize, null);
                        g2d.dispose();
                        g.drawImage(rotatedImage, i * cellSize, j * cellSize, null);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        if (changed == null) {
            for (int i = 0; i < map.getHeight(); i++) {
                for (int j = 0; j < map.getWidth(); j++) {
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
