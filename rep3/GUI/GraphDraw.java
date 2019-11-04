import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Polygon;

public class GraphDraw extends JPanel {
	int width;
	int height;

	ArrayList<Node> nodes;
	ArrayList<edge> edges;

	public GraphDraw() { // Constructor
		nodes = new ArrayList<Node>();
		edges = new ArrayList<edge>();
		width = 30;
		height = 30;
	}

	class Node {
		int x, y;
		String name;

		public Node(String myName, int myX, int myY) {
			x = myX;
			y = myY;
			name = myName;
		}
	}

	class edge {
		int i, j;

		public edge(int ii, int jj) {
			i = ii;
			j = jj;
		}
	}

	public void addNode(String name, int x, int y) {
		// add a node at pixel (x,y)
		nodes.add(new Node(name, x, y));
		this.repaint();
	}

	public void addEdge(int i, int j) {
		// add an edge between nodes i and j
		edges.add(new edge(i, j));
		this.repaint();
	}

	public void paint(Graphics g) { // draw the nodes and edges
		FontMetrics f = g.getFontMetrics();
		int nodeHeight = Math.max(height, f.getHeight());

		g.setColor(Color.black);
		for (edge e : edges) {
			int endx = nodes.get(e.j).x;
			int endy = nodes.get(e.j).y;
			g.drawLine(nodes.get(e.i).x, nodes.get(e.i).y, nodes.get(e.j).x, nodes.get(e.j).y);
			Polygon arrowHead = new Polygon();
			arrowHead.addPoint(endx, endy + 5);
			arrowHead.addPoint(endx - 5, endy - 5);
			arrowHead.addPoint(endx + 5, endy - 5);
			g.fillPolygon(arrowHead);
		}

		for (Node n : nodes) {
			int nodeWidth = Math.max(width, f.stringWidth(n.name) + width / 2);
			g.setColor(Color.white);
			g.fillOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2, nodeWidth, nodeHeight);
			g.setColor(Color.black);
			g.drawOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2, nodeWidth, nodeHeight);

			g.drawString(n.name, n.x - f.stringWidth(n.name) / 2, n.y + f.getHeight() / 2);
		}
	}
}
