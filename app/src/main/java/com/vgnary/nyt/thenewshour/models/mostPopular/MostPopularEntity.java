package com.vgnary.nyt.thenewshour.models.mostPopular;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MostPopularEntity implements Serializable {
    @SerializedName("meta")
    public MetaIntData metaIntData;

    @SerializedName("docs")
    public List<MetaStringData> basicEntities;
}
