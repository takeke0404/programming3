
class testGraphDraw {
    //Here is some example syntax for the GraphDraw class
    public static void main(String[] args) {
	GraphDraw frame = new GraphDraw("Test Window");

	frame.setSize(400,300);
	
	frame.setVisible(true);

	frame.addNode("a", 50,50);
	frame.addNode("b", 50,100);
	frame.addNode("c",250,250);
	frame.addNode("longNode", 200,200);
	frame.addEdge(0,2);
	frame.addEdge(0,4);
	}
}