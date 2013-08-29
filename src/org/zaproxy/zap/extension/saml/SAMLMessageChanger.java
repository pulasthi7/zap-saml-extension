package org.zaproxy.zap.extension.saml;

import org.joda.time.DateTime;

import java.util.Map;

public class SAMLMessageChanger {

    private SAMLMessage message;
    private Map<String, Attribute> attributeMap;

    public SAMLMessageChanger(SAMLMessage message) {
        this.message = message;
    }

    private void initAttributes(){

    }

    public boolean changeAttributeValueTo(String attributeName, String value){
        if(attributeMap.containsKey(attributeName)){
            Attribute attribute = attributeMap.get(attributeName);
            Object newValue = validateValueType(attribute.getValueType(), value);
            if(newValue!=null){
                attribute.setValue(newValue);
                return true;
            }
        }
        return false;
    }

    private Object validateValueType(Attribute.SAMLAttributeValueType type, String value){
        try {
            switch (type){
                case String:
                    return value;
                case Decimal:
                    return Double.valueOf(value);
                case Integer:
                    return Integer.parseInt(value);
                case TimeStamp:
                    return DateTime.parse(value);
                default:
                    return value;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
