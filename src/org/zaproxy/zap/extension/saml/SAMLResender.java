package org.zaproxy.zap.extension.saml;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.extension.history.ExtensionHistory;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpSender;

import java.io.IOException;

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
        switch (samlMsgBinding){
            case HTTPPost:
                String paramString = msg.getRequestBody().toString();
                String[] params = paramString.split("&");
                StringBuilder newParamBuilder = new StringBuilder();
                for (int i = 0; i < params.length; i++) {
                    if(params[i].startsWith(samlParam)){
                        newParamBuilder.append(samlParam + "=" + encodedSAMLMessage);
                    }else{
                        newParamBuilder.append(params[i]); //No change needed;
                    }
                    if(i<params.length-1){
                        newParamBuilder.append("&");  //add '&' between params for separation
                    }
                }
                msg.setRequestBody(newParamBuilder.toString());
                break;

            case HTTPRedirect:
                try {
                    paramString = msg.getRequestHeader().getURI().getQuery();
                    params = paramString.split("&");
                    newParamBuilder = new StringBuilder();
                    for (int i = 0; i < params.length; i++) {
                        if(params[i].startsWith(samlParam)){
                            newParamBuilder.append(samlParam + "=" + encodedSAMLMessage);
                        }else{
                            newParamBuilder.append(params[i]); //No change needed;
                        }
                        if(i<params.length-1){
                            newParamBuilder.append("&");  //add '&' between params for separation
                        }
                    }
                    msg.getRequestHeader().getURI().setQuery(newParamBuilder.toString());
                } catch (URIException e) {
                }
                break;
        }
    }

    /**
     * Resend the message to the desired endpoint and get the response
     * @param msg The message to be sent
     */
    public static void resendMessage(final HttpMessage msg) throws SAMLException {
        HttpSender sender = new HttpSender(Model.getSingleton().getOptionsParam().getConnectionParam(),true,
                HttpSender.MANUAL_REQUEST_INITIATOR);
        try {
            sender.sendAndReceive(msg,true);
            if (!msg.getResponseHeader().isEmpty()) {
                final ExtensionHistory extension = (ExtensionHistory) Control.getSingleton()
                        .getExtensionLoader().getExtension(ExtensionHistory.NAME);

                final int finalType = HistoryReference.TYPE_MANUAL;
                extension.addHistory(msg,finalType);
            }

        } catch (IOException e) {
            throw new SAMLException("Message sending failed",e);
        }
    }
}
