package com.vgnary.nyt.thenewshour.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.models.database.NewsFeedData;
import com.vgnary.nyt.thenewshour.models.database.SavedFeedData;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class NewsFeedDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "news_feed_database.db";
    private static final int DB_VERSION = 1;
    private static final String TAG = "ShikshaDatabaseHelper";
    private Context mContext;

    private Dao<NewsFeedData, Integer> newsFeedDao = null;
    private Dao<SavedFeedData, Integer> savedFeedDao = null;

    public NewsFeedDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION, R.raw.nyt_db_schema);
        mContext = context;
        try {
            geNewsFeedDao();
            getSavedFeedDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            //Create Recent Search table
            TableUtils.createTableIfNotExists(connectionSource, NewsFeedData.class);
            TableUtils.clearTable(connectionSource, NewsFeedData.class);
            TableUtils.createTableIfNotExists(connectionSource, SavedFeedData.class);
            TableUtils.clearTable(connectionSource, SavedFeedData.class);

            //Create User preference Table

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, NewsFeedData.class, false);
            TableUtils.dropTable(connectionSource, SavedFeedData.class, false);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This functions maintains singleton object of database helper class.
     */
    public Dao<NewsFeedData, Integer> geNewsFeedDao() throws SQLException {
        if (newsFeedDao == null) {
            newsFeedDao = getDao(NewsFeedData.class);
        }
        return newsFeedDao;
    }

    public Dao<SavedFeedData, Integer> getSavedFeedDao() throws SQLException {
        if (savedFeedDao == null) {
            savedFeedDao = getDao(SavedFeedData.class);
        }
        return savedFeedDao;
    }


    public void addNewsFeedInCache(NewsFeedData data) {
        if (newsFeedDao != null) {
            try {
                createOrUpdate(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                newsFeedDao = getDao(NewsFeedData.class);
                createOrUpdate(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addSavedNewsFeedInCache(SavedFeedData data) {
        if (savedFeedDao != null) {
            try {
                createOrUpdateSavedFeed(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                savedFeedDao = getDao(SavedFeedData.class);
                createOrUpdateSavedFeed(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public String getNewsFeedData(int entityType) {

        String responseString = null;
        try {
            if (newsFeedDao != null) {
                QueryBuilder<NewsFeedData, Integer> newsFeedQB = newsFeedDao.queryBuilder();
                newsFeedQB.where().eq(NewsFeedData.ENTITY_TYPE, entityType);
                if (newsFeedDao.query(newsFeedQB.prepare()) != null && newsFeedDao.query(newsFeedQB.prepare()).size() == 1) {
                    responseString = newsFeedDao.query(newsFeedQB.prepare()).get(0).entityResponse;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    public List<SavedFeedData> getSavedFeedData() {
        List<SavedFeedData> savedFeedDataList = new ArrayList<>();
        try {
            if (savedFeedDao != null) {
                QueryBuilder<SavedFeedData, Integer> newsFeedQB = savedFeedDao.queryBuilder();
                savedFeedDataList = savedFeedDao.query(newsFeedQB.prepare());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return savedFeedDataList;
    }

    @Override
    public void close() {
        super.close();
        newsFeedDao = null;
    }

    /**
     * This function checks for unique entries in database table.
     * Unique row is identical on basis of entityId and userId.
     * If row with unique combination alreay exists,This function updates its timestamp
     *
     * @param data row values to be inserted/updated into recent search database
     */

    public void createOrUpdate(NewsFeedData data) throws SQLException {
        List<NewsFeedData> newsFeedDataList = null;
        QueryBuilder<NewsFeedData, Integer> newsFeedQB = newsFeedDao.queryBuilder();
        newsFeedQB.where().eq(NewsFeedData.ENTITY_TYPE, data.entityType);
        newsFeedDataList = newsFeedDao.query(newsFeedQB.prepare());
        for (NewsFeedData savedFeedRecord : newsFeedDataList) {
            if (savedFeedRecord.entityType == data.entityType) {
                newsFeedDao.update(savedFeedRecord);
                return;
            }
        }
        newsFeedDao.create(data);


    }

    public void createOrUpdateSavedFeed(SavedFeedData data) throws SQLException {
        List<SavedFeedData> SavedFeedDataList = null;
        QueryBuilder<SavedFeedData, Integer> newsFeedQB = savedFeedDao.queryBuilder();
        newsFeedQB.where().eq(SavedFeedData.SAVED_FEED_URL, data.newsUrl);
        SavedFeedDataList = savedFeedDao.query(newsFeedQB.prepare());
        for (SavedFeedData newsFeedRecord : SavedFeedDataList) {
            if (newsFeedRecord.newsUrl.equalsIgnoreCase(data.newsUrl)) {
                savedFeedDao.update(newsFeedRecord);
                return;
            }
        }
        savedFeedDao.create(data);

    }

    public void clearCache() {
        try {
            TableUtils.dropTable(connectionSource, NewsFeedData.class, false);
            TableUtils.createTableIfNotExists(connectionSource, NewsFeedData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearSavedContent() {
        try {
            TableUtils.dropTable(connectionSource, SavedFeedData.class, false);
            TableUtils.createTableIfNotExists(connectionSource, SavedFeedData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean isUrlSaved(String pageUrl){

        boolean isUrlSaved = false;
        try {
            if (savedFeedDao != null) {
                QueryBuilder<SavedFeedData, Integer> newsFeedQB = savedFeedDao.queryBuilder();
                newsFeedQB.where().eq(SavedFeedData.SAVED_FEED_URL, pageUrl);
                if (savedFeedDao.query(newsFeedQB.prepare()) != null && savedFeedDao.query(newsFeedQB.prepare()).size() == 1) {
                    isUrlSaved = savedFeedDao.query(newsFeedQB.prepare()).get(0).isFeedSaved;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUrlSaved;


    }

}