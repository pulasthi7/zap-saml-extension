package org.zaproxy.zap.extension.saml;

import org.opensaml.Configuration;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public class SAMLMessageWrapper {
    private String originalMessage;
    private Map<String,String> attributeMapping;
    private String samlParameter;

    public SAMLMessageWrapper(String originalMessage, String samlParameter) {
        this.originalMessage = originalMessage;
        this.samlParameter = samlParameter;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public Map<String, String> getAttributeMapping() {
        return attributeMapping;
    }

    private void extractAttributes(){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = null;
            docBuilder = documentBuilderFactory.newDocumentBuilder();
            ByteArrayInputStream is = new ByteArrayInputStream(originalMessage.getBytes("UTF-8"));

            Document document = docBuilder.parse(is);
            Element element = document.getDocumentElement();
            UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
            Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
            XMLObject xmlObject = unmarshaller.unmarshall(element);

            if("SAMLResponse".equals(samlParameter)){
                Response response = (Response) xmlObject;
                Assertion assertion = response.getAssertions().get(0);
                attributeMapping.put("Assertion ID",assertion.getID());
                attributeMapping.put("version",assertion.getVersion().toString());
                attributeMapping.put("Issuer",assertion.getIssuer().getValue());
                attributeMapping.put("Issuer format",assertion.getIssuer().getFormat());

            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (UnmarshallingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
