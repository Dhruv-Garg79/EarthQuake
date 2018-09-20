package com.example.android.earthquake;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>>{

    private String url_to_parse = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&minmag=2&limit=20";
    private static final int EARTHQUAKE_LOADER_ID = 1;
    TextView not_available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        not_available = findViewById(R.id.not_available);

//      Update and build API url to be used
//        buildUrl();

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if( networkInfo != null && networkInfo.isConnected()){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getSupportLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else {
            findViewById(R.id.progress).setVisibility(View.GONE);
            not_available.setText(R.string.network);
        }
    }

    private void creatingLayout(ArrayList<Earthquake> list){

        //Select recycle view
        RecyclerView recyclerView = findViewById(R.id.recycle);

        //declare layout manager for recycle view as linear
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        //set orientation of linear layout manger as vertical
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        //set manager to recycler view
        recyclerView.setLayoutManager(manager);

        EarthquakeAdapter adapter = new EarthquakeAdapter(MainActivity.this,list);

        //set a custom adpter for list representation of recycler view
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new EarthquakeLoader(MainActivity.this, url_to_parse);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> earthquakes) {

        findViewById(R.id.progress).setVisibility(View.GONE);

        if (earthquakes != null && !earthquakes.isEmpty()) {
            not_available.setVisibility(View.INVISIBLE);
            creatingLayout(earthquakes);
        }
        else{
            not_available.setText(R.string.not_detected);
            not_available.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Earthquake>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
