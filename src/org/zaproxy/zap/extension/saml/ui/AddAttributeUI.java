package org.zaproxy.zap.extension.saml.ui;

import org.zaproxy.zap.extension.saml.Attribute;
import org.zaproxy.zap.extension.saml.AttributeListener;
import org.zaproxy.zap.extension.saml.SamlI18n;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddAttributeUI extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldAttributeName;
    private JTextField textFieldViewName;
    private JTextField textFieldXpath;
    private AttributeListener attributeListener;

    /**
     * Create the frame.
     */
    public AddAttributeUI(AttributeListener l) {
        this.attributeListener = l;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        JLabel lblAddNewAttribute = new JLabel(SamlI18n.getMessage("saml.addattrib.header"));
        contentPane.add(lblAddNewAttribute, BorderLayout.PAGE_START);

        JPanel centerPanel = new JPanel();
        contentPane.add(centerPanel, BorderLayout.CENTER);
        centerPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.fill=GridBagConstraints.HORIZONTAL;

        JLabel lblAttributeName = new JLabel(SamlI18n.getMessage("saml.addattrib.attribname"));
        centerPanel.add(lblAttributeName, c);

        c.gridx++;

        textFieldAttributeName = new JTextField();
        centerPanel.add(textFieldAttributeName, c);

        c.gridx = 0;
        c.gridy++;

        JLabel lblAttributeViewName = new JLabel(SamlI18n.getMessage("saml.addattrib.attribviewname"));
        centerPanel.add(lblAttributeViewName,c);

        c.gridx++;

        textFieldViewName = new JTextField();
        centerPanel.add(textFieldViewName,c);

        c.gridx = 0;
        c.gridy++;
        JLabel lblXpath = new JLabel(SamlI18n.getMessage("saml.addattrib.attribxpath"));
        centerPanel.add(lblXpath,c);

        c.gridx++;
        textFieldXpath = new JTextField();
        centerPanel.add(textFieldXpath,c);

        c.gridx = 0;
        c.gridy++;

        JLabel lblValueType = new JLabel(SamlI18n.getMessage("saml.addattrib.attribvaluetype"));
        centerPanel.add(lblValueType,c);

        c.gridx++;
        final JComboBox<Attribute.SAMLAttributeValueType> comboBoxValueType = new JComboBox<>();
        centerPanel.add(comboBoxValueType,c);

        for (Attribute.SAMLAttributeValueType samlAttributeValueType : Attribute.SAMLAttributeValueType.values()) {
            comboBoxValueType.addItem(samlAttributeValueType);
        }

        JPanel bottomPanel = new JPanel();
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        JButton btnNewButton = new JButton(SamlI18n.getMessage("saml.addattrib.button.saveexit"));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String error = "";
                if (textFieldAttributeName.getText().equals("")) {
                    error = SamlI18n.getMessage("saml.addattrib.error.noname");
                }
                if (textFieldViewName.getText().equals("")) {
                    error += SamlI18n.getMessage("saml.addattrib.error.noviewname");
                }
                if (textFieldViewName.getText().length() > 30) {
                    error += SamlI18n.getMessage("saml.addattrib.error.longviewname");
                }
                if (textFieldXpath.getText().equals("")) {
                    error += SamlI18n.getMessage("saml.addattrib.error.noxpath");
                }

                //validate xpath expression
                XPathFactory xFactory = XPathFactory.newInstance();
                XPath xpath = xFactory.newXPath();
                try {
                    XPathExpression expression = xpath.compile(textFieldXpath.getText());
                } catch (XPathExpressionException e1) {
                    error += SamlI18n.getMessage("saml.addattrib.error.invalidxpath");
                }

                if (!error.equals("")) {
                    //Something wrong with inputs
                    JOptionPane.showMessageDialog(AddAttributeUI.this, error, SamlI18n.getMessage("saml.addattrib.error.error"), JOptionPane.ERROR_MESSAGE);
                } else {
                    //valid input
                    Attribute attribute = new Attribute();
                    attribute.setName(textFieldAttributeName.getText());
                    attribute.setViewName(textFieldViewName.getText());
                    attribute.setxPath(textFieldXpath.getText());
                    attribute.setValueType((Attribute.SAMLAttributeValueType) comboBoxValueType.getSelectedItem());
                    attributeListener.onAttributeAdd(attribute);
                    close();
                }

            }
        });
        bottomPanel.add(btnNewButton);

        JButton btnCancel = new JButton(SamlI18n.getMessage("saml.addattrib.button.cancel"));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(AddAttributeUI.this, SamlI18n.getMessage("saml.addattrib.msg.confirm"), SamlI18n.getMessage("saml.addattrib.msg.confirmexit"),
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    close();
                }

            }
        });
        bottomPanel.add(btnCancel);
    }

    private void close() {
        setVisible(false);
        dispose();
    }
}
