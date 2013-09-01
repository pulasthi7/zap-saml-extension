package org.zaproxy.zap.extension.saml;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({SamlUtilsUnitTest.class})
public final class SamlExtensionUnitTestSuite {
    @BeforeClass
    public static void initSuit() throws Exception {
        TestConstants.initConstants();
    }
}
