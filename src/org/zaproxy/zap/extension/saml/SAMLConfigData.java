package org.zaproxy.zap.extension.saml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(namespace = "org.zaproxy.zap.extension.saml")
public class SAMLConfigData {
    private boolean autoChangerEnabled;
    private boolean xswEnabled;

    @XmlElementWrapper(name = "AllAttributes")
    @XmlElement(name = "Attribute")
    private Set<Attribute> availableAttributes;

    @XmlElementWrapper(name = "AutoChangeAttributes")
    @XmlElement(name = "Attribute")
    private Set<Attribute> autoChangeValues;

    public boolean isAutoChangerEnabled() {
        return autoChangerEnabled;
    }

    public void setAutoChangerEnabled(boolean autoChangerEnabled) {
        this.autoChangerEnabled = autoChangerEnabled;
    }

    public Set<Attribute> getAvailableAttributes() {
        return availableAttributes;
    }

    public void setAvailableAttributes(Set<Attribute> availableAttributes) {
        this.availableAttributes = availableAttributes;
    }

    public Set<Attribute> getAutoChangeValues() {
        return autoChangeValues;
    }

    public void setAutoChangeValues(Set<Attribute> autoChangeValues) {
        this.autoChangeValues = autoChangeValues;
    }
}
