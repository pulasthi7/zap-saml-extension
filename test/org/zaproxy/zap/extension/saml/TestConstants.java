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

    /**
     * Initialize the test constants
     * @throws Exception
     */
    public static void initConstants() throws Exception {
        samlGetRequestMessage = new HttpMessage();
        samlGetRequestMessage.setRequestHeader(new HttpRequestHeader());
        samlGetRequestMessage.getRequestHeader().setURI(new HttpsURL("localhost",443,"/samlsso",
                "SAMLRequest=jZPBbtswDIZfRdDdseNmQCzEKdIUxQJ0qxe7O%2FSmykwjQJY8kU6zt5%2FsxF0ORdCryJ%2Fk%2F5Fa3B4bww7gUTub8%2Bkk4QyscrW2bzl%2Frh6iOb9dLlA2phWrjvZ2C386QGJBZ1EMgZx33gonUaOwsgEUpES5%2BvEo0kkiWu%2FIKWc4WyGCp9Bo7Sx2DfgS%2FEEreN4%2B5nxP1Io4Nk5Js3dIYp7MkxjRGfembRATef3aEZzEYb6zemNrOIbR05ssnX3LZhlnD84rGKbN%2BU4aBM429zkP3jZYSER9gP8BxC7UQJKWcp4m05soyaJkWk1nIk1Fmkxm2fyFs%2BJs407bE5xrnl9PSSi%2BV1URFU9lxdnvEXJI4CPSobv%2FOkw5IuTLsnwqZdMaWLXtIr4sNxb%2FGeSb%2B8IZrf6ylTHufe1BUvBOvoMBUyPpesP%2BRdfRbkgVbe8BCSxxVhZ9%2FV%2BdNHqnwef81JzHY%2FvzpUA9bCKsjeBIbO2aVnqNPQk4SkVnFuIya22C0S3sLsB8mcvVNCVUXzo893fw7nzd7xVUmLLy0mLrPJ1gfjrPcgT9qbeP6OVHWf4D&RelayState=null"));

        samlPostRequestMessage = new HttpMessage();
        samlPostRequestMessage.setRequestHeader(new HttpRequestHeader());
        samlPostRequestMessage.getRequestHeader().setURI(new HttpsURL("localhost",443,"/samlsso"));
        samlPostRequestMessage.setRequestBody(new HttpRequestBody("SAMLRequest=jZPBbtswDIZfRdDdseNmQCzEKdIUxQJ0qxe7O%2FSmykwjQJY8kU6zt5%2FsxF0ORdCryJ%2Fk%2F5Fa3B4bww7gUTub8%2Bkk4QyscrW2bzl%2Frh6iOb9dLlA2phWrjvZ2C386QGJBZ1EMgZx33gonUaOwsgEUpES5%2BvEo0kkiWu%2FIKWc4WyGCp9Bo7Sx2DfgS%2FEEreN4%2B5nxP1Io4Nk5Js3dIYp7MkxjRGfembRATef3aEZzEYb6zemNrOIbR05ssnX3LZhlnD84rGKbN%2BU4aBM429zkP3jZYSER9gP8BxC7UQJKWcp4m05soyaJkWk1nIk1Fmkxm2fyFs%2BJs407bE5xrnl9PSSi%2BV1URFU9lxdnvEXJI4CPSobv%2FOkw5IuTLsnwqZdMaWLXtIr4sNxb%2FGeSb%2B8IZrf6ylTHufe1BUvBOvoMBUyPpesP%2BRdfRbkgVbe8BCSxxVhZ9%2FV%2BdNHqnwef81JzHY%2FvzpUA9bCKsjeBIbO2aVnqNPQk4SkVnFuIya22C0S3sLsB8mcvVNCVUXzo893fw7nzd7xVUmLLy0mLrPJ1gfjrPcgT9qbeP6OVHWf4D&RelayState=null"));

        samlGetResponseMessage = new HttpMessage();
        samlGetResponseMessage.setRequestHeader(new HttpRequestHeader());
        samlGetResponseMessage.getRequestHeader().setURI(new HttpsURL("localhost",443,"/samlsso",
                "SAMLResponse=jZPBbtswDIZfRdDdseNmQCzEKdIUxQJ0qxe7O%2FSmykwjQJY8kU6zt5%2FsxF0ORdCryJ%2Fk" +
                        "%2F5Fa3B4bww7gUTub8%2Bkk4QyscrW2bzl%2Frh6iOb9dLlA2phWrjvZ2C386QGJBZ1EMgZx33gonUaOwsgEUpES5%2BvEo0kkiWu%2FIKWc4WyGCp9Bo7Sx2DfgS%2FEEreN4%2B5nxP1Io4Nk5Js3dIYp7MkxjRGfembRATef3aEZzEYb6zemNrOIbR05ssnX3LZhlnD84rGKbN%2BU4aBM429zkP3jZYSER9gP8BxC7UQJKWcp4m05soyaJkWk1nIk1Fmkxm2fyFs%2BJs407bE5xrnl9PSSi%2BV1URFU9lxdnvEXJI4CPSobv%2FOkw5IuTLsnwqZdMaWLXtIr4sNxb%2FGeSb%2B8IZrf6ylTHufe1BUvBOvoMBUyPpesP%2BRdfRbkgVbe8BCSxxVhZ9%2FV%2BdNHqnwef81JzHY%2FvzpUA9bCKsjeBIbO2aVnqNPQk4SkVnFuIya22C0S3sLsB8mcvVNCVUXzo893fw7nzd7xVUmLLy0mLrPJ1gfjrPcgT9qbeP6OVHWf4D&RelayState=null"));

        samlPostResponseMessage = new HttpMessage();
        samlPostResponseMessage.setRequestHeader(new HttpRequestHeader());
        samlPostResponseMessage.getRequestHeader().setURI(new HttpsURL("localhost",443,"/samlsso"));
        samlPostResponseMessage.setRequestBody(new HttpRequestBody
                ("SAMLResponse=jZPBbtswDIZfRdDdseNmQCzEKdIUxQJ0qxe7O%2FSmykwjQJY8kU6zt5%2FsxF0ORdCryJ%2Fk" +
                        "%2F5Fa3B4bww7gUTub8%2Bkk4QyscrW2bzl%2Frh6iOb9dLlA2phWrjvZ2C386QGJBZ1EMgZx33gonUaOwsgEUpES5%2BvEo0kkiWu%2FIKWc4WyGCp9Bo7Sx2DfgS%2FEEreN4%2B5nxP1Io4Nk5Js3dIYp7MkxjRGfembRATef3aEZzEYb6zemNrOIbR05ssnX3LZhlnD84rGKbN%2BU4aBM429zkP3jZYSER9gP8BxC7UQJKWcp4m05soyaJkWk1nIk1Fmkxm2fyFs%2BJs407bE5xrnl9PSSi%2BV1URFU9lxdnvEXJI4CPSobv%2FOkw5IuTLsnwqZdMaWLXtIr4sNxb%2FGeSb%2B8IZrf6ylTHufe1BUvBOvoMBUyPpesP%2BRdfRbkgVbe8BCSxxVhZ9%2FV%2BdNHqnwef81JzHY%2FvzpUA9bCKsjeBIbO2aVnqNPQk4SkVnFuIya22C0S3sLsB8mcvVNCVUXzo893fw7nzd7xVUmLLy0mLrPJ1gfjrPcgT9qbeP6OVHWf4D&RelayState=null"));


        nonSamlGetRequestMessage = new HttpMessage();
        nonSamlGetRequestMessage.setRequestHeader(new HttpRequestHeader());
        nonSamlGetRequestMessage.getRequestHeader().setURI(new HttpsURL("localhost",443,"/samlsso",
                "p=value1&q=value2"));

        nonSamlPostRequestMessage = new HttpMessage();
        nonSamlPostRequestMessage.setRequestHeader(new HttpRequestHeader());
        nonSamlPostRequestMessage.getRequestHeader().setURI(new HttpsURL("localhost",443,"/samlsso"));
        nonSamlPostRequestMessage.setRequestBody(new HttpRequestBody("p=value1&q=value2"));
    }
}
