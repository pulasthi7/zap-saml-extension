package org.zaproxy.zap.extension.saml;

import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.view.PopupMenuHttpMessage;

public class SAMLResendMenuItem extends PopupMenuHttpMessage {

    public SAMLResendMenuItem(String label) {
        super(label);
    }

    @Override
    public void performAction(HttpMessage httpMessage) throws Exception {
        if(!isSAMLMessage(httpMessage)){
            View.getSingleton().showWarningDialog("Not a valid SAML request");
            return;
        }
        SAMLRequestEditor editor = new SAMLRequestEditor(httpMessage);
        editor.showUI();
    }

    @Override
    public boolean isEnableForInvoker(Invoker invoker) {
        //TODO filter out the unnecessary invokers
        return true;
    }

    private boolean isSAMLMessage(HttpMessage message){
        for (HtmlParameter parameter : message.getUrlParams()) {
            if(parameter.getName().equals("SAMLRequest") && hasValue(parameter.getValue())){
                return true;
            }
            if(parameter.getName().equals("SAMLResponse") && hasValue(parameter.getValue())){
                return true;
            }
        }
        for (HtmlParameter parameter : message.getFormParams()) {
            if(parameter.getName().equals("SAMLRequest") && hasValue(parameter.getValue())){
                return true;
            }
            if(parameter.getName().equals("SAMLResponse") && hasValue(parameter.getValue())){
                return true;
            }
        }
        return false;
    }

    private boolean hasValue(String param){
        if(param!=null){
            return !"".equals(param);
        }
        return false;
    }
}
