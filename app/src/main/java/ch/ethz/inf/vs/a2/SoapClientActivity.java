package ch.ethz.inf.vs.a2;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ch.ethz.inf.vs.a2.sensor.SensorListener;
import ch.ethz.inf.vs.a2.sensor.SoapSensor;
import ch.ethz.inf.vs.a2.sensor.XmlSensor;

public class SoapClientActivity extends AppCompatActivity implements SensorListener {

    TextView text,temperature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soap_client);

        //init of the Buttons and the Textfields
        text = (TextView)findViewById(R.id.text);
        temperature = (TextView)findViewById(R.id.temperature);
        initSoapManualButton();
        initSoapLibaryButton();

    }

    //init of the "manual" Button
    private void initSoapManualButton(){
        Button manual = (Button)findViewById(R.id.manual);
        final XmlSensor soapManualButton = new XmlSensor();
        soapManualButton.registerListener(this);
        manual.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                text.setText(R.string.temperatureM);
                temperature.setText(R.string.loading);
                soapManualButton.getTemperature();
            }
        });
    }

    //init of the "libary" Button
    private void initSoapLibaryButton(){
        Button libary = (Button)findViewById(R.id.library);
        final SoapSensor soapLibaryButton = new SoapSensor();
        soapLibaryButton.registerListener(this);
        libary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                text.setText(R.string.temperatureL);
                temperature.setText(R.string.loading);
                soapLibaryButton.getTemperature();
            }
        });
    }

    //set temperature on receive
    public void onReceiveSensorValue(double value){
        temperature.setText(""+ String.valueOf(value)+"°C");
    }

    public void onReceiveMessage(String message){

    }
}
