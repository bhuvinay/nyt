package com.vgnary.nyt.thenewshour.models.database;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;


public class SavedFeedData implements Serializable {
    public static final String SAVED__FEED_TITLE = "savedfeedTitle";
    public static final String SAVED_FEED_SUMMARY = "savedfeedSummary";
    public static final String SAVED_FEED_URL = "savedfeedUrl";
    public static final String IS_SAVED_URL = "issaved";


    @DatabaseField(columnName = SAVED__FEED_TITLE)
    public String newsTitle;

    @DatabaseField(columnName = SAVED_FEED_SUMMARY)
    public String newsSummary;
    @DatabaseField(columnName = IS_SAVED_URL)
    public boolean isFeedSaved;

    @DatabaseField(columnName = SAVED_FEED_URL, unique = true)
    public String newsUrl;

    //Used by Ormlite
    public SavedFeedData() {

    }

    public SavedFeedData(String entityResponse, String newsSummary, String newsUrl,boolean isFeedSaved) {
        this.newsTitle = entityResponse;
        this.newsSummary = newsSummary;
        this.newsUrl = newsUrl;
        this.isFeedSaved = isFeedSaved;
    }
}
