
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.util.*;

class Zone {
    String[] info;

    public Zone(String[] myInfo) {
        info = myInfo;
    }
}

class State {
    int x, y;
    ArrayList<Zone> zones;

    public State(ArrayList<Zone> myZones, int myX, int myY) {
        x = myX;
        y = myY;
        zones = myZones;
    }
}

class GraphDraw extends JPanel {
    int block_width;
    int block_height;

    ArrayList<State> states;

    GraphDraw() { // Constructor
        states = new ArrayList<State>();
        block_width = 30;
        block_height = 30;
    }

    public void addState(ArrayList<Zone> zones, int x, int y) {
        states.add(new State(zones, x, y));
        this.repaint();
    }

    public void paint(Graphics g) { // draw the zone
        int block_x = 0;
        int block_y = 0;
        FontMetrics fontMetric = g.getFontMetrics();

        for (State state : states) {
            int previous_block_x = 0;
            for (Zone zone : state.zones) {
                if (previous_block_x == 0)
                    block_x = state.x;
                else
                    block_x = previous_block_x;
                System.out.println("blockx: " + block_x);
                previous_block_x = block_x + block_width + 10;
                // draw all block in zone
                int previous_block_y = 0;
                for (String block : zone.info) {
                    System.out.println("Block name: " + block);
                    // draw block
                    if (previous_block_y == 0)
                        block_y = state.y;
                    else
                        block_y = previous_block_y;
                    previous_block_y = block_y - block_height - 1;
                    if (block == "A")
                        g.setColor(Color.BLUE);
                    else if (block == "B")
                        g.setColor(Color.YELLOW);
                    else if (block == "C")
                        g.setColor(Color.GREEN);
                    else
                        g.setColor(Color.PINK);
                    g.fillRect(block_x, block_y, block_width, block_height);

                    // fill string
                    g.setColor(Color.black);
                    g.drawString(block, block_x - fontMetric.stringWidth(block) / 2 + 12,
                            block_y + fontMetric.getHeight() / 2 + 12);
                }
            }
            // draw bounding box around state
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }
}

public class previewGraphDraw {
    private JFrame frame;
    // private Drawing drawing;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new previewGraphDraw()::createAndShowGui);
    }

    private void createAndShowGui() {
        frame = new JFrame(getClass().getSimpleName());

        GraphDraw graph = new GraphDraw();

        String[] z1infos = { "A", "E", "F", "G" };
        String[] z2infos = { "B", "D" };
        String[] z3infos = { "C" };
        Zone z1 = new Zone(z1infos);
        Zone z2 = new Zone(z2infos);
        Zone z3 = new Zone(z3infos);

        //　一つの状態を定義し、グラフに入れる
        ArrayList<Zone> state = new ArrayList<Zone>();
        state.add(z1);
        state.add(z2);
        state.add(z3);
        graph.addState(state, 100, 100);        // 状態をグラフに入れる

        JScrollPane scroll = new JScrollPane(graph);

        frame.add(scroll);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
