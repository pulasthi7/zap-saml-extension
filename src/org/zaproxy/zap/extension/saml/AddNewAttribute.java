package org.zaproxy.zap.extension.saml;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddNewAttribute extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblAttributeName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddNewAttribute dialog = new AddNewAttribute();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddNewAttribute() {
		setTitle("Add New Attribute");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel attribNamePanel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) attribNamePanel.getLayout();
			flowLayout.setHgap(0);
			flowLayout.setAlignment(FlowLayout.LEFT);
			contentPanel.add(attribNamePanel, BorderLayout.NORTH);
			{
				lblAttributeName = new JLabel("Attribute Name");
				attribNamePanel.add(lblAttributeName);
			}
			{
				JComboBox comboBoxAttribSelect = new JComboBox();
				lblAttributeName.setLabelFor(comboBoxAttribSelect);
				comboBoxAttribSelect.setMaximumRowCount(5);
				attribNamePanel.add(comboBoxAttribSelect);
			}
		}
		{
			JPanel attribValuesPanel = new JPanel();
			contentPanel.add(attribValuesPanel, BorderLayout.CENTER);
			attribValuesPanel.setLayout(new BorderLayout(5, 5));
			{
				JLabel lblAttributeValues = new JLabel("Attribute Values");
				lblAttributeValues.setHorizontalAlignment(SwingConstants.LEFT);
				attribValuesPanel.add(lblAttributeValues, BorderLayout.NORTH);
			}
			{
				JScrollPane scrollPaneAttribValues = new JScrollPane();
				attribValuesPanel.add(scrollPaneAttribValues, BorderLayout.CENTER);
				{
					JTextArea textAreaAttribValues = new JTextArea();
					scrollPaneAttribValues.setViewportView(textAreaAttribValues);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
