package ch.ethz.inf.vs.a2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ch.ethz.inf.vs.a2.sensor.SensorListener;

public class RestClientActivity extends AppCompatActivity implements SensorListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_client);
    }

    @Override
    public void onReceiveSensorValue(double value) {

    }

    @Override
    public void onReceiveMessage(String message) {

    }
}
