package com.ihujia.hujia.login.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.AnalysisUtil;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.UserEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.utils.InputMethodUtil;
import com.ihujia.hujia.utils.InputUtil;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.IdentityHashMap;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by liuzhichao on 2016/12/16.
 * 登录
 */
public class LoginActivity extends ToolBarActivity implements View.OnClickListener {

	private static final int REQUEST_NET_GET_CODE = 10;
	private static final int REQUEST_NET_LOGIN = 20;

	@ViewInject(R.id.et_login_phone)
	private EditText etLoginPhone;
	@ViewInject(R.id.et_login_verification_code)
	private EditText etLoginVerificationCode;
	@ViewInject(R.id.tv_login_get_code)
	private TextView tvLoginGetCode;
	@ViewInject(R.id.tv_login_confirm)
	private View tvLoginConfirm;

	private CountDownTimer countDownTimer;

	public static void startLoginActivity(Context context) {
		context.startActivity(new Intent(context, LoginActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		mBtnTitleLeft.setVisibility(View.GONE);
		mTxtLeft.setText(getString(R.string.ignore));
		mTxtLeft.setOnClickListener(this);
		mTxtLeft.setVisibility(View.VISIBLE);
		toolbarTitleCenter.setText(getString(R.string.login_text));
		toolbarTitleCenter.setVisibility(View.VISIBLE);
		InputUtil.editIsEmpty(tvLoginGetCode, etLoginPhone);
		InputUtil.editIsEmpty(tvLoginConfirm, etLoginPhone, etLoginVerificationCode);

		InputMethodUtil.showInput(this,etLoginPhone);

		countDownTimer = new CountDownTimer(60000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				tvLoginGetCode.setEnabled(false);
				tvLoginGetCode.setText(String.format(Locale.CHINA, getString(R.string.login_count_down_text), millisUntilFinished / 1000));
			}

			@Override
			public void onFinish() {
				tvLoginGetCode.setEnabled(true);
				tvLoginGetCode.setText(getString(R.string.login_get_verify_code));
				cancel();
			}
		};

		tvLoginGetCode.setOnClickListener(this);
		tvLoginConfirm.setOnClickListener(this);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case REQUEST_NET_GET_CODE:
					ToastUtil.shortShow(this, getString(R.string.login_sms_send_success));
					break;
				case REQUEST_NET_LOGIN:
					closeProgressDialog();
					UserEntity user = Parsers.getUser(data);
					if (user != null) {
						UserCenter.saveUserInfo(this, user);
						finish();
					} else {
						ToastUtil.shortShow(this, getString(R.string.login_failed));
					}
					break;
			}
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		ToastUtil.shortShow(this, errorMessage);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.left_text:
				finish();
				break;
			case R.id.tv_login_get_code:{
				String phone = etLoginPhone.getText().toString().trim();
				if (checkPhone(phone)) return;
				IdentityHashMap<String, String> params = new IdentityHashMap<>();
				params.put("cellphone_num", phone);
				requestHttpData(Constants.Urls.URL_POST_IDENTIFY_CODE, REQUEST_NET_GET_CODE, FProtocol.HttpMethod.POST, params);
				countDownTimer.start();
				break;
			}
			case R.id.tv_login_confirm:{
				AnalysisUtil.onEvent(this, "denglu");
				String phoneNum = etLoginPhone.getText().toString().trim();
				String pwd = etLoginVerificationCode.getText().toString().trim();
				if (checkPhoneAndPassword(phoneNum, pwd)) return;
				IdentityHashMap<String, String> params = new IdentityHashMap<>();
				params.put("login_name", phoneNum);
				params.put("password", pwd);
				params.put("token", JPushInterface.getRegistrationID(this));//极光推送Id
				showProgressDialog();
				requestHttpData(Constants.Urls.URL_POST_LOGIN, REQUEST_NET_LOGIN, FProtocol.HttpMethod.POST, params);
				break;
			}
		}
	}

	private boolean checkPhone(String phone) {
		if (StringUtil.isEmpty(phone)) {
			ToastUtil.shortShow(this, getString(R.string.login_phone_num_empty));
			return true;
		}
		if (phone.length() != 11) {
			ToastUtil.shortShow(this, getString(R.string.login_input_phone_num));
			return true;
		}
		if (!phone.startsWith("1")) {
			ToastUtil.shortShow(this, getString(R.string.login_input_real_phone_num));
			return true;
		}
		return false;
	}

	private boolean checkPhoneAndPassword(String phone, String password) {
		if (checkPhone(phone)) return true;
		if (StringUtil.isEmpty(password)) {
			return true;
		}
		return false;
	}

	@Override
	public void onDestroy() {
		if (countDownTimer != null) {
			countDownTimer.cancel();
			countDownTimer = null;
		}
		super.onDestroy();
	}
}
