package com.ihujia.hujia.base;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.common.utils.LogUtil;
import com.common.utils.StringUtil;
import com.ihujia.hujia.R;
import com.ihujia.hujia.main.controller.MainActivity;
import com.ihujia.hujia.otherstore.jsinterface.IcallbackJS;
import com.ihujia.hujia.otherstore.jsinterface.IhujiaJavaScript;

/**
 * 公用WebView加载页
 */
public class WebViewActivity extends ToolBarActivity implements IcallbackJS {

	public static final String EXTRA_URL = "URL";
	public static final String EXTRA_TITLE = "title";
	public static final int FROM_PUSH = 1;
	public static final int FROM_CARD_PKG_DETAIL = 2;
	public static final int FROM_PRIVILEGE_EXPLAIN = 3;
	private ProgressBar mHorizontalProgress;
	private WebView mWebView;
	private ImageView backBtn;
	private boolean mIsImmediateBack = false;
	private boolean mIsLeftBtnDisplay = true;
	private boolean mIsRightBtnDisplay = true;
	private int from;
	private String url;
	private String title;
	private Handler refreshProgressHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
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
					break;
				case 1:
					String jsMethod = (String) msg.obj;
					if (mWebView != null && !StringUtil.isEmpty(jsMethod)) {
						mWebView.loadUrl(jsMethod);
					}
					break;
			}
		}
	};

	@SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.act_webview);

		url = getIntent().getStringExtra(EXTRA_URL);
		//url = "file:///android_asset/test.html";
		title = getIntent().getStringExtra(EXTRA_TITLE);
		from = getIntent().getIntExtra(EXTRA_FROM, 0);
		if (title == null) {
			title = "";
		}
		setLeftTitle(title);
		mBtnTitleLeft.setOnClickListener(v -> {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			} else {
				onBackPressed();
			}
		});

		mHorizontalProgress = (ProgressBar) findViewById(R.id.progress_horizontal);
		mWebView = (WebView) findViewById(R.id.webview);
		// 设置支持JavaScript
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new IhujiaJavaScript(this).setIcallbackJS(this), "vjia");
		webSettings.setSupportZoom(true);
//		webSettings.setDatabaseEnabled(true);
//		String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
		webSettings.setGeolocationEnabled(true);
//		webSettings.setGeolocationDatabasePath(dir);
		webSettings.setDomStorageEnabled(true);
		webSettings.setUseWideViewPort(true);//图片调整到适合WebView的大小
		webSettings.setLoadWithOverviewMode(true);//缩放至屏幕大小

		//网页缓存
//		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				LogUtil.e("WebViewActivity", "url=" + url);
				if (url.contains("ihujia.com")) {
					mWebView.loadUrl(url);
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
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

	@Override
	protected void onResume() {
		super.onResume();
		mWebView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mWebView.onPause();
	}

	@Override
	public void callJS(String params) {
		//js接口都是在子线程中，webView.loadUrl方法必须在主线程中调用
		Message message = refreshProgressHandler.obtainMessage(1, params);
		refreshProgressHandler.sendMessage(message);
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
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void finish() {
		switch (from) {
			case FROM_PUSH:
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				break;
		}
		super.finish();
	}

	@Override
	public void onDestroy() {
		mWebView.destroy();
		super.onDestroy();
	}
}
