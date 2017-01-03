package com.inspius.yo365.modules.news;

import com.inspius.yo365.R;
import com.inspius.yo365.api.RPC;
import com.inspius.yo365.fragment.webview.WebViewFragment;
import com.inspius.yo365.model.NewsModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class MNewsDetailFragment extends WebViewFragment {
    public static final String TAG = MNewsDetailFragment.class.getSimpleName();

    public static MNewsDetailFragment newInstance(NewsModel newsModel) {
        MNewsDetailFragment fragment = new MNewsDetailFragment();
        fragment.newsModel = newsModel;

        fragment.headerName = newsModel.getTitle();
        fragment.urlContent = newsModel.getDetailPath();
        fragment.urlShare = newsModel.getDetailPath();
        return fragment;
    }

    private NewsModel newsModel;

    @Override
    public void onInitView() {
        super.onInitView();

        RPC.updateNewsViewCounter(newsModel.getID(), null);
    }
}
