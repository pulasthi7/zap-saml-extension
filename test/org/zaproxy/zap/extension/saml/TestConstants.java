package org.zaproxy.zap.extension.saml;

import org.apache.commons.httpclient.HttpsURL;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;
import org.zaproxy.zap.network.HttpRequestBody;

public class TestConstants {

    public static HttpMessage nonSamlGetRequestMessage;
    public static HttpMessage nonSamlPostRequestMessage;
    public static HttpMessage samlGetRequestMessage;
    public static HttpMessage samlPostRequestMessage;
    public static HttpMessage samlGetResponseMessage;
    public static HttpMessage samlPostResponseMessage;

    public static SAMLConfiguration testConfigutations;

    /**
     * The message that contain in every saml message
     */
    public static String samlMessageString;

    /**
     * Initialize the test constants
     * @throws Exception
     */
    public static void initConstants() throws Exception {
        initMessages();
        initConfigurations();
    }

    private static void initMessages() throws Exception {
        samlGetRequestMessage = new HttpMessage();
        samlGetRequestMessage.setRequestHeader(new HttpRequestHeader());
        samlGetRequestMessage.getRequestHeader().setURI(new HttpsURL("localhost",443,"/samlsso"));
        samlGetRequestMessage.getRequestHeader().getURI().setEscapedQuery("SAMLRequest=jZPBbtswDIZfRdDdseNmQCzEKdIUxQJ0qxe7O%2FSmykwjQJY8kU6zt5%2FsxF0ORdCryJ%2Fk%2F5Fa3B4bww7gUTub8%2Bkk4QyscrW2bzl%2Frh6iOb9dLlA2phWrjvZ2C386QGJBZ1EMgZx33gonUaOwsgEUpES5%2BvEo0kkiWu%2FIKWc4WyGCp9Bo7Sx2DfgS%2FEEreN4%2B5nxP1Io4Nk5Js3dIYp7MkxjRGfembRATef3aEZzEYb6zemNrOIbR05ssnX3LZhlnD84rGKbN%2BU4aBM429zkP3jZYSER9gP8BxC7UQJKWcp4m05soyaJkWk1nIk1Fmkxm2fyFs%2BJs407bE5xrnl9PSSi%2BV1URFU9lxdnvEXJI4CPSobv%2FOkw5IuTLsnwqZdMaWLXtIr4sNxb%2FGeSb%2B8IZrf6ylTHufe1BUvBOvoMBUyPpesP%2BRdfRbkgVbe8BCSxxVhZ9%2FV%2BdNHqnwef81JzHY%2FvzpUA9bCKsjeBIbO2aVnqNPQk4SkVnFuIya22C0S3sLsB8mcvVNCVUXzo893fw7nzd7xVUmLLy0mLrPJ1gfjrPcgT9qbeP6OVHWf4D&RelayState=null");

        samlPostRequestMessage = new HttpMessage();
        samlPostRequestMessage.setRequestHeader(new HttpRequestHeader());
        samlPostRequestMessage.getRequestHeader().setURI(new HttpsURL("localhost", 443, "/samlsso"));
        samlPostRequestMessage.setRequestBody(new HttpRequestBody("SAMLRequest=jZPBbtswDIZfRdDdseNmQCzEKdIUxQJ0qxe7O%2FSmykwjQJY8kU6zt5%2FsxF0ORdCryJ%2Fk%2F5Fa3B4bww7gUTub8%2Bkk4QyscrW2bzl%2Frh6iOb9dLlA2phWrjvZ2C386QGJBZ1EMgZx33gonUaOwsgEUpES5%2BvEo0kkiWu%2FIKWc4WyGCp9Bo7Sx2DfgS%2FEEreN4%2B5nxP1Io4Nk5Js3dIYp7MkxjRGfembRATef3aEZzEYb6zemNrOIbR05ssnX3LZhlnD84rGKbN%2BU4aBM429zkP3jZYSER9gP8BxC7UQJKWcp4m05soyaJkWk1nIk1Fmkxm2fyFs%2BJs407bE5xrnl9PSSi%2BV1URFU9lxdnvEXJI4CPSobv%2FOkw5IuTLsnwqZdMaWLXtIr4sNxb%2FGeSb%2B8IZrf6ylTHufe1BUvBOvoMBUyPpesP%2BRdfRbkgVbe8BCSxxVhZ9%2FV%2BdNHqnwef81JzHY%2FvzpUA9bCKsjeBIbO2aVnqNPQk4SkVnFuIya22C0S3sLsB8mcvVNCVUXzo893fw7nzd7xVUmLLy0mLrPJ1gfjrPcgT9qbeP6OVHWf4D&RelayState=null"));

        samlGetResponseMessage = new HttpMessage();
        samlGetResponseMessage.setRequestHeader(new HttpRequestHeader());
        samlGetResponseMessage.getRequestHeader().setURI(new HttpsURL("localhost",443,"/samlsso"));
        samlGetResponseMessage.getRequestHeader().getURI().setEscapedQuery("SAMLResponse=jZPBbtswDIZfRdDdseNmQCzEKdIUxQJ0qxe7O%2FSmykwjQJY8kU6zt5%2FsxF0ORdCryJ%2Fk%2F5Fa3B4bww7gUTub8%2Bkk4QyscrW2bzl%2Frh6iOb9dLlA2phWrjvZ2C386QGJBZ1EMgZx33gonUaOwsgEUpES5%2BvEo0kkiWu%2FIKWc4WyGCp9Bo7Sx2DfgS%2FEEreN4%2B5nxP1Io4Nk5Js3dIYp7MkxjRGfembRATef3aEZzEYb6zemNrOIbR05ssnX3LZhlnD84rGKbN%2BU4aBM429zkP3jZYSER9gP8BxC7UQJKWcp4m05soyaJkWk1nIk1Fmkxm2fyFs%2BJs407bE5xrnl9PSSi%2BV1URFU9lxdnvEXJI4CPSobv%2FOkw5IuTLsnwqZdMaWLXtIr4sNxb%2FGeSb%2B8IZrf6ylTHufe1BUvBOvoMBUyPpesP%2BRdfRbkgVbe8BCSxxVhZ9%2FV%2BdNHqnwef81JzHY%2FvzpUA9bCKsjeBIbO2aVnqNPQk4SkVnFuIya22C0S3sLsB8mcvVNCVUXzo893fw7nzd7xVUmLLy0mLrPJ1gfjrPcgT9qbeP6OVHWf4D&RelayState=null");

        samlPostResponseMessage = new HttpMessage();
        samlPostResponseMessage.setRequestHeader(new HttpRequestHeader());
        samlPostResponseMessage.getRequestHeader().setURI(new HttpsURL("localhost",443,"/samlsso"));
        samlPostResponseMessage.setRequestBody(new HttpRequestBody
                ("SAMLResponse=jZPBbtswDIZfRdDdseNmQCzEKdIUxQJ0qxe7O%2FSmykwjQJY8kU6zt5%2FsxF0ORdCryJ%2Fk" +
                        "%2F5Fa3B4bww7gUTub8%2Bkk4QyscrW2bzl%2Frh6iOb9dLlA2phWrjvZ2C386QGJBZ1EMgZx33gonUaOwsgEUpES5%2BvEo0kkiWu%2FIKWc4WyGCp9Bo7Sx2DfgS%2FEEreN4%2B5nxP1Io4Nk5Js3dIYp7MkxjRGfembRATef3aEZzEYb6zemNrOIbR05ssnX3LZhlnD84rGKbN%2BU4aBM429zkP3jZYSER9gP8BxC7UQJKWcp4m05soyaJkWk1nIk1Fmkxm2fyFs%2BJs407bE5xrnl9PSSi%2BV1URFU9lxdnvEXJI4CPSobv%2FOkw5IuTLsnwqZdMaWLXtIr4sNxb%2FGeSb%2B8IZrf6ylTHufe1BUvBOvoMBUyPpesP%2BRdfRbkgVbe8BCSxxVhZ9%2FV%2BdNHqnwef81JzHY%2FvzpUA9bCKsjeBIbO2aVnqNPQk4SkVnFuIya22C0S3sLsB8mcvVNCVUXzo893fw7nzd7xVUmLLy0mLrPJ1gfjrPcgT9qbeP6OVHWf4D&RelayState=null"));


        nonSamlGetRequestMessage = new HttpMessage();
        nonSamlGetRequestMessage.setRequestHeader(new HttpRequestHeader());
        nonSamlGetRequestMessage.getRequestHeader().setURI(new HttpsURL("localhost", 443, "/samlsso",
                "p=value1&q=value2"));

        nonSamlPostRequestMessage = new HttpMessage();
        nonSamlPostRequestMessage.setRequestHeader(new HttpRequestHeader());
        nonSamlPostRequestMessage.getRequestHeader().setURI(new HttpsURL("localhost",443,"/samlsso"));
        nonSamlPostRequestMessage.setRequestBody(new HttpRequestBody("p=value1&q=value2"));

        samlMessageString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><samlp:AuthnRequest xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" AssertionConsumerServiceURL=\"http://localhost:8080/ssologin\" AttributeConsumingServiceIndex=\"1239245949\" ForceAuthn=\"false\" ID=\"0\" IsPassive=\"false\" IssueInstant=\"2013-09-01T14:22:20.498Z\" ProtocolBinding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST\" Version=\"2.0\">\n" +
                "    <samlp:Issuer xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:assertion\">SSOSampleApp</samlp:Issuer>\n" +
                "    <samlp:NameIDPolicy AllowCreate=\"true\" Format=\"urn:oasis:names:tc:SAML:2.0:nameid-format:persistent\" SPNameQualifier=\"Issuer\"/>\n" +
                "    <samlp:RequestedAuthnContext Comparison=\"exact\">\n" +
                "        <saml:AuthnContextClassRef xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\">urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport</saml:AuthnContextClassRef>\n" +
                "    </samlp:RequestedAuthnContext>\n" +
                "</samlp:AuthnRequest>";
    }

    private static void initConfigurations() throws SAMLException {
        testConfigutations = SAMLConfiguration.getInstance();
        testConfigutations.initialize(TestConstants.class.getResource("zap_saml_conf.xml").getFile());
    }
}
