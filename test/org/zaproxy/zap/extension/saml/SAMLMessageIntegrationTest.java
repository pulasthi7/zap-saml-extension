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


}
