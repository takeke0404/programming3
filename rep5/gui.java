package swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class gui extends JFrame {

	private JPanel contentPane;

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
		contentPane.add(operatorPanel, BorderLayout.NORTH);
		
		JButton initButton = new JButton("初期状態");
		initButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		operatorPanel.add(initButton);
		
		JButton goalButton = new JButton("目標状態");
		goalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		operatorPanel.add(goalButton);
		
		JButton runButton = new JButton("実行");
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVisible(true);
		
		JTextPane textPanel = new JTextPane();
		textPanel.setText("test");
		GroupLayout gl_viewPanel = new GroupLayout(viewPanel);
		gl_viewPanel.setHorizontalGroup(
			gl_viewPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_viewPanel.createSequentialGroup()
					.addGroup(gl_viewPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 0, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, gl_viewPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(textPanel, GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_viewPanel.setVerticalGroup(
			gl_viewPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_viewPanel.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 0, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textPanel, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE))
		);
		viewPanel.setLayout(gl_viewPanel);
		
		JPanel statePanel = new JPanel();
		contentPane.add(statePanel, BorderLayout.SOUTH);
		
		JButton previousState = new JButton("前の状態");
		previousState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		statePanel.add(previousState);
		
		JButton nextState = new JButton("次の状態");
		statePanel.add(nextState);
	}
}
