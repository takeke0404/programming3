import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.util.regex.*;
import java.sql.*;
import java.awt.event.*;


public class GuiTest extends JFrame {

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
	public GuiTest() {
        PrintStream sysOut = System.out;
		JTextPane pane = new JTextPane();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(new BufferedOutputStream(out)));

		Planner p = new Planner();
		p.main(new String[]{});
        System.out.flush();
        String output = out.toString();

        System.setOut(sysOut);

		//結果の表示とボタンの表示
		String[] line = output.toString().split("\n");
		boolean flag = false;

		ArrayList<ArrayList<String[]>> st = new ArrayList<>();

		for (int i = 0; i < line.length; i++) {
			if (flag&&line[i].contains("initialState")) {
                ArrayList<ArrayList<String>> initial = new ArrayList<>();
				ArrayList<String> tmp = new ArrayList<>();
				String[] tmpline=line[i].replace("initialState:[","").replace("]","").split(",");
				while (tmp.size() < tmpline.length-1) {
					for (int j = 0; j < tmpline.length; j++) {
                        if(!tmp.contains(tmpline[j])&&tmpline[j].contains("ontable")){
                            tmp.add(tmpline[j]);
                            ArrayList<String> a = new ArrayList<>();
                            a.add(tmpline[j].replace(" ontable ",""));
                            initial.add(a);
                            continue;
                        }
                        if(!tmp.contains(tmpline[j])&&tmpline[j].contains("clear")){
                            tmp.add(tmpline[j]);
                            continue;
                        }
                        if(!tmp.contains(tmpline[j])&&tmpline[j].contains("on")){
                            for(int k=0;k<initial.size();k++){
                                if(tmpline[j].contains(initial.get(k).get(initial.get(k).size()-1))){
                                    tmp.add(tmpline[j]);
                                    initial.get(k).add(tmpline[j].replace(" on "+initial.get(k).get(initial.get(k).size()-1),"").replace(" ",""));
                                }
                            }
                        }
					}
				}

                ArrayList<String[]> a = new ArrayList<>();
                for(int j=0;j<initial.size();j++){
                    a.add(initial.get(j).toArray(new String[initial.get(j).size()]));
                }

                st.add(a);
			}
			if(line[i].equals("***** This is a plan! *****")){
				flag = true;
            }
		}

        for(int i=0;i<st.size();i++){
            for(int j=0;j<st.get(i).size();j++){
                for(int k=0;k<st.get(i).get(j).length;k++){
                    System.out.print(st.get(i).get(j)[k]);
                }
                System.out.println();
            }
        }

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	}
}
