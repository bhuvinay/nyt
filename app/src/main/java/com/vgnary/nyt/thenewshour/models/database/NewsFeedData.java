package com.vgnary.nyt.thenewshour.models.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "news_feed_table")
public class NewsFeedData implements Serializable {

    public static final String ENTITY_RESPONSE = "entity_name";
    public static final String ENTITY_TYPE = "entity_type";


    @DatabaseField(columnName = ENTITY_RESPONSE)
    public String entityResponse;

    @DatabaseField(columnName = ENTITY_TYPE,unique = true)
    public int entityType;


    //Used by Ormlite
    public NewsFeedData() {

    }

    public NewsFeedData(String entityResponse, int type) {
        this.entityResponse = entityResponse;
        this.entityType = type;
    }

}