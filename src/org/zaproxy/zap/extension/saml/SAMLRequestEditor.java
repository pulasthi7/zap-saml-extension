package org.zaproxy.zap.extension.saml;

import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;

import javax.swing.*;
import java.awt.*;

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

        requestSplitPane.setTopComponent(requestHeaderTextArea);
        requestSplitPane.setBottomComponent(requestBodyTextArea);
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
            requestHeader.append(urlParameter.getName()).append(" : ").append(urlParameter.getValue()).append("\n");
        }
        StringBuilder requestParams = new StringBuilder();
        for (HtmlParameter formParameter : httpMessage.getFormParams()) {
            requestParams.append(formParameter.getName()).append(" : ").append(formParameter.getValue()).append("\n");
        }
        requestHeaderTextArea.setText(requestHeader.toString());
        requestBodyTextArea.setText(requestParams.toString());
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
