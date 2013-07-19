package org.zaproxy.zap.extension.saml;

import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.*;
import org.opensaml.xml.ConfigurationException;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SAMLMessage {
    private String originalMessage;
    private Map<String,String> attributeMapping;
    private String samlParameter;

    public SAMLMessage(String originalMessage, String samlParameter) {
        this.originalMessage = originalMessage;
        this.samlParameter = samlParameter;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    /**
     * Convert the raw saml xml string to a pretty formatted String
     * @return The Pretty formatted XML String
     * @throws SAMLException If XML parsing failed
     */
    public String getPrettyFormattedMessage() throws SAMLException {
        try {
            Source xmlInput = new StreamSource(new StringReader(originalMessage));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 4);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new SAMLException("Formatting XML failed",e);
        }
    }

    public Map<String, String> getAttributeMapping() {
        if(attributeMapping==null||attributeMapping.isEmpty()){
            extractAttributes();
        }
        return attributeMapping;
    }

    private void extractAttributes(){
        try {
            DefaultBootstrap.bootstrap();
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
            attributeMapping = new LinkedHashMap<>();
            if("SAMLResponse".equals(samlParameter)){
                extractResponseAttributes(xmlObject);
            } else if("SAMLRequest".equals(samlParameter)){
                extractRequestAttributes(xmlObject);
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
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void extractRequestAttributes(XMLObject xmlObject){
        AuthnRequest request = (AuthnRequest) xmlObject;
        attributeMapping.put("Request ID",request.getID());
        attributeMapping.put("Assertion Consumer Service URL",request.getAssertionConsumerServiceURL());
        attributeMapping.put("Assertion Consumer Service Index", String.valueOf(request.getAssertionConsumerServiceIndex()));
        attributeMapping.put("Assertion Issue Instant", request.getIssueInstant().toString());
        attributeMapping.put("Assertion Protocol Binding", request.getProtocolBinding());
        attributeMapping.put("Assertion Version", request.getVersion().toString());

        attributeMapping.put("Issuer", request.getIssuer().getValue());
        attributeMapping.put("NameIDPolicy Format", request.getNameIDPolicy().getFormat());
        attributeMapping.put("NameIDPolicy SPNameQualifier",request.getNameIDPolicy().getSPNameQualifier());
        attributeMapping.put("NameIDPolicy AllowCreate",request.getNameIDPolicy().getAllowCreate().toString());

        if(request.getRequestedAuthnContext().getAuthnContextClassRefs().size()>0){
            attributeMapping.put("AuthContextClassRef",request.getRequestedAuthnContext().getAuthnContextClassRefs()
                    .get(0).getAuthnContextClassRef());
        }
    }

    private void extractResponseAttributes(XMLObject xmlObject){
        Response response = (Response) xmlObject;
        Assertion assertion = response.getAssertions().get(0);
        attributeMapping.put("Assertion ID",assertion.getID());
        attributeMapping.put("version",assertion.getVersion().toString());
        attributeMapping.put("Issuer",assertion.getIssuer().getValue());
        attributeMapping.put("Issuer format",assertion.getIssuer().getFormat());
        attributeMapping.put("Subject : NameID",assertion.getSubject().getNameID().getValue());
        if(assertion.getSubject().getSubjectConfirmations().size()>0){
            SubjectConfirmation subjectConfirmation = assertion.getSubject().getSubjectConfirmations().get(0);
            attributeMapping.put("Subject : Confirmation Method",subjectConfirmation.getMethod());
            attributeMapping.put("Subject : Confirmation Address",
                    subjectConfirmation.getSubjectConfirmationData().getAddress());
            attributeMapping.put("Subject : InResponseTo",subjectConfirmation.getSubjectConfirmationData()
                    .getInResponseTo());
            attributeMapping.put("Subject : Recipient",subjectConfirmation.getSubjectConfirmationData()
                    .getRecipient());
            attributeMapping.put("Subject : NotAfter",subjectConfirmation.getSubjectConfirmationData()
                    .getNotOnOrAfter().toString());
        }

        attributeMapping.put("Conditions : NotBefore",assertion.getConditions().getNotBefore().toString());
        attributeMapping.put("Conditions : NotAfter",assertion.getConditions().getNotOnOrAfter().toString());

        int restrictionNo = 0;
        for (AudienceRestriction audienceRestriction : assertion.getConditions().getAudienceRestrictions()) {
            attributeMapping.put("Audience "+restrictionNo,audienceRestriction.getAudiences().get(0).getAudienceURI());
        }

        for (AuthnStatement authnStatement : assertion.getAuthnStatements()) {
            attributeMapping.put("AuthStatement : Session Index",authnStatement.getSessionIndex());
            attributeMapping.put("AuthStatement : Auth Instant",authnStatement.getAuthnInstant().toString());
            attributeMapping.put("AuthStatement : AuthContextClassRef",authnStatement.getAuthnContext()
                    .getAuthnContextClassRef()
                    .getAuthnContextClassRef());

        }
    }
}
