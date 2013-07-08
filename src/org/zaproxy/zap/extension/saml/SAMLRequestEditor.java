package org.zaproxy.zap.extension.saml;

import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;

import javax.swing.*;
import java.awt.*;
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
            }
            requestPanel.revalidate();
            getParams.put(urlParameter.getName(),urlParameter.getValue());
        }

        for (HtmlParameter formParameter : httpMessage.getFormParams()) {
            if(formParameter.getName().equals("SAMLRequest")||formParameter.getName().equals("SAMLResponse")){
                requestTextArea.setText(extractSAMLMessage(formParameter.getValue(), Binding.HTTPPost));  //decode and show
                // the saml message
                samlBinding = Binding.HTTPPost;
            }
            postParams.put(formParameter.getName(),formParameter.getValue());
        }
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
}
