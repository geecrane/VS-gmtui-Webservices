package ch.ethz.inf.vs.a2;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


/**
 * Created by martin on 19.10.16.
 */

public class ServerService extends Service {
    private ServerSocket serverSocket;
    private Thread serverThread;
    private List<Sensor> sensorList;
    private SensorManager sensorManager;
    private final LocalBinder lBinder = new LocalBinder();

    @Override
    public void onCreate() {
        try {
            serverSocket =  new ServerSocket(RestServerActivity.portNum);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get sensorlist
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // create our Server
        serverThread = new Thread(new ServerThread());
        serverThread.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return lBinder;
    }

    public void startServer(){
        try {
            serverSocket = new ServerSocket(RestServerActivity.portNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // restart a new ServerThread
        serverThread = new Thread(new ServerThread());
        serverThread.start();
    }

    public void stopServer(){
        // close serverThread
        serverThread.interrupt();

        //safely close up the Socket
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class LocalBinder extends Binder{
       ServerService getService(){
           return ServerService.this;
       }
    }

    class ServerThread implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {

                    // accept new incomming traffic
                    Socket socket = serverSocket.accept();

                    // transfer traffic to communication thread
                    CommThread commt = new CommThread(socket);
                    (new Thread(commt)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    class CommThread implements Runnable{
        private Integer TimeOut = 500;
        private Socket clientSocket;
        private BufferedReader input;
        private PrintStream output;
        private String htmlStart = "<html><head></head><body><ul>";
        private String htmlEnd = "</ul></body></html>";
        private String htmlback = "<a href=\"../\"> back </a>";
        String route;

        public CommThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // receive request
                    this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
                    String line = this.input.readLine();

                    // check request for get
                    while (line != null && !line.equals("")) {
                        if (line.startsWith("GET /")) {
                            int start = line.indexOf("/") + 1;
                            int end = line.indexOf(" ", start);
                            route = line.substring(start, end);
                            break;
                        }
                        line = this.input.readLine();
                    }

                    output = new PrintStream(this.clientSocket.getOutputStream());

                    // check if got a valid request
                    if(route == null){
                            output.println("HTTP/1.0 500 ERROR");
                    }
                    else{
                        byte[] out = new byte[]{1};
                        try {
                            out = getOutputBytes(route);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        output.println("HTTP/1.0 200 OK");
                        output.println("Content-Type: text/html");
                        output.println("Content-Length: " + out.length);
                        output.println();
                        output.write(out);
                    }

                    output.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                    // safely close connections
                    if (output != null) {
                        output.close();
                    }
                    if (this.input != null) {
                        try {
                            this.input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private byte[] getOutputBytes(String route) throws InterruptedException {
            // check what part of our "website" we want to access

            if (route.equals("")) {
                //home
                return getHTMLlist().getBytes();
            }
            else if(route.matches("\\d*\\.?\\d+") && Integer.parseInt(route) < sensorList.size()){
                // sensor
                return getSensorValue(Integer.parseInt(route)).getBytes();
            }
            else if(route.equals("vibrator")){
                // let phone vibrate
                vibrate();
                String html = htmlStart;
                html +="vibrating for 5 seconds<p>";
                html +="<a href=\"./\"> again </a><p>";
                html += htmlback;
                html += htmlEnd;
               return html.getBytes();
            }
            else if(route.equals("sound")){
                // play sound
                play_sound();
                String html = htmlStart;
                html += "sending command to play sound!<p>";
                html += "<a href=\"./\"> again </a><p>";
                html += htmlback;
                html += htmlEnd;
                return html.getBytes();
            }
            else{

                return "Nothing to see here".getBytes();
            }

        }

        private String getHTMLlist(){
            //create home html depending on the sensoer the phone has acces to
            String html;
            Integer x = 0;
            html = htmlStart;

            // make a hyperlink for each sensor in sensorlist
            for(Sensor sensor: sensorList){
                html += "<li><a  href=\"./" + x.toString() + "\">" + sensor.getName() + "</a></li>";
                x += 1;
            }

            html += "<p>";
            html += "<li><a href=\"./vibrator\"> vibrate </a></li>";
            html += "<li><a href=\"./sound\"> sound </a></li>";
            html += htmlEnd;
           return html;
        }

        private String getSensorValue(Integer position) throws InterruptedException {
            String html;
            Sensor sensor = sensorList.get(position);
            html = sensor.getName().toString();

            // create Thread that reads out sensordata
            DataReader readData = new DataReader(sensor);
            (new Thread(readData)).start();

            // block until sensor returns value or sensor times out
            synchronized (sensor){
                sensor.wait(this.TimeOut);
            }

            float[] values = readData.data;

            // the sensor times out we give a sensorvalue of 0
            if(values == null){
                values =new float[]{0};
            }

            // print out every sensor-value
            for(float val: values){
                html += "<li>" + val + "</li>";
            }

            // link back
            html += "<a href=\"../\"> back </a>";
            return html;
        }

        private void play_sound(){
            MediaPlayer mp = RestServerActivity.mediaPlayer;
            mp.setVolume(1.0f, 1.0f);
            mp.start();
        }

        private void vibrate(){
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(5000);
        }
    }

    class DataReader implements Runnable, SensorEventListener{
        public float[] data;
        Sensor mySensor;

        public DataReader(Sensor sensor){
            mySensor = sensor;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // make data readable for later
            data = event.values.clone();

            // notify our waiting communitaction-thread
            synchronized (mySensor){
                mySensor.notifyAll();
            }

            // close up
            sensorManager.unregisterListener(this);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void run() {
            // register listener for our sensor
            sensorManager.registerListener(this, mySensor, sensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}
