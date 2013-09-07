package org.zaproxy.zap.extension.saml.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

public class SamlManualEditor extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SamlManualEditor frame = new SamlManualEditor();
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
	public SamlManualEditor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 800, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel reqPanel = new JPanel();
		tabbedPane.addTab("Request", null, reqPanel, null);
		reqPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		reqPanel.add(topPanel, BorderLayout.NORTH);
		
		JLabel lblNote = new JLabel("Note : ");
		topPanel.add(lblNote);
		
		JPanel centerPanel = new JPanel();
		reqPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(2, 1, 0, 10));
		
		JScrollPane msgScrollPane = new JScrollPane();
		centerPanel.add(msgScrollPane);
		
		JTextPane msgPane = new JTextPane();
		msgScrollPane.setViewportView(msgPane);
		
		JScrollPane attribScrollPane = new JScrollPane();
		centerPanel.add(attribScrollPane);
		
		JPanel attributesPane = new JPanel();
		attribScrollPane.setViewportView(attributesPane);
		attributesPane.setLayout(new GridLayout(10, 1, 0, 5));
		
		JPanel bottomPanel = new JPanel();
		reqPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		JButton btnResend = new JButton("Resend");
		bottomPanel.add(btnResend);
		
		JButton btnReset = new JButton("Reset");
		bottomPanel.add(btnReset);
		
		JPanel respPanel = new JPanel();
		tabbedPane.addTab("Response", null, respPanel, null);
		respPanel.setLayout(new GridLayout(2, 1, 0, 15));
		
		JScrollPane resHeadScrollPane = new JScrollPane();
		respPanel.add(resHeadScrollPane);
		
		JTextPane respHeadTextPane = new JTextPane();
		resHeadScrollPane.setViewportView(respHeadTextPane);
		
		JScrollPane resBodyScrollPane = new JScrollPane();
		respPanel.add(resBodyScrollPane);
		
		JTextPane respBodyTextPane = new JTextPane();
		resBodyScrollPane.setViewportView(respBodyTextPane);
	}

}
