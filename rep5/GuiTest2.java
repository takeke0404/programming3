import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.util.regex.*;
import java.sql.*;
import java.awt.event.*;
 

public class GuiTest2 extends JFrame {

	private JPanel contentPane;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiTest frame = new GuiTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GuiTest2() {
		JTextPane pane = new JTextPane();
		String argv[];
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(new BufferedOutputStream(out)));
		Planner p = new Planner();
		p.main(argv);

		//結果の表示とボタンの表示
		System.out.flush();
		String[] line = out.toString().split("\n");
		boolean flag = flase;

		ArrayList<ArrayList<String[]>> st = new ArrayList<>();
		

		for (int i = 0; i < line.length; i++) {
			if (flag&&line[i].contains("initialState")) {
				ArrayList<String> tmp = new ArrayList<>();
				String[] tmpline=line[i].replace("initialState:[","").replace("]","").split(",");
				while (tmp.size() < tmpline.length-1) {
					for (int j = 0; j < templine.length; j++) {
						
					}
					
				}
			}
			if(line[i].equals("***** This is a plan! *****"))
				flag = true;
			
		}
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	}
}
