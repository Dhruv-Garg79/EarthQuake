package com.example.android.earthquake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class EarthquakeLoader extends AsyncTaskLoader {

    private String mUrl;

    EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public Object loadInBackground() {
        if (mUrl == null) return null;
        URL url = null;

        try {
            url = new URL(mUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String JSONresponse = null;
        try{
            JSONresponse = QueryUtils.makeHttpRequest(url);
        }catch (IOException e){
            e.getStackTrace();
        }

        return QueryUtils.extractEarthquakes(JSONresponse);
    }
}
