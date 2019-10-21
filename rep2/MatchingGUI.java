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
    JPanel buttonPane = new JPanel();
    JTextPane pane = new JTextPane();
    JScrollPane scrollPane = new JScrollPane(pane);
    private static JFrame frame = new JFrame();

    Screen(String title){
	setTitle(title);
	setBounds(660,340,600,400);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	pane.setEditable(true);

	JButton searchButton = new JButton("検索");
	JButton saveButton = new JButton("保存");

	saveButton.addActionListener(
	    new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    saveDataSet();
		    JLabel msg = new JLabel("保存されました");
		    JOptionPane.showMessageDialog(frame, msg);
		}
	    }
	);
    
	buttonPane.add(searchButton);
	buttonPane.add(saveButton);

	contentPane.add(scrollPane, BorderLayout.CENTER);
	contentPane.add(buttonPane, BorderLayout.SOUTH);

	loadDataSet();
    }

    void setText(String text){
	pane.setText(text);
	pane.setCaretPosition(0);
    }

    void loadDataSet(){
	//setTextを呼ぶ
	setText("aaaaaaaaaaaaaaaaaa\naaaaaaaaaaaaaaaaaaa");
    }

    void saveDataSet(){
	//saveDataSet
    }
}
