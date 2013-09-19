package org.zaproxy.zap.extension.saml.ui;

import org.zaproxy.zap.extension.saml.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;
import java.util.Set;

public class SamlExtentionSettingsUI extends JFrame implements AttributeChangeListener {

    private JScrollPane settingsScrollPane;
    private Set<Attribute> attributeSet;
    JCheckBox chckbxEnablePassiveChanger;
    JCheckBox chckbxRemoveMessageSignatures;
    JCheckBox chckbxValidateAttributeValue;

    /**
	 * Create the frame.
	 */
	public SamlExtentionSettingsUI(final SAMLProxyListener listener) {
		setTitle("SAML Automatic Request Changer Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 700);
		setLocationRelativeTo(null);
        JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLabel lblHeaderlabel = new JLabel("<html><h2>SAML Settings</h2></html>");
		contentPane.add(lblHeaderlabel, BorderLayout.NORTH);
		
		settingsScrollPane = new JScrollPane();
        contentPane.add(settingsScrollPane, BorderLayout.CENTER);
		


		JPanel footerPanel = new JPanel();
		contentPane.add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JButton btnAdd = new JButton("Add more attributes");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNewAttributeDialog dialog = new AddNewAttributeDialog(SamlExtentionSettingsUI.this);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            }
        });
		footerPanel.add(btnAdd);
		
		JButton btnSaveChanges = new JButton("Save Changes");
        btnSaveChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SAMLConfiguration samlConfiguration = SAMLConfiguration.getConfigurations();
                samlConfiguration.getAutoChangeAttributes().clear();
                samlConfiguration.getAutoChangeAttributes().addAll(attributeSet);
                listener.loadAutoChangeAttributes();
                samlConfiguration.setAutochangeEnabled(chckbxEnablePassiveChanger.isSelected());
                samlConfiguration.setXSWEnabled(chckbxRemoveMessageSignatures.isSelected());
                samlConfiguration.setValidationEnabled(chckbxValidateAttributeValue.isSelected());
                boolean success = samlConfiguration.saveConfiguration();
                if(success){
                    JOptionPane.showMessageDialog(SamlExtentionSettingsUI.this,"Changes saved","Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else{
                    JOptionPane.showMessageDialog(SamlExtentionSettingsUI.this,"Could not save changes. Please retry",
                            "Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
		footerPanel.add(btnSaveChanges);
		
		JButton btnResetChanges = new JButton("Reset changes");
        btnResetChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAutoChangeAttributes();
                initAttributes();
            }
        });
		footerPanel.add(btnResetChanges);
		
		JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SamlExtentionSettingsUI.this.setVisible(false);
            }
        });
		footerPanel.add(btnExit);
        loadAutoChangeAttributes();
        initAttributes();
	}

    private void loadAutoChangeAttributes(){
        attributeSet = new LinkedHashSet<>();
        for (Attribute autoChangeAttribute : SAMLConfiguration.getConfigurations().getAutoChangeAttributes()) {
            Attribute clonedAttribute = autoChangeAttribute.createCopy();
            clonedAttribute.setValue(autoChangeAttribute.getValue());
            attributeSet.add(clonedAttribute);
        }
    }

    private void initAttributes(){
        JPanel settingsPanel = new JPanel();
        settingsScrollPane.setViewportView(settingsPanel);
        settingsPanel.setLayout(new GridLayout(2, 1, 5, 15));
        
        JPanel globalSettingsPanel = new JPanel();
        globalSettingsPanel.setBorder(new TitledBorder(null, "Global Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        settingsPanel.add(globalSettingsPanel);
        globalSettingsPanel.setLayout(new BoxLayout(globalSettingsPanel, BoxLayout.Y_AXIS));

        SAMLConfiguration configuration = SAMLConfiguration.getConfigurations();
        chckbxEnablePassiveChanger = new JCheckBox("Enable Passive changer");
        chckbxEnablePassiveChanger.setSelected(configuration.getAutoChangeEnabled());
        globalSettingsPanel.add(chckbxEnablePassiveChanger);

        chckbxRemoveMessageSignatures = new JCheckBox("Remove message signatures");
        chckbxRemoveMessageSignatures.setSelected(configuration.getXSWEnabled());
        globalSettingsPanel.add(chckbxRemoveMessageSignatures);
        
        chckbxValidateAttributeValue = new JCheckBox("Validate attribute value types");
        chckbxValidateAttributeValue.setSelected(configuration.isValidationEnabled());
        globalSettingsPanel.add(chckbxValidateAttributeValue);
        JPanel attributePanel = new JPanel();
        attributePanel.setBorder(new TitledBorder(null, "Auto Change Attributes and Values", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        settingsPanel.add(attributePanel);
        attributePanel.setLayout(new GridLayout(Math.max(10,attributeSet.size()), 1, 0, 5));
        for (final Attribute attribute : attributeSet) {
            JPanel panel = new JPanel();
            attributePanel.add(panel);
            panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

            final JLabel lblAttribute = new JLabel(attribute.getViewName());
            Dimension size = lblAttribute.getPreferredSize();
            size.width = 200;
            lblAttribute.setMinimumSize(size);
            lblAttribute.setPreferredSize(size);
            panel.add(lblAttribute);

            JTextField txtValue = new JTextField();
            lblAttribute.setLabelFor(txtValue);
            txtValue.setText(attribute.getValue().toString());
            panel.add(txtValue);
            txtValue.setColumns(20);

            JButton btnAddeditValues = new JButton("Add/Edit Values");
            btnAddeditValues.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddNewAttributeDialog editDialog = new AddNewAttributeDialog(SamlExtentionSettingsUI.this);
                    editDialog.getComboBoxAttribSelect().removeAllItems();
                    editDialog.getComboBoxAttribSelect().addItem(attribute);
                    editDialog.getTxtAttribValues().setText(attribute.getValue().toString().replaceAll(",", "\n"));
                    editDialog.setVisible(true);
                }
            });
            panel.add(btnAddeditValues);

            JButton btnRemoveAttribute = new JButton("Remove Attribute");
            btnRemoveAttribute.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int response = JOptionPane.showConfirmDialog(SamlExtentionSettingsUI.this,
                            "Are you sure to remove the attribute","Confirm",JOptionPane.YES_NO_OPTION);
                    if(response == JOptionPane.YES_OPTION){
                        onDeleteDesiredAttribute(attribute);
                    }
                }
            });
            panel.add(btnRemoveAttribute);
        }

    }

    @Override
    public void onDesiredAttributeValueChange(Attribute attribute) {
        onAddDesiredAttribute(attribute);
        initAttributes();
    }

    @Override
    public void onAddDesiredAttribute(Attribute attribute) {
        attributeSet.add(attribute);
        initAttributes();
    }

    @Override
    public void onDeleteDesiredAttribute(Attribute attribute) {
        attributeSet.remove(attribute);
        initAttributes();
    }

    @Override
    public Set<Attribute> getDesiredAttributes() {
        return attributeSet;
    }
}
