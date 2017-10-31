package com.example.lucainnocenti.photonotation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.nio.channels.FileChannel;
import java.util.Locale;

public class SlopeActivity extends AppCompatActivity implements SensorEventListener, TextToSpeech.OnInitListener {
    private SensorManager mSensorManager;
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;

    private double az;
    private double pi;
    private double ro;

    private TextView pendenza;
    private Button parla;

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slope);

        pendenza = (TextView) findViewById(R.id.txtslope);
        parla = (Button) findViewById(R.id.buttonspeech);
        tts = new TextToSpeech(this,this);

        parla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakOut();

            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }


    @Override    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);

    }

    @Override    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override    public void onSensorChanged(SensorEvent event) {

        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mAccelerometerReading, 0, mAccelerometerReading.length);
        }
        else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mMagnetometerReading, 0, mMagnetometerReading.length);
        }
        updateOrientationAngles();
    }


    public void updateOrientationAngles() {
        mSensorManager.getRotationMatrix(mRotationMatrix, null, mAccelerometerReading, mMagnetometerReading);
        mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
        az = mOrientationAngles[0];
        pi = mOrientationAngles[1];
        ro = mOrientationAngles[2];
        //https://benthamopen.com/contents/pdf/TOASJ/TOASJ-6-36.pdf
        Double max_slope = Math.toDegrees(Math.asin((Math.sqrt((Math.sin(pi)*Math.sin(pi))+(Math.sin(ro)* Math.sin(ro))))));
        Log.d("slope", "Slope:"+Double.toString(max_slope));
        int ss =  (int) Math.round(max_slope);
        pendenza.setText(Integer.toString(ss)+" " + (char) 0x00B0 );
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                parla.setEnabled(true);
                //speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
            parla.setEnabled(false);
        }

    }

    private void speakOut() {
        CharSequence text = pendenza.getText();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,"id1");
    }


}
