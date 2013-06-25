package org.zaproxy.zap.extension.saml;

import net.htmlparser.jericho.Source;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.pscan.PassiveScanThread;
import org.zaproxy.zap.extension.pscan.PluginPassiveScanner;

public class SAMLPassiveScanner extends PluginPassiveScanner {
    @Override
    public void scanHttpRequestSend(HttpMessage msg, int id) {
        System.out.println("in SAML Passive scanner...");
        for (HtmlParameter parameter : msg.getUrlParams()) {
            if(parameter.getName().equals("SAMLRequest")){
                System.out.println("SAMLRequest: "+parameter.getValue());
            }
            if(parameter.getName().equals("SAMLResponse")){
                System.out.println("SAMLResponse: "+parameter.getValue());
            }
        }
        for (HtmlParameter parameter : msg.getFormParams()) {
            if(parameter.getName().equals("SAMLRequest")){
                System.out.println("SAMLRequest: "+parameter.getValue());
            }
            if(parameter.getName().equals("SAMLResponse")){
                System.out.println("SAMLResponse: "+parameter.getValue());
            }
        }
    }

    @Override
    public void scanHttpResponseReceive(HttpMessage msg, int id, Source source) {
    }

    @Override
    public void setParent(PassiveScanThread parent) {
    }

    @Override
    public String getName() {
        return "SAML Passive Scanner";
    }
}
