package org.zaproxy.zap.extension.saml;

import org.junit.Test;
import org.parosproxy.paros.network.HttpMessage;

import static org.junit.Assert.*;


public class SAMLMessageIntegrationTest {

    @Test
    public void shouldReturnNotNullSamlMessageForSamlGetRequests() throws Exception {
        HttpMessage requestMessage = TestConstants.samlGetRequestMessage;
        SAMLMessage message = new SAMLMessage(requestMessage);
        assertNotNull(message);

        HttpMessage responseMessage = TestConstants.samlGetResponseMessage;
        message = new SAMLMessage(responseMessage);
        assertNotNull(message);
    }

    @Test
    public void shouldReturnNotNullSamlMessageForSamlPostRequests() throws Exception {
        HttpMessage requestMessage = TestConstants.samlPostRequestMessage;
        SAMLMessage message = new SAMLMessage(requestMessage);
        assertNotNull(message);

        HttpMessage responseMessage = TestConstants.samlPostResponseMessage;
        message = new SAMLMessage(responseMessage);
        assertNotNull(message);
    }

    @Test(expected = SAMLException.class)
    public void shouldReturnNullForNonSamlGetRequests() throws Exception {
        HttpMessage getRequestMessage = TestConstants.nonSamlGetRequestMessage;
        SAMLMessage message = new SAMLMessage(getRequestMessage);
    }

    @Test(expected = SAMLException.class)
    public void shouldReturnNullForNonSamlPostRequests() throws Exception {
        HttpMessage postRequestMessage = TestConstants.nonSamlPostRequestMessage;
        SAMLMessage message = new SAMLMessage(postRequestMessage);
    }


}
