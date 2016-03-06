package com.vgnary.nyt.thenewshour.models.bestSellerList;

import com.vgnary.nyt.thenewshour.models.BasicResponse;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class BestSellerListResponse extends BasicResponse implements Serializable {
    @SerializedName("results")
    public List<BestSellerListEntity> bestSellerListEntityList;
}
