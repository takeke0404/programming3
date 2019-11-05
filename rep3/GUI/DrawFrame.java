import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Polygon;

public class DrawFrame extends JPanel {
    int width;
    int height;

    ArrayList<Frame> Frames;

    public DrawFrame() { //Constructor
        Frames = new ArrayList<Frame>();
        width = 60;
        height = 120;
    }

    class Frame {
        int x, y;
        String data[];

        public Frame(String[] myData, int myX, int myY) {
            x = myX;
            y = myY;
            data = myData;
        }
    }


    public void addFrame(String[] data, int x, int y) {
        //data: duy,29114154,情報,知能,伊藤・マスタファ,サッカー,python
        //add a Frame at pixel (x,y)
        Frames.add(new Frame(data,x,y));
        this.repaint();
    }

    public void paint(Graphics g) { 
        FontMetrics f = g.getFontMetrics();
        int frameHeight = 180;
        int frameWidth = 230;

        g.setColor(Color.black);

        String[] header = {"studentNo" ,"major","field","laboName","hobby","language"};

        for (Frame n : Frames) {
            g.setColor(Color.white);
            g.fillRect(n.x, n.y, frameWidth, frameHeight);
            g.setColor(Color.black);
            g.drawRect(n.x, n.y, frameWidth, frameHeight);

            g.drawString(n.data[0], n.x+10, n.y+14);

            for(int i = 1;i < header.length+1;i++){
                g.drawString(header[i-1], n.x+10, n.y+20+20*i);
            }

            for(int i = 1;i<n.data.length;i++){
                g.drawString(n.data[i], n.x+90, n.y+20+20*i);
                g.drawRect(n.x+85, n.y+20*i+5, 120, 22);
            }
        }
        
        
        

    }
}
