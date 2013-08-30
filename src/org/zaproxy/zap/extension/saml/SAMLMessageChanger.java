package org.zaproxy.zap.extension.saml;

import org.joda.time.DateTime;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

public class SAMLMessageChanger {

    private boolean messageChanged;
    private HttpMessage httpMessage;
    private String samlMessageString;
    private String relayState;
    private Document xmlDocument;
    private Map<String, Attribute> attributeMap;

    /**
     * Create a new saml message object from the httpmessage
     * @param httpMessage
     * @throws SAMLException
     */
    public SAMLMessageChanger(HttpMessage httpMessage) throws SAMLException {
        this.httpMessage = httpMessage;
        messageChanged = false;
        init();
    }

    /**
     * Process and initialize saml attribute values
     * @throws SAMLException
     */
    private void init() throws SAMLException {
        processHTTPMessage();
        buildXMLDocument();
        buildAttributeMap();
    }

    /**
     * Process the httpmessage and get the saml message and relay state
     * @throws SAMLException
     */
    private void processHTTPMessage() throws SAMLException {
        //check whether a saml message
        if(!SAMLUtils.hasSAMLMessage(httpMessage)){
            throw new SAMLException("Not a SAML Message");
        }

        //get the saml message from the parameters
        for (HtmlParameter urlParameter : httpMessage.getUrlParams()) {
            if (urlParameter.getName().equals("SAMLRequest") || urlParameter.getName().equals("SAMLResponse")) {
                samlMessageString = SAMLUtils.extractSAMLMessage(urlParameter.getValue(), Binding.HTTPRedirect);

            } else if (urlParameter.getName().equals("RelayState")) {
                relayState = urlParameter.getValue();
            }
        }
        for (HtmlParameter formParameter : httpMessage.getFormParams()) {
            if (formParameter.getName().equals("SAMLRequest") || formParameter.getName().equals("SAMLResponse")) {
                samlMessageString = SAMLUtils.extractSAMLMessage(formParameter.getValue(), Binding.HTTPPost);
            } else if (formParameter.getName().equals("RelayState")) {
                relayState = formParameter.getValue();
            }
        }



    }

    /**
     * Build XML document to be manipulated
     * @throws SAMLException
     */
    private void buildXMLDocument() throws SAMLException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            xmlDocument = docBuilder.parse(new InputSource(new StringReader(samlMessageString)));
            xmlDocument.getDocumentElement ().normalize ();
        } catch (ParserConfigurationException|SAXException|IOException e) {
            throw new SAMLException("XML document building failed");
        }
    }

    /**
     * Get the saml attributes from the saml message which are also in the configured list
     * @throws SAMLException
     */
    private void buildAttributeMap() throws SAMLException {
        // xpath initialization
        XPathFactory xFactory = XPathFactory.newInstance();
        XPath xpath = xFactory.newXPath();
        Set<Attribute> allAttributes = SAMLConfiguration.getConfiguration().getAvailableAttributes().getAttributes();
        for (Attribute attribute : allAttributes) {
            try {
                XPathExpression expression = xpath.compile(attribute.getxPath());
                String value = expression.evaluate(xmlDocument);
                Attribute newAttrib = (Attribute) attribute.clone();
                newAttrib.setValue(value);
                attributeMap.put(attribute.getName(),newAttrib);
            } catch (XPathExpressionException e) {
                //can't be evaluated
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (CloneNotSupportedException e) {
                //won't be thrown
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    /**
     * Check whether the values are applicable to the selected attribute
     * @param type
     * @param value
     * @return
     */
    private Object validateValueType(Attribute.SAMLAttributeValueType type, String value){
        try {
            switch (type){
                case String:
                    return value;
                case Decimal:
                    return Double.valueOf(value);
                case Integer:
                    return Integer.parseInt(value);
                case TimeStamp:
                    return DateTime.parse(value);
                default:
                    return value;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void updateXMLDocument(){
        XPathFactory xFactory = XPathFactory.newInstance();
        XPath xpath = xFactory.newXPath();
        for (Attribute attribute : attributeMap.values()) {
            try {
                NodeList nodeList = (NodeList) xpath.compile(attribute.getxPath()).evaluate(xmlDocument, XPathConstants.NODESET);
                nodeList.item(0).setNodeValue(attribute.getValue().toString());
            } catch (XPathExpressionException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private void updateMessage(){
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));
            samlMessageString = writer.getBuffer().toString().replaceAll("\n|\r", "");
        } catch (TransformerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Change the attribute value to the given new value
     * @param attributeName name of the attribute to be changed
     * @param value new value to be changed to
     * @return whether the change is successful
     */
    public boolean changeAttributeValueTo(String attributeName, String value){
        if(attributeMap.containsKey(attributeName)){
            Attribute attribute = attributeMap.get(attributeName);
            Object newValue = validateValueType(attribute.getValueType(), value);
            if(newValue!=null){
                attribute.setValue(newValue);
                messageChanged = true;
                return true;
            }
        }
        return false;
    }

    public HttpMessage getChangedMessege(){
        if(!messageChanged){
            return httpMessage;
        }else{
            //build the new message
        }
        return httpMessage;
    }

}
