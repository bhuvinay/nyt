package com.vgnary.nyt.thenewshour.models.movieReview;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class MovieMultimedia implements Serializable {
    @SerializedName("resource")
    public MovieResource movieResource;
}
