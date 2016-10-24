package ch.ethz.inf.vs.a2.sensor;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ch.ethz.inf.vs.a2.http.HttpRawRequest;
import ch.ethz.inf.vs.a2.http.HttpRawRequestImpl;
import ch.ethz.inf.vs.a2.http.RemoteServerConfiguration;

/**
 * Created by george on 18.10.16.
 */

public class RawHttpSensor extends AbstractSensor {

    @Override
    public String executeRequest() throws Exception  {
        StringBuffer response = new StringBuffer();

        HttpRawRequest req = new HttpRawRequestImpl();
        String rawReq = req.generateRequest(RemoteServerConfiguration.HOST, RemoteServerConfiguration.REST_PORT, RemoteServerConfiguration.REST_PATH);

        Socket s = new Socket(RemoteServerConfiguration.HOST, RemoteServerConfiguration.REST_PORT);
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        pw.print(rawReq);
        pw.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        pw.close();
        s.close();

        //return response.toString();
        return in.toString();
    }

    @Override
    public double parseResponse(String response) {
        return Double.parseDouble(response.split("close")[1]);

    }
}
