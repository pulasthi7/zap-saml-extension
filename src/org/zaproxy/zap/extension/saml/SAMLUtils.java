package org.zaproxy.zap.extension.saml;

import org.parosproxy.paros.extension.encoder.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class SAMLUtils {
    private static final int MAX_INFLATED_SIZE = 5000;

    public static byte[] b64Decode(String message) throws IOException {
        return Base64.decode(message);
    }

    public static String b64Encode(byte[] data){
        return Base64.encodeBytes(data);
    }

    public static String inflateMessage(byte[] data) throws DataFormatException, UnsupportedEncodingException {
        Inflater inflater = new Inflater(true);
        inflater.setInput(data);
        byte[] xmlMessageBytes = new byte[MAX_INFLATED_SIZE];
        int resultLength = inflater.inflate(xmlMessageBytes);

        if (!inflater.finished()) {
            throw new RuntimeException("didn't allocate enough space to hold decompressed data");
        }

        inflater.end();

        String decodedResponse = new String(xmlMessageBytes, 0, resultLength,
                "UTF-8");
        return decodedResponse;
    }

    public static String deflateMessage(String message){
        return null;
    }

}
