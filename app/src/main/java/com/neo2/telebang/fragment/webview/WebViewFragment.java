package com.neo2.telebang.fragment.webview;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.coreapp.helper.InspiusIntentUtils;
import com.inspius.coreapp.helper.Logger;
import com.neo2.telebang.R;
import com.neo2.telebang.base.StdFragment;
import com.neo2.telebang.helper.ConnectionDetector;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class WebViewFragment extends StdFragment {
    public static final String TAG = WebViewFragment.class.getSimpleName();

    public static WebViewFragment newInstance(String headerName, String urlContent) {
        return newInstance(headerName, urlContent, urlContent);
    }

    public static WebViewFragment newInstance(String headerName, String urlContent, String urlShare) {
        WebViewFragment fragment = new WebViewFragment();
        fragment.headerName = headerName;
        fragment.urlContent = urlContent;
        fragment.urlShare = urlShare;
        return fragment;
    }

    @BindView(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @BindView(R.id.tvnMessage)
    TextView tvnMessage;

    @BindView(R.id.imvHeaderLeft)
    ImageView imvHeaderLeft;

    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @BindView(R.id.webView)
    WebView mWebView;

    protected String headerName;
    protected String urlContent;
    protected String urlShare;

    @Override
    public int getLayout() {
        return R.layout.fragment_webview;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onInitView() {
        updateHeaderTitle(headerName);
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

    /**
     * Sets up the action bar.
     */
    @OnClick(R.id.imvHeaderLeft)
    void doHeaderLeft() {
        onLeftClicked();
    }

    void onLeftClicked() {
        onBackPressed();
    }

    @OnClick(R.id.imvHeaderShare)
    void doShare() {
        onShareClicked();
    }

    void onShareClicked() {
        Intent intent = InspiusIntentUtils.shareText(headerName, urlShare);
        startActivity(intent);
    }

    public void updateHeaderTitle(String name) {
        if (TextUtils.isEmpty(name))
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
                if (!(new ConnectionDetector(mContext)).isConnectingToInternet()) {
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
