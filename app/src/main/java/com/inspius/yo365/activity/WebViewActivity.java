package com.inspius.yo365.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.inspius.yo365.R;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.base.StdActivity;
import com.inspius.yo365.helper.ConnectionDetector;
import com.inspius.coreapp.helper.InspiusIntentUtils;
import com.inspius.coreapp.helper.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends StdActivity {

    public static Intent getIntent(Context mContext, String headerName, String urlContent) {
        return WebViewActivity.getIntent(mContext, urlContent, headerName, urlContent);
    }

    public static Intent getIntent(Context mContext, String headerName, String urlContent, String urlShare) {
        if (TextUtils.isEmpty(urlShare))
            urlShare = urlContent;

        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra(AppConstant.KEY_BUNDLE_URL_PAGE, urlContent);
        intent.putExtra(AppConstant.KEY_BUNDLE_TITLE, headerName);
        intent.putExtra(AppConstant.KEY_BUNDLE_URL_SHARE, urlShare);
        return intent;
    }

    @BindView(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @BindView(R.id.tvnMessage)
    TextView tvnMessage;

    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    protected int getLayoutResourceId() {
        return 0;
    }

    private String headerName;
    private String urlContent;
    private String urlShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app);
        ButterKnife.bind(this);

        setupActionBar();

        headerName = getIntent().getExtras().getString(AppConstant.KEY_BUNDLE_TITLE);
        urlContent = getIntent().getExtras().getString(AppConstant.KEY_BUNDLE_URL_PAGE);
        urlShare = getIntent().getExtras().getString(AppConstant.KEY_BUNDLE_URL_SHARE);

        updateHeaderTitle(headerName);
        startWebView(urlContent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null)
            mWebView.loadUrl(urlContent);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null)
            mWebView.onPause();
    }

    /**
     * Sets up the action bar.
     */
    @OnClick(R.id.imvHeaderMenu)
    void doShowMenu() {
        onBackPressed();
    }

    @OnClick(R.id.imvHeaderShare)
    void doShare() {
        Intent intent = InspiusIntentUtils.shareText(headerName, urlShare);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else
            finish();
    }

    private void setupActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void updateHeaderTitle(String name) {
        if (name == null)
            name = getString(R.string.app_name);

        tvnHeaderTitle.setText(name);
    }

    void startAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.smoothToShow();
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.smoothToHide();
    }

    public void startWebView(String url) {
        Logger.d("startWebView", url);
        // Create new webview Client to show progress dialog
        // When opening a link or click on link
        startAnimLoading();
        mWebView.setVisibility(View.INVISIBLE);
        tvnMessage.setVisibility(View.GONE);

        mWebView.setWebViewClient(new WebViewClient() {
            //ProgressDialog progressDialog;

            // If you will not use this method link links are opeen in new brower
            // not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (view != null)
                    view.loadUrl(url);
                return true;
            }

            // Show loader on link load
            public void onLoadResource(WebView view, String url) {

            }

            public void onPageFinished(WebView view, String url) {
                // dismiss loading
                stopAnimLoading();
                if (mWebView != null)
                    mWebView.setVisibility(View.VISIBLE);

                tvnMessage.setVisibility(View.GONE);
                if (!(new ConnectionDetector(WebViewActivity.this)).isConnectingToInternet()) {
                    mWebView.setVisibility(View.GONE);
                    tvnMessage.setVisibility(View.VISIBLE);
                    tvnMessage.setText(getText(R.string.no_internet));
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                stopAnimLoading();
                if (mWebView != null)
                    mWebView.setVisibility(View.INVISIBLE);

                tvnMessage.setVisibility(View.VISIBLE);
                tvnMessage.setText("The web page opening slowly or not loading");
            }
        });

        // Javascript inabled on webview
        mWebView.getSettings().setJavaScriptEnabled(true);
    }

    @OnClick(R.id.tvnMessage)
    void doReload() {
        tvnMessage.setVisibility(View.GONE);
        mWebView.loadUrl(urlContent);
    }
}
