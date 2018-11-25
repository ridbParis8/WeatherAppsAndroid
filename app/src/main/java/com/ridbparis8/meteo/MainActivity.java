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
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.ridbparis8.meteo.Utilites;

public class MainActivity extends AppCompatActivity {

    TextView titre_tv_item;
    TextView temp_max_tv_item;
    TextView temp_min_tv_item;
    TextView ville_tv_item;
    ImageView image_iv_item;

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

        titre_tv_item = (TextView)findViewById(R.id.titre_tv);
        temp_max_tv_item = (TextView)findViewById(R.id.temp_max_tv);
        temp_min_tv_item = (TextView)findViewById(R.id.temp_min_tv);
        ville_tv_item = (TextView)findViewById(R.id.ville_tv);
        image_iv_item = (ImageView)findViewById(R.id.icon_iv);


        new Request5Jours().execute();
        //new TestRequest().execute();
    }

    private class Request5Jours extends AsyncTask<Void, Void, ClimatElement[]>{

        @Override
        protected ClimatElement[] doInBackground(Void... voids) {

            String urlString = "https://api.openweathermap.org/data/2.5/forecast?q=Paris,us&appid=435442d234c7d367d5a62fbc7eb0b96d";

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(urlString)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String bodyReponse = response.body().string();
                //climatElement = parseJSON(bodyReponse); // Contient maintenant les données et devient donc un objet de données météos
                Log.i("Reponse", response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new ClimatElement[0];
        }
    }

    private class TestRequest extends AsyncTask<Void, Void, ClimatElement>{

        @Override
        protected ClimatElement doInBackground(Void... voids) {
            Log.i("TestRequest", "Je ne suis pas synchro !");

            String urlTest = "http://api.openweathermap.org/data/2.5/weather?q=Paris&APPID=435442d234c7d367d5a62fbc7eb0b96d";
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(urlTest)
                    .build();

            ClimatElement climatElement = null;

            try {
                Response response = client.newCall(request).execute();
                String bodyReponse = response.body().string();
                climatElement = parseJSON(bodyReponse); // Contient maintenant les données et devient donc un objet de données météos
                Log.i("Reponse", response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return climatElement;
        }

        @Override
        protected void onPostExecute(ClimatElement climatElement) {
            super.onPostExecute(climatElement);

            String titreItem = climatElement.getNomDuJour() + " " +
                    climatElement.getJour() + " " +
                    climatElement.getNomDuMois() + " " +
                    climatElement.getAnnee();

            titre_tv_item.setText(titreItem);
            temp_max_tv_item.setText("Temp max: " + climatElement.getMaxTemp());
            temp_min_tv_item.setText("Temp min: " + climatElement.getMinTemp());
            ville_tv_item.setText(climatElement.getLocation());
        }
    }

    private ClimatElement parseJSON(String bodyReponse) throws JSONException {
        // Permet de récuperer le lot d'informations
        JSONObject mainJSON = new JSONObject(bodyReponse);

        // Permet de récuperer les informations concernant le nom de la ville
        String ville = mainJSON.get("name").toString();

        // Permet de récuperer les informations de l'objet sys
        JSONObject sys = mainJSON.getJSONObject("sys");
        String pays = sys.get("country").toString();

        // Localisation
        String location = ville + ", " + pays;

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

        // Formattage du mois & Formattage du jour
        String nomDuMois = Utilites.getMois(mydate.get(Calendar.MONTH));
        String nomDuJour = Utilites.getJour(mydate.get(Calendar.DAY_OF_WEEK));

        // Classe des élements nécessaire pour une localisation du climent
        ClimatElement climatElement = new ClimatElement(
                ville,
                pays,
                location,
                minTemp,
                maxTemp,
                timestamp,
                nomDuMois,
                nomDuJour,
                mydate.get(Calendar.DAY_OF_MONTH),
                mydate.get(Calendar.MONTH),
                mydate.get(Calendar.YEAR)
                );

        return climatElement;

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
