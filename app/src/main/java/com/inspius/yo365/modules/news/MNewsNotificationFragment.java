package com.inspius.yo365.modules.news;

import com.inspius.yo365.fragment.webview.WebViewFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class MNewsNotificationFragment extends WebViewFragment {
    public static final String TAG = MNewsNotificationFragment.class.getSimpleName();

    public static MNewsNotificationFragment newInstance(MNewsModel newsModel) {
        MNewsNotificationFragment fragment = new MNewsNotificationFragment();
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

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return false;
    }
}
