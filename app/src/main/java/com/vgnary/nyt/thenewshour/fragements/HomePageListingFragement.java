package com.vgnary.nyt.thenewshour.fragements;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.adapters.HomePageListFragementAdapter;
import com.vgnary.nyt.thenewshour.constants.AppConstants;
import com.vgnary.nyt.thenewshour.constants.UrlConfig;
import com.vgnary.nyt.thenewshour.eventBus.ClearListEvent;
import com.vgnary.nyt.thenewshour.interfaces.SwipeToRefreshPermissionProvider;
import com.vgnary.nyt.thenewshour.models.BasicResponse;
import com.vgnary.nyt.thenewshour.models.bestSellerList.BestSellerListResponse;
import com.vgnary.nyt.thenewshour.models.geographicDetail.GeographicDataResponse;
import com.vgnary.nyt.thenewshour.models.mostPopular.MostPopularResponse;
import com.vgnary.nyt.thenewshour.models.movieReview.MovieReviewResponse;
import com.vgnary.nyt.thenewshour.network.AppJsonRequestManager;
import com.vgnary.nyt.thenewshour.network.OnNetworkResponseListener;
import com.vgnary.nyt.thenewshour.models.database.NewsFeedData;
import com.vgnary.nyt.thenewshour.utils.AppUtils;
import com.vgnary.nyt.thenewshour.utils.NewsFeedDatabaseHelper;
import com.vgnary.nyt.thenewshour.view.AppSwipeToRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

import de.greenrobot.event.EventBus;


public class HomePageListingFragement extends BaseFragement implements OnNetworkResponseListener<BasicResponse>, SwipeRefreshLayout.OnRefreshListener, SwipeToRefreshPermissionProvider {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView mRecyclerView;
    private Context context;
    private LinearLayoutManager mLayoutManager;
    private AppSwipeToRefreshLayout mSwipeToRefresh;
    private HomePageListFragementAdapter mHomePageListFragementAdapter;
    private MostPopularResponse mMostPopularData;
    private MovieReviewResponse mMovieReviewData;
    private GeographicDataResponse mGeographicData;
    private BestSellerListResponse mBestSellerData;
    int pastVisiblesItems;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int visibleItemCount, totalItemCount;
    private EventBus bus = EventBus.getDefault();
    private NewsFeedDatabaseHelper mUserPrefDBHelper = null;
    private boolean isSwipeToRefresh = false;
    private boolean isDataCachedShown = false;


    public static Fragment getInstance(int FragmentType) {
        HomePageListingFragement fragment = new HomePageListingFragement();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.FRAGEMENT_TYPE, FragmentType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_news_feed);
            mLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(mLayoutManager);
            initAdapter();
            bus.register(this);
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                        if (loading) {
                            if ((visibleItemCount + pastVisiblesItems + visibleThreshold) >= totalItemCount) {
                                loading = false;
                            }
                        }
                    }
                }
            });
            setSwipeToRefreshLayout(view);
            if (savedInstanceState == null) {
                showDataFromCache();
                getDataFromServer();

            } else {
                switch (getArguments().getInt(AppConstants.FRAGEMENT_TYPE)) {
                    case AppConstants.MOST_POPULAR_DATA:
                        mMostPopularData = (MostPopularResponse) savedInstanceState.getSerializable(AppConstants.RESPONSE_DATA_KEY);
                        setData(mMostPopularData, AppConstants.MOST_POPULAR_DATA);
                        break;
                    case AppConstants.MOVIE_REVIEW_DATA:
                        mMovieReviewData = (MovieReviewResponse) savedInstanceState.getSerializable(AppConstants.RESPONSE_DATA_KEY);
                        setData(mMovieReviewData, AppConstants.MOVIE_REVIEW_DATA);
                        break;
                    case AppConstants.GEOGRAPHIC_DATA:
                        mGeographicData = (GeographicDataResponse) savedInstanceState.getSerializable(AppConstants.RESPONSE_DATA_KEY);
                        setData(mGeographicData, AppConstants.GEOGRAPHIC_DATA);
                        break;
                    case AppConstants.BESTSELLER_DATA:
                        mBestSellerData = (BestSellerListResponse) savedInstanceState.getSerializable(AppConstants.RESPONSE_DATA_KEY);
                        setData(mBestSellerData, AppConstants.BESTSELLER_DATA);
                        break;
                }
            }
            return view;
        }
        return null;
    }

    private void showDataFromCache() {
        BasicResponse baseData = null;
        isDataCachedShown = true;
        switch (getArguments().getInt(AppConstants.FRAGEMENT_TYPE)) {
            case AppConstants.MOST_POPULAR_DATA:
                mMostPopularData = (MostPopularResponse) getResponseFromDb(MostPopularResponse.class);
                baseData = mMostPopularData;
                setData(baseData, getArguments().getInt(AppConstants.FRAGEMENT_TYPE));
                break;
            case AppConstants.MOVIE_REVIEW_DATA:
                mMovieReviewData = (MovieReviewResponse) getResponseFromDb(MovieReviewResponse.class);
                baseData = mMovieReviewData;
                setData(baseData, getArguments().getInt(AppConstants.FRAGEMENT_TYPE));
                break;
            case AppConstants.GEOGRAPHIC_DATA:
                mGeographicData = (GeographicDataResponse) getResponseFromDb(GeographicDataResponse.class);
                baseData = mGeographicData;
                setData(baseData, getArguments().getInt(AppConstants.FRAGEMENT_TYPE));
                break;
            case AppConstants.BESTSELLER_DATA:
                mBestSellerData = (BestSellerListResponse) getResponseFromDb(BestSellerListResponse.class);
                baseData = mBestSellerData;
                setData(baseData, getArguments().getInt(AppConstants.FRAGEMENT_TYPE));
                break;
        }
    }

    private void initAdapter() {
        mHomePageListFragementAdapter = new HomePageListFragementAdapter();
    }


    public void setData(BasicResponse data, int FragementType) {
        if (data != null) {
            mHomePageListFragementAdapter.setHomePageListAdapterData(data, FragementType, context);
            mRecyclerView.setAdapter(mHomePageListFragementAdapter);
            mHomePageListFragementAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        switch (getArguments().getInt(AppConstants.FRAGEMENT_TYPE)) {
            case AppConstants.MOST_POPULAR_DATA:
                saveInstanceState.putSerializable(AppConstants.RESPONSE_DATA_KEY, mMostPopularData);
                break;
            case AppConstants.MOVIE_REVIEW_DATA:
                saveInstanceState.putSerializable(AppConstants.RESPONSE_DATA_KEY, mMovieReviewData);
                break;
            case AppConstants.GEOGRAPHIC_DATA:
                saveInstanceState.putSerializable(AppConstants.RESPONSE_DATA_KEY, mGeographicData);
                break;
            case AppConstants.BESTSELLER_DATA:
                saveInstanceState.putSerializable(AppConstants.RESPONSE_DATA_KEY, mBestSellerData);
                break;
        }
        super.onSaveInstanceState(saveInstanceState);
    }

    private void getMostPopularData() {
        if (!isSwipeToRefresh) {
            showLoadingView();
        }
        try {
            AppJsonRequestManager.getInstance(getActivity()).executeRequest(Request.Method.GET, UrlConfig.URL_MOST_POPULAR_DATA, null, AppConstants.GET_MOST_POULAR_DATA, MostPopularResponse.class, this);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void getBestSellerList() {
        if (!isSwipeToRefresh) {
            showLoadingView();
        }
        try {
            AppJsonRequestManager.getInstance(getActivity()).executeRequest(Request.Method.GET, UrlConfig.URL_BEST_SELLER_DATA, null, AppConstants.GET_BESTSELLER_DATA, BestSellerListResponse.class, this);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void getMovieReviewData() {
        if (!isSwipeToRefresh) {
            showLoadingView();
        }
        {
            try {
                AppJsonRequestManager.getInstance(getActivity()).executeRequest(Request.Method.GET, UrlConfig.URL_MOVIE_REVIEW, null, AppConstants.GET_REVIEW_DATA, MovieReviewResponse.class, this);
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void getGeographicData() {
        if (!isSwipeToRefresh) {
            showLoadingView();
        }
        try {
            AppJsonRequestManager.getInstance(getActivity()).executeRequest(Request.Method.GET, UrlConfig.URL_GEOGRAPHIC_DATA, null, AppConstants.GET_GEOGRAPHIC_DATA, GeographicDataResponse.class, this);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDownloadComplete(int requestId, String requestUrl, BasicResponse responseData) {
        hideLoadingView();
        Gson gson = new Gson();
        String json = gson.toJson(responseData);
        saveResponseToDb(json);
        if (isSwipeToRefresh) {
            clearList();
            isSwipeToRefresh = false;
        }
        if (isDataCachedShown) {
            clearList();
            isDataCachedShown = false;
        }
        setData(responseData, getArguments().getInt(AppConstants.FRAGEMENT_TYPE));
        switch (getArguments().getInt(AppConstants.FRAGEMENT_TYPE)) {
            case AppConstants.MOST_POPULAR_DATA:
                mMostPopularData = (MostPopularResponse) responseData;
                break;
            case AppConstants.MOVIE_REVIEW_DATA:
                mMovieReviewData = (MovieReviewResponse) responseData;
                break;
            case AppConstants.GEOGRAPHIC_DATA:
                mGeographicData = (GeographicDataResponse) responseData;
                break;
            case AppConstants.BESTSELLER_DATA:
                mBestSellerData = (BestSellerListResponse) responseData;
                break;
        }
        mSwipeToRefresh.setRefreshing(false);
    }

    private void saveResponseToDb(String response) {
        if (response != null) {
            mUserPrefDBHelper = getHelper();
            mUserPrefDBHelper.addNewsFeedInCache(new NewsFeedData(response, getArguments().getInt(AppConstants.FRAGEMENT_TYPE)));
        }
    }

    @Override
    public void onDownloadFailed(int requestId, String requestUrl, VolleyError networkError) {
        hideLoadingView();
        showDataFromCache();
        AppUtils.showErrorDialog(context);
        mSwipeToRefresh.setRefreshing(false);
    }

    private Object getResponseFromDb(Class temp) {
        mUserPrefDBHelper = getHelper();
        String savedResponse = mUserPrefDBHelper.getNewsFeedData(getArguments().getInt(AppConstants.FRAGEMENT_TYPE));
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(savedResponse,
                temp);
    }

    private void setSwipeToRefreshLayout(View view) {
        mSwipeToRefresh = (AppSwipeToRefreshLayout) view.findViewById(R.id.swipe_to_refresh_home_page);
        mSwipeToRefresh.setOnRefreshListener(this);
        mSwipeToRefresh.setProgressViewOffset(true, (int) getResources().getDimension(R.dimen.app_toolbar_height), (int) getResources().getDimension(R.dimen.swipe_to_refresh_sling_shot_distance));
        mSwipeToRefresh.setSwipePermissionProvider(this);
        mSwipeToRefresh.setEnabled(true);
    }


    @Override
    public void onRefresh() {
        isSwipeToRefresh = true;
        getDataFromServer();
    }

    private void getDataFromServer() {
        switch (getArguments().getInt(AppConstants.FRAGEMENT_TYPE)) {
            case AppConstants.MOST_POPULAR_DATA:
                getMostPopularData();
                break;
            case AppConstants.MOVIE_REVIEW_DATA:
                getMovieReviewData();
                break;
            case AppConstants.GEOGRAPHIC_DATA:
                getGeographicData();
                break;
            case AppConstants.BESTSELLER_DATA:
                getBestSellerList();
                break;
        }
    }

    @Override
    public boolean canSwipeToRefresh() {
        return true;
    }

    @Override
    public void onDestroy() {
        bus.unregister(mHomePageListFragementAdapter);
        bus.unregister(this);
        super.onDestroy();
    }

    private NewsFeedDatabaseHelper getHelper() {
        if (mUserPrefDBHelper == null) {
            mUserPrefDBHelper = OpenHelperManager.getHelper(context, NewsFeedDatabaseHelper.class);
            mUserPrefDBHelper.getWritableDatabase();
        }
        return mUserPrefDBHelper;
    }

    public void onEvent(String refreshEvent) {
        Toast.makeText(context, "On refresh called", Toast.LENGTH_SHORT).show();
        getDataFromServer();
    }

    private void clearList() {
        bus.post(new ClearListEvent(getArguments().getInt(AppConstants.FRAGEMENT_TYPE)));
    }
}

