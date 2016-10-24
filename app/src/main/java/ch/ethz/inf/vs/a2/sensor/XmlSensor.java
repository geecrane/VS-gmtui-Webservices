package ch.ethz.inf.vs.a2.sensor;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;



public class XmlSensor extends AbstractSensor {

    // parsing response with help of an xml pull parser (-274 if error)
    @Override
    public double parseResponse(String response) {
        XmlPullParserFactory factory;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(response));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG && parser.getName().equals("temperature")) {
                    parser.next();
                    return Double.parseDouble(parser.getText());
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -274;
    }


    @Override
    public  String executeRequest() throws Exception {

        //create URL request and a Http connection, then make a connection
        URL requestURL = new URL("http://vslab.inf.ethz.ch:8080/SunSPOTWebServices/SunSPOTWebservice");
        HttpURLConnection requestConnection = (HttpURLConnection) requestURL.openConnection();
        initConnection(requestConnection);

        //create output stream and write the request in the stream and send
        DataOutputStream dos = new DataOutputStream(requestConnection.getOutputStream());
        writeRequest(dos);
        dos.flush();
        dos.close();

        //read the answer from input stream and return the response as a string
        BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream(requestConnection)));
        return getStringFromInputStream(reader);
    }

    //writing request into the output stream
    private void writeRequest(DataOutputStream dos) throws Exception{
        dos.writeBytes("<?xml version=\"1.0\" encoding=\"UTF-8\"?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<S:Header/>" +
                "<S:Body>" +
                "<ns2:getSpot xmlns:ns2=\"http://webservices.vslecture.vs.inf.ethz.ch/\">" +
                "<id>Spot4</id>" +
                "</ns2:getSpot>" +
                "</S:Body>" +
                "</S:Envelope>");
    }

    //init the http-connection correctly such that the request is POST and as a xml
    private void initConnection(HttpURLConnection connection) throws Exception{
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "text/xml");
    }

    //reading the answer out of the input stream
    private InputStream getInputStream (HttpURLConnection connection) throws Exception {
        if (connection.getResponseCode() != 401) {
            return  connection.getInputStream();
        } else {
            return connection.getErrorStream();
        }
    }

    //transforming the answer out of the input stream in a string
    private String getStringFromInputStream (BufferedReader in) throws Exception{
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

}
