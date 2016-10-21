package ch.ethz.inf.vs.a2.http;


import ch.ethz.inf.vs.a2.http.HttpRawRequest;

/**
 * Created by george on 18.10.16.
 */

public class HttpRawRequestImpl implements HttpRawRequest {
    @Override
    public String generateRequest(String host, int port, String path) {
        String request = "";
        request += "GET "+path+" HTTP/1.1\r\n";
        request += "Host: " + host + ":"+port+"\r\n";
        request += "Accept: text/plain\r\n";
        request += "Connection: close\r\n";
        request += "\r\n";

        return request;
    }
}
