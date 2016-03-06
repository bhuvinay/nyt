package com.vgnary.nyt.thenewshour.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.constants.AppConstants;
import com.vgnary.nyt.thenewshour.models.database.SavedFeedData;
import com.vgnary.nyt.thenewshour.network.AppJsonRequestManager;
import com.vgnary.nyt.thenewshour.network.OnNetworkResponseListener;
import com.vgnary.nyt.thenewshour.utils.AppUtils;
import com.vgnary.nyt.thenewshour.utils.LogUtils;
import com.vgnary.nyt.thenewshour.utils.NewsFeedDatabaseHelper;

import org.json.JSONException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;

public class FeedDetailPageJsoupActivity extends BaseActivity
        implements OnNetworkResponseListener<Document> {
    private TextView mTextHeading;
    private TextView mTextSummary;
    private String mPageHeading;
    private String mPageSummary;

    String mPageUrl;
    private NewsFeedDatabaseHelper mUserPrefDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_feed_detai_page);
        init();
        mPageUrl = getIntent().getStringExtra(AppConstants.INTENT_KEY_STATIC_PAGE_URL);
        if (mPageUrl == null && getIntent().getData() != null) {
            mPageUrl = getIntent().getData().toString();
        }
        parsePagedData(mPageUrl);
    }

    private void parsePagedData(String pageUrl) {
        if (pageUrl != null) {
            showLoadingView();
            try {
                AppJsonRequestManager.getInstance(getApplicationContext()).executeJsoupRequest(Request.Method.GET, pageUrl, null, AppConstants.GET_DETAIL_PAGE, Document.class, this);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        mTextHeading = (TextView) findViewById(R.id.tv_haeder);
        mTextSummary = (TextView) findViewById(R.id.tv_summary);
        Toolbar appToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(" ");
        }
    }

    @Override
    public void onDownloadComplete(int requestId, String requestUrl, Document responseData) {
        hideLoadingView();
        htmlFileParser(responseData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed_detail_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toolbar_share:
                AppUtils.showShareDialog(getResources().getString(R.string.label_share) + mPageUrl, getApplicationContext());
                break;
            case R.id.action_toolbar_save:
                performSaveAction();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void performSaveAction() {
        mUserPrefDBHelper = AppUtils.getHelper(getApplicationContext());
        if (mUserPrefDBHelper.isUrlSaved(mPageUrl)) {
            LogUtils.makeToast("Conten already saved", Toast.LENGTH_LONG, getApplicationContext());
        } else {
            LogUtils.makeToast("Content  saved", Toast.LENGTH_LONG, getApplicationContext());
            saveFeedToDb();
        }
    }

    private void saveFeedToDb() {
        mUserPrefDBHelper = AppUtils.getHelper(getApplicationContext());
        mUserPrefDBHelper.addSavedNewsFeedInCache(new SavedFeedData(mPageHeading, mPageSummary, mPageUrl, true));
    }


    private void htmlFileParser(Document responseData) {
        Elements descriptionHeader = responseData
                .select("meta[name=hdl]");
        mPageHeading = descriptionHeader.attr("content");
        Elements descriptionSummary = responseData
                .select("meta[name=lp]");
        mPageSummary = descriptionSummary.attr("content");
        if (mPageHeading != null) {
            mTextHeading.setText(mPageHeading);
        }
        if (mPageSummary != null) {
            mTextSummary.setText(mPageSummary);
        }
    }

    @Override
    public void onDownloadFailed(int requestId, String requestUrl, VolleyError networkError) {
        hideLoadingView();
       /* AppUtils.showErrorDialog(getApplicationContext());*/
    }
}
