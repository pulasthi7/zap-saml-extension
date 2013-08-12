package org.zaproxy.zap.extension.saml;

import org.apache.commons.httpclient.URIException;
import org.apache.log4j.Logger;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.extension.history.ExtensionHistory;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpSender;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SAMLResender {

    private static Logger log = Logger.getLogger(SAMLResender.class.getName());

    private SAMLResender() {

    }

    /**
     * Rebuild the http request with the given url or form parameters
     *
     * @param msg            HTTPMessage that contains the request
     * @param samlParam      The name of the SAML parameter (i.e. 'SAMLRequest' or 'SAMLResponse')
     * @param samlMessage    The SAML message
     * @param samlMsgBinding The binding to be used to send SAML Message
     * @throws SAMLException
     */
    public static void buildSAMLRequest(HttpMessage msg, String samlParam, String samlMessage, Binding samlMsgBinding) throws SAMLException {
        String encodedSAMLMessage = SAMLUtils.b64Encode(SAMLUtils.deflateMessage(samlMessage));
        switch (samlMsgBinding) {
            case HTTPPost:
                String paramString = msg.getRequestBody().toString();
                String[] params = paramString.split("&");
                StringBuilder newParamBuilder = new StringBuilder();
                for (int i = 0; i < params.length; i++) {
                    if (params[i].startsWith(samlParam)) {
                        try {
                            newParamBuilder.append(samlParam + "=" + URLEncoder.encode(encodedSAMLMessage, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            newParamBuilder.append(params[i]);
                            log.warn("Could not set value for '" + samlParam + "', keeping original value");
                        }
                    } else {
                        newParamBuilder.append(params[i]); //No change needed;
                    }
                    if (i < params.length - 1) {
                        newParamBuilder.append("&");  //add '&' between params for separation
                    }
                }
                msg.setRequestBody(newParamBuilder.toString());
                log.debug("Message request body set to : "+newParamBuilder.toString());
                break;

            case HTTPRedirect:
                try {
                    paramString = msg.getRequestHeader().getURI().getQuery();
                    params = paramString.split("&");
                    newParamBuilder = new StringBuilder();
                    for (int i = 0; i < params.length; i++) {
                        if (params[i].startsWith(samlParam)) {
                            newParamBuilder.append(samlParam + "=" + URLEncoder.encode(encodedSAMLMessage, "UTF-8"));
                        } else {
                            newParamBuilder.append(params[i]); //No change needed;
                        }
                        if (i < params.length - 1) {
                            newParamBuilder.append("&");  //add '&' between params for separation
                        }
                    }
                    msg.getRequestHeader().getURI().setEscapedQuery(newParamBuilder.toString());
                    log.debug("Message Url params set to : "+newParamBuilder.toString());
                } catch (URIException | UnsupportedEncodingException e) {
                    log.error(e.getMessage());
                }
                break;
        }
    }

    /**
     * Resend the message to the desired endpoint and get the response
     * @param msg The message to be sent
     */
    public static void resendMessage(final HttpMessage msg) throws SAMLException {
        HttpSender sender = new HttpSender(Model.getSingleton().getOptionsParam().getConnectionParam(), true,
                HttpSender.MANUAL_REQUEST_INITIATOR);
        try {
            sender.sendAndReceive(msg, true);
            if (!msg.getResponseHeader().isEmpty()) {
                final ExtensionHistory extension = (ExtensionHistory) Control.getSingleton()
                        .getExtensionLoader().getExtension(ExtensionHistory.NAME);

                final int finalType = HistoryReference.TYPE_MANUAL;
                extension.addHistory(msg, finalType);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new SAMLException("Message sending failed", e);
        }
    }
}
