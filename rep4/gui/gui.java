
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.ComponentOrientation;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import javax.swing.JTextPane;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class gui extends JFrame {

	private JPanel contentPane;
	private JTextField queryInputField;
	private String databaseFilePath = "./../Original.data";
	private boolean searchFlag = false;
	private boolean backwardChainMode = false;

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
		// read data
		String str = "";

		str = read_str(databaseFilePath);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel queryPanel = new JPanel();
		JPanel databasePanel = new JPanel();
		contentPane.add(queryPanel, BorderLayout.NORTH);

		JLabel queryLabel = new JLabel("Query");
		JLabel SaveStatusLabel = new JLabel("Saved!");
		JButton searchButton = new JButton("Search");
		JButton saveButton = new JButton("Save");
		JButton BackwardChainButton = new JButton("Backward Chain");
		JButton ForwardChainButton = new JButton("Forward Chain");
		JTextPane databaseText = new JTextPane();
		JLabel scrollPaneLabel = new JLabel(
				"Edit (add, delete, change) rules. Click the Save button to save your changes.");

		queryInputField = new JTextField();
		queryInputField.setMinimumSize(new Dimension(500, 30));
		queryInputField.setText("What is made in USA");
		queryInputField.setColumns(10);
		GroupLayout gl_queryPanel = new GroupLayout(queryPanel);
		gl_queryPanel.setHorizontalGroup(gl_queryPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_queryPanel.createSequentialGroup().addGap(20)
						.addComponent(queryLabel, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(queryInputField, GroupLayout.PREFERRED_SIZE, 615, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(searchButton).addGap(16)));
		gl_queryPanel.setVerticalGroup(gl_queryPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_queryPanel
				.createSequentialGroup()
				.addGroup(gl_queryPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_queryPanel.createSequentialGroup().addGap(11).addComponent(queryLabel))
						.addGroup(gl_queryPanel.createSequentialGroup().addGap(5).addComponent(searchButton))
						.addGroup(gl_queryPanel.createSequentialGroup().addContainerGap().addComponent(queryInputField,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		queryPanel.setLayout(gl_queryPanel);

		contentPane.add(databasePanel, BorderLayout.CENTER);
		databaseText.setText(str);
		JScrollPane scrollPane = new JScrollPane(databaseText);
		SaveStatusLabel.setVisible(false);
		BackwardChainButton.setVisible(false);
		ForwardChainButton.setVisible(false);

		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SaveStatusLabel.setVisible(false);
			}
		};

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (searchFlag) {
					scrollPane.setViewportView(databaseText);
					scrollPane.repaint();
					saveButton.setText("Save");
					scrollPaneLabel
							.setText("Edit (add, delete, change) rules. Click the Save button to save your changes.");
					BackwardChainButton.setVisible(false);
				} else {
					try {
						File file = new File(databaseFilePath);
						if (checkBeforeWritefile(file)) {
							FileWriter filewriter = new FileWriter(file, false);
							filewriter.write(databaseText.getText());
							filewriter.close();
							SaveStatusLabel.setVisible(true);
							int delay = 2000; // milliseconds
							new javax.swing.Timer(delay, taskPerformer).start();
						}
					} catch (IOException e1) {
						System.out.println(e1);
					}
				}
			}
		});

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 質問の処理
				RuleBaseSystem backwardChainInstance = new RuleBaseSystem();
				backwardChainInstance.setQuery(queryInputField.getText());
				System.out.println(queryInputField.getText());
				String answer = backwardChainInstance.getAnswer();
				// View 処理
				// 初期値：前向き処理を表示させる
				JTextPane answerPane = new JTextPane();
				answerPane.setText(answer);
				scrollPane.setViewportView(answerPane);
				scrollPane.repaint();
				saveButton.setText("Back");
				scrollPaneLabel.setText("Click the Forward/Backward Chain button to view the inference process");
				searchFlag = true;
				BackwardChainButton.setVisible(true);
				ForwardChainButton.setVisible(true);
			}
		});

		BackwardChainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 後ろ向き処理の表示
				scrollPaneLabel.setText("Switch to Forward Chain by click to Forward Chain button");

			}
		});

		ForwardChainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 前向き処理の表示
				ForwardGraphDraw forwardGraph = new ForwardGraphDraw();
				scrollPaneLabel.setText("Switch to Backward Chain by click to Backward Chain button");
				scrollPane.setViewportView(forwardGraph);
				scrollPane.repaint();
			}
		});

		GroupLayout gl_databasePanel = new GroupLayout(databasePanel);
		gl_databasePanel.setHorizontalGroup(gl_databasePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_databasePanel.createSequentialGroup().addGroup(gl_databasePanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_databasePanel.createSequentialGroup().addContainerGap().addComponent(saveButton)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(SaveStatusLabel))
						.addGroup(gl_databasePanel.createSequentialGroup().addGap(6).addGroup(gl_databasePanel
								.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_databasePanel.createSequentialGroup().addComponent(scrollPaneLabel)
										.addPreferredGap(ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
										.addComponent(ForwardChainButton).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(BackwardChainButton))
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE))))
						.addContainerGap()));
		gl_databasePanel
				.setVerticalGroup(gl_databasePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_databasePanel.createSequentialGroup().addContainerGap()
								.addGroup(gl_databasePanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(scrollPaneLabel, GroupLayout.PREFERRED_SIZE, 29,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(BackwardChainButton).addComponent(ForwardChainButton))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 443, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_databasePanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(saveButton).addComponent(SaveStatusLabel))
								.addContainerGap(8, Short.MAX_VALUE)));
		databasePanel.setLayout(gl_databasePanel);
	}

	public List<String> read_data(String filePath) {
		List<String> data = new ArrayList<String>();
		// Java 8以降
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		Path path = FileSystems.getDefault().getPath(filePath);
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String text;
			String rule = "";
			while ((text = br.readLine()) != null) {
				if (text.trim().isEmpty()) {
					data.add(rule);
					rule = "";
				} else {
					rule += text + "\n";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public String read_str(String filePath) {
		String str = "";
		try {
			File file = new File(filePath);

			// ファイルが存在しない場合に例外が発生するので確認する
			if (!file.exists()) {
				System.out.print("ファイルが存在しません");
			}
			FileReader fileReader = new FileReader(file);
			int data;
			while ((data = fileReader.read()) != -1) {
				str += (char) data;
			}
			fileReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	private static boolean checkBeforeWritefile(File file) {
		if (file.exists()) {
			if (file.isFile() && file.canWrite()) {
				return true;
			}
		}

		return false;
	}
}
