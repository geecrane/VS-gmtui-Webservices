package ch.ethz.inf.vs.a2.sensor;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.ethz.inf.vs.a2.sensor.AbstractSensor;

/**
 * Created by george on 18.10.16.
 */

public class JsonSensor extends AbstractSensor {
    private String CONTENT_TYPE = "application/json";

    @Override
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

        try {
            JSONObject jObject = new JSONObject(response);
            return jObject.getDouble("value");
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }

    }
}
