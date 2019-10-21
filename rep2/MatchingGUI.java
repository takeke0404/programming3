import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.util.regex.*;
import java.sql.*;
import java.awt.event.*;

public class MatchingGUI{
    public static void main(String[] args) throws Exception{
	Screen s = new Screen("matchingGUI");
	s.setVisible(true);
    }
}

class Screen extends JFrame{
    Container contentPane=getContentPane();
    JTextPane pane = new JTextPane();
    JScrollPane scrollPane = new JScrollPane(pane);
    private static JTextField searchPane = new JTextField();
    private static JPanel buttonPane = new JPanel();
    JButton searchButton = new JButton("検索");
    JButton saveButton = new JButton("保存");
    JButton backButton = new JButton("戻る");
    private static JPanel southPane = new JPanel();
    private static JFrame frame = new JFrame();
    private static String dataSet = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    Screen(String title){
	setTitle(title);
	setBounds(660,340,600,400);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	pane.setEditable(true);

	searchButton.addActionListener(
	    new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    search(searchPane.getText());
		}
	    }
	);
	
	saveButton.addActionListener(
	    new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    saveDataSet();
		    JLabel msg = new JLabel("保存されました");
		    JOptionPane.showMessageDialog(frame, msg);
		}
	    }
	);

	backButton.addActionListener(
            new ActionListener(){
	        public void actionPerformed(ActionEvent event){
		    back();
		}
	    }
	);

       	buttonPane.add(saveButton);
	buttonPane.add(backButton);
	buttonPane.add(searchButton);

	backButton.setVisible(false);

	JPanel southPane = new JPanel();
	southPane.setLayout(new BoxLayout(southPane, BoxLayout.PAGE_AXIS));

       	southPane.add(searchPane);
      	southPane.add(buttonPane);

	contentPane.add(scrollPane, BorderLayout.CENTER);
	contentPane.add(southPane, BorderLayout.SOUTH);
	
	loadDataSet();
    }

    void setText(String text){
	pane.setText(text);
	pane.setCaretPosition(0);
    }

    void loadDataSet(){
	//setTextを呼ぶ
	//dataSetに格納する
	setText(dataSet);
    }

    void saveDataSet(){
	//saveDataSet
    }

    void search(String searchText){
        Pattern p = Pattern.compile("\"[^\"]*\"");
        java.util.regex.Matcher countGroup = p.matcher(searchText);
	int count = 0;
	while(countGroup.find()){
	    count++;
	}
	String[] searchTexts = new String[count];
	java.util.regex.Matcher args = p.matcher(searchText);
	count = 0;
	while(args.find()){
	    searchTexts[count] = args.group().replaceAll("\"","");
	    count++;
	}
	//標準出力を受け取る
        ByteArrayOutputStream out = new ByteArrayOutputStream();
	System.setOut(new PrintStream(new BufferedOutputStream(out)));
	Matching m = new Matching();
	m.main(searchTexts);

	//結果の表示とボタンの表示
	System.out.flush();
      	setText(out.toString());
	pane.setEditable(false);
        saveButton.setVisible(false);
	backButton.setVisible(true);
	System.setOut(System.out);
    }

    void back(){
	//検索結果からdatasetの一覧に戻る
	pane.setEditable(true);
	setText(dataSet);
	saveButton.setVisible(true);
	backButton.setVisible(false);
    }
}


/*
参考
https://qiita.com/boss_ape/items/b68b0bd6d85cf18d1626
https://www.javadrive.jp/tutorial/jbutton/
 */
