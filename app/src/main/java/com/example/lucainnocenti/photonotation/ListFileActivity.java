package com.example.lucainnocenti.photonotation;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListFileActivity extends ListActivity
{
    private String path;
    private File file_jpg;
    private File file_json;
    private ListView lista;
    private String loc;
    private String lat;
    private String lng;
    private String rsk;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_file);

       refresh_list();
    }

    private void refresh_list()
    {
        // Use the current directory as title
        path = "/storage/emulated/0/Photonotation/";
        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path");
        }
        setTitle(path);

        // Read all files sorted into the values-array
        List values = new ArrayList();
        File dir = new File(path);
        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }
        String[] list = dir.list();
        int conta = 0;
        if (list != null) {
            for (String file : list) {
                if (file.endsWith(".json"))
                {
                    conta = conta + 1;
                    values.add(file);
                }
            }
        }
        Collections.sort(values);

        // Put the data into the list
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, values);
        setListAdapter(adapter);

        lista = getListView();

        //cancella i file
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("Photonotation","Cancella");
                String filename = (String) getListAdapter().getItem(position);

                String senzaestensione = filename.substring(0,filename.lastIndexOf('.'));
                Log.d("filemanager", senzaestensione);

                String fj =  senzaestensione + ".json";
                String fp =  senzaestensione + ".jpg";

                if (path.endsWith(File.separator)) {
                    fj = path + fj;
                    fp = path + fp;
                } else {
                    fj = path + File.separator + fj;
                    fp = path + File.separator + fp;
                }

                file_json = new File(fj);
                file_jpg = new File (fp);

                Log.d("filemanager", fj);
                Log.d("filemanager", fp);



                AlertDialog.Builder builder = new AlertDialog.Builder(ListFileActivity.this);
                builder.setMessage("Delete this file?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                file_json.delete();
                                file_jpg.delete();
                                refresh_list();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                builder.show();
                return true;
            }
        });

        if (conta == 0) {
            Toast.makeText(this, "No files", Toast.LENGTH_LONG).show();
        }

    }




    // PARSE JSON
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {


        Log.d("Photonotation","Mostra");
        String filename = (String) getListAdapter().getItem(position);
        String fj =  filename;

        if (path.endsWith(File.separator)) {
            fj = path + fj;
        } else {
            fj = path + File.separator + fj;
        }

        Log.d("Photonotation",fj);

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(fj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("Photonotation","Errore apertura file");
        }
        String jsonStr = null;
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

            jsonStr = Charset.defaultCharset().decode(bb).toString();
            Log.d("Photonotation",jsonStr);

        }
        catch(Exception e){
            e.printStackTrace();
            Log.d("Photonotation","Errore creazione jsonStr");
        }
        finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Photonotation","Errore close");
            }
        }


        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Photonotation","Errore creazione jsonObj");

        }

        try {
            loc = jsonObj.getString("location");
            lat = jsonObj.getString("latitude");
            lng = jsonObj.getString("longitude");
            rsk = jsonObj.getString("Risk");
            Log.d("Photonotaion", jsonObj.getString("location"));
            Log.d("Photonotaion", jsonObj.getString("azimuth"));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Photonotation","Errore estrazione dati");

        }


        AlertDialog.Builder builder = new AlertDialog.Builder(ListFileActivity.this);
        builder.setMessage(Html.fromHtml("<b>Location:</b><br>"+loc+"<br><br><b>Latitude:</b><br>"+lat+"<br><br><b>Longitude:</b><br>"+lng+"<br><br><b>Risk:</b><br>"+rsk,Html.FROM_HTML_MODE_LEGACY))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        refresh_list();
                    }
                });

        builder.show();

    }


}
