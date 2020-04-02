package com.flow.lab6.networkmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherData {
    public WeatherData(MainData mainData, Coord coord, List<Weather> weather, Sys sys, Wind wind, Clouds clouds, String base, int visibility, long dt, int timeZone, int id, String name, int cod) {
        this.mainData = mainData;
        this.coord = coord;
        this.weather = weather;
        this.sys = sys;
        this.wind = wind;
        this.clouds = clouds;
        this.base = base;
        this.visibility = visibility;
        this.dt = dt;
        this.timeZone = timeZone;
        this.id = id;
        this.name = name;
        this.cod = cod;
    }

    @SerializedName("main")
    @Expose
    public MainData mainData;
    @SerializedName("coord")
    @Expose
    Coord coord;
    @SerializedName("weather")
    @Expose
    List<Weather> weather;
    @SerializedName("sys")
    @Expose
    Sys sys;
    @SerializedName("wind")
    @Expose
    Wind wind;
    @SerializedName("clouds")
    @Expose
    Clouds clouds;
    @SerializedName("base")
    @Expose
    public String base;
    @SerializedName("visibility")
    @Expose
    public int visibility;
    @SerializedName("dit")
    @Expose
    public long dt;
    @SerializedName("timezone")
    @Expose
    public int timeZone;
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("cod")
    @Expose
    int cod;

}
