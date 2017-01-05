package com.inspius.yo365.modules.news;

import com.inspius.yo365.fragment.webview.WebViewFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class MNewsDetailFragment extends WebViewFragment {
    public static final String TAG = MNewsDetailFragment.class.getSimpleName();

    public static MNewsDetailFragment newInstance(MNewsModel newsModel) {
        MNewsDetailFragment fragment = new MNewsDetailFragment();
        fragment.newsModel = newsModel;

        fragment.headerName = newsModel.getTitle();
        fragment.urlContent = newsModel.getDetailPath();
        fragment.urlShare = newsModel.getDetailPath();
        return fragment;
    }

    private MNewsModel newsModel;

    @Override
    public void onInitView() {
        super.onInitView();

        MNewsRPC.updateNewsViewCounter(newsModel.getID(), null);
    }
}
