package com.example.android.earthquake;

class Earthquake {
    private double magnitude;
    private String location;
    private String mydate;
    private String offSet;
    private String url;

    Earthquake(double magnitude, String offSet, String location, String date, String url) {
        this.magnitude = magnitude;
        this.location = location;
        this.mydate = date;
        this.offSet = offSet;
        this.url = url;
    }

    public double getMagnitude(){
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return mydate;
    }

    public String getOffSet() {
        return offSet;
    }

    public String getUrl() {
        return url;
    }
}
