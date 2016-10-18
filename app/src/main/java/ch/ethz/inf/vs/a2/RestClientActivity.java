package ch.ethz.inf.vs.a2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import ch.ethz.inf.vs.a2.sensor.RawHttpSensor;
import ch.ethz.inf.vs.a2.sensor.SensorListener;

public class RestClientActivity extends AppCompatActivity implements SensorListener {
    private RawHttpSensor rawSensor = new RawHttpSensor();
    @Override
    protected void onDestroy() {
        rawSensor.unregisterListener(this);

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_client);

        rawSensor.registerListener(this);
        rawSensor.getTemperature();
    }

    @Override
    public void onReceiveSensorValue(double value) {
        TextView temp = (TextView) findViewById(R.id.temperature);
        temp.setText(Double.toString(value) + " °C");
    }

    @Override
    public void onReceiveMessage(String message) {
        String m = message;
    }
}
