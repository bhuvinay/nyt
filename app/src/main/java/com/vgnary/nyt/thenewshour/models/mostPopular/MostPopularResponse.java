package com.vgnary.nyt.thenewshour.models.mostPopular;

import com.vgnary.nyt.thenewshour.models.BasicResponse;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class MostPopularResponse extends BasicResponse
        implements Serializable {
    @SerializedName("response")
    public MostPopularEntity mostPopularEntity;


}
