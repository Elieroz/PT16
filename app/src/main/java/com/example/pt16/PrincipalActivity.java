package com.example.pt16;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener {

    final static String API_KEY = "39a7209b1611d9c3c5cc2dc170c63323";

    final static String DEFAULT_DOWNLOAD_FORMAT = "JSON";

    // TODO Ciutat no trobada -> Toast

    // TODO Missatges de sense connexió i no a la BDs.

    // TODO Posar les temperatures i tal a un fragment dinàmic. Li haurem de passar la ciutat.
    //  El fragment mirarà la base de dades i blabla.

    private EditText edtCity;
    private Button btnLoadInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        this.edtCity = findViewById(R.id.edtCity);

        this.btnLoadInfo = findViewById(R.id.btnSearch);
        this.btnLoadInfo.setOnClickListener(this);

//        this.initializeBlocRecyclerView();
    }

    @Override
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
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.reset_database) {
            // TODO
            TemperaturesHelper temperaturesHelper = new TemperaturesHelper(this);
            temperaturesHelper.onUpgrade(temperaturesHelper.getWritableDatabase(), 1, 1);
            Toast.makeText(this, "Database reset OK", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnSearch) {
            String cityName = this.edtCity.getText().toString();
            this.retrieveCityInfo(cityName);
        }
    }

    private void retrieveCityInfo(String cityName) {
        TemperaturesHelper temperaturesHelper = new TemperaturesHelper(getApplicationContext());

        if (temperaturesHelper.isCityInfoDownloaded(cityName)) {
            if (temperaturesHelper.isCityInfoUpToDate(cityName)) {
                Toast.makeText(this, "Llegint de la base de dades...", Toast.LENGTH_SHORT).show();

                // TODO Al seu propi mètode?
                ArrayList<Bloc> blocs = temperaturesHelper.llegeix(cityName);
                this.createBlocListFragment(blocs);

            } else {
                Toast.makeText(this, "Dades desactualitzades, descarregant...", Toast.LENGTH_SHORT).show();
                temperaturesHelper.deleteNotUpToDate(cityName);
                this.downloadData(cityName);
            }
        } else {
            Toast.makeText(this, "Dades no disponibles a la base de dades, descarregant...", Toast.LENGTH_SHORT).show();
            this.downloadData(cityName);
        }

//        if (temperaturesHelper.cityAvailableAndUpToDate(cityName)) {
//            Toast.makeText(this, "Llegint de la base de dades... TODO", Toast.LENGTH_SHORT).show();
//
//            // TODO Al seu propi mètode?
//            ArrayList<Bloc> blocs = temperaturesHelper.llegeix(cityName);
//            this.createBlocListFragment(blocs);
//        } else {
//            Toast.makeText(this, "Realitzant petició de descàrrega...", Toast.LENGTH_SHORT).show();
//            new PrincipalActivity.Descarregador(this).execute(cityName);
//        }
    }

    private void downloadData(String cityName) {
        Toast.makeText(this, "Realitzant petició de descàrrega...", Toast.LENGTH_SHORT).show();
        new PrincipalActivity.Descarregador(this).execute(cityName);
    }

    private void onDataDownloaded(String nomCiutat, String data) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String downloadFormat = sharedPreferences.getString(
                "download_format",
                PrincipalActivity.DEFAULT_DOWNLOAD_FORMAT
        );

        ParsejadorBloc parsejadorBloc;
        if (downloadFormat.equals("XML")) {
            parsejadorBloc = new ParsejadorBlocXML();
        } else {
            parsejadorBloc = new ParsejadorBlocJSON();
        }

        try {
            ArrayList<Bloc> blocs = parsejadorBloc.parseja(nomCiutat, data);
            this.createBlocListFragment(blocs);

            // TODO
            TemperaturesHelper temperaturesHelper = new TemperaturesHelper(getApplicationContext());
            temperaturesHelper.guarda(blocs);
            Toast.makeText(this, "Operació realitzada ^_^", Toast.LENGTH_SHORT).show();
        } catch (XmlPullParserException | IOException e) {
            Toast.makeText(this, "Error parsejant data (format " + downloadFormat + ")", Toast.LENGTH_LONG).show();
        }
    }

    private void onDataLoadedFromDatabase(ArrayList<Bloc> blocs) {

    }

    private void createBlocListFragment(ArrayList<Bloc> blocs) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String blocLayoutString = sharedPreferences.getString("bloc_layout_mode", "Compact");

        int blocLayout;
        if (blocLayoutString.equals("Bigger")) {
            blocLayout = R.layout.bloc_layout_alt;
        } else {
            blocLayout = R.layout.bloc_layout;
        }

        String temperatureUnitString = sharedPreferences.getString("temperature_unit", "Celsius");

        BlocAdapter.TemperatureUnit temperatureUnit = BlocAdapter.TemperatureUnit.valueOf(temperatureUnitString);

        BlocListFragment blocListFragment = BlocListFragment.newInstance(blocs, blocLayout, temperatureUnit);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.temperatures_hores_fragment, blocListFragment)
                .commit()
        ;
    }

    // TODO Boolean? Buenu, es pot fer un Progress Spinner mentre busca o algo.
    // TODO Constructor amb Context context?
    static class Descarregador extends AsyncTask<String, Boolean, Descarregador.Result> {
        // TODO Perquè la classe pugui ser estàtica i no tingui referències chungas de l'activitat.
        //  https://stackoverflow.com/questions/44309241/warning-this-asynctask-class-should-be-static-or-leaks-might-occur
        private WeakReference<PrincipalActivity> principalActivity;
        private String nomCiutat;

        class Result {
            boolean isError;
            String message;

            Result(boolean isError, String message) {
                this.isError = isError;
                this.message = message;
            }
        }

        Descarregador(PrincipalActivity principalActivity) {
            this.principalActivity = new WeakReference<>(principalActivity);
        }

        @Override
        protected Result doInBackground(String... nomCiutats) {
            this.nomCiutat = nomCiutats[0];

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.principalActivity.get());
            String downloadFormat = sharedPreferences.getString(
                    "download_format",
                    PrincipalActivity.DEFAULT_DOWNLOAD_FORMAT
            );

            try {
                // TODO En comptes de London posar la ciutat, i en comptes d'uk posar...?
                //  Ah, doncs només amb London funciona igual.
                URL url = new URL(
                        "http://api.openweathermap.org/data/2.5/forecast?q="+ this.nomCiutat
                                + "&mode=" + downloadFormat.toLowerCase()
                                + "&APPID=" + PrincipalActivity.API_KEY
                );
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                // https://www.codejava.net/java-se/networking/how-to-use-java-urlconnection-and-httpurlconnection
                // Note that when the header fields are read, the connection is implicitly established,
                // without calling connect().
                // httpURLConnection.connect();

                // TODO Mirem l'HTTP response code per a veure si ha anat bé o malament i per a fer el connect de pas?
                int responseCode = httpURLConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    StringBuilder stringBuilder = new StringBuilder();

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    return new Result(false, stringBuilder.toString());
                } else {
                    return new Result(true, "Invalid HTTP request. City noy found?");
                }
            } catch (IOException e) {
                return new Result(true, e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(Result r) {
            if (r.isError) {
                Toast.makeText(this.principalActivity.get(), r.message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.principalActivity.get(), "Descàrrega OK, presentant info...", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this.principalActivity.get(), r.message, Toast.LENGTH_SHORT).show();
//                this.principalActivity.get().onXmlDownloaded(this.nomCiutat, r.message);
                this.principalActivity.get().onDataDownloaded(this.nomCiutat, r.message);
            }
        }
    }
}
