import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.util.*;
import java.io.*;
import java.util.regex.*;
import java.sql.*;
import java.awt.event.*;

public class DBMatchingGUI {
	public static void main(String[] args) throws Exception {
		Screen s = new Screen("DBMatchingGUI");
		s.setVisible(true);
	}
}

class Screen extends JFrame {
	SentenceDAO senDAO = new SentenceDAO();
	Container contentPane = getContentPane();
	JTextPane pane = new JTextPane();
	JScrollPane scrollPane = new JScrollPane(pane);
	private static JTextField searchPane = new JTextField();
	private static JPanel buttonPane = new JPanel();
	private static JPanel databasePanel = new JPanel();
	private static JList list = new JList();
	private static JTextField databaseNewData = new JTextField();
	private static JScrollPane databaseScrollPane = new JScrollPane(list);

	JButton searchButton = new JButton("検索");
	JButton insertButton = new JButton("挿入");
	JButton deleteButton = new JButton("削除");
	private static JPanel searchMatchingPanel = new JPanel();
	private static JFrame frame = new JFrame();
	private static String dataSet = "";
	java.util.List<Sentence> senList;

	Screen(String title) {
		dataSet = "検索例：\"?x is a boy\" \"?y is a girl\"";
		list = loadDatabase();
		databaseScrollPane = new JScrollPane(list);
		setTitle(title);
		setBounds(660, 340, 600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Font font = new Font("WenQuanYi Micro Hei Mono", Font.PLAIN, 14);
		pane.setFont(font);
		searchPane.setFont(font);
		pane.setEditable(true);

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				search(searchPane.getText());
			}
		});

		insertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				insert();
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				delete();
			}
		});

		buttonPane.add(searchButton);

		JPanel	searchMatchingPanel = new JPanel();
		JLabel searchLabel = new JLabel("検索");
		JLabel resultLabel = new JLabel("検索結果");
		searchMatchingPanel.setLayout(new BoxLayout(searchMatchingPanel, BoxLayout.PAGE_AXIS));
		searchMatchingPanel.add(searchLabel);
		searchMatchingPanel.add(searchPane);
		searchMatchingPanel.add(buttonPane);
		searchMatchingPanel.add(resultLabel);
		searchMatchingPanel.add(scrollPane, BorderLayout.SOUTH);

		// database operating panel
		JPanel databaseOperationJPanel = new JPanel();
		JLabel databaseLabel = new JLabel("データベース操作");
		JLabel databaseAdd = new JLabel("データ追加");
		databaseOperationJPanel.add(insertButton);
		databaseOperationJPanel.add(deleteButton);

		databasePanel.setLayout(new BoxLayout(databasePanel, BoxLayout.PAGE_AXIS));
		databasePanel.add(databaseLabel);
		databasePanel.add(databaseScrollPane);
		databasePanel.add(databaseAdd);
		databasePanel.add(databaseNewData);
		databasePanel.add(databaseOperationJPanel, BorderLayout.SOUTH);


		// main panel
		contentPane.add(searchMatchingPanel, BorderLayout.BEFORE_FIRST_LINE);
		contentPane.add(databasePanel, BorderLayout.CENTER);

		scrollPane.setMinimumSize(new Dimension(600, 80));
		databaseScrollPane.setMinimumSize(new Dimension(600,150));
		databaseScrollPane.setPreferredSize(new Dimension(600,200));
		databaseNewData.setMaximumSize(new Dimension(600,80));
		
		loadDataSet();
	}

	JList loadDatabase(){
		senList = senDAO.findAll();
		ArrayList<String> sentencesList = new ArrayList<String>();
		for(Sentence sentence:senList){
			sentencesList.add(sentence.getName());
		}
		String[] sentences = sentencesList.toArray(new String[sentencesList.size()]);
		JList returnList = new JList(sentences);
		return returnList;
	}

	void setText(String text) {
		pane.setText(text);
		pane.setCaretPosition(0);
	}

	void loadDataSet() {
		// setTextを呼ぶ
		// dataSetに格納する
		setText(dataSet);
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
		// 標準出力を受け取る
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(new BufferedOutputStream(out)));
		DBMatching m = new DBMatching(); /// ここ重要！！！！！！！！！！
		m.main(searchTexts);

		// 結果の表示とボタンの表示
		System.out.flush();
		setText(out.toString());
		pane.setEditable(false);
		System.setOut(System.out);
	}

	void insert() {
		String text = databaseNewData.getText();
		Sentence x = new Sentence(text);
		senList = senDAO.insert(x); // レコードの追加
		JList dataList = loadDatabase();
		ListModel model = dataList.getModel();
		list.setModel(model);
		databaseNewData.setText("");
	}

	void delete() {
		String deleteString = String.valueOf(list.getSelectedValue());
		Sentence x = new Sentence(deleteString);
		senList = senDAO.delete(x); // レコードの削除
		JList dataList = loadDatabase();
		ListModel model = dataList.getModel();
		list.setModel(model);
	}
}

/*
 * 参考 https://qiita.com/boss_ape/items/b68b0bd6d85cf18d1626
 * https://www.javadrive.jp/tutorial/jbutton/
 */
