package com.flow.lab6.networkmanagement;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Sys {
    public Sys(int type, int id, String country, long sunrise, long sunset) {
        this.type = type;
        this.id = id;
        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    @SerializedName("type")
    @Expose
    int type;
    @SerializedName("id")
    @Expose
    int id ;
    @SerializedName("country")
    @Expose
    String country;
    @SerializedName("sunrise")
    @Expose
    long sunrise;
    @SerializedName("sunset")
    @Expose
    long sunset;
}
