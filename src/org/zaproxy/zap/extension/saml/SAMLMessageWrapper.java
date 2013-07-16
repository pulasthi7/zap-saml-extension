package org.zaproxy.zap.extension.saml;

import java.util.Map;

public class SAMLMessageWrapper {
    private String originalMessage;
    private Map<String,String> attributeMapping;

    public SAMLMessageWrapper(String originalMessage) {
        this.originalMessage = originalMessage;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public Map<String, String> getAttributeMapping() {
        return attributeMapping;
    }
}
