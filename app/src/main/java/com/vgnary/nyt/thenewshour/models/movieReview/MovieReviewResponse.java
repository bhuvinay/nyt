package com.vgnary.nyt.thenewshour.models.movieReview;

import com.vgnary.nyt.thenewshour.models.BasicResponse;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class MovieReviewResponse extends BasicResponse implements Serializable {

    @SerializedName("results")
    public List<MovieReviewEntity> movieReviewEntity;
}
