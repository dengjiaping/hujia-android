package com.ihujia.hujia.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.common.utils.ToastUtil;
import com.ihujia.hujia.common.CommonConfig;
import com.ihujia.hujia.home.utils.PayUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, CommonConfig.WECHAT_APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
			case 0://成功支付
				PayUtils.gotoPaySuccess(this);
				break;
			case -1://支付失败
				ToastUtil.shortShow(this, "支付失败");
				break;
			case -2://取消支付
				ToastUtil.shortShow(this, "取消支付");
				break;
		}
		finish();
	}
}