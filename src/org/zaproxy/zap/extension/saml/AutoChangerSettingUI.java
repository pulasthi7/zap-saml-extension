package org.zaproxy.zap.extension.saml;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;

public class AutoChangerSettingUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AutoChangerSettingUI frame = new AutoChangerSettingUI();
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
	public AutoChangerSettingUI() {
		setTitle("SAML Automatic Request Changer Settings");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 700);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel lblHeaderlabel = new JLabel("<html><h2>Add/Edit autochange attributes/values</h2><p>Following attributes will be changed to the given values automatically. Add/Edit the attributes and values below</p></html>");
		contentPane.add(lblHeaderlabel, BorderLayout.NORTH);
		
		JPanel attributePanel = new JPanel();
		contentPane.add(attributePanel, BorderLayout.CENTER);
		
		JPanel footerPanel = new JPanel();
		contentPane.add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JButton btnAdd = new JButton("Add more attributes");
		footerPanel.add(btnAdd);
		btnAdd.setSize(50, 20);
		
		JButton btnSaveChanges = new JButton("Save Changes");
		footerPanel.add(btnSaveChanges);
		btnSaveChanges.setSize(50, 20);
		
		JButton btnResetChanges = new JButton("Reset changes");
		footerPanel.add(btnResetChanges);
		btnResetChanges.setSize(50, 20);
		
		JButton btnExit = new JButton("Exit");
		footerPanel.add(btnExit);
		btnExit.setSize(50, 20);
	}

}
