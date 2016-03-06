package com.vgnary.nyt.thenewshour.models.mostPopular;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class MetaStringData  implements Serializable {
    @SerializedName("web_url")
    public String web_url;

    @SerializedName("snippet")
    public String snippet;

    @SerializedName("lead_paragraph")
    public String lead_paragraph;

}
