package org.zaproxy.zap.extension.saml;

import org.parosproxy.paros.core.proxy.ProxyListener;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;

public class SAMLProxyListener implements ProxyListener {
    @Override
    public boolean onHttpRequestSend(HttpMessage msg) {
        for (HtmlParameter parameter : msg.getUrlParams()) {
            if(parameter.getName().equals("SAMLRequest")){
                System.out.println(parameter.getValue());
            }
        }
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onHttpResponseReceive(HttpMessage msg) {
        return false;
    }

    @Override
    public int getArrangeableListenerOrder() {
        return 0;
    }
}
