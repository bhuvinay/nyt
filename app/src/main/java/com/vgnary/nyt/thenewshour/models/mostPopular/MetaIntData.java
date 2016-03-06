package com.vgnary.nyt.thenewshour.models.mostPopular;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class MetaIntData  implements Serializable {
    @SerializedName("hits")
    public int hits;

    @SerializedName("time")
    public int time;

    @SerializedName("offset")
    public int offset;
}
