package org.zaproxy.zap.extension.saml;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.SessionChangedListener;
import org.parosproxy.paros.model.Session;

import java.net.MalformedURLException;
import java.net.URL;

public class SAMLExtension extends ExtensionAdaptor implements SessionChangedListener {

    @Override
    public URL getURL() {
        try {
            return new URL(Constant.ZAP_HOMEPAGE);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public String getAuthor() {
        return Constant.ZAP_TEAM;
    }

    @Override
    public void hook(ExtensionHook extensionHook) {
        super.hook(extensionHook);
//        extensionHook.addProxyListener(new SAMLProxyListener());
//        extensionHook.addSessionListener(this);
        System.out.println("Started SAML extension");

    }

    @Override
    public void sessionChanged(Session session) {
    }

    @Override
    public void sessionAboutToChange(Session session) {
    }

    @Override
    public void sessionScopeChanged(Session session) {
    }

    @Override
    public void sessionModeChanged(Control.Mode mode) {
    }
}
