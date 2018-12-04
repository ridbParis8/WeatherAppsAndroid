package com.ridbparis8.meteo;

import android.content.ClipboardManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ClimatAdapteur climatAdapteur;

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

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);


        new Request5Jours().execute();
        //new TestRequest().execute();
    }

    private class Request5Jours extends AsyncTask<Void, Void, Climat>{

        @Override
        protected Climat doInBackground(Void... voids) {

            //String urlString = "https://api.openweathermap.org/data/2.5/forecast?q=Paris,us&appid=435442d234c7d367d5a62fbc7eb0b96d";

            Climat climat = null;

            final String QUERY_PARAM = "q";
            final String APPID_PARAM = "appid";
            final String UNITS_PARAM = "units";

            // Permet de recréer l'url en la sécurisant
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("https")
                    .authority("api.openweathermap.org")
                    .appendPath("data")
                    .appendPath("2.5")
                    .appendPath("forecast")
                    .appendQueryParameter(QUERY_PARAM,"Paris")
                    .appendQueryParameter(UNITS_PARAM,"metric") // Passage en Celcius
                    .appendQueryParameter(APPID_PARAM, "435442d234c7d367d5a62fbc7eb0b96d") // Clé API
                    .build();

            URL url = null;

            // Test de réussite de la recréation de l'URL
            try{
                url = new URL(uriBuilder.toString());
                int h1 = 1;
            }catch (MalformedURLException e){
                e.printStackTrace();
            }

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String bodyReponse = response.body().string();
                Location location = parseLocation(bodyReponse);
                climat = parseMain(bodyReponse);
                climat.setLocation(location);

                //climatElement = parseJSON(bodyReponse); // Contient maintenant les données et devient donc un objet de données météos
                Log.i("Reponse", response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();;
            }

            return climat; // SI retourne null c'est qu'on a pas pu faire notre request
        }

        @Override
        protected void onPostExecute(Climat climat) { // Fil principl du user
            super.onPostExecute(climat);
            Toast.makeText(MainActivity.this, "Bienvenu dans Met8os", Toast.LENGTH_SHORT).show();

            String nomDeJours = climat.tempsArray.get(0).nomDeJours;
            int temperature = (int)climat.climatInfoArray.get(0).temperature;

            climatAdapteur = new ClimatAdapteur(climat);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(climatAdapteur);
        }
    }

    private Climat parseMain(String bodyReponse) throws JSONException {
        JSONObject mainJSON = new JSONObject(bodyReponse);
        JSONArray list = mainJSON.getJSONArray("list");

        ArrayList<Temps> tempsArray = new ArrayList<Temps>();
        ArrayList<ClimatInfo> climatInfoArray = new ArrayList<ClimatInfo>();

        // Récupération des informations de la liste entière (40 éléments)
        // Ces opérations permettent donc de récupérer uniquement quelques unes d'entre elles
        int i = 0;
        for(i=0; i < list.length(); i++){

            JSONObject elementi = list.getJSONObject(i);
            Temps tempsi = parseTemps(elementi);

            // Si element 0 > 15 je prends
            if(i == 0){
                if(Integer.valueOf(tempsi.dt_text.substring(11, 13)) > 15){
                    tempsArray.add(tempsi);
                    climatInfoArray.add(parseClimatInfo(elementi));
                }
            }


            // Si temps == 15h on prend
            if(tempsi.dt_text.substring(11, 13).equals("15")){
                tempsArray.add(tempsi);
                climatInfoArray.add(parseClimatInfo(elementi));
            }

        }

        Climat climat = new Climat(tempsArray, climatInfoArray);
        return climat;

    }

    private Temps parseTemps(JSONObject element0) throws JSONException{
        //Temps
        int dt = element0.getInt("dt");
        String dt_text = element0.getString("dt_txt");
        Temps temps0 = new Temps(dt, dt_text);

        return temps0;
    }

    private ClimatInfo parseClimatInfo(JSONObject element0) throws JSONException{
        ////JSON -> main
        JSONObject main = element0.getJSONObject("main");
        float temperature = (float) main.getDouble("temp");
        float pression = (float) main.getDouble("pressure");
        float humidite = (float) main.getDouble("humidity");

        ////JSON -> weather
        JSONArray weather = element0.getJSONArray("weather");
        JSONObject weather0 = weather.getJSONObject(0);
        int weatherId = weather0.getInt("id");
        String weatherMain = weather0.getString("main");
        String weatherDescription = weather0.getString("description");
        String weatherIcon = weather0.getString("icon");

        //// JSON -> wind
        JSONObject vent = element0.getJSONObject("wind");
        float vent_vitesse = (float) vent.getDouble("speed");

        ClimatInfo climatInfo = new ClimatInfo(
                temperature,
                pression,
                humidite,
                vent_vitesse,
                weatherMain,
                weatherDescription,
                weatherIcon,
                weatherId);

        return climatInfo;
    }

    private Location parseLocation(String bodyReponse) throws JSONException {
        JSONObject mainJSON = new JSONObject(bodyReponse);
        JSONObject city = mainJSON.getJSONObject("city");
        int id = city.getInt("id");
        String ville = city.getString("name");
        String pays = city.getString("country");
        JSONObject coord = city.getJSONObject("coord");
        float lat = (float)coord.getDouble("lat");
        float lon = (float)coord.getDouble("lon");

        Location location = new Location(id, lat, lon, ville,pays);
        return location;
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

            //titre_tv_item.setText(titreItem);
            //temp_max_tv_item.setText("Temp max: " + climatElement.getMaxTemp());
            //temp_min_tv_item.setText("Temp min: " + climatElement.getMinTemp());
            //ville_tv_item.setText(climatElement.getLocation());
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
        int timestamp = Integer.valueOf(dt);
        Calendar mydate = Calendar.getInstance();
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
