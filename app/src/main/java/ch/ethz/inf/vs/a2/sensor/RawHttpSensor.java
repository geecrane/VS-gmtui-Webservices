package ch.ethz.inf.vs.a2.sensor;

import ch.ethz.inf.vs.a2.sensor.AbstractSensor;

/**
 * Created by george on 18.10.16.
 */

public class RawHttpSensor extends AbstractSensor {
    @Override
    public String executeRequest() throws Exception {
        /*BufferedReader br = null;
        try {
            Socket s = new Socket(host, 80);
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.print("GET / HTTP/1.1 \r\n");
            pw.print("Host: " + host + "\r\n\r\n");
            pw.flush();

            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String t;

            while ((t = br.readLine()) != null) Log.d("RAWHTTP", t);
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    public double parseResponse(String response) {
        return 0;
    }
}
