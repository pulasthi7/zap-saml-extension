package org.zaproxy.zap.extension.saml;

import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class SAMLRequestEditor{
    private JPanel samlEditorPanel;
    private JTabbedPane tabbedPane1RequestResponse;
    private JTextArea responseHeaderTextArea;
    private JTextArea responseBodyTextArea;
    private JPanel requestPanel;
    private JPanel responsePanel;
    private JSplitPane responseSplitPane;
    private JTextArea requestBodyTextArea;
    private JTextArea requestHeaderTextArea;
    private JSplitPane requestSplitPane;
    private JScrollPane requestHeaderPane;
    private JScrollPane requestBodyPane;

    public SAMLRequestEditor() throws HeadlessException {
        init();
    }

    /**
     * Initialize UI components and layouts
     */
    protected void init(){
        samlEditorPanel = new JPanel();
        tabbedPane1RequestResponse = new JTabbedPane();
        requestBodyTextArea = new JTextArea();
        responseBodyTextArea = new JTextArea();
        requestHeaderTextArea = new JTextArea();
        responseHeaderTextArea = new JTextArea();
        requestPanel = new JPanel();
        responsePanel = new JPanel();
        responseSplitPane = new JSplitPane();
        requestSplitPane = new JSplitPane();
        requestBodyPane = new JScrollPane(requestBodyTextArea);
        requestHeaderPane = new JScrollPane(requestHeaderTextArea);

        samlEditorPanel.setLayout(new BorderLayout());
        samlEditorPanel.add(tabbedPane1RequestResponse);
        tabbedPane1RequestResponse.add("Request",requestPanel);
        tabbedPane1RequestResponse.add("Response",responsePanel);

        requestPanel.setLayout(new BorderLayout());
        responsePanel.setLayout(new BorderLayout());

        requestPanel.add(requestSplitPane);
        responsePanel.add(responseSplitPane);

        responseSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        requestSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        requestSplitPane.setTopComponent(requestHeaderPane);
        requestSplitPane.setBottomComponent(requestBodyPane);
        requestSplitPane.setResizeWeight(0.5);


        responseSplitPane.setTopComponent(responseHeaderTextArea);
        responseSplitPane.setBottomComponent(responseBodyTextArea);
        responseSplitPane.setResizeWeight(0.5);
    }

    /**
     * Set the request tab with the original request that was sent
     * @param httpMessage The message that was exchanged
     */
    public void setMessage(HttpMessage httpMessage){
        StringBuilder requestHeader = new StringBuilder();

        for (HtmlParameter urlParameter : httpMessage.getUrlParams()) {
            requestHeader.append(urlParameter.getName()).append(" : ");
            if(urlParameter.getName().equals("SAMLRequest")||urlParameter.getName().equals("SAMLResponse")){
                requestHeader.append(extractSAMLMessage(urlParameter.getValue(),Binding.HTTPRedirect));  //decode and show the saml message
            } else {
                requestHeader.append(urlParameter.getValue());  //show the raw parameter
            }
            requestHeader.append("\n");
        }
        StringBuilder requestParams = new StringBuilder();
        for (HtmlParameter formParameter : httpMessage.getFormParams()) {
            requestParams.append(formParameter.getName()).append(" : ");
            if(formParameter.getName().equals("SAMLRequest")||formParameter.getName().equals("SAMLResponse")){
                requestParams.append(extractSAMLMessage(formParameter.getValue(),Binding.HTTPPost));  //decode and show the saml
                // message
            } else {
                requestParams.append(formParameter.getValue());  //show the raw parameter
            }
            requestParams.append("\n");
        }
        requestHeaderTextArea.setText(requestHeader.toString());
        requestBodyTextArea.setText(requestParams.toString());
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
                    val = URLDecoder.decode(val,"UTF-8");
                case HTTPRedirect:
                    byte[] b64decoded = SAMLUtils.b64Decode(val);
                    return SAMLUtils.inflateMessage(b64decoded);
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
}
