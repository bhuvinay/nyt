package com.vgnary.nyt.thenewshour.models.geographicDetail;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class GeographicDetailEntity implements Serializable {
    @SerializedName("concept_name")
    public String concept_name;
    @SerializedName("name")
    public String name;
    @SerializedName("population")
    public String population;
    @SerializedName("feature_code_name")
    public String feature_code_name;
    @SerializedName("time_zone_id")
    public String time_zone_id;
    @SerializedName("latitude")
    public String latitude;
    @SerializedName("longitude")
    public String longitude;
}
