package org.zaproxy.zap.extension.saml;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.parosproxy.paros.view.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AutoChangerSettingUI extends JFrame implements DesiredAttributeChangeListner {

	private JPanel contentPane;
    private JPanel attributePanel;

    private XMLConfiguration configuration;

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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNewAttribute dialog = new AddNewAttribute();
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            }
        });
		footerPanel.add(btnAdd);
		
		JButton btnSaveChanges = new JButton("Save Changes");
        btnSaveChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<HierarchicalConfiguration> autochangeAttributes = configuration.configurationsAt("autochange.attributes");
                    for (HierarchicalConfiguration configuration : autochangeAttributes) {
                        valueMap.put(configuration.getString("name"),configuration.getString("values"));
                    }
                    configuration.save();
                } catch (ConfigurationException e1) {
                    View.getSingleton().showWarningDialog("Save Failed");
                }
            }
        });
		footerPanel.add(btnSaveChanges);
		
		JButton btnResetChanges = new JButton("Reset changes");
        btnResetChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initConfigurations();
                initAttributes();
            }
        });
		footerPanel.add(btnResetChanges);
		
		JButton btnExit = new JButton("Exit");
		footerPanel.add(btnExit);
        initConfigurations();
        initAttributes();
	}

    private void initConfigurations() {
        configuration = (XMLConfiguration) SAMLUtils.getConfigurations();
        valueMap = new LinkedHashMap<>();
        List<HierarchicalConfiguration> autochangeAttributes = configuration.configurationsAt("autochange.attributes");
        for (HierarchicalConfiguration configuration : autochangeAttributes) {
            valueMap.put(configuration.getString("name"),configuration.getString("values"));
        }
        //todo : Breakadddialog class org.zaproxy.zap.extension.brk.impl.http
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

    @Override
    public void onDesiredAttributeValueChange(String attribute, String value) {
        onAddDesiredAttribute(attribute,value);
        initAttributes();
    }

    @Override
    public void onAddDesiredAttribute(String attribute, String values) {
        valueMap.put(attribute,values);
        initAttributes();
    }

    @Override
    public void onDeleteDesiredAttribute(String attribute) {
        if(valueMap.containsKey(attribute)){
            valueMap.remove(attribute);
            initAttributes();
        }
    }

    @Override
    public Set<String> getDesiredAttributes() {
        return valueMap.keySet();
    }
}
