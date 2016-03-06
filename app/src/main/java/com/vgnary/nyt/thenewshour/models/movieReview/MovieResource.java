package com.vgnary.nyt.thenewshour.models.movieReview;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class MovieResource implements Serializable {

    @SerializedName("type")
    public String type;

    @SerializedName("src")
    public String src;

    @SerializedName("height")
    public String height;

    @SerializedName("width")
    public String width;
}
