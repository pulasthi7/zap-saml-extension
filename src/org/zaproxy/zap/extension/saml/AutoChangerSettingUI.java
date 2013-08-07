package org.zaproxy.zap.extension.saml;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JTextField;

public class AutoChangerSettingUI extends JFrame {

	private JPanel contentPane;
    private JPanel attributePanel;

    private XMLConfiguration configutation;

    private Map<String,String> valueMap;
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
		
		JScrollPane attributeScrollPane = new JScrollPane();
		contentPane.add(attributeScrollPane, BorderLayout.CENTER);
		
		attributePanel = new JPanel();
		attributeScrollPane.setViewportView(attributePanel);
		attributePanel.setLayout(new GridLayout(15, 1, 0, 0));

		JPanel footerPanel = new JPanel();
		contentPane.add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JButton btnAdd = new JButton("Add more attributes");
		footerPanel.add(btnAdd);
		
		JButton btnSaveChanges = new JButton("Save Changes");
		footerPanel.add(btnSaveChanges);
		
		JButton btnResetChanges = new JButton("Reset changes");
		footerPanel.add(btnResetChanges);
		
		JButton btnExit = new JButton("Exit");
		footerPanel.add(btnExit);
	}

    private void initConfigutations() throws SAMLException {
        configutation = (XMLConfiguration) SAMLUtils.getConfigutation();
        valueMap = new LinkedHashMap<>();
        List<HierarchicalConfiguration> autochangeAttributes = configutation.configurationsAt("autochange.attributes");
        for (HierarchicalConfiguration configuration : autochangeAttributes) {
            valueMap.put(configuration.getString("name"),configuration.getString("values"));
        }
    }

    private void initAttributes(){

        for (Map.Entry<String, String> entry : valueMap.entrySet()) {
            JPanel panel = new JPanel();
            attributePanel.add(panel);
            panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

            JLabel lblAttribute = new JLabel(entry.getKey());
            panel.add(lblAttribute);

            JTextField txtValue = new JTextField();
            lblAttribute.setLabelFor(txtValue);
            txtValue.setText(entry.getValue());
            panel.add(txtValue);
            txtValue.setColumns(20);

            JButton btnAddeditValues = new JButton("Add/Edit Values");
            panel.add(btnAddeditValues);

            JButton btnRemoveAttribute = new JButton("Remove Attribute");
            panel.add(btnRemoveAttribute);
        }
    }
}
