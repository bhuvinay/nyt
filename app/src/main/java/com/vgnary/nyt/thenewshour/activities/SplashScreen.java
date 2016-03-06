package com.vgnary.nyt.thenewshour.activities;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.constants.AppConstants;
import com.vgnary.nyt.thenewshour.constants.UrlConfig;
import com.vgnary.nyt.thenewshour.models.BasicResponse;
import com.vgnary.nyt.thenewshour.models.mostPopular.MostPopularResponse;
import com.vgnary.nyt.thenewshour.network.AppJsonRequestManager;
import com.vgnary.nyt.thenewshour.network.OnNetworkResponseListener;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;


public class SplashScreen extends BaseActivity implements OnNetworkResponseListener<BasicResponse> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash_screen);
        /*launchHomePageWithDelay();*/
        Intent sendDataIntent = new Intent(this, HomePageActivity.class);
        sendDataIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(sendDataIntent);
    }

    private void launchHomePageWithDelay() {
        showLoadingView();
        try {
            AppJsonRequestManager.getInstance(getApplicationContext()).executeRequest(Request.Method.GET, UrlConfig.URL_MOST_POPULAR_DATA, null, AppConstants.GET_MOST_POULAR_DATA, MostPopularResponse.class, this);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDownloadComplete(int requestId, String requestUrl, BasicResponse responseData) {
        hideLoadingView();
        if (responseData.status.equalsIgnoreCase("OK")) {
            if (requestId == AppConstants.GET_MOST_POULAR_DATA) {
                MostPopularResponse mostPopularData = (MostPopularResponse) responseData;
                sendDataToNextPage(mostPopularData);
            }
        }
    }

    private void sendDataToNextPage(MostPopularResponse mostPopularData) {
        Intent sendDataIntent = new Intent(this, HomePageActivity.class);
        sendDataIntent.putExtra(AppConstants.INTENT_SPLASH_SCREEN_DATA, mostPopularData);
        sendDataIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(sendDataIntent);
    }

    @Override
    public void onDownloadFailed(int requestId, String requestUrl, VolleyError networkError) {
        hideLoadingView();
    }
}

