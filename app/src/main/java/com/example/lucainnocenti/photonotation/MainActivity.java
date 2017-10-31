package com.example.lucainnocenti.photonotation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;


import android.hardware.SensorEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SensorEventListener,LocationListener{
    private static final int CAMERA_REQUEST = 1888;
    private ImageView foto;
    private Bitmap ff;
    private EditText note;
    private EditText rischio;
    private EditText locationtxt;




    //ACCELEROMETRO
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

    // GPS
    LocationManager locationManager;
    String provider;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private double lng;
    private double lat;

    //JSON
    private int requestCode;
    private int grantResults[];
    private FileOutputStream out;
    private JsonWriter writer;
    File card;


    private Bitmap bm;

    private ScrollView scrolla;
    private int scattato;
    private Spinner g11;
    private Spinner g12;
    private Spinner g13;
    private Spinner g14;
    private Spinner g15;
    private Spinner g16;
    private Spinner g17;
    private Spinner g18;
    private Spinner g19;
    private Spinner m21;
    private Spinner m22;
    private Spinner m23;
    private Spinner m24;
    private Spinner b31;
    private Spinner b32;
    private Spinner b33;
    private Spinner b34;



    private int g11_score;
    private int g12_score;
    private int g13_score;
    private int g14_score;
    private int g15_score;
    private int g16_score;
    private int g17_score;
    private int g18_score;
    private int g19_score;
    private int m21_score;
    private int m22_score;
    private int m23_score;
    private int m24_score;
    private int b31_score;
    private int b32_score;
    private int b33_score;
    private int b34_score;

    private String risk;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scattato = 0;
        g11_score = 1;
        g12_score = 1;
        g13_score = 1;
        g14_score = 1;
        g15_score = 1;
        g16_score = 1;
        g17_score = 1;
        g18_score = 1;
        g19_score = 1;
        m21_score = 1;
        m22_score = 1;
        m23_score = 1;
        m24_score = 1;
        b31_score = 1;
        b32_score = 1;
        b33_score = 1;
        b34_score = 1;


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Log.i("Location Info", "Location achieved!");
        } else {
            Log.i("Location Info", "No location :(");
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        onRequestPermissionsResult(requestCode, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, grantResults);


        foto = (ImageView) findViewById(R.id.imageView);
        note = (EditText) findViewById(R.id.editText2);
        rischio = (EditText) findViewById(R.id.editrisk);
        locationtxt = (EditText) findViewById(R.id.editlocation2);


        scrolla = (ScrollView) findViewById(R.id.scroller);
        g11 = (Spinner) findViewById(R.id.spinner_g11);
        g12 = (Spinner) findViewById(R.id.spinner_g12);
        g13 = (Spinner) findViewById(R.id.spinner_g13);
        g14 = (Spinner) findViewById(R.id.spinner_g14);
        g15 = (Spinner) findViewById(R.id.spinner_g15);
        g16 = (Spinner) findViewById(R.id.spinner_g16);
        g17 = (Spinner) findViewById(R.id.spinner_g17);
        g18 = (Spinner) findViewById(R.id.spinner_g18);
        g19 = (Spinner) findViewById(R.id.spinner_g19);
        m21 = (Spinner) findViewById(R.id.spinner_m21);
        m22 = (Spinner) findViewById(R.id.spinner_m22);
        m23 = (Spinner) findViewById(R.id.spinner_m23);
        m24 = (Spinner) findViewById(R.id.spinner_m24);
        b31 = (Spinner) findViewById(R.id.spinner_b31);
        b32 = (Spinner) findViewById(R.id.spinner_b32);
        b33 = (Spinner) findViewById(R.id.spinner_b33);
        b34 = (Spinner) findViewById(R.id.spinner_b34);

        // G11
        g11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","G11 " + Integer.toString(i));
                switch(i){
                    case 0:
                        g11_score = 1;
                        break;
                    case 1:
                        g11_score = 10;
                        break;
                    case 2:
                        g11_score = 20;
                        break;
                }
                calcola();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE G11

        // G12
        g12.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","G12 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        g12_score = 1;
                        break;
                    case 1:
                        g12_score = 5;
                        break;
                    case 2:
                        g12_score = 10;
                        break;
                    case 3:
                        g12_score = 20;
                        break;
                }
                calcola();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE G12

        // G13
        g13.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","G13 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        g13_score = 1;
                        break;
                    case 1:
                        g13_score = 5;
                        break;
                    case 2:
                        g13_score = 10;
                        break;
                }
                calcola();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE G13

        // G14
        g14.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","G14 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        g14_score = 1;
                        break;
                    case 1:
                        g14_score = 5;
                        break;
                    case 2:
                        g14_score = 10;
                        break;
                }
                calcola();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE G14

        // G15
        g15.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","G15 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        g15_score = 0;
                        break;
                    case 1:
                        g15_score = 1;
                        break;
                    case 2:
                        g15_score = 3;
                        break;
                    case 3:
                        g15_score = 5;
                        break;
                    case 4:
                        g15_score = 10;
                        break;

                }
                calcola();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE G15


        // G16
        g16.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","G16 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        g16_score = 0;
                        break;
                    case 1:
                        g16_score = 1;
                        break;
                    case 2:
                        g16_score = 5;
                        break;
                    case 3:
                        g16_score = 10;
                        break;
                }
                calcola();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE G16


        // G17
        g17.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","G17 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        g17_score = 1;
                        break;
                    case 1:
                        g17_score = 10;
                        break;
                }
                calcola();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE G17


        // G18
        g18.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","G18 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        g18_score = 1;
                        break;
                    case 1:
                        g18_score = 20;
                        break;
                }
                calcola();
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE G18


        // G19
        g19.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","G19 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        g19_score = 1;
                        break;
                    case 1:
                        g19_score = 20;
                        break;
                }
                calcola();
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE G19

        // M21
        m21.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","M21 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        m21_score = 1;
                        break;
                    case 1:
                        m21_score = 20;
                        break;
                }
                calcola();
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE M21

        // M22
        m22.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","M22 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        m22_score = 1;
                        break;
                    case 1:
                        m22_score = 20;
                        break;
                }
                calcola();
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE M22

        // M23
        m23.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","M23 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        m23_score = 1;
                        break;
                    case 1:
                        m23_score = 10;
                        break;
                }
                calcola();
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE M23

        // M24
        m24.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","M24 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        m24_score = 1;
                        break;
                    case 1:
                        m24_score = 10;
                        break;
                }
                calcola();
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE M24


        // B31
        b31.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","B31 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        b31_score = 1;
                        break;
                    case 1:
                        b31_score = 10;
                        break;
                }
                calcola();
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE B31


        // B32
        b32.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","B32 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        b32_score = 1;
                        break;
                    case 1:
                        b32_score = 5;
                        break;
                    case 2:
                        b32_score = 10;
                        break;

                }
                calcola();
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE B32

        // B33
        b33.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","B33 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        b33_score = 0;
                        break;
                    case 1:
                        b33_score = 1;
                        break;
                    case 2:
                        b33_score = 10;
                        break;

                }
                calcola();
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE B33

        // B33
        b34.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Photonotation","B34 "+ Integer.toString(i));
                switch(i){
                    case 0:
                        b34_score = 1;
                        break;
                    case 1:
                        b34_score = 10;
                        break;
                }
                calcola();
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // FINE B33

        foto.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(camera, CAMERA_REQUEST);
                                    }
                                }
        );

        Button salva = (Button) findViewById(R.id.button1);
        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scattato == 1) {
                    card = Environment.getExternalStorageDirectory();
                    File file = new File(Environment.getExternalStorageDirectory().getPath(), "Photonotation");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String percorso = file.getAbsolutePath() + "/" + timeStamp;
                    String uriStringjson = percorso + ".json";
                    String uriStringjpeg = percorso + ".jpg";
                    try {
                        out = new FileOutputStream(uriStringjson);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        writer.beginObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        writer.name("location").value(locationtxt.getText().toString());
                        writer.name("azimuth").value(Double.toString(az));
                        writer.name("pitch").value(Double.toString(pi));
                        writer.name("roll").value(Double.toString(ro));
                        writer.name("latitude").value(Double.toString(lat));
                        writer.name("longitude").value(Double.toString(lng));
                        writer.name("G11").value(Integer.toString(g11_score));
                        writer.name("G12").value(Integer.toString(g12_score));
                        writer.name("G13").value(Integer.toString(g13_score));
                        writer.name("G14").value(Integer.toString(g14_score));
                        writer.name("G15").value(Integer.toString(g15_score));
                        writer.name("G16").value(Integer.toString(g16_score));
                        writer.name("G17").value(Integer.toString(g17_score));
                        writer.name("G18").value(Integer.toString(g18_score));
                        writer.name("G19").value(Integer.toString(g19_score));
                        writer.name("M21").value(Integer.toString(m21_score));
                        writer.name("M22").value(Integer.toString(m22_score));
                        writer.name("M23").value(Integer.toString(m23_score));
                        writer.name("M24").value(Integer.toString(m24_score));
                        writer.name("B31").value(Integer.toString(b31_score));
                        writer.name("B32").value(Integer.toString(b32_score));
                        writer.name("B33").value(Integer.toString(b33_score));
                        writer.name("B34").value(Integer.toString(b34_score));

                        writer.name("Risk").value(risk);
                        writer.name("note").value(note.getText().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        writer.endObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        FileOutputStream outjpeg = null;
                        outjpeg = new FileOutputStream(uriStringjpeg);
                        bm.compress(Bitmap.CompressFormat.PNG, 100, outjpeg);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, "Saving data", Toast.LENGTH_LONG).show();

                    clear_all();
                    scattato = 0;
                }
                else {
                    Toast.makeText(MainActivity.this, "Photo not taken", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    protected void calcola()
    {
        ///calcola
        int w_g11;
        int w_g12;
        int w_g13;
        int w_g14;
        int w_g15;
        int w_g16;
        int w_g17;
        int w_g18;
        int w_g19;
        int w_m21;
        int w_m22;
        int w_m23;
        int w_m24;
        int w_b31;
        int w_b32;
        int w_b33;
        int w_b34;

        int max_g11;
        int max_g12;
        int max_g13;
        int max_g14;
        int max_g15;
        int max_g16;
        int max_g17;
        int max_g18;
        int max_g19;
        int max_m21;
        int max_m22;
        int max_m23;
        int max_m24;
        int max_b31;
        int max_b32;
        int max_b33;
        int max_b34;




        w_g11 = 20;
        w_g12 = 10;
        w_g13 = 10;
        w_g14 = 10;
        w_g15 = 1;
        w_g16 = 5;
        w_g17 = 1;
        w_g18 = 10;
        w_g19 = 10;
        w_m21 = 20;
        w_m22 = 20;
        w_m23 = 1;
        w_m24 = 1;
        w_b31 = 1;
        w_b32 = 1;
        w_b33 = 1;
        w_b34 = 10;

        max_g11 = 20;
        max_g12 = 20;
        max_g13 = 10;
        max_g14 = 10;
        max_g15 = 10;
        max_g16 = 10;
        max_g17 = 10;
        max_g18 = 20;
        max_g19 = 20;
        max_m21 = 20;
        max_m22 = 20;
        max_m23 = 10;
        max_m24 = 10;
        max_b31 = 10;
        max_b32 = 10;
        max_b33 = 10;
        max_b34 = 10;

        Log.d("Photonotation", "G11 "+ g11_score);
        Log.d("Photonotation", "G12 "+ g12_score);
        Log.d("Photonotation", "G13 "+ g13_score);
        Log.d("Photonotation", "G14 "+ g14_score);
        Log.d("Photonotation", "G15 "+ g15_score);
        Log.d("Photonotation", "G16 "+ g16_score);
        Log.d("Photonotation", "G17 "+ g17_score);
        Log.d("Photonotation", "G18 "+ g18_score);
        Log.d("Photonotation", "G19 "+ g19_score);

        Log.d("Photonotation", "M21 "+ m21_score);
        Log.d("Photonotation", "M22 "+ m22_score);
        Log.d("Photonotation", "M23 "+ m23_score);
        Log.d("Photonotation", "M24 "+ m24_score);

        Log.d("Photonotation", "B31 "+ b31_score);
        Log.d("Photonotation", "B32 "+ b32_score);
        Log.d("Photonotation", "B33 "+ b33_score);
        Log.d("Photonotation", "B34 "+ b34_score);


        int somma_c1 = (max_g11*w_g11)+(max_g12*w_g12)+(max_g13*w_g13)+(max_g14*w_g14)+(max_g15*w_g15)+(max_g16*w_g16)+(max_g17*w_g17)+(max_g18*w_g18)+(max_g19*w_g19)+(max_m21*w_m21)+(max_m22*w_m22)+(max_m23*w_m23)+(max_m24*w_m24)+(max_b31*w_b31)+(max_b32*w_b32)+(max_b33*w_b33)+(max_b34*w_b34);
        double somma_c2 = (w_g11*g11_score)+(w_g12*g12_score)+(w_g13*g13_score)+(w_g14*g14_score)+(w_g15*g15_score)+(w_g16*g16_score)+(w_g17*g17_score)+(w_g18*g18_score)+(w_g19*g19_score)+(w_m21*m21_score)+(w_m22*m22_score)+(w_m23*m23_score)+(w_m24*m24_score)+ (w_b31*b31_score)+(w_b32*b32_score)+(w_b32*b32_score)+(w_b33*b33_score)+(w_b34*b34_score);
        double Consequenses = somma_c2/somma_c1;
        Log.d("Photonotation", "Somma C1 "+ somma_c1);
        Log.d("Photonotation", "Somma C2 "+ somma_c2);

        Log.d("Photonotation", "Consequences "+ Consequenses);

        int somma_score = g11_score+g12_score+g13_score+g14_score+g15_score+g16_score+g17_score+g18_score+g19_score+m21_score+m22_score+m23_score+m24_score+b31_score+b32_score+b33_score+b34_score;
        double somma_max_score = max_g11+max_g12+max_g13+max_g14+max_g15+max_g16+max_g17+max_g18+max_g19+max_m21+max_m22+max_m23+max_m24+max_b31+max_b32+max_b33+max_b34;
        double Pof = somma_score/somma_max_score;
        Log.d("Photonotation", "Somma Score "+ somma_score);
        Log.d("Photonotation", "Somma Max Score "+ somma_max_score);



        Log.d("Photonotation", "Pof "+ Pof);



        Double y_low  = (-2.27*Pof)+1.23;
        Double y_high = (-2.38*Pof)+1.595;

        if (Consequenses < y_low)
        {
            risk = "Low Risk";
            rischio.setBackgroundColor(Color.parseColor("#00FF00"));


        }
        if ((Consequenses>= y_low) && (Consequenses< y_high))
        {
            risk = "Medium Risk";
            rischio.setBackgroundColor(Color.parseColor("#FFFF00"));

        }
        if (Consequenses >= y_high)
        {
            risk = "High Risk";
            rischio.setBackgroundColor(Color.parseColor("#FF0000"));

        }
        Log.d("Photonotation", risk);
        rischio.setText(risk);
    }

    protected void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        if (requestCode== CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            ff = (Bitmap) data.getExtras().get("data");
            foto.setImageBitmap(ff);
            foto.setImageURI(data.getData());
            bm=((BitmapDrawable)foto.getDrawable()).getBitmap();
            scattato = 1 ;
            Log.d("Photonotation","Scattato - "+Integer.toString(scattato));
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
            return;
        }
        locationManager.removeUpdates((LocationListener) this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

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
        az = Math.toDegrees(mOrientationAngles[0]);
        pi = Math.toDegrees(mOrientationAngles[1]);
        ro = Math.toDegrees(mOrientationAngles[2]);
        //Log.d("orienta", "Az:"+String.valueOf(az)+" Pi:"+String.valueOf(pi)+" Ro:"+String.valueOf(ro));

    }


    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lng = location.getLongitude();

        Log.i("Location info: Lat", Double.toString(lat));
        Log.i("Location info: Lng", Double.toString(lng));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void getLocation(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        onLocationChanged(location);


    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {


                new AlertDialog.Builder(this)
                        .setTitle("Permesso GPS")
                        .setMessage("Permesso GPS")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d("permission", "granted");
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    onDestroy();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied,

                }
                return;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void clear_all()
    {
        note.setText("");
        rischio.setText("");
        locationtxt.setText("");
        g11.setSelection(0);
        g12.setSelection(0);
        g13.setSelection(0);
        g14.setSelection(0);
        g15.setSelection(0);
        g16.setSelection(0);
        g17.setSelection(0);
        g18.setSelection(0);
        g19.setSelection(0);
        m21.setSelection(0);
        m22.setSelection(0);
        m23.setSelection(0);
        m24.setSelection(0);
        b31.setSelection(0);
        b32.setSelection(0);
        b33.setSelection(0);
        b34.setSelection(0);

        scrolla.fullScroll(ScrollView.FOCUS_UP);

        foto.setImageResource(R.drawable.web_hi_res_512);

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem item = menu.add("File Manager");
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent filemanager = new Intent(MainActivity.this,ListFileActivity.class);
                startActivity(filemanager);
                return false;
            }
        });

        MenuItem item2 = menu.add("Clinometer");
        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent slope = new Intent(MainActivity.this,SlopeActivity.class);
                startActivity(slope);
                return false;
            }
        });

        return true;
    }
}

