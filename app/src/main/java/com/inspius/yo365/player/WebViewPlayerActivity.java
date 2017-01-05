package com.inspius.yo365.player;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.coreapp.helper.Logger;
import com.inspius.yo365.R;
import com.inspius.yo365.helper.ConnectionDetector;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Billy on 1/6/17.
 */

public class WebViewPlayerActivity extends BaseVideoPlayerActivity {
    @BindView(R.id.tvnMessage)
    TextView tvnMessage;

    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @BindView(R.id.webView)
    WebView mWebView;

    private String urlContent;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_webview_player;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        urlContent = videoModel.getVideoPath();
        if (TextUtils.isEmpty(urlContent))
            finish();

        startWebView(urlContent);
    }

    @Override
    public void onResume() {
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
                if (!(new ConnectionDetector(WebViewPlayerActivity.this)).isConnectingToInternet()) {
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
