
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

public class GraphDraw extends JPanel {
    int block_width;
    int block_height;

    ArrayList<State> states;

    String pickup_block;

    GraphDraw() { // Constructor
        states = new ArrayList<State>();
        block_width = 30;
        block_height = 30;
    }

    public void addState(ArrayList<Zone> zones, int x, int y) {
        states.add(new State(zones, x, y));
        this.repaint();
    }

    public void pickup(String x){
        pickup_block=x;
    }

    public void paint(Graphics g) { // draw the zone
        int block_x = 0;
        int block_y = 0;
        FontMetrics fontMetric = g.getFontMetrics();

        for (State state : states) {
            int table_line_x1 = state.x - 10;
            int table_line_x2 = state.x;
            int previous_block_x = 0;
            for (Zone zone : state.zones) {
                if (previous_block_x == 0)
                    block_x = state.x;
                else
                    block_x = previous_block_x;
                // System.out.println("blockx: " + block_x);
                previous_block_x = block_x + block_width + 10;
                table_line_x2 += block_width;
                // draw all block in zone
                int previous_block_y = 0;
                for (String block : zone.info) {
                    // System.out.println("Block name: " + block);
                    // draw block
                    if (previous_block_y == 0)
                        block_y = state.y;
                    else
                        block_y = previous_block_y;
                    previous_block_y = block_y - block_height - 1;

                    //pickup
                    if (pickup_block!=null&&block.equals(pickup_block)){
                        block_y-=40;
                        drawArm(g, block_x, block_y, block_width, block_height);
                        pickup_block=null;
                    }

                    // それぞれのブロックの色を決める
                    if (block.contains("A"))
                        g.setColor(Color.BLUE);
                    else if (block.contains("B"))
                        g.setColor(Color.YELLOW);
                    else if (block.contains("C"))
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
            // draw table line
            table_line_x2 += block_width;
            int table_line_y = state.y + block_height + 1;
            g.setColor(Color.black);
            g.drawLine(table_line_x1, table_line_y, table_line_x2, table_line_y);
        }
    }

    public void drawArm(Graphics g,int block_x,int block_y,int block_width,int block_height){
        g.drawLine(block_x-5,block_y,block_x,block_y+block_height/2);
        g.drawLine(block_x+block_width+5,block_y,block_x+block_width,block_y+block_height/2);
        g.drawLine(block_x+block_width/2-7,block_y-10,block_x-5,block_y);
        g.drawLine(block_x+block_width/2+7,block_y-10,block_x+block_width+5,block_y);
        g.drawLine(block_x+block_width/2-7,block_y-10,block_x+block_width/2+7,block_y-10);
        g.drawLine(block_x+block_width/2,block_y-50,block_x+block_width/2,block_y-10);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(320, 320);
    }
}
