package com.flow.lab6.networkmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Clouds {
    public Clouds(int all) {
        this.all = all;
    }

    @SerializedName("all")
    @Expose
    int all;
}
