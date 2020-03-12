package com.flow.lab6.networkmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Wind {
    public Wind(double speed, double deg) {
        this.speed = speed;
        this.deg = deg;
    }

    @SerializedName("speed")
    @Expose
    double speed;
    @SerializedName("deg")
    @Expose
    double deg;
}
