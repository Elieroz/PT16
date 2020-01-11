package com.example.pt16;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener {

    final static String API_KEY = "39a7209b1611d9c3c5cc2dc170c63323";

    // TODO Ciutat no trobada -> Toast

    // TODO Missatges de sense connexió i no a la BDs.

    // TODO Posar les temperatures i tal a un fragment dinàmic. Li haurem de passar la ciutat.
    //  El fragment mirarà la base de dades i blabla.

    private RecyclerView blocRecyclerView;
    private RecyclerView.Adapter<BlocAdapter.BlocViewHolder> blocViewHolderAdapter;
//    private RecyclerView.Adapter tAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Bloc> blocs;

    private EditText edtCity;
    private Button btnLoadInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        this.initializeBlocRecyclerView();
    }

    private void initializeBlocRecyclerView() {
        this.blocRecyclerView = findViewById(R.id.bloc_recycler_view);
        // La llista no canvia en runtime; mateixa mida. TODO I quan la buido i torno a omplir?
        this.blocRecyclerView.setHasFixedSize(true);

        this.layoutManager = new LinearLayoutManager(this);

        // Create an empty Adapter - there's no info yet.
        this.blocs = new ArrayList<>();
        this.blocViewHolderAdapter = new BlocAdapter(this.blocs);

        this.blocRecyclerView.setLayoutManager(this.layoutManager);
        this.blocRecyclerView.setAdapter(this.blocViewHolderAdapter);

        this.edtCity = findViewById(R.id.edtCity);

        this.btnLoadInfo = findViewById(R.id.btnSearch);
        this.btnLoadInfo.setOnClickListener(this);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnSearch) {
            String city = this.edtCity.getText().toString();

            // TODO Context? getApplicationContext o getBaseContext?
            TemperaturesHelper temperaturesHelper = new TemperaturesHelper(getApplicationContext());
            if (temperaturesHelper.estaCiutatDescarregada(city)) {
                Toast.makeText(this, "Llegint de la base de dades... TODO", Toast.LENGTH_SHORT).show();
                // TODO
            } else {
                Toast.makeText(this, "Realitzant petició de descàrrega...", Toast.LENGTH_SHORT).show();
                new PrincipalActivity.Descarregador().execute(city);
            }
        }
    }

    // TODO Moure el mètode a TemperaturesHelper o algo?
    private void onXmlDownloaded(String nomCiutat, String xml) {
        Parsejador parsejador = new Parsejador();

        try {
            ArrayList<Bloc> blocs = parsejador.parseja(xml);
            this.fillBlocRecyclerView(blocs);

            TemperaturesHelper temperaturesHelper = new TemperaturesHelper(getApplicationContext());
            temperaturesHelper.guarda(nomCiutat, blocs);
            Toast.makeText(this, "Operació realitzada ^_^", Toast.LENGTH_SHORT).show();
        } catch (XmlPullParserException | IOException e) {
            Toast.makeText(this, "Error parsejant XML", Toast.LENGTH_SHORT).show();
        }
    }

    private void onDataLoadedFromDatabase(ArrayList<Bloc> blocs) {

    }

    private void fillBlocRecyclerView(ArrayList<Bloc> blocs) {
        this.blocs.clear();
        this.blocs.addAll(blocs);
        this.blocViewHolderAdapter.notifyDataSetChanged();
    }

    // TODO Boolean? Buenu, es pot fer un Progress Spinner mentre busca o algo.
    // TODO Constructor amb Context context?
    class Descarregador extends AsyncTask<String, Boolean, Descarregador.Result> {
        private String nomCiutat;

        class Result {
            boolean isError;
            String message;

            Result(boolean isError, String message) {
                this.isError = isError;
                this.message = message;
            }
        }

        // TODO Si tot va bé es retorna un ArrayList<String> amb el contingut; si no, null.
        @Override
        protected Result doInBackground(String... nomCiutats) {
            this.nomCiutat = nomCiutats[0];
            try {
                // TODO Noms més descriptius?
                // TODO En comptes de London posar la ciutat, i en comptes d'uk posar...?
                //  Ah, doncs només amb London funciona igual.
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=" + this.nomCiutat + "&mode=xml&APPID=" + PrincipalActivity.API_KEY);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                // https://www.codejava.net/java-se/networking/how-to-use-java-urlconnection-and-httpurlconnection
                // Note that when the header fields are read, the connection is implicitly established,
                // without calling connect().
//        httpURLConnection.connect();

                // TODO Mirem l'HTTP response code per a veure si ha anat bé o malament i per a fer el connect de pas?
                int responseCode = httpURLConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    // TODO Ara cada línia que es llegeixi tindrà la info per a crear Blocs?

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
                Toast.makeText(PrincipalActivity.this, r.message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PrincipalActivity.this, "Descàrrega OK, presentant info...", Toast.LENGTH_SHORT).show();
                Toast.makeText(PrincipalActivity.this, r.message, Toast.LENGTH_SHORT).show();
                onXmlDownloaded(this.nomCiutat, r.message);
            }
        }
    }
}