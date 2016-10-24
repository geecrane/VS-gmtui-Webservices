package ch.ethz.inf.vs.a2.sensor;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class SoapSensor extends AbstractSensor {
    //fields used for creation/processing the request with help of the libary
    private static final String OPERATION_NAME = "getSpot";
    private static final String WSDL_TARGET_NAMESPACE = "http://webservices.vslecture.vs.inf.ethz.ch/";
    private static final String SOAP_ADDRESS = "http://vslab.inf.ethz.ch:8080/SunSPOTWebServices/SunSPOTWebservice";
    private static final String SOAP_ACTION = "http://webservices.vslecture.vs.inf.ethz.ch/getSpot";


    @Override
    public String executeRequest() throws Exception {
        //create a soap object
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
        request.addProperty("id", "Spot4");

        //create an envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        //create a http transport with corresponnding adress
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        try {
            //call ...
            httpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            return response.getProperty("temperature").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // parsing response by converting to double
    @Override
    public double parseResponse(String response) {
        return Double.parseDouble(response);
    }

}
