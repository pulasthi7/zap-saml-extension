package org.zaproxy.zap.extension.saml;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.view.PopupMenuHttpMessage;

public class SAMLResendMenuItem extends PopupMenuHttpMessage {

    private ManualSAMLRequestEditorDialog resendDialog;

    public SAMLResendMenuItem(String label) {
        super(label);
    }

    @Override
    public void performAction(HttpMessage httpMessage) throws Exception {
        ManualSAMLRequestEditorDialog editorDialog = getResendDialog();
    }

    @Override
    public boolean isEnableForInvoker(Invoker invoker) {
        //TODO filter out the unnecessary invokers
        return true;
    }

    private ManualSAMLRequestEditorDialog getResendDialog() {
        if (resendDialog == null) {
            resendDialog = new ManualSAMLRequestEditorDialog(true, "resend");
            resendDialog.setTitle("Resend SAML Request");	//TODO port to ZAP i18n
        }
        return resendDialog;
    }
}
