package com.vgnary.nyt.thenewshour.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class BasicResponse implements Serializable {
    @SerializedName("status")
    public String status;

    @SerializedName("copyright")
    public String Copyright;
}
