package org.zaproxy.zap.extension.saml;

import org.apache.log4j.Logger;
import org.parosproxy.paros.core.proxy.ProxyListener;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;

import java.util.Map;
import java.util.Properties;

public class SAMLProxyListener implements ProxyListener {

    private boolean active;
    private Properties autoChangeAttribs;

    protected static Logger log = Logger.getLogger(SAMLProxyListener.class.getName());

    public SAMLProxyListener() {
        setActive(true);
    }

    public void setActive(boolean value) {
        active = value;
        if (active && autoChangeAttribs == null) {
            loadAutoChangeAttributes();
        }
    }

    @Override
    public boolean onHttpRequestSend(HttpMessage message) {
        if (active && SAMLUtils.hasSAMLMessage(message)) {
            try {
                SAMLMessageChanger samlMessage = new SAMLMessageChanger(message);

                //change the params
                for (Map.Entry<Object, Object> entry : autoChangeAttribs.entrySet()) {
                    //todo:for now, only the first value is taken into consideration, need to fix this
                    String value = entry.getValue().toString().split(",")[0];
                    samlMessage.changeAttributeValueTo(entry.getKey().toString(), value);
                }

                //change the original message
                HttpMessage changedMessege = samlMessage.getChangedMessege();
                if(changedMessege!=message){
                    //check for reference, if they are same the message is already changed,
                    // else the header and body are changed
                    message.setRequestBody(changedMessege.getRequestBody());
                    message.setRequestHeader(changedMessege.getRequestHeader());
                }
            } catch (SAMLException e) {
            }
        }
        return true;
    }

    @Override
    public boolean onHttpResponseReceive(HttpMessage message) {
        return true;
    }

    @Override
    public int getArrangeableListenerOrder() {
        return 0;
    }

    public void loadAutoChangeAttributes() {
        autoChangeAttribs = SAMLUtils.loadConfigurations();
    }
}
