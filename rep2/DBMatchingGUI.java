import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.util.regex.*;
import java.sql.*;
import java.awt.event.*;

public class DBMatchingGUI{
	public static void main(String[] args) throws Exception {
	Screen s = new Screen("DBMatchingGUI");
	s.setVisible(true);
    }
}

class Screen extends JFrame {
	SentenceDAO senDAO = new SentenceDAO();
    Container contentPane=getContentPane();
    JTextPane pane = new JTextPane();
    JScrollPane scrollPane = new JScrollPane(pane);
    private static JTextField searchPane = new JTextField();
	private static JPanel buttonPane = new JPanel();
	
	JButton searchButton = new JButton("検索");
	JButton insertButton = new JButton("挿入");
	JButton deleteButton = new JButton("削除");
    JButton saveButton = new JButton("保存");
    JButton backButton = new JButton("戻る");
    private static JPanel southPane = new JPanel();
	private static JFrame frame = new JFrame();
	private static String dataSet = "";
	java.util.List<Sentence> senList;


	Screen(String title) {
		senList = senDAO.findAll();
		for (Sentence sen : senList) {
			dataSet+=sen.getName()+"\n";
		}
	setTitle(title);
	setBounds(660,340,600,400);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	Font font = new Font("WenQuanYi Micro Hei Mono",Font.PLAIN,14);
	pane.setFont(font);
	searchPane.setFont(font);
	pane.setEditable(true);

	searchButton.addActionListener(
	    new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    search(searchPane.getText());
		}
	    }
	);
	
	insertButton.addActionListener(
	    new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    insert(searchPane.getText());
		}
	    }
	);
	
	deleteButton.addActionListener(
	    new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    delete(searchPane.getText());
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
	buttonPane.add(insertButton);
	buttonPane.add(deleteButton);

	backButton.setVisible(false);

	JPanel southPane = new JPanel();
	southPane.setLayout(new BoxLayout(southPane, BoxLayout.PAGE_AXIS));

       	southPane.add(searchPane);
		southPane.add(buttonPane);

	contentPane.add(scrollPane, BorderLayout.CENTER);
	contentPane.add(southPane, BorderLayout.SOUTH);
	
	loadDataSet();
    }

	void setText(String text) {
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

	void search(String searchText) {
		Pattern p = Pattern.compile("\"[^\"]*\"");
		java.util.regex.Matcher countGroup = p.matcher(searchText);
		int count = 0;
		while (countGroup.find()) {
			count++;
		}
		String[] searchTexts = new String[count];
		java.util.regex.Matcher args = p.matcher(searchText);
		count = 0;
		while (args.find()) {
			searchTexts[count] = args.group().replaceAll("\"", "");
			count++;
		}
		//標準出力を受け取る
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(new BufferedOutputStream(out)));
		DBMatching m = new DBMatching(); ///ここ重要！！！！！！！！！！
		m.main(searchTexts);

		//結果の表示とボタンの表示
		System.out.flush();
		setText(out.toString());
		pane.setEditable(false);
		saveButton.setVisible(false);
		backButton.setVisible(true);
		System.setOut(System.out);
	}

	void insert(String searchText) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(new BufferedOutputStream(out)));
		senList = senDAO.findAll();
		Sentence x = new Sentence(searchText);
		senList = senDAO.insert(x); //レコードの追加

		System.out.flush();
		setText(out.toString());
		pane.setEditable(false);
		saveButton.setVisible(false);
		backButton.setVisible(true);
		System.setOut(System.out);
	}

	void delete(String searchText) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(new BufferedOutputStream(out)));
		senList = senDAO.findAll();
		Sentence x = new Sentence(searchText);
		senList = senDAO.delete(x); //レコードの追加

		System.out.flush();
		setText(out.toString());
		pane.setEditable(false);
		saveButton.setVisible(false);
		backButton.setVisible(true);
		System.setOut(System.out);
	}

    void back(){
		//検索結果からdatasetの一覧に戻る
		senList = senDAO.findAll();
		dataSet = "";
		for (Sentence sen : senList) {
			dataSet+=sen.getName()+"\n";
		}
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
