import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.io.*;
import java.sql.*;
import java.awt.*;
import java.util.*;
import java.applet.Applet;

public class gui extends JFrame {

    private JPanel contentPane;
    int i=0;
    ArrayList<ArrayList<String[]>> st;
    ArrayList<Zone> previousState;

    String intialStateString = "";
    String goalString = "";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    gui frame = new gui();
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
    public gui() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 640, 480);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel operatorPanel = new JPanel();

        JScrollPane scrollPane = new JScrollPane();

        JTextPane textPane = new JTextPane();

        JButton previousStateButton = new JButton("前の状態");
        JButton nextState = new JButton("次の状態");

        contentPane.add(operatorPanel, BorderLayout.NORTH);

        String firstInitial = "clear A\nclear B\nclear C\nontable A\nontable B\nontable C\nhandEmpty";

        JButton initButton = new JButton("初期状態");
        initButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textPane.setText(firstInitial);
                scrollPane.setViewportView(textPane);
            }
        });
        operatorPanel.add(initButton);


        JButton goalButton = new JButton("目標状態");
        goalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // set initial state input string
                intialStateString = textPane.getText();
                // clear text panel
                textPane.setText("A on B\nB on C");
            }
        });
        operatorPanel.add(goalButton);

        JButton runButton = new JButton("実行");
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // get string from panel
                goalString = textPane.getText();
                String[] goalInput = goalString.split("\n", 0);
                String[] initialInput = intialStateString.split("\n", 0);
                Vector initialState = initInitialState(initialInput);
                Vector goal = initGoalList(goalInput);
                st = new ArrayList<>();
                st = makeLists(goal, initialState);

                System.out.println(st);
                i=0;
                // 状態を描画するとき、GraphDrawのインスタンスを生成する
                GraphDraw graph = new GraphDraw();

                // 一つのZoneは一つの山に相当する
                ArrayList<Zone> state = new ArrayList<Zone>();

                if(i!=0){
                    if(i%2==0){
                        for(int j=0;j<st.get(i-2).size();j++){
                            state.add(new Zone(st.get(i-2).get(j)));
                        }
                    }else{
                        for(int j=0;j<st.get(i-1).size();j++){
                            state.add(new Zone(st.get(i-1).get(j)));
                        }
                    }
                    previousState = state;
                }

                state = new ArrayList<Zone>();

                for(int j=0;j<st.get(i).size();j++){
                    if(st.get(i).get(j)[0].contains("pick up")){
                        state = previousState;
                        graph.pickup(st.get(i).get(j)[0].split(" ")[2]);
                        break;
                    }
                    //st.get(i).get(j)で一つの山の情報のString[]が取得可能
                    //state.add(new Zone(z1))でstateに山z１を入れる
                    state.add(new Zone(st.get(i).get(j)));
                }

                // 一つの状態を定義し、グラフに入れる
                int state1_x = 100;
                int state1_y = 200;

                graph.addState(state, state1_x, state1_y); // 状態をグラフに入れる


                scrollPane.setViewportView(graph);
                nextState.setVisible(true);


            }
        });
        operatorPanel.add(runButton);

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        operatorPanel.add(saveButton);

        JPanel viewPanel = new JPanel();
        contentPane.add(viewPanel, BorderLayout.CENTER);



        GroupLayout gl_viewPanel = new GroupLayout(viewPanel);
        gl_viewPanel.setHorizontalGroup(
        	gl_viewPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_viewPanel.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
        			.addContainerGap())
        );
        gl_viewPanel.setVerticalGroup(
        	gl_viewPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_viewPanel.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
        			.addContainerGap())
        );

        textPane.setText(firstInitial);
        scrollPane.setViewportView(textPane);
        viewPanel.setLayout(gl_viewPanel);

        JPanel statePanel = new JPanel();
        contentPane.add(statePanel, BorderLayout.SOUTH);

        previousStateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                i -=1;
                System.out.println("previous state " +i);
                if(i>=0){
                    if(!nextState.isVisible()){
                        nextState.setVisible(true);
                    }
                    if(i==0){
                        previousStateButton.setVisible(false);
                    }
                    // 状態を描画するとき、GraphDrawのインスタンスを生成する
                    GraphDraw graph = new GraphDraw();

                    // 一つのZoneは一つの山に相当する
                    ArrayList<Zone> state = new ArrayList<Zone>();

                    if(i!=0){
                        if(i%2==0){
                            for(int j=0;j<st.get(i-2).size();j++){
                                state.add(new Zone(st.get(i-2).get(j)));
                            }
                        }else{
                            for(int j=0;j<st.get(i-1).size();j++){
                                state.add(new Zone(st.get(i-1).get(j)));
                            }
                        }
                        previousState = state;
                    }

                    state = new ArrayList<Zone>();

                    for(int j=0;j<st.get(i).size();j++){
                        if(st.get(i).get(j)[0].contains("pick up")){
                            state = previousState;
                            graph.pickup(st.get(i).get(j)[0].split(" ")[2]);
                            break;
                        }
                        //st.get(i).get(j)で一つの山の情報のString[]が取得可能
                        //state.add(new Zone(z1))でstateに山z１を入れる
                        state.add(new Zone(st.get(i).get(j)));
                    }

                    // 一つの状態を定義し、グラフに入れる
                    int state1_x = 100;
                    int state1_y = 200;

                    graph.addState(state, state1_x, state1_y); // 状態をグラフに入れる


                    scrollPane.setViewportView(graph);
                }
                else{
                    previousStateButton.setVisible(false);
                }
            }
        });
        statePanel.add(previousStateButton);
        statePanel.add(nextState);

        previousStateButton.setVisible(false);
        nextState.setVisible(false);

        nextState.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                i++;
                System.out.println("next state " +i);
                if(i<st.size()){
                    if(!previousStateButton.isVisible()){
                        previousStateButton.setVisible(true);
                    }
                    if(i==st.size()-1){
                        nextState.setVisible(false);
                    }
                    // 状態を描画するとき、GraphDrawのインスタンスを生成する
                    GraphDraw graph = new GraphDraw();

                    // 一つのZoneは一つの山に相当する
                    ArrayList<Zone> state = new ArrayList<Zone>();

                    if(i!=0){
                        if(i%2==0){
                            for(int j=0;j<st.get(i-2).size();j++){
                                state.add(new Zone(st.get(i-2).get(j)));
                            }
                        }else{
                            for(int j=0;j<st.get(i-1).size();j++){
                                state.add(new Zone(st.get(i-1).get(j)));
                            }
                        }
                        previousState = state;
                    }

                    state = new ArrayList<Zone>();

                    for(int j=0;j<st.get(i).size();j++){
                        if(st.get(i).get(j)[0].contains("pick up")){
                            state = previousState;
                            graph.pickup(st.get(i).get(j)[0].split(" ")[2]);
                            break;
                        }
                        //st.get(i).get(j)で一つの山の情報のString[]が取得可能
                        //state.add(new Zone(z1))でstateに山z１を入れる
                        state.add(new Zone(st.get(i).get(j)));
                    }

                    // 一つの状態を定義し、グラフに入れる
                    int state1_x = 100;
                    int state1_y = 200;

                    graph.addState(state, state1_x, state1_y); // 状態をグラフに入れる


                    scrollPane.setViewportView(graph);
                }
                else{
                    nextState.setVisible(false);
                }
            }
        });
    }

    // for planning
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

    private Vector initGoalList(String[] input){
		Vector goalList = new Vector();
		goalList.addElement("B on A");
		goalList.addElement("A on C");
		return goalList;
	}

	//変更点12/1(新たな定義を追加)
	private Vector initInitialState(String[] input){
		Vector initialState = new Vector();
		initialState.addElement("clear A");
		initialState.addElement("clear B");
		initialState.addElement("clear C");
		//initialState.addElement("clear blue");
  		//initialState.addElement("clear yellow");
  		//initialState.addElement("clear C");

		//initialState.addElement("B on A");
		//initialState.addElement("C on B");

		initialState.addElement("ontable A");
		initialState.addElement("ontable B");
		initialState.addElement("ontable C");
		//initialState.addElement("ontable triangle");
  		//initialState.addElement("ontable green");

		initialState.addElement("handEmpty");
		return initialState;
	}


    private ArrayList<ArrayList<String[]>> makeLists(Vector goal, Vector initial){
        PrintStream sysOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(new BufferedOutputStream(out)));
        Planner p = new Planner(goal, initial);
        p.main(new String[]{}, goal, initial);
        System.out.flush();
        String output = out.toString();
        System.setOut(sysOut);


        //planの解析
        String[] lines = output.toString().split("\n");
        boolean flag = false;
        String ini="";
        ArrayList<ArrayList<String[]>> st = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            // System.out.println(i);
            // System.out.println(lines[i]);
            if (flag && lines[i].contains("initialState")) {
                ini = lines[i].replace("initialState:", "").replace("[", "").replace("]", "");
                st.add(getArrays(ini));
            }

            else if (flag && lines[i].contains("pick up")) {
                ArrayList<String[]> pickup_state = new ArrayList<>();
                String[] pickup = new String[1];
                pickup[0]="pick up "+lines[i].split(" ")[2];
                pickup_state.add(pickup);
                st.add(pickup_state);
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
        // System.out.println("done");
        return st;
    }
}
