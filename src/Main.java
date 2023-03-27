import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        

        String[][] map = new String[1000][1000];

        for (int i = 0; i<map.length; i++) {
            for (int j = 0; j<map[i].length; j++) {
                map[i][j] = new Grass();
            }
        }
//1. Create the frame.
        JFrame frame = new JFrame("FrameDemo");
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


//        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
//
//        frame.pack();

        frame.setVisible(true);

    }
}
