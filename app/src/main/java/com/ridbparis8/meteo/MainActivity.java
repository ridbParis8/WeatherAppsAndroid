package com.ridbparis8.meteo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Boutton (floating action bouton)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/




        new TestRequest().execute();
    }

    private class TestRequest extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("TestRequest", "Je ne suis pas synchro !");

            String urlTest = "http://api.openweathermap.org/data/2.5/weather?q=Paris&APPID=435442d234c7d367d5a62fbc7eb0b96d";
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(urlTest)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String bodyReponse = response.body().string();
                parseJSON(bodyReponse);
                Log.i("Reponse", response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void parseJSON(String bodyReponse) throws JSONException {
        // Permet de récuperer le lot d'informations
        JSONObject mainJSON = new JSONObject(bodyReponse);

        // Permet de récuperer les informations concernant le nom de la ville
        String ville = mainJSON.get("name").toString();

        // Permet de récuperer les informations de l'objet sys
        JSONObject sys = mainJSON.getJSONObject("sys");
        String pays = sys.get("country").toString();

        // localisation
        String location = ville + ", " + pays;
        int salut = 1;

        // Permet de récuperer les températures
        JSONObject main = mainJSON.getJSONObject("main");
        String minTemp = main.get("temp_min").toString();
        String maxTemp = main.get("temp_max").toString();

        // Permet de récuperer la date -> Convertir Date (dt) car format UNIX
        String dt = mainJSON.get("dt").toString();

        // Conversion en affichage de date normal --> Code StackOverflow
        Calendar mydate = Calendar.getInstance();
        int timestamp = Integer.valueOf(dt);
        mydate.setTimeInMillis((long)timestamp*1000);
        String resultat = mydate.get(Calendar.DAY_OF_MONTH)+"."+mydate.get(Calendar.MONTH)+"."+mydate.get(Calendar.YEAR);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
