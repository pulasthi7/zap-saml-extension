package org.zaproxy.zap.extension.saml;

import org.junit.Before;
import org.junit.Test;
import org.parosproxy.paros.network.HttpMessage;

import static org.junit.Assert.*;


public class SAMLMessageUnitTest {

    @Before
    public void setUp() throws Exception {
        TestConstants.initConstants();
    }

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
        assertNull(message);
    }

    @Test(expected = SAMLException.class)
    public void shouldReturnNullForNonSamlPostRequests() throws Exception {
        HttpMessage postRequestMessage = TestConstants.nonSamlPostRequestMessage;
        SAMLMessage message = new SAMLMessage(postRequestMessage);
        assertNull(message);
    }

    @Test
    public void shouldReturnSamlMessageForSamlGetRequests() throws Exception{
        HttpMessage requestMessage = TestConstants.samlGetRequestMessage;
        String samlString = new SAMLMessage(requestMessage).getSamlMessageString();
        assertEquals(samlString.trim(),TestConstants.samlMessageString.trim());

        HttpMessage responseMessage = TestConstants.samlGetResponseMessage;
        samlString = new SAMLMessage(responseMessage).getSamlMessageString();
        assertEquals(samlString.trim(),TestConstants.samlMessageString.trim());
    }

    @Test
    public void shouldReturnSamlMessageForSamlPostRequests() throws Exception{
        HttpMessage requestMessage = TestConstants.samlPostRequestMessage;
        String samlString = new SAMLMessage(requestMessage).getSamlMessageString();
        assertEquals(samlString.trim(),TestConstants.samlMessageString.trim());

        HttpMessage responseMessage = TestConstants.samlPostResponseMessage;
        samlString = new SAMLMessage(responseMessage).getSamlMessageString();
        assertEquals(samlString.trim(),TestConstants.samlMessageString.trim());
    }

    @Test
    public void shouldReturnRelayStateForSamlGetRequests() throws Exception{
        HttpMessage requestMessage = TestConstants.samlGetRequestMessage;
        String relayState = new SAMLMessage(requestMessage).getRelayState();
        assertEquals(relayState.trim(),"null");

        HttpMessage responseMessage = TestConstants.samlGetResponseMessage;
        relayState = new SAMLMessage(responseMessage).getRelayState();
        assertEquals(relayState.trim(), "null");
    }

    @Test
    public void shouldReturnRelayStateForSamlPostRequests() throws Exception{
        HttpMessage requestMessage = TestConstants.samlPostRequestMessage;
        String relayState = new SAMLMessage(requestMessage).getRelayState();
        assertEquals(relayState.trim(),"null");

        HttpMessage responseMessage = TestConstants.samlPostResponseMessage;
        relayState = new SAMLMessage(responseMessage).getRelayState();
        assertEquals(relayState.trim(), "null");
    }

    @Test
    public void shouldSetRelayStateForSamlGetRequests() throws Exception{
        HttpMessage requestMessage = TestConstants.samlGetRequestMessage;
        SAMLMessage samlMessage = new SAMLMessage(requestMessage);
        samlMessage.setRelayState("newValueReq");
        HttpMessage changedHttpMessage = samlMessage.getChangedMessage();
        String relayState = new SAMLMessage(changedHttpMessage).getRelayState();
        assertEquals(relayState.trim(),"newValueReq");


        HttpMessage responseMessage = TestConstants.samlGetResponseMessage;
        samlMessage = new SAMLMessage(responseMessage);
        samlMessage.setRelayState("newValueRsp");
        changedHttpMessage = samlMessage.getChangedMessage();
        relayState = new SAMLMessage(changedHttpMessage).getRelayState();
        assertEquals(relayState.trim(),"newValueRsp");
    }

    @Test
    public void shouldSetRelayStateForSamlPostRequests() throws Exception{
        HttpMessage requestMessage = TestConstants.samlPostRequestMessage;
        SAMLMessage samlMessage = new SAMLMessage(requestMessage);
        samlMessage.setRelayState("newValuePostReq");
        HttpMessage changedHttpMessage = samlMessage.getChangedMessage();
        String relayState = new SAMLMessage(changedHttpMessage).getRelayState();
        assertEquals(relayState.trim(),"newValuePostReq");


        HttpMessage responseMessage = TestConstants.samlPostResponseMessage;
        samlMessage = new SAMLMessage(responseMessage);
        samlMessage.setRelayState("newValuePostRsp");
        changedHttpMessage = samlMessage.getChangedMessage();
        relayState = new SAMLMessage(changedHttpMessage).getRelayState();
        assertEquals(relayState.trim(),"newValuePostRsp");
    }
}
