package org.zaproxy.zap.extension.saml;

public interface AttributeListener {
    void onAttributeAdd(Attribute a);
    void onAttributeDelete(Attribute a);
}
