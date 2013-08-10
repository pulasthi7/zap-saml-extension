package org.zaproxy.zap.extension.saml;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.parosproxy.paros.extension.encoder.Base64;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;

/**
 * Contains some frequent methods related to decoding and encoding SAML messages
 */
public class SAMLUtils {
    private static final int MAX_INFLATED_SIZE = 5000;
    private static final String SAML_CONF_FILE_NAME = "saml.conf";

    protected static Logger log = Logger.getLogger(SAMLUtils.class);
    /**
     * Private constructor, because this class is and Util class and the methods are static
     */
    private SAMLUtils(){
    }

    /**
     * Base 64 decode a given string and gives the decoded data as a byte array
     * @param message The String to base 64 decode
     * @return Byte array of the decoded string
     * @throws SAMLException
     */
    public static byte[] b64Decode(String message) throws SAMLException{
        try {
            return Base64.decode(message);
        } catch (IOException e) {
            throw new SAMLException("Base 64 Decode of failed for message: \n"+message,e);
        }
    }

    /**
     * Base 64 encode the given byte array and gives the encoded string
     * @param data The data to encode
     * @return Encoded string
     */
    public static String b64Encode(byte[] data){
        return Base64.encodeBytes(data);
    }

    /**
     * Inflate a message (that had been deflated) and gets the original message
     * @param data Byte array of deflated data that need to be inflated
     * @return Original message after inflation
     * @throws SAMLException
     */
    public static String inflateMessage(byte[] data) throws SAMLException {
        try {
            Inflater inflater = new Inflater(true);
            inflater.setInput(data);
            byte[] xmlMessageBytes = new byte[MAX_INFLATED_SIZE];
            int resultLength = inflater.inflate(xmlMessageBytes);

            if (!inflater.finished()) {
                throw new SAMLException("Out of space allocated for inflated data");
            }

            inflater.end();

            return new String(xmlMessageBytes, 0, resultLength,
                    "UTF-8");
        } catch (DataFormatException e) {
            throw new SAMLException("Invalid data format",e);
        } catch (UnsupportedEncodingException e) {
            throw new SAMLException("Inflated data is not in valid encoding format",e);
        }
    }

    /**
     * Deflate a message to be send over a preferred binding
     * @param message Message to be deflated
     * @return The deflated message as a byte array
     */
    public static byte[] deflateMessage(String message) throws SAMLException {
        try {
            Deflater deflater = new Deflater(Deflater.DEFLATED, true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DeflaterOutputStream deflaterOutputStream =
                    new DeflaterOutputStream(byteArrayOutputStream,
                            deflater);

            deflaterOutputStream.write(message.getBytes());
            deflaterOutputStream.close();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
           throw new SAMLException("Message Deflation failed",e);
        }
    }

    /**
     * Get the configuration for the ZAP SAML extension
     * @return
     */
    public static Properties loadConfigurations() {

        Properties config = new Properties();
        try {
            File savedPropertiesFile = new File(getSamlConfFileName());
            if(savedPropertiesFile.exists()){
                config.loadFromXML(new FileInputStream(savedPropertiesFile));
            }
        } catch (IOException e) {
            log.warn("Error loading saved configurations, New configuration will be initiated");
        }
        return config;
    }

    public static void saveConfigurations(Properties p) throws SAMLException {
        try {
            p.storeToXML(new FileOutputStream(getSamlConfFileName()),"");
        } catch (IOException e) {
            throw new SAMLException("Couldn't save configuration",e);
        }
    }

    public static String getSamlConfFileName(){
        return Model.getSingleton().getOptionsParam(). getUserDirectory().getAbsolutePath()+ "/" + SAML_CONF_FILE_NAME;
    }

    public static Set<String> getSAMLAttributes(){
        Set<String> allAttibutes = new HashSet<>();
        allAttibutes.add("AuthnRequest[ID]");
        allAttibutes.add("AuthnRequest[AssertionConsumerServiceURL]");
        allAttibutes.add("AuthnRequest[AttributeConsumingServiceIndex]");
        allAttibutes.add("AuthnRequest[IssueInstant]");
        allAttibutes.add("AuthnRequest[ProtocolBinding]");
        allAttibutes.add("AuthnRequest[Version]");
        allAttibutes.add("AuthnRequest:Issuer");
        allAttibutes.add("AuthnRequest:NameIDPolicy[Format]");
        allAttibutes.add("AuthnRequest:NameIDPolicy[SPNameQualifier]");
        allAttibutes.add("AuthnRequest:NameIDPolicy[AllowCreate]");
        allAttibutes.add("AuthnRequest:RequestedAuthnContext[Comparison]");
        allAttibutes.add("AuthnRequest:RequestedAuthnContext:AuthnContextClassRef");

        allAttibutes.add("Assertion[ID]");
        allAttibutes.add("Assertion[IssueInstant]");
        allAttibutes.add("Assertion[Version]");
        allAttibutes.add("Assertion:Issuer");
        allAttibutes.add("Assertion:Issuer[Format]");
        allAttibutes.add("Assertion:Subject:NameID");
        allAttibutes.add("Assertion:Subject:SubjectConfirmation[Method]");
        allAttibutes.add("Assertion:Subject:SubjectConfirmation:SubjectConfirmationData[InResponseTo]");
        allAttibutes.add("Assertion:Subject:SubjectConfirmation:SubjectConfirmationData[Recipient]");
        allAttibutes.add("Assertion:Subject:SubjectConfirmation:SubjectConfirmationData[NotOnOrAfter]");
        allAttibutes.add("Assertion:Conditions[NotOnOrAfter]");
        allAttibutes.add("Assertion:Conditions[NotBefore]");
        allAttibutes.add("Assertion:Conditions:AudienceRestriction:Audience");
        allAttibutes.add("Assertion:AuthnStatement[AuthnInstant]");
        allAttibutes.add("Assertion:AuthnStatement[SessionIndex]");
        allAttibutes.add("Assertion:AuthnStatement:AuthnContext:AuthnContextClassRef");

        return allAttibutes;
    }

    public static boolean hasSAMLMessage(HttpMessage message){
        for (HtmlParameter parameter : message.getUrlParams()) {
            if(parameter.getName().equals("SAMLRequest") && hasValue(parameter.getValue())){
                return true;
            }
            if(parameter.getName().equals("SAMLResponse") && hasValue(parameter.getValue())){
                return true;
            }
        }
        for (HtmlParameter parameter : message.getFormParams()) {
            if(parameter.getName().equals("SAMLRequest") && hasValue(parameter.getValue())){
                return true;
            }
            if(parameter.getName().equals("SAMLResponse") && hasValue(parameter.getValue())){
                return true;
            }
        }
        return false;
    }

    private static boolean hasValue(String param){
        if(param!=null){
            return !"".equals(param);
        }
        return false;
    }

    /**
     * Decode the SAML messages based on the binding used
     * @param val the SAML message to decode
     * @param binding The binding used
     * @return The decoded SAML message if success, or the original string if failed
     */
    public static String extractSAMLMessage(String val, Binding binding){
        try {
            switch (binding) {
                case HTTPPost:
                    val = URLDecoder.decode(val, "UTF-8");
                case HTTPRedirect:
                    byte[] b64decoded = b64Decode(val);
                    return inflateMessage(b64decoded);
                default:
                    break;
            }
        } catch (UnsupportedEncodingException e) {

        } catch (SAMLException e) {

        }
        return val;
    }
}
