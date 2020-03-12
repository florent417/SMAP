package com.flow.lab6.networkmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Weather{
    public Weather(int id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("main")
    @Expose
    String main;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("icon")
    @Expose
    String icon;
}
