package com.example.pt16;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final static String API_KEY = "39a7209b1611d9c3c5cc2dc170c63323";

    // TODO Ciutat no trobada -> Toast

    // TODO Missatges de sense connexió i no a la BDs.

    // TODO Posar les temperatures i tal a un fragment dinàmic. Li haurem de passar la ciutat.
    //  El fragment mirarà la base de dades i blabla.

    private RecyclerView blocRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter tAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
