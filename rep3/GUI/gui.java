import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JLayeredPane;
import javax.swing.JTable;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
 

public class gui extends JFrame {

	private JPanel contentPane;
	private JTable table;

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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton subject1 = new JButton("課題１");
		subject1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("subject1");
			}
		});
		panel.add(subject1);
		
		JButton subject2 = new JButton("課題２");
		subject2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("subject2");
			}
		});
		panel.add(subject2);
		
		ArrayList<String[]> data = readcsv();
		// スロット値
//		String[] columns = {"studentNo", "major", "field", "laboName", "hobby", "language"};
		if(!data.isEmpty()) {
			String[] columns = data.get(0);
			data.remove(0);
			String[][] tableData = new String[data.size()][];
			if(!data.isEmpty()) {
				for(int j = 0; j < data.size(); j++){
					tableData[j] = data.get(j);
				}
				// draw table
				DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
				table = new JTable(tableModel);
				for(int i = 0 ; i < data.size() ; i++){
				    tableModel.addRow(tableData[i]);
				}
			}
		}
		JScrollPane sp = new JScrollPane(table);
		contentPane.add(sp, BorderLayout.CENTER);
		
	}
	
	ArrayList<String[]> readcsv() {
		ArrayList<String[]> temp = new ArrayList<String[]>();
		try {
		      File f = new File("data.csv");
		      BufferedReader br = new BufferedReader(new FileReader(f));
		      String line;
		      // 1行ずつCSVファイルを読み込む
		      while ((line = br.readLine()) != null) {
		        String[] data1 = line.split(",", 0); // 行をカンマ区切りで配列に変換
		        temp.add(data1);
		      }
		      br.close();
		    } catch (IOException e) {
		      System.out.println(e);
		    }
		return temp;
		
	}

}
