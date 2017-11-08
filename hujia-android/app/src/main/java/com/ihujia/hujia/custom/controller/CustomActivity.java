package com.ihujia.hujia.custom.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.ShopCarNumEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.controller.MyShopCarActivity;
import com.ihujia.hujia.utils.PermissionUtils;
import com.ihujia.hujia.utils.ViewInjectUtils;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.IdentityHashMap;


/**
 * Created by liuzhichao on 2017/2/25.
 * 定制Activity
 */
public class CustomActivity extends BaseActivity {

	public static final String EXTRA_URL = "extra_url";
	private static final int REQUEST_ACT_PHOTO = 10;
	private static final int REQUEST_NET_ADD_TO_CART = 30;
	private static final int REQUEST_NET_CART_NUM = 20;

	@ViewInject(R.id.custom_progress_horizontal)
	private ProgressBar mHorizontalProgress;
	@ViewInject(R.id.custom_webview)
	private WebView mWebView;
	@ViewInject(R.id.left_button)
	private View leftButton;
	@ViewInject(R.id.right_layout)
	private View rightLayout;
	@ViewInject(R.id.right_button)
	private View rightButton;
	@ViewInject(R.id.right_text)
	private TextView rightText;

	private String url;
	private Handler refreshProgressHandler = new Handler() {
		@Override
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
					String show = (String) msg.obj;
					if ("1".equals(show)) {
						leftButton.setVisibility(View.VISIBLE);
						rightLayout.setVisibility(View.VISIBLE);
					} else {
						leftButton.setVisibility(View.INVISIBLE);
						rightLayout.setVisibility(View.INVISIBLE);
					}
					break;
			}
		}
	};

	public static void startCustomActivity(Context context, String url) {
		Intent intent = new Intent(context, CustomActivity.class);
		intent.putExtra(EXTRA_URL, url);
		context.startActivity(intent);
	}

	@SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_custom);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		leftButton.setOnClickListener(v -> finish());
		rightButton.setOnClickListener(v -> startActivity(new Intent(CustomActivity.this, MyShopCarActivity.class)));
		url = getIntent().getStringExtra(EXTRA_URL);

		WebSettings webSetting = mWebView.getSettings();
		webSetting.setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new CustomActivity.IhujiaJavaScript(), "wjika");
		webSetting.setSupportZoom(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
		webSetting.setUseWideViewPort(true);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setSupportMultipleWindows(false);
		webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
		//H5缓存
//		webSetting.setAppCacheEnabled(true);
//		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
//		webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
		//网页缓存
//		mWebView.clearCache(true);//清除缓存
		webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);

		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		// webSetting.setPreFectch(true);

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
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
			public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissionsCallback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}

			@Override
			public boolean onJsConfirm(WebView webView, String s, String s1, JsResult jsResult) {
				return super.onJsConfirm(webView, s, s1, jsResult);
			}

			View myVideoView;
			View myNormalView;
			IX5WebChromeClient.CustomViewCallback callback;

			/**
			 * 全屏播放配置
			 */
			@Override
			public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
				super.onShowCustomView(view, customViewCallback);
			}

			@Override
			public void onHideCustomView() {
				if (callback != null) {
					callback.onCustomViewHidden();
					callback = null;
				}
				if (myVideoView != null) {
					ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
					viewGroup.removeView(myVideoView);
					viewGroup.addView(myNormalView);
				}
			}

			@Override
			public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
				return super.onJsAlert(null, s, s1, jsResult);
			}
		});
		mWebView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		});
		mWebView.loadUrl("http://static.ihujia.com/custom/dist/?user_id=" + UserCenter.getUserId(this) + "#/");
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().sync();
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}

	private void loadData() {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("user_id", UserCenter.getUserId(this));
		requestHttpData(Constants.Urls.URL_POST_GET_SHOPCAR_NUM, REQUEST_NET_CART_NUM, FProtocol.HttpMethod.POST, params);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent == null || mWebView == null || intent.getData() == null)
			return;
		mWebView.loadUrl(intent.getData().toString());
	}

	@Override
	public void onDestroy() {
		if (refreshProgressHandler != null)
			refreshProgressHandler.removeCallbacksAndMessages(null);
		if (mWebView != null)
			mWebView.destroy();
		super.onDestroy();
	}

	private class IhujiaJavaScript {

		@JavascriptInterface
		public void showTitle(String params) {
			if (StringUtil.isEmpty(params)) {
				return;
			}
			try {
				JSONObject jsonObject = new JSONObject(params);
				Message message = refreshProgressHandler.obtainMessage(1, jsonObject.optString("show"));
				refreshProgressHandler.sendMessage(message);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@JavascriptInterface
		public void addToCart(String params) {
			if (StringUtil.isEmpty(params)) {
				return;
			}
			try {
				JSONObject jsonObject = new JSONObject(params);
				jsonObject.optString("type");
				String id = jsonObject.optString("goods_id");
				String skuId = jsonObject.optString("sku_id");
				CustomActivity.this.addToCart(id, skuId);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@JavascriptInterface
		public void showCamera(String params) {
			if (StringUtil.isEmpty(params)) {
				return;
			}
			try {
				JSONObject jsonObject = new JSONObject(params);
				int show = jsonObject.optInt("show");
				if (1 == show) {
					if (PermissionUtils.isGetPermission(CustomActivity.this, Manifest.permission.CAMERA)) {
						CustomCameraActivity.startCustomCameraActivity(CustomActivity.this, REQUEST_ACT_PHOTO);
					} else {
						PermissionUtils.secondRequest(CustomActivity.this, REQUEST_PERMISSION_CODE, Manifest.permission.CAMERA);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (REQUEST_PERMISSION_CODE == requestCode) {
			if (grantResults.length > 0 && Manifest.permission.CAMERA.equals(permissions[0]) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				CustomCameraActivity.startCustomCameraActivity(CustomActivity.this, REQUEST_ACT_PHOTO);
			} else {
				ToastUtil.shortShow(this, getString(R.string.get_permission_failed));
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (REQUEST_ACT_PHOTO == requestCode && RESULT_OK == resultCode && data != null) {
			String picUrl = data.getStringExtra("picUrl");
			//java调用js方法
			mWebView.loadUrl("javascript:pushPhoto('" + picUrl + "')");
		}
	}

	private void addToCart(String id, String skuId) {
		IdentityHashMap<String, String> param = new IdentityHashMap<>();
		param.put("user_id", UserCenter.getUserId(CustomActivity.this));
		param.put("goods_id", id);
		param.put("sku_id", skuId);
		param.put("goods_count", "1");
		requestHttpData(Constants.Urls.URL_POST_PRODUCT_ADD_SHOPCAR, REQUEST_NET_ADD_TO_CART, FProtocol.HttpMethod.POST, param);
	}

	@Override
	public void success(int requestCode, String data) {
		Entity result = Parsers.getResult(data);
		switch (requestCode) {
			case REQUEST_NET_CART_NUM:
				ShopCarNumEntity shopCarNumEntity = Parsers.getShopCarNum(data);
				if (REQUEST_NET_SUCCESS.equals(shopCarNumEntity.getResultCode())) {
					String shopCarNum = shopCarNumEntity.getShopcarNum();
					if (StringUtil.isEmpty(shopCarNum) || "0".equals(shopCarNum)) {
						rightText.setVisibility(View.GONE);
					} else {
						rightText.setVisibility(View.VISIBLE);
						rightText.setText(shopCarNum);
					}
				} else {
					ToastUtil.shortShow(this, shopCarNumEntity.getResultDesc());
				}
				break;
			case REQUEST_NET_ADD_TO_CART:
				if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
					ToastUtil.shortShow(this, getString(R.string.custom_add_shop_car_success));
					loadData();
				} else {
					ToastUtil.shortShow(this, result.getResultMsg());
				}
				break;
		}
	}
}
