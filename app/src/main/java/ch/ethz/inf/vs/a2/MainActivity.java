package ch.ethz.inf.vs.a2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ch.ethz.inf.vs.a2.sensor.RawHttpSensor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickTask(View v){
        Intent intent;
        switch (v.getId()) {
            case R.id.btnTask1:
                 intent = new Intent(this,RestClientActivity.class);
                startActivity(intent);
                break;
            case R.id.btnTask2: break;
            case R.id.btnTask3:
                intent = new Intent(this,RestServerActivity.class);
                startActivity(intent);
                break;
        }
    }

}
