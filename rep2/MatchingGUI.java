import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.util.regex.*;
import java.sql.*;

public class MatchingGUI{
    public static void main(String[] args) throws Exception{
	Screen s = new Screen("matching");
	s.setVisible(true);
    }
}

class Screen extends JFrame{
    Container contentPane=getContentPane();
    JPanel buttonPane = new JPanel();
    JTextPane pane = new JTextPane();
    JScrollPane scrollPane = new JScrollPane(pane);

    Screen(String title){
	setTitle(title);
	setBounds(100,100,600,400);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	pane.setEditable(false);

	JButton searchButton = new JButton("検索");
	JButton addButton = new JButton("追加");
	JButton deleteButton = new JButton("削除");
    
	buttonPane.add(searchButton);
	buttonPane.add(addButton);
	buttonPane.add(deleteButton);

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
	setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    }
}
