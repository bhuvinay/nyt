package com.vgnary.nyt.thenewshour.models.movieReview;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class MovieSuggestedLink implements Serializable {

    @SerializedName("type")
    public String articleType;

    @SerializedName("url")
    public String url;

    @SerializedName("suggested_link_text")
    public String suggested_link_text;
}
