package com.example.recorddata;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.toString();
    private boolean startRecording = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ToggleButton button = (ToggleButton) findViewById(R.id.button1);
        button.setOnCheckedChangeListener((compoundButton, on) -> {
            startRecording = on;
        });
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                TextView txtStatus = (TextView) findViewById(R.id.accData);
                txtStatus.setText(event.values[0] + "\n" + event.values[1] + "\n" + event.values[2]);
                if (startRecording) {
                    saveAccelerationData(event);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void saveAccelerationData(SensorEvent event) {
        File dataFile = new File(getFilesDir().getAbsolutePath() + "/data.txt");
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
        long timestamp = System.currentTimeMillis() + (event.timestamp - SystemClock.elapsedRealtimeNanos()) / 1000000;
        String timeStamp = timeStampFormat.format(new Date(timestamp));
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(dataFile, true);
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            writer.append(Arrays.toString(event.values)).append("\t").append(timeStamp).append("\n");
            writer.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}