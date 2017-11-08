package com.ihujia.hujia.otherstore.jsinterface;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.common.utils.LogUtil;
import com.common.utils.StringUtil;
import com.ihujia.hujia.home.controller.ProductDetailActivity;
import com.ihujia.hujia.login.controller.LoginActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.store.controller.ProductActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liuzhichao on 2017/4/18.
 * 活动和店铺装修JS交互接口
 */
public class IhujiaJavaScript {

	private static final String TAG = IhujiaJavaScript.class.getSimpleName();

	private Context context;
	private String storeId;
	private IcallbackJS icallbackJS;

	public IhujiaJavaScript(Context context) {
		this.context = context;
	}

	public IhujiaJavaScript setStoreId(String storeId) {
		this.storeId = storeId;
		return this;
	}

	public IhujiaJavaScript setIcallbackJS(IcallbackJS icallbackJS) {
		this.icallbackJS = icallbackJS;
		return this;
	}

	@JavascriptInterface
	public void open(String params) {
		if (StringUtil.isEmpty(params)) {
			return;
		}
		try {
			JSONObject jsonObject = new JSONObject(params);
			String type = jsonObject.optString("type");
			switch (type) {
				case "goodsdetail":{
					String goodsId = jsonObject.optString("goods_id");
					ProductDetailActivity.startProductDetailActivity(context, goodsId);
					break;
				}
				case "goodslist":
					ProductActivity.startProductActivity(context, ProductActivity.FROM_STORE_DETAIL, storeId);
					break;
				case "coupon":{
					if (UserCenter.isLogin(context)) {
						String couponId = jsonObject.optString("coupon_id");
						String title = jsonObject.optString("title");
						String desc = jsonObject.optString("desc");
					} else {
						LoginActivity.startLoginActivity(context);
					}
					break;
				}
				case "getUserId":{
					if (UserCenter.isLogin(context)) {
						if (icallbackJS != null) {
							icallbackJS.callJS("javascript:setUserId('" + UserCenter.getUserId(context) + "')");
						} else {
							LogUtil.e(TAG, "未设置JS回调接口");
						}
					} else {
						LoginActivity.startLoginActivity(context);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
