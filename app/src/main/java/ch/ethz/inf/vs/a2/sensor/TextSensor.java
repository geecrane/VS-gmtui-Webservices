package ch.ethz.inf.vs.a2.sensor;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.ethz.inf.vs.a2.sensor.AbstractSensor;

/**
 * Created by george on 18.10.16.
 */

public class TextSensor extends AbstractSensor {
    private String CONTENT_TYPE = "text/plain";

    public String executeRequest() throws Exception {


        URL obj = new URL("http://"+RawHttpSensor.HOST+":"+RawHttpSensor.PORT+RawHttpSensor.PATH);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", CONTENT_TYPE);
        con.setRequestProperty("Connection", "close");


        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        return response.toString();
    }

    @Override
    public double parseResponse(String response) {
        double t = Double.parseDouble(response);
        return t;
    }
}
