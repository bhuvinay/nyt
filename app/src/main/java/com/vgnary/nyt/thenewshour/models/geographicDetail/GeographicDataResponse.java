package com.vgnary.nyt.thenewshour.models.geographicDetail;

import com.vgnary.nyt.thenewshour.models.BasicResponse;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class GeographicDataResponse extends BasicResponse implements Serializable {
    @SerializedName("results")
    public List<GeographicDetailEntity> geographicDetailList;
}
