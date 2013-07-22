package org.zaproxy.zap.extension.saml;

import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpSender;

import java.io.IOException;
import java.util.Map;
import java.util.TreeSet;

public class SAMLResender {
    
    private SAMLResender(){
        
    }

    /**
     * Rebuild the http request with the given url or form parameters
     * @param msg HTTPMessage that contains the request
     * @param samlParam The name of the SAML parameter (i.e. 'SAMLRequest' or 'SAMLResponse')
     * @param samlMessage The SAML message
     * @param samlMsgBinding The binding to be used to send SAML Message
     * @throws SAMLException
     */
    public static void buildSAMLRequest(HttpMessage msg, String samlParam,String samlMessage, Binding samlMsgBinding) throws SAMLException {
        String encodedSAMLMessage = SAMLUtils.b64Encode(SAMLUtils.deflateMessage(samlMessage));
        HtmlParameter parameter;
        switch (samlMsgBinding){
            case HTTPPost:
                for (HtmlParameter param : msg.getFormParams()) {
                    if(param.getName().equals(samlParam)){
                        param.setValue(encodedSAMLMessage);
                    }
                }
                break;
            case HTTPRedirect:
                for (HtmlParameter param : msg.getUrlParams()) {
                    if(param.getName().equals(samlParam)){
                        param.setValue(encodedSAMLMessage);
                    }
                }
                break;
        }
    }

    /**
     * Resend the message to the desired endpoint and get the response
     * @param msg The message to be sent
     */
    public static void resendMessage(HttpMessage msg){
        HttpSender sender = new HttpSender(Model.getSingleton().getOptionsParam().getConnectionParam(),true,
                HttpSender.MANUAL_REQUEST_INITIATOR);
        try {
            sender.sendAndReceive(msg);
        } catch (IOException e) {
            //todo handle
        }
    }
}
