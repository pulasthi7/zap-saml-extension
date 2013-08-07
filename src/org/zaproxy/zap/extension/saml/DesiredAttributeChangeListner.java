package org.zaproxy.zap.extension.saml;

import java.util.Set;

public interface DesiredAttributeChangeListner {
    void onDesiredAttributeValueChange(String attribute, String value);
    void onAddDesiredAttribute(String attribute, String values);
    void onDeleteDesiredAttribute(String attribute);
    Set<String> getDesiredAttributes();
}
