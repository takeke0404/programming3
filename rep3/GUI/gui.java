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
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		JScrollPane ScrollPane_1 = new JScrollPane();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		// define element
		GraphDraw semanticNet = new GraphDraw();
		DrawFrame frames = new DrawFrame();
		JTable tableScrollPanel;

		JButton subject1 = new JButton("課題１");
		subject1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("subject1");
				ScrollPane_1.setViewportView(semanticNet);
			}
		});
		panel.add(subject1);
		
		JButton subject2 = new JButton("課題２");
		subject2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("subject2");
				ScrollPane_1.setViewportView(frames);

			}
		});
		panel.add(subject2);

		// フレーム情報を取得する
		ArrayList<String[]> data = readcsv("data.csv");
		// スロット値
		String[] columns = data.get(0);
		data.remove(0);
		String[][] frameData = new String[data.size()][];
		if(!data.isEmpty()) {
			if(!data.isEmpty()) {
				for(int j = 0; j < data.size(); j++){
					frameData[j] = data.get(j);
				}
			}
		}
		

		int x = 20;
		int y = 50;
		int counter = 0;
		for(int i=0;i<Math.max(3,Math.round(frameData.length/3));i++){
			x = 20;
			for(int j=0;j<3;j++){
				if(counter>frameData.length-1){
					break;
				}
				String[] row = frameData[counter];
				frames.addFrame(row, x, y);
				counter++;
				x+=250;
			}
			y+=210;
		}

		// セマンティックネット情報を取得する
		ArrayList<String[]> semanticNetData = readcsv("semanticNet.csv");
		columns = semanticNetData.get(0);
		semanticNetData.remove(0);
		String[][] tableData = new String[semanticNetData.size()][];
		if(!semanticNetData.isEmpty()) {
			if(!semanticNetData.isEmpty()) {
				for(int j = 0; j < semanticNetData.size(); j++){
					tableData[j] = semanticNetData.get(j);
				}
			}
		}
		

		x = 100;
		y = 80;

		counter = 0;
		for(int i=0;i<Math.max(6,Math.round(tableData.length/6));i++){
			x = 100;
			for(int j=0;j<6;j++){
				if(counter>tableData.length-1){
					break;
				}
				String[] row = tableData[counter];
				semanticNet.addNode(row,x,y);
				counter++;
				x+=120;
			}
			y+=200;
		}
		
		ScrollPane_1.setViewportView(semanticNet);
		contentPane.add(ScrollPane_1, BorderLayout.CENTER);
		ScrollPane_1.setVisible(true);
	}
	
	ArrayList<String[]> readcsv(String filename) {
		ArrayList<String[]> temp = new ArrayList<String[]>();
		try {
		      File f = new File(filename);
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
