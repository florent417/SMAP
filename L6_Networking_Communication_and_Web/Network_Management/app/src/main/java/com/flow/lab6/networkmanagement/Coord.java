package com.flow.lab6.networkmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coord{
    public Coord(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    @SerializedName("lon")
    @Expose
    double lon;
    @SerializedName("lat")
    @Expose
    double lat;
}
