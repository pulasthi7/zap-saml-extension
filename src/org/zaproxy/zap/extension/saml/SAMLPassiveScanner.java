package org.zaproxy.zap.extension.saml;

import net.htmlparser.jericho.Source;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.pscan.PassiveScanThread;
import org.zaproxy.zap.extension.pscan.PluginPassiveScanner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;

public class SAMLPassiveScanner extends PluginPassiveScanner {
    @Override
    public void scanHttpRequestSend(HttpMessage msg, int id) {
        for (HtmlParameter parameter : msg.getUrlParams()) {
            if(parameter.getName().equals("SAMLRequest")){
                System.out.println("Processing SAMLRequest: ");
                System.out.println(extractSAMLMessage(parameter.getValue()));
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

    private String extractSAMLMessage(String val){
        try {
            //String urlDecoded = SAMLUtils.urlDecode(param);
            byte[] b64decoded = SAMLUtils.b64Decode(val);
            return SAMLUtils.inflateMessage(b64decoded);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
        return "";
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
