package com.ihujia.hujia.otherstore.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.ShareEntity;
import com.ihujia.hujia.network.entities.StoreRenovationEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.otherstore.jsinterface.IcallbackJS;
import com.ihujia.hujia.otherstore.jsinterface.IhujiaJavaScript;
import com.ihujia.hujia.utils.CommonShareUtil;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.IdentityHashMap;

/**
 * Created by liuzhichao on 2017/3/27.
 * 店铺装修
 */
public class StoreRenovationActivity extends BaseActivity implements IcallbackJS, View.OnClickListener {

    private static final int REQUEST_NET_SHARE_INFO = 10;
    private static final int REQUEST_NET_STORE_INFO = 20;
    public static final String EXTRA_URL = "extra_url";
    public static final String EXTRA_NAME = "extra_name";

    @ViewInject(R.id.progress_horizontal)
    private ProgressBar mHorizontalProgress;
    @ViewInject(R.id.iv_store_renovation_back)
    private ImageView storeBack;
    @ViewInject(R.id.tv_store_renovation_title)
    private TextView storeTitle;
    @ViewInject(R.id.iv_store_renovation_share)
    private ImageView storeShare;
    @ViewInject(R.id.sdv_store_renovation_logo)
    private SimpleDraweeView storeLogo;
    @ViewInject(R.id.tv_store_renovation_name)
    private TextView storeName;
    @ViewInject(R.id.tv_store_renovation_type)
    private TextView storeType;
    @ViewInject(R.id.tv_store_renovation_num)
    private TextView storeNum;
    @ViewInject(R.id.webview)
    private WebView mWebView;

    private Handler refreshProgressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 >= 100) {
                if (mHorizontalProgress != null) {
                    mHorizontalProgress.setVisibility(View.GONE);
                }
            } else {
                if (mHorizontalProgress != null && msg.arg1 >= 0) {
                    mHorizontalProgress.setVisibility(View.VISIBLE);
                    mHorizontalProgress.setProgress(msg.arg1);
                }
            }
        }
    };
    private String storeId;
    private String url;

    public static void startStoreRenovationActivity(Context context, String storeId, String name) {
        Intent intent = new Intent(context, StoreRenovationActivity.class);
        intent.putExtra(EXTRA_ID, storeId);
        intent.putExtra(EXTRA_NAME, name);
        context.startActivity(intent);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_store_renovation);
        ViewInjectUtils.inject(this);
        initView();
        loadData();
    }

    private void loadData() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("store_id", storeId);
        requestHttpData(Constants.Urls.URL_POST_OTHER_STORE_DETAIL, REQUEST_NET_STORE_INFO, FProtocol.HttpMethod.POST, params);
    }

    private void initView() {
        storeId = getIntent().getStringExtra(EXTRA_ID);
        String name = getIntent().getStringExtra(EXTRA_NAME);

        storeTitle.setText(name);
        storeName.setText(name);

        storeBack.setOnClickListener(this);
        storeShare.setOnClickListener(this);
    }

    private void initWebView() {
        // 设置支持JavaScript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new IhujiaJavaScript(this).setStoreId(storeId).setIcallbackJS(this), "vjia");
        webSettings.setSupportZoom(true);
//		webSettings.setDatabaseEnabled(true);
//		String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationEnabled(true);
//		webSettings.setGeolocationDatabasePath(dir);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);//图片调整到适合WebView的大小
        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕大小

        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能、
//		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
//		webSettings.supportMultipleWindows();  //多窗口
//		webSettings.setBlockNetworkImage(false);//同步加载图片

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //长按时间消费掉不做处理，防止弹出复制pop
        mWebView.setOnLongClickListener(v -> true);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                resend.sendToTarget();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(view.getContext(), getString(R.string.custom_net_error), Toast.LENGTH_SHORT).show();
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (refreshProgressHandler != null) {
                    if (refreshProgressHandler.hasMessages(0)) {
                        refreshProgressHandler.removeMessages(0);
                    }
                    Message mMessage = refreshProgressHandler.obtainMessage(0, newProgress, 0, null);
                    refreshProgressHandler.sendMessageDelayed(mMessage, 100);
                }
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        mWebView.loadUrl(url);
    }

    private void loadShareInfo() {
        showProgressDialog();
        requestHttpData(Constants.Urls.URL_POST_SHARE_INFO, REQUEST_NET_SHARE_INFO, FProtocol.HttpMethod.POST, null);
    }

    @Override
    public void success(int requestCode, String data) {
        super.success(requestCode, data);
        closeProgressDialog();
            switch (requestCode) {
                case REQUEST_NET_STORE_INFO:
                    StoreRenovationEntity entity = Parsers.storeRenovationEntity(data);
                    if (entity != null) {
                        storeType.setText(entity.getStoreTypeName());
                        storeNum.setText(entity.getGoodsCount());
                        ImageUtils.setSmallImg(storeLogo, entity.getStoreLogo());
                        url = entity.getStoreUrl();
                        initWebView();
                    }
                    break;
                case REQUEST_NET_SHARE_INFO:
                    ShareEntity share = Parsers.getShare(data);
                    if (share != null) {
                        CommonShareUtil.share(this, share.getDesc(), share.getTitle(), share.getImg(), share.getUrl());
                    }
                    break;
            }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        closeProgressDialog();
        super.mistake(requestCode, status, errorMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void callJS(String params) {
        if (mWebView != null && !StringUtil.isEmpty(params)) {
            mWebView.loadUrl(params);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_store_renovation_back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    onBackPressed();
                }
                break;
            case R.id.iv_store_renovation_share:
                loadShareInfo();
                break;
        }
    }
}
