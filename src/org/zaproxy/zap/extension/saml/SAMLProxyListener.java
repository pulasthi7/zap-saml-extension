package org.zaproxy.zap.extension.saml;

import org.parosproxy.paros.core.proxy.ProxyListener;
import org.parosproxy.paros.network.HttpMessage;

public class SAMLProxyListener implements ProxyListener{
    @Override
    public boolean onHttpRequestSend(HttpMessage message) {
        //todo change the parameters
        return true;
    }

    @Override
    public boolean onHttpResponseReceive(HttpMessage message) {
        return false;
    }

    @Override
    public int getArrangeableListenerOrder() {
        return 0;
    }
}
