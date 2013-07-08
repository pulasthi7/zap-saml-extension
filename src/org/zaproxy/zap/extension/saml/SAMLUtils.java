package org.zaproxy.zap.extension.saml;

import org.parosproxy.paros.extension.encoder.Base64;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;

/**
 * Contains some frequent methods related to decoding and encoding SAML messages
 */
public class SAMLUtils {
    private static final int MAX_INFLATED_SIZE = 5000;

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
     * Convert a XML String to a pretty formatted String
     * @param xmlString The unformatted XML String
     * @return The Pretty formatted XML String
     * @throws SAMLException If XML parsing failed
     */
    public static String prettyFormatXML(String xmlString) throws SAMLException {
        try {
            Source xmlInput = new StreamSource(new StringReader(xmlString));
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

}
