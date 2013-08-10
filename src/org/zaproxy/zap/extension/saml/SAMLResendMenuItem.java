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
        if(!SAMLUtils.hasSAMLMessage(httpMessage)){
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

}
