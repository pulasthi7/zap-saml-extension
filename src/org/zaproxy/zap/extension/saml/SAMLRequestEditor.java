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
    private JSplitPane responseSplitPane;
    private JTextArea requestTextArea;
    private JScrollPane requestScrollPane;
    private JPanel headerPanel;
    private JButton sendButton;
    private JButton editOtherParamButton;

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
        requestTextArea = new JTextArea();
        responseBodyTextArea = new JTextArea();
        responseHeaderTextArea = new JTextArea();
        requestPanel = new JPanel();
        responsePanel = new JPanel();
        responseSplitPane = new JSplitPane();
        requestScrollPane = new JScrollPane(requestTextArea);

        samlEditorPanel.setLayout(new BorderLayout());
        samlEditorPanel.add(tabbedPane1RequestResponse);
        tabbedPane1RequestResponse.add("Request",requestPanel);
        tabbedPane1RequestResponse.add("Response",responsePanel);

        requestPanel.setLayout(new BorderLayout());
        responsePanel.setLayout(new BorderLayout());

        headerPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        sendButton = new JButton("Resend");
        editOtherParamButton = new JButton("Edit other parameters");
        headerPanel.add(sendButton);
        headerPanel.add(editOtherParamButton);
        requestPanel.add(headerPanel,BorderLayout.PAGE_START);
        requestPanel.add(requestScrollPane,BorderLayout.CENTER);

        responsePanel.add(responseSplitPane);

        responseSplitPane.setTopComponent(responseHeaderTextArea);
        responseSplitPane.setBottomComponent(responseBodyTextArea);
        responseSplitPane.setResizeWeight(0.5);

        getParams = new HashMap<>();
        postParams = new HashMap<>();
        initButton();

    }

    private void initButton(){
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String samlMessage =  requestTextArea.getText();
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
                requestTextArea.setText(extractSAMLMessage(urlParameter.getValue(), Binding.HTTPRedirect));
                samlBinding = Binding.HTTPRedirect;
                samlParameter = urlParameter.getName();
            } else {
                getParams.put(urlParameter.getName(),urlParameter.getValue());
            }
        }

        for (HtmlParameter formParameter : httpMessage.getFormParams()) {
            if(formParameter.getName().equals("SAMLRequest")||formParameter.getName().equals("SAMLResponse")){
                requestTextArea.setText(extractSAMLMessage(formParameter.getValue(), Binding.HTTPPost));  //decode and show
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
