package org.zaproxy.zap.extension.saml;

import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class SAMLRequestEditor{
    private JPanel samlEditorPanel;
    private JTabbedPane tabbedPane1RequestResponse;
    private JTextArea responseHeaderTextArea;
    private JTextArea responseBodyTextArea;
    private JPanel requestPanel;
    private JPanel responsePanel;
    private JPanel footerPanel;
    private JPanel attribPanel;
    private JSplitPane responseSplitPane;
    private JTextArea samlMsgTxtArea;
    private JScrollPane reqAttribScrollPane;
    private JScrollPane samlMsgScrollPane;

    private JLabel lblWarningMsg;
    private JButton resendButton;
    private JButton resetButton;


    private Map<String,String> getParams;
    private Map<String,String> postParams;
    private Binding samlBinding;
    private HttpMessage httpMessage;
    private String samlParameter;


    public SAMLRequestEditor() throws HeadlessException {
        init();
    }

    /**
     * Initialize UI components and layouts
     */
    protected void init(){
        samlEditorPanel = new JPanel();
        tabbedPane1RequestResponse = new JTabbedPane();
        responseBodyTextArea = new JTextArea();
        responseHeaderTextArea = new JTextArea();
        requestPanel = new JPanel();
        responsePanel = new JPanel();
        footerPanel = new JPanel();
        attribPanel = new JPanel();
        responseSplitPane = new JSplitPane();
        samlMsgTxtArea = new JTextArea();
        reqAttribScrollPane = new JScrollPane();
        samlMsgScrollPane = new JScrollPane();
        resendButton = new JButton();
        resetButton = new JButton();
        lblWarningMsg = new JLabel();

        samlEditorPanel.setLayout(new BorderLayout());
        samlEditorPanel.add(tabbedPane1RequestResponse);
        tabbedPane1RequestResponse.add("Request",requestPanel);
        tabbedPane1RequestResponse.add("Response",responsePanel);

        requestPanel.setLayout(new BorderLayout());
        samlMsgScrollPane.setViewportView(samlMsgTxtArea);
        requestPanel.add(samlMsgScrollPane, BorderLayout.PAGE_START);

        attribPanel.setLayout(new java.awt.GridLayout(0, 1, 5, 5));
        initSAMLArributes();
        reqAttribScrollPane.setViewportView(attribPanel);

        requestPanel.add(reqAttribScrollPane,BorderLayout.CENTER);

        initFooter();

        requestPanel.add(footerPanel,BorderLayout.PAGE_END);

        responsePanel.setLayout(new BorderLayout());
        responsePanel.add(responseSplitPane);

        responseSplitPane.setTopComponent(responseHeaderTextArea);
        responseSplitPane.setBottomComponent(responseBodyTextArea);
        responseSplitPane.setResizeWeight(0.5);

        getParams = new HashMap<>();
        postParams = new HashMap<>();
        initButton();

    }

    private void initButton(){
        resendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String samlMessage = samlMsgTxtArea.getText();
                try {
                    HttpMessage message = SAMLResender.buildSAMLRequest(httpMessage, getParams, postParams,
                            samlParameter, samlMessage, samlBinding);
                    SAMLResender.resendMessage(message);
                    updateResponse(message);
                } catch (SAMLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void initSAMLArributes(){
        Map<String, String> samlAttributes = getSAMLAttributes();
        for (Map.Entry<String, String> entry : samlAttributes.entrySet()) {
            JSplitPane sPane = new JSplitPane();
            JLabel lbl = new JLabel();
            JTextField txtValue = new JTextField();

            sPane.setDividerLocation(100);
            sPane.setDividerSize(0);

            lbl.setText(entry.getKey());
            sPane.setLeftComponent(lbl);

            txtValue.setText(entry.getValue());
            sPane.setRightComponent(txtValue);
            attribPanel.add(sPane);
        }
    }

    private void initFooter(){
        footerPanel.setLayout(new java.awt.GridLayout(2, 1));
        lblWarningMsg.setText("Warning"); //TODO change the warning
        JSplitPane buttonSplitPane = new JSplitPane();
        buttonSplitPane.setDividerSize(0);
        buttonSplitPane.setLeftComponent(resendButton);
        buttonSplitPane.setRightComponent(resetButton);
        resendButton.setText("Resend");
        resetButton.setText("Reset");
        footerPanel.add(buttonSplitPane);
        footerPanel.add(lblWarningMsg);
    }

    private Map<String,String> getSAMLAttributes(){
        //TODO sample implementation, need to be changed
        Map<String,String> attribMap = new HashMap<>();
        attribMap.put("issuer","SSOSampleApp");
        attribMap.put("Attribute 2","Value 2");
        attribMap.put("Attribute 3","Value 3");
        attribMap.put("Attribute 4","Value 4");
        attribMap.put("Attribute 5","Value 5");
        attribMap.put("Attribute 6","Value 6");
        attribMap.put("Attribute 7","Value 7");
        attribMap.put("Attribute 8","Value 8");
        attribMap.put("Attribute 9","Value 9");
        attribMap.put("Attribute 10","Value 10");
        attribMap.put("Attribute 11","Value 11");
        attribMap.put("Attribute 12","Value 12");
        return attribMap;
    }

    private void updateResponse(HttpMessage msg){
        System.out.println(msg.getResponseBody().createCachedString("UTF-8"));

    }

    /**
     * Set the request tab with the original request that was sent
     * @param httpMessage The message that was exchanged
     */
    public void setMessage(HttpMessage httpMessage){
        for (HtmlParameter urlParameter : httpMessage.getUrlParams()) {
            if(urlParameter.getName().equals("SAMLRequest")||urlParameter.getName().equals("SAMLResponse")){
                samlMsgTxtArea.setText(extractSAMLMessage(urlParameter.getValue(), Binding.HTTPRedirect));
                samlBinding = Binding.HTTPRedirect;
                samlParameter = urlParameter.getName();
            } else {
                getParams.put(urlParameter.getName(),urlParameter.getValue());
            }
        }

        for (HtmlParameter formParameter : httpMessage.getFormParams()) {
            if(formParameter.getName().equals("SAMLRequest")||formParameter.getName().equals("SAMLResponse")){
                samlMsgTxtArea.setText(extractSAMLMessage(formParameter.getValue(), Binding.HTTPPost));  //decode and show
                // the saml message
                samlBinding = Binding.HTTPPost;
                samlParameter = formParameter.getName();
            }else {
                postParams.put(formParameter.getName(),formParameter.getValue());
            }
        }
        this.httpMessage = httpMessage;
    }

    /**
     * Decode the SAML messages based on the binding used
     * @param val the SAML message to decode
     * @param binding The binding used
     * @return The decoded SAML message if success, or the original string if failed
     */
    private String extractSAMLMessage(String val, Binding binding){
        try {
            switch (binding) {
                case HTTPPost:
                case HTTPRedirect:
                    val = URLDecoder.decode(val,"UTF-8");

                    byte[] b64decoded = SAMLUtils.b64Decode(val);
                    String rawMessage = SAMLUtils.inflateMessage(b64decoded);
                    return SAMLUtils.prettyFormatXML(rawMessage);
                default:
                    break;
            }
        } catch (UnsupportedEncodingException e) {

        } catch (SAMLException e) {

        }
        return val;
    }


    /**
     * Shows the extension UI
     */
    public void showUI(){
        JFrame frame = new JFrame("SAML Request editor");
        frame.setSize(640,480);
        frame.setLayout(new BorderLayout());
        frame.setContentPane(samlEditorPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws URIException, HttpMalformedHeaderException {
        SAMLRequestEditor requestEditor = new SAMLRequestEditor();
        HttpMessage msg= new HttpMessage(new HttpsURL("https://localhost:9443/samlsso?SAMLRequest=jZNPb%2BIwEMW%2FiuV7SPgnwCJUlKpapO42S9I99OaaoVhy7KxnQtlvv05CdjlUqFfP85t5v7GXd%2BfSsBN41M6mfDhIOAOr3F7b95S%2FFI%2FRnN%2BtlihLU4l1TUe7g981ILFwz6JoCymvvRVOokZhZQkoSIl8%2Ff1JjAaJqLwjp5zhbI0InkKjjbNYl%2BBz8Cet4GX3lPIjUSXi2DglzdEhiXkyT%2BI8f85lWRlYV1WM6Ix71zY4EXn9VhN0TmHYi9XW7uEccozGi9FkupgsOHt0XkE7esoP0iBwtn1IeQi6xUwi6hP8LyDWwQNJWkr5KBmOo2QWJeNiuBCjmZgkg%2Bls%2BspZdsl0r21H6haAt06E4ltRZFH2nBec%2FeqJBwHv%2Bbbd%2FdfJyp4nX11zWsbXdr35j3B9%2B5A5o9UftjbGfWw8SArZydfQYiol3W7YnOh9dGilomoyIIElzvKs8f9ZS6MPGnzKu%2BY87ttfng3s202EtRGciW1cWUmvsSEBZ6nowkJcqzYmBN3B4QrMl7nclCmhGutw3LyDD%2Bf3zV5BhSkLLy1WzlMH89N5Vj3oT7P9q17%2FmtVf&RelayState=null"));
        requestEditor.setMessage(msg);
        requestEditor.showUI();
    }
}
