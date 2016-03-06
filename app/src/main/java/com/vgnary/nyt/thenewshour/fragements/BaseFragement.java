package com.vgnary.nyt.thenewshour.fragements;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.vgnary.nyt.thenewshour.R;
import com.vgnary.nyt.thenewshour.application.AppController;
import com.squareup.leakcanary.RefWatcher;


public abstract class BaseFragement extends Fragment {
    private RelativeLayout mLoadingView;
    private static BaseFragement baseFragement;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_fragment_home_page, container, false);
        mLoadingView = (RelativeLayout) view.findViewById(R.id.rl_loader_view);
        return view;
    }

    protected void showLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingView() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = AppController.getRefWatcher(getActivity());
        refWatcher.watch(this);
        baseFragement = this;
    }
}
