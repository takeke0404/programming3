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

        //標準出力の受け取り
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(new BufferedOutputStream(out)));
		Planner p = new Planner();
		p.main(new String[]{});
        System.out.flush();
        String output = out.toString();
        System.setOut(sysOut);

		//planの解析
		String[] lines = output.toString().split("\n");
        boolean flag = false;
        String ini="";
		ArrayList<ArrayList<String[]>> st = new ArrayList<>();
		for (int i = 0; i < lines.length; i++) {
            if (flag && lines[i].contains("initialState")) {
                ini = lines[i].replace("initialState:", "").replace("[", "").replace("]", "");
                st.add(getArrays(ini));
            }
            
            else if (flag && lines[i].contains("pick up")) {    //pick up はGUIで表示するか未定(今は前の状態と同じにしている)
                st.add(st.get(st.size() - 1));
                
            }
            
            else if (flag && lines[i].contains("Place")) {
                String[] s = lines[i].replace("Place ", "").split(" on ");
                ini=ini.replace(", ontable "+s[0], "");
                ini = ini.replace(", clear " + s[1], "");
                ini += ", " + s[0] + " on " + s[1];
                st.add(getArrays(ini));
            }
            
            else if (flag && lines[i].contains("remove")) {
                String[] s = lines[i].replace("remove ", "").split(" from on top ");
                ini +=", ontable "+s[0];
                ini += ", clear " + s[1];
                ini = ini.replace(", " + s[0] + " on " + s[1], "");
                st.add(getArrays(ini));
            }
			if(lines[i].equals("***** This is a plan! *****")){
				flag = true;
            }
        }


        //コンソールに表示
        for (int i = 0; i < st.size(); i++) {
            System.out.println("fase" + i);
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

    /*
    状態 [clear A, clear C, A on B, ontable B, ontable C, handEmpty]から
    配列のリスト[BA][C]を作成
    */
    private ArrayList<String[]> getArrays(String line) {
        String[] state = line.split(",");
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        ArrayList<String> tmp = new ArrayList<>();
        while (tmp.size() < state.length - 1) {
            for (int j = 0; j < state.length; j++) {
                if (!tmp.contains(state[j]) && state[j].contains("ontable")) {
                    tmp.add(state[j]);
                    ArrayList<String> a = new ArrayList<>();
                    a.add(state[j].replace(" ontable ", ""));
                    list.add(a);
                    continue;
                }
                if (!tmp.contains(state[j]) && state[j].contains("clear")) {
                    tmp.add(state[j]);
                    continue;
                }
                if (!tmp.contains(state[j]) && state[j].contains("on")) {
                    for (int k = 0; k < list.size(); k++) {
                        if (state[j].contains(list.get(k).get(list.get(k).size() - 1))) {
                            tmp.add(state[j]);
                            list.get(k).add(state[j].replace(" on " + list.get(k).get(list.get(k).size() - 1), "")
                                    .replace(" ", ""));
                        }
                    }
                }
            }
        }

        ArrayList<String[]> a = new ArrayList<>();
        for (int j = 0; j < list.size(); j++) {
            a.add(list.get(j).toArray(new String[list.get(j).size()]));
        }

        return a;
    }
}
