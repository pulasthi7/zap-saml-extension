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
     * @param getParams The GET parameters to add
     * @param postParams The POST parameters to add
     * @param samlParam The name of the SAML parameter (i.e. 'SAMLRequest' or 'SAMLResponse')
     * @param samlMessage The SAML message
     * @param samlMsgBinding The binding to be used to send SAML Message
     * @throws SAMLException
     */
    public static void buildSAMLRequest(HttpMessage msg, Map<String,String> getParams,Map<String,
            String> postParams, String samlParam,String samlMessage, Binding samlMsgBinding) throws SAMLException {
        TreeSet<HtmlParameter> getParameters = new TreeSet<>();
        for (Map.Entry<String, String> getParam : getParams.entrySet()) {
            HtmlParameter parameter = new HtmlParameter(HtmlParameter.Type.url,getParam.getKey(),getParam.getValue());
            getParameters.add(parameter);
        }

        TreeSet<HtmlParameter> postParameters = new TreeSet<>();
        for (Map.Entry<String, String> postParam : getParams.entrySet()) {
            HtmlParameter parameter = new HtmlParameter(HtmlParameter.Type.form,postParam.getKey(),postParam.getValue());
            postParameters.add(parameter);
        }
        String encodedSAMLMessage = SAMLUtils.b64Encode(SAMLUtils.deflateMessage(samlMessage));
        HtmlParameter parameter;
        switch (samlMsgBinding){
            case HTTPPost:
                    parameter = new HtmlParameter(HtmlParameter.Type.form,samlParam,samlMessage);
                    postParameters.add(parameter);
                    break;
            case HTTPRedirect:
                    parameter = new HtmlParameter(HtmlParameter.Type.url,samlParam,samlMessage);
                    getParameters.add(parameter);
                    break;
        }
        msg.setGetParams(getParameters);
        msg.setFormParams(postParameters);
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
