package org.zaproxy.zap.extension.saml;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedHashSet;
import java.util.Set;

@XmlRootElement(namespace = "org.zaproxy.zap.extension.saml")
public class AttributeSet {
//    @XmlElementWrapper
    private Set<Attribute> attributes;

    public Set<Attribute> getAttributes() {
        if(attributes == null){
            attributes = new LinkedHashSet<>();
        }
        return attributes;
    }

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }
}
