package org.zaproxy.zap.extension.saml;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.zaproxy.zap.ZAP;

@RunWith(Suite.class)
@Suite.SuiteClasses({SAMLMessageUnitTest.class})
public final class SamlExtensionIntegrationTestSuite {
    @BeforeClass
    public static void initialize() throws Exception {
        TestConstants.initConstants();
        ZAP.main(new String[]{"-cmd"});
    }
}
