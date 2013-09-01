package org.zaproxy.zap.extension.saml;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SamlUtilsUnitTest {

    @Test
    public void shouldReturnTrueWhenGetRequestHasSAMLMessage() throws Exception {
        assertThat(SAMLUtils.hasSAMLMessage(TestConstants.samlGetRequestMessage), is(true));
    }

    @Test
    public void shouldReturnTrueWhenPostRequestHasSAMLMessage() throws Exception {
        assertThat(SAMLUtils.hasSAMLMessage(TestConstants.samlPostRequestMessage), is(true));
    }

    @Test
    public void shouldReturnTrueWhenGetResponseHasSAMLMessage() throws Exception {
        assertThat(SAMLUtils.hasSAMLMessage(TestConstants.samlGetResponseMessage), is(true));
    }

    @Test
    public void shouldReturnTrueWhenPostResponseHasSAMLMessage() throws Exception {
        assertThat(SAMLUtils.hasSAMLMessage(TestConstants.samlPostResponseMessage), is(true));
    }

    @Test
    public void shouldReturnFalseWhenGetRequestHasNoSAMLMessage() throws Exception {
        assertThat(SAMLUtils.hasSAMLMessage(TestConstants.nonSamlGetRequestMessage), is(false));
    }

    @Test
    public void shouldReturnFalseWhenPostRequestHasNoSAMLMessage() throws Exception {
        assertThat(SAMLUtils.hasSAMLMessage(TestConstants.nonSamlPostRequestMessage), is(false));
    }
}
