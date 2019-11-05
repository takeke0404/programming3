import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Polygon;

public class GraphDraw extends JPanel {
	int width;
	int height;

	ArrayList<Node> nodes;

	public GraphDraw() { // Constructor
		nodes = new ArrayList<Node>();
		width = 30;
		height = 30;
	}

	class Node {
		int x, y;
		String[] info;

		public Node(String[] myInfo, int myX, int myY) {
			x = myX;
			y = myY;
			info = myInfo;
		}
	}


	public void addNode(String[] info, int x, int y) {
		// add a node at pixel (x,y)
		nodes.add(new Node(info, x, y));
		this.repaint();
	}


	public void paint(Graphics g) { // draw the nodes
		FontMetrics f = g.getFontMetrics();
		int nodeHeight = Math.max(height, f.getHeight());
		int labelHeight = 20;
		int length = 120;
		int nodex = 0;
		int nodey = 0;

		g.setColor(Color.black);
		for (Node n : nodes) {
			// tail
			int nodeWidth = Math.max(width, f.stringWidth(n.info[1]) + width / 2);
			nodex = n.x-nodeWidth/2;
			nodey = n.y -nodeHeight/2;
			g.setColor(Color.white);
			g.fillOval(nodex, nodey, nodeWidth, nodeHeight);
			g.setColor(Color.black);
			g.drawOval(nodex, nodey, nodeWidth, nodeHeight);
			g.drawString(n.info[1], n.x - f.stringWidth(n.info[1]) / 2, n.y + f.getHeight() / 2);

			// head
			nodeWidth = Math.max(width, f.stringWidth(n.info[0]) + width / 2);
			nodex = n.x-nodeWidth/2;
			g.setColor(Color.white);
			g.fillOval(nodex, nodey+length, nodeWidth, nodeHeight);
			g.setColor(Color.black);
			g.drawOval(nodex, nodey+length, nodeWidth, nodeHeight);
			g.drawString(n.info[0], n.x - f.stringWidth(n.info[0])/2, n.y+f.getHeight()/2+length);

			// label
			String label = n.info[2];
			int startx = nodex+nodeWidth/2;
			int starty = nodey+nodeHeight;
			int endx = startx;
			int endy = nodey+length;
			g.drawLine(startx, starty, endx, starty+length/2-labelHeight);
			g.drawLine(startx, starty+length/2+5, endx, endy);
			g.drawString(label, startx-f.stringWidth(label)/2, starty+length/2);

			Polygon arrowHead = new Polygon();
			arrowHead.addPoint(endx, endy);
			arrowHead.addPoint(endx - 3, endy - 5);
			arrowHead.addPoint(endx + 3, endy - 5);
			g.fillPolygon(arrowHead);
			
		}
	}
}
