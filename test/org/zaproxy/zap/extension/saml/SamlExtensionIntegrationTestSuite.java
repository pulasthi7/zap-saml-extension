package org.zaproxy.zap.extension.saml;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({SAMLMessageIntegrationTest.class})
public final class SamlExtensionIntegrationTestSuite {
    @BeforeClass
    public static void initialize() throws Exception {
        TestConstants.initConstants();
    }
}
