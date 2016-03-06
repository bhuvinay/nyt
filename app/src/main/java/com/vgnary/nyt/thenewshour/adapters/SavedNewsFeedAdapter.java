package com.vgnary.nyt.thenewshour.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.interfaces.OnItemClickListener;
import com.vgnary.nyt.thenewshour.viewHolders.NewsFeedItemViewHolder;

import java.util.ArrayList;
import java.util.List;


public class SavedNewsFeedAdapter extends RecyclerView.Adapter<NewsFeedItemViewHolder> implements OnItemClickListener {
    private String mFeedDetailPageUrl;
    private String mFeedDetailPageTitle;
    private List<SavedNewsFeedData> mSavedNewsList = new ArrayList<>();

    @Override
    public NewsFeedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_feed_tuple, null);
        return new NewsFeedItemViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(NewsFeedItemViewHolder holder, int position) {
        SavedNewsFeedData savedNewsFeedData = mSavedNewsList.get(position);
        setViewHolderItems(holder, savedNewsFeedData.newsFeedTitile, savedNewsFeedData.newsFeedSummary);
    }

    @Override
    public int getItemCount() {
        return mSavedNewsList.size();
    }

    @Override
    public void onItemClick(int position, NewsFeedItemViewHolder tupleSelectUnselectView) {

    }

    private void setViewHolderItems(final NewsFeedItemViewHolder holder, String titleText, String summaryText) {
        if (!(TextUtils.isEmpty(titleText) && TextUtils.isEmpty(summaryText))) {
            holder.titleText.setText(titleText);
            holder.summaryText.setText(summaryText);

        }
    }

    public void setData(List<SavedNewsFeedData> savedNewsFeedDataList) {
        this.mSavedNewsList = savedNewsFeedDataList;
        notifyDataSetChanged();
    }
}
