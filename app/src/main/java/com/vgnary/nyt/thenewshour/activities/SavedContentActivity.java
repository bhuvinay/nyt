package com.vgnary.nyt.thenewshour.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.adapters.ItemTouchHelperCallback;
import com.vgnary.nyt.thenewshour.adapters.SavedNewsFeedAdapter;
import com.vgnary.nyt.thenewshour.adapters.SavedNewsFeedData;
import com.vgnary.nyt.thenewshour.interfaces.CallbackItemTouch;
import com.vgnary.nyt.thenewshour.models.database.SavedFeedData;
import com.vgnary.nyt.thenewshour.utils.AppUtils;
import com.vgnary.nyt.thenewshour.utils.NewsFeedDatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SavedContentActivity extends BaseActivity implements CallbackItemTouch {
    private RecyclerView mSavedNewsView;
    private List<SavedNewsFeedData> mSavedNewsFeedData = new ArrayList<>();
    private NewsFeedDatabaseHelper mUserPrefDBHelper;
    private TextView mNoContentView;
    private SavedNewsFeedAdapter mSavedNewsFeedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_saved_content_activity);
        init();
        setListeners();
        getListFromDb();
        setDataToAdapter();
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(this);// create MyItemTouchHelperCallback
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback); // Create ItemTouchHelper and pass with parameter the MyItemTouchHelperCallback
        touchHelper.attachToRecyclerView(mSavedNewsView); // Attach ItemTouchHelper to RecyclerView
    }

    private void setDataToAdapter() {
        if (mSavedNewsFeedData != null && mSavedNewsFeedData.size() != 0) {
            mSavedNewsFeedAdapter = new SavedNewsFeedAdapter();
            mSavedNewsFeedAdapter.setData(mSavedNewsFeedData);
            mSavedNewsView.setAdapter(mSavedNewsFeedAdapter);
        } else {
            mNoContentView.setVisibility(View.VISIBLE);
            mSavedNewsView.setVisibility(View.GONE);
            mNoContentView.setText(getResources().getString(R.string.label_no_content));
        }
    }

    private List<SavedNewsFeedData> getListFromDb() {

        mUserPrefDBHelper = AppUtils.getHelper(getApplicationContext());
        List<SavedFeedData> savedResponse = mUserPrefDBHelper.getSavedFeedData();
        if (savedResponse != null)
            for (int i = 0; i < savedResponse.size(); i++) {
                SavedNewsFeedData savedNewsFeedData = new SavedNewsFeedData();
                savedNewsFeedData.newsFeedTitile = savedResponse.get(i).newsTitle;
                savedNewsFeedData.newsFeedSummary = savedResponse.get(i).newsSummary;
                savedNewsFeedData.newsFeedUrl = savedResponse.get(i).newsUrl;
                mSavedNewsFeedData.add(savedNewsFeedData);


            }
        return mSavedNewsFeedData;
    }


    private void init() {
        mSavedNewsView = (RecyclerView) findViewById(R.id.rv_saved_news_feed);
        mNoContentView = (TextView) findViewById(R.id.tv_no_content);
        mSavedNewsView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void setListeners() {

    }

    @Override
    public void itemTouchOnMove(int oldPosition, int newPosition) {
        Collections.swap(mSavedNewsFeedData, oldPosition, newPosition); // change position
        mSavedNewsFeedAdapter.notifyItemMoved(oldPosition, newPosition);
    }
}
