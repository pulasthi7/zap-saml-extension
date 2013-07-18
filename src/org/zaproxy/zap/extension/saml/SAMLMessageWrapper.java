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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
}
