package ch.ethz.inf.vs.a2.sensor;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ch.ethz.inf.vs.a2.http.HttpRawRequest;
import ch.ethz.inf.vs.a2.http.HttpRawRequestImpl;

/**
 * Created by george on 18.10.16.
 */

public class RawHttpSensor extends AbstractSensor {
    public static final int PORT = 8081;
    public static final String HOST = "vslab.inf.ethz.ch";
    public static final String PATH = "/sunspots/Spot1/sensors/temperature";

    @Override
    public String executeRequest() throws Exception  {
        StringBuffer response = new StringBuffer();

        HttpRawRequest req = new HttpRawRequestImpl();
        String rawReq = req.generateRequest(HOST, PORT, PATH);

        Socket s = new Socket(HOST, PORT);
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

        return response.toString();
    }

    @Override
    public double parseResponse(String response) {
        String[] matches = response.split("getterValue\">")[1].split("</");
        return Double.parseDouble(matches[0]);

    }
}
