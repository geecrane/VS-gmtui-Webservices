package ch.ethz.inf.vs.a2.sensor;

import android.util.Log;

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
    private final int PORT = 8081;
    private final String HOST = "vslab.inf.ethz.ch";
    private final String PATH = "/sunspots/Spot1/sensors/temperature";

    @Override
    public String executeRequest() throws Exception  {
        HttpRawRequest req = new HttpRawRequestImpl();
        String rawReq = req.generateRequest(HOST, PORT, PATH);
        BufferedReader br;
        String response = "";


        Socket s = new Socket(HOST, PORT);
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        pw.print(rawReq);
        pw.flush();

        br = new BufferedReader(new InputStreamReader(s.getInputStream()));

        String t;
        while ((t = br.readLine()) != null) {
            Log.d("HTTP_RESPONSE", t);
            response +=  t + " \r\n";
        }

        br.close();
        pw.close();
        s.close();


        return response;
    }

    @Override
    public double parseResponse(String response) {
        String[] matches = response.split("getterValue\">")[1].split("</");
        return Double.parseDouble(matches[0]);

    }
}
