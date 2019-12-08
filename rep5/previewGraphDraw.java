import java.io.*;
import java.sql.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.util.*;
import java.applet.Applet;

public class previewGraphDraw {
    private JFrame frame;
    // private Drawing drawing;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new previewGraphDraw()::createAndShowGui);
    }

    private void createAndShowGui() {
        ArrayList<ArrayList<String[]>> st = new ArrayList<>();
        st=makeLists();
        for (int i = 0; i < st.size(); i++) {   
        frame = new JFrame(getClass().getSimpleName());
        
        
        
        //下のfor文を回しても,リペイントされない・・・・どうしようか？？
                      
        // 状態を描画するとき、GraphDrawのインスタンスを生成する
        GraphDraw graph = new GraphDraw();
    
        // 一つのZoneは一つの山に相当する
        ArrayList<Zone> state = new ArrayList<Zone>();
        for(int j=0;j<st.get(i).size();j++){
            //st.get(i).get(j)で一つの山の情報のString[]が取得可能
            //state.add(new Zone(z1))でstateに山z１を入れる
            state.add(new Zone(st.get(i).get(j)));
        }

        // 一つの状態を定義し、グラフに入れる
        int state1_x = 100;
        int state1_y = 100;
        
        graph.addState(state, state1_x, state1_y); // 状態をグラフに入れる
        
        JScrollPane scroll = new JScrollPane(graph);
        frame.add(scroll);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
        
    }

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

    private ArrayList<ArrayList<String[]>> makeLists(){
        PrintStream sysOut = System.out;
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
        return st;
    }
    
}
