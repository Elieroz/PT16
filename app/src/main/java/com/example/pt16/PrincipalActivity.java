package com.example.pt16;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener {

    final static String API_KEY = "39a7209b1611d9c3c5cc2dc170c63323";

    // TODO Ciutat no trobada -> Toast

    // TODO Missatges de sense connexió i no a la BDs.

    // TODO Posar les temperatures i tal a un fragment dinàmic. Li haurem de passar la ciutat.
    //  El fragment mirarà la base de dades i blabla.

    private RecyclerView blocRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter tAdapter;
    private RecyclerView.LayoutManager layoutManager;

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


        this.blocRecyclerView = findViewById(R.id.bloc_recycler_view);
        // La llista no canvia en runtime; mateixa mida.
        this.blocRecyclerView.setHasFixedSize(true);

        this.layoutManager = new LinearLayoutManager(this);
        this.blocRecyclerView.setLayoutManager(this.layoutManager);

        ArrayList<String> input = new ArrayList<>();
        for (int i = 0; i < 100; i += 1) {
            input.add("Test_" + i);
        }

//        this.mAdapter = new MyAdapter(input);
        this.blocRecyclerView.setAdapter(this.mAdapter);

        this.edtCity = findViewById(R.id.edtCity);

        this.btnLoadInfo = findViewById(R.id.btnLoadInfo);
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

        if (id == R.id.btnLoadInfo) {
            String city = this.edtCity.getText().toString();
            new PrincipalActivity.Descarregador().execute(city);
        }
    }

    class Descarregador extends AsyncTask<String, Boolean, Descarregador.Result> {
        class Result {
            boolean isError;
            String message;

            public Result(boolean isError, String message) {
                this.isError = isError;
                this.message = message;
            }
        }

        // TODO Si tot va bé es retorna un ArrayList<String> amb el contingut; si no, null.
        @Override
        protected Result doInBackground(String... strings) {
            String city = strings[0];
            try {
                // TODO Noms més descriptius?
                // TODO En comptes de London posar la ciutat, i en comptes d'uk posar...?
                //  Ah, doncs només amb London funciona igual.
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=" + PrincipalActivity.API_KEY);
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
            Toast.makeText(PrincipalActivity.this, r.message, Toast.LENGTH_SHORT).show();
        }
    }
}
