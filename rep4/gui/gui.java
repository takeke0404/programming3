

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
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;

public class gui extends JFrame {

	private JPanel contentPane;
	private JTextField queryInputField;

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
		
		JPanel queryPanel = new JPanel();
		contentPane.add(queryPanel, BorderLayout.NORTH);
		
		JLabel queryLabel = new JLabel("Query");
		
		queryInputField = new JTextField();
		queryInputField.setMinimumSize(new Dimension(500, 30));
		queryInputField.setText("Input field");
		queryInputField.setColumns(10);
		
		JButton searchButton = new JButton("Search");
		GroupLayout gl_queryPanel = new GroupLayout(queryPanel);
		gl_queryPanel.setHorizontalGroup(
			gl_queryPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_queryPanel.createSequentialGroup()
					.addGap(20)
					.addComponent(queryLabel, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(queryInputField, GroupLayout.PREFERRED_SIZE, 615, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(searchButton)
					.addGap(16))
		);
		gl_queryPanel.setVerticalGroup(
			gl_queryPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_queryPanel.createSequentialGroup()
					.addGroup(gl_queryPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_queryPanel.createSequentialGroup()
							.addGap(11)
							.addComponent(queryLabel))
						.addGroup(gl_queryPanel.createSequentialGroup()
							.addGap(5)
							.addComponent(searchButton))
						.addGroup(gl_queryPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(queryInputField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		queryPanel.setLayout(gl_queryPanel);
		
		JPanel databasePanel = new JPanel();
		contentPane.add(databasePanel, BorderLayout.CENTER);
		
		JButton addNewRuleButton = new JButton("Add new rule");
		addNewRuleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_databasePanel = new GroupLayout(databasePanel);
		gl_databasePanel.setHorizontalGroup(
			gl_databasePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_databasePanel.createSequentialGroup()
					.addComponent(addNewRuleButton)
					.addGap(173))
				.addGroup(gl_databasePanel.createSequentialGroup()
					.addGap(6)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_databasePanel.setVerticalGroup(
			gl_databasePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_databasePanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(addNewRuleButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE))
		);
		databasePanel.setLayout(gl_databasePanel);
	}
}
