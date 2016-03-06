package com.vgnary.nyt.thenewshour.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.activities.FeedDetailPageJsoupActivity;
import com.vgnary.nyt.thenewshour.constants.AppConstants;
import com.vgnary.nyt.thenewshour.eventBus.ClearListEvent;
import com.vgnary.nyt.thenewshour.eventBus.TextSizeEvent;
import com.vgnary.nyt.thenewshour.interfaces.OnItemClickListener;
import com.vgnary.nyt.thenewshour.models.bestSellerList.BestSellerListEntity;
import com.vgnary.nyt.thenewshour.models.bestSellerList.BestSellerListResponse;
import com.vgnary.nyt.thenewshour.models.geographicDetail.GeographicDetailEntity;
import com.vgnary.nyt.thenewshour.models.mostPopular.MetaStringData;
import com.vgnary.nyt.thenewshour.models.movieReview.MovieResource;
import com.vgnary.nyt.thenewshour.models.movieReview.MovieReviewEntity;
import com.vgnary.nyt.thenewshour.models.movieReview.MovieReviewResponse;
import com.vgnary.nyt.thenewshour.models.BasicResponse;
import com.vgnary.nyt.thenewshour.models.geographicDetail.GeographicDataResponse;
import com.vgnary.nyt.thenewshour.models.mostPopular.MostPopularResponse;
import com.vgnary.nyt.thenewshour.viewHolders.NewsFeedItemViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;


public class HomePageListFragementAdapter extends RecyclerView.Adapter<NewsFeedItemViewHolder> implements OnItemClickListener {

    private int mResponseType;
    private int itemCount = 0;
    private Context mContext;
    private String mFeedDetailPageUrl;
    private String mFeedDetailPageTitle;
    private float mTextSizelarge = 0;
    private float mTextSizeSmall = 0;
    private List<MetaStringData> mMostPopularResponseList = new ArrayList<>();
    private List<MovieReviewEntity> mMovieReviewResponseList = new ArrayList<>();
    private List<BestSellerListEntity> mBestSellerResponseList = new ArrayList<>();
    private List<GeographicDetailEntity> mGeographicResponseList = new ArrayList<>();
    private EventBus bus = EventBus.getDefault();
    private boolean isTextSizeAltered = false;


    public HomePageListFragementAdapter() {
        bus.register(this);
    }

    public void setHomePageListAdapterData(BasicResponse networkResponse, int responseType, Context context) {

        this.mResponseType = responseType;
        mContext = context;
        if (this.mResponseType == AppConstants.MOST_POPULAR_DATA) {
            MostPopularResponse mostPopularResponse = (MostPopularResponse) networkResponse;
            mMostPopularResponseList.addAll(mostPopularResponse.mostPopularEntity.basicEntities);
            Collections.reverse(mMostPopularResponseList);
            itemCount = mMostPopularResponseList.size();
        } else if (this.mResponseType == AppConstants.MOVIE_REVIEW_DATA) {
            MovieReviewResponse movieReviewResponse = (MovieReviewResponse) networkResponse;
            mMovieReviewResponseList.addAll(movieReviewResponse.movieReviewEntity);
            Collections.reverse(mMovieReviewResponseList);
            itemCount = mMovieReviewResponseList.size();
        } else if (this.mResponseType == AppConstants.GEOGRAPHIC_DATA) {
            GeographicDataResponse geographicDataResponse = (GeographicDataResponse) networkResponse;
            mGeographicResponseList.addAll(geographicDataResponse.geographicDetailList);
            Collections.reverse(mGeographicResponseList);
            itemCount = mGeographicResponseList.size();
        } else if (this.mResponseType == AppConstants.BESTSELLER_DATA) {
            BestSellerListResponse bestSellerListResponse = (BestSellerListResponse) networkResponse;
            mBestSellerResponseList.addAll(bestSellerListResponse.bestSellerListEntityList);
            Collections.reverse(mBestSellerResponseList);
            itemCount = mBestSellerResponseList.size();
        }
    }


    @Override
    public NewsFeedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_feed_tuple, null);
        return new NewsFeedItemViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(NewsFeedItemViewHolder holder, int position) {
        if (mResponseType == AppConstants.MOST_POPULAR_DATA && mMostPopularResponseList.get(position) != null) {
            setViewHolderItems(holder, mMostPopularResponseList.get(position).snippet, mMostPopularResponseList.get(position).lead_paragraph, mMostPopularResponseList.get(position).web_url, mMostPopularResponseList.get(position).snippet, null);
        } else if (mResponseType == AppConstants.MOVIE_REVIEW_DATA && mMovieReviewResponseList.get(position) != null) {
            if (mMovieReviewResponseList.get(position).movieMultimedia != null) {
                setViewHolderItems(holder, mMovieReviewResponseList.get(position).headline, mMovieReviewResponseList.get(position).summary_short, mMovieReviewResponseList.get(position).feedMovieSuggestedLink.url, mMovieReviewResponseList.get(position).headline, mMovieReviewResponseList.get(position).movieMultimedia.movieResource);
            } else {
                setViewHolderItems(holder, mMovieReviewResponseList.get(position).headline, mMovieReviewResponseList.get(position).summary_short, mMovieReviewResponseList.get(position).feedMovieSuggestedLink.url, mMovieReviewResponseList.get(position).headline, null);
            }
        } else if (mResponseType == AppConstants.GEOGRAPHIC_DATA && mGeographicResponseList.get(position) != null) {
            setViewHolderItems(holder, mGeographicResponseList.get(position).name, mGeographicResponseList.get(position).population, null, mGeographicResponseList.get(position).name, null);
        } else if (mResponseType == AppConstants.BESTSELLER_DATA && mBestSellerResponseList.get(position) != null) {
            setViewHolderItems(holder, mBestSellerResponseList.get(position).displayName, mBestSellerResponseList.get(position).newestPublishedDate, null, mBestSellerResponseList.get(position).updatedTime, null);
        }
    }


    @Override
    public int getItemCount() {
        return itemCount;
    }

    private void setViewHolderItems(final NewsFeedItemViewHolder holder, String titleText, String summaryText, String feedDetailPageUrl, String feedDetailPageTitle, MovieResource imageUrl) {
        if (!(TextUtils.isEmpty(titleText) && TextUtils.isEmpty(summaryText))) {
            holder.titleText.setText(titleText);
            holder.summaryText.setText(summaryText);
            mFeedDetailPageUrl = feedDetailPageUrl;
            mFeedDetailPageTitle = feedDetailPageTitle;
            if (isTextSizeAltered) {
                holder.titleText.setTextSize(mTextSizelarge);
                holder.summaryText.setTextSize(mTextSizeSmall);
                isTextSizeAltered = false;
            } else {
                setTextAsperDb(holder);
            }
            if (imageUrl != null) {
                setCircularImage(holder, imageUrl);
            }

        } else {
            holder.tupleContainer.removeAllViews();
        }
    }

    private void setCircularImage(final NewsFeedItemViewHolder holder, MovieResource imageUrl) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        boolean isdDontSaveImageoffline = sharedPreferences.getBoolean(AppConstants.SAVE_IMAGES_OFFLINE, false);
        Glide.with(mContext).load(imageUrl.src).into(holder.newsThumbnail);
        holder.newsThumbnail.setVisibility(View.VISIBLE);
    }

    private void setTextAsperDb(NewsFeedItemViewHolder holder) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        int textTypeSelected = sharedPreferences.getInt(AppConstants.LOCAL_TEXTSIZE_VALUE, 0);
        switch (textTypeSelected) {
            case AppConstants.LARGE_TEXT:
                holder.summaryText.setTextSize(mContext.getResources().getDimension(R.dimen.text_size_large_summary));
                holder.titleText.setTextSize(mContext.getResources().getDimension(R.dimen.text_size_large_heading));
                break;
            case AppConstants.NORMAL_TEXT:
                holder.summaryText.setTextSize(mContext.getResources().getDimension(R.dimen.text_size_normal_summary));
                holder.titleText.setTextSize(mContext.getResources().getDimension(R.dimen.text_size_normal_heading));
                break;
            case AppConstants.SMALL_TEXT:
                holder.summaryText.setTextSize(mContext.getResources().getDimension(R.dimen.text_size_small_summary));
                holder.titleText.setTextSize(mContext.getResources().getDimension(R.dimen.text_size_small_heading));
                break;
        }
    }


    @Override
    public void onItemClick(int position, NewsFeedItemViewHolder viewHolder) {
        if (mFeedDetailPageUrl != null) {
            Intent onItemClickintent = new Intent(mContext.getApplicationContext(), FeedDetailPageJsoupActivity.class);
            onItemClickintent.putExtra(AppConstants.INTENT_KEY_STATIC_PAGE_URL, mFeedDetailPageUrl);
            onItemClickintent.putExtra(AppConstants.INTENT_KEY_MENU_TITLE, mFeedDetailPageTitle);
            onItemClickintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(onItemClickintent);
        }
    }

    public void onEvent(TextSizeEvent event) {
        mTextSizelarge = event.getlargeTextsize();
        mTextSizeSmall = event.getSmallTextsize();
        isTextSizeAltered = true;
        notifyDataSetChanged();
    }

    public void onEvent(ClearListEvent event) {
        switch (event.getListType()) {
            case AppConstants.MOST_POPULAR_DATA:
                mMostPopularResponseList.clear();
                break;
            case AppConstants.MOVIE_REVIEW_DATA:
                mMovieReviewResponseList.clear();
                break;
            case AppConstants.GEOGRAPHIC_DATA:
                mGeographicResponseList.clear();
                break;
            case AppConstants.BESTSELLER_DATA:
                mBestSellerResponseList.clear();
                break;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        bus.unregister(this);
        super.finalize();
    }
}
