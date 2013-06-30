package org.zaproxy.zap.extension.saml;

import org.parosproxy.paros.extension.manualrequest.ManualRequestEditorDialog;
import org.parosproxy.paros.extension.manualrequest.MessageSender;
import org.zaproxy.zap.extension.httppanel.HttpPanelRequest;
import org.zaproxy.zap.extension.httppanel.Message;

import javax.swing.*;
import java.awt.*;

public class ManualSAMLRequestEditorDialog extends ManualRequestEditorDialog {

    public ManualSAMLRequestEditorDialog(boolean isSendEnabled, String configurationKey) throws HeadlessException {
        super(isSendEnabled, configurationKey);
    }

    @Override
    public Class<? extends Message> getMessageType() {
        return null;
    }

    @Override
    protected MessageSender getMessageSender() {
        return null;
    }

    @Override
    public JMenuItem getMenuItem() {
        return null;
    }

    @Override
    protected Component getManualSendPanel() {
        return null;
    }

    @Override
    public void setDefaultMessage() {

    }

    @Override
    public void setMessage(Message message) {

    }

    @Override
    public Message getMessage() {
        return null;
    }

    @Override
    protected void btnSendAction() {

    }

    @Override
    protected void saveConfig() {

    }

    @Override
    protected HttpPanelRequest getRequestPanel() {
        return null;
    }
}
