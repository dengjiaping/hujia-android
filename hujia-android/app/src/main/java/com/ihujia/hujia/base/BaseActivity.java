package com.ihujia.hujia.base;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.interfaces.IActivityHelper;
import com.common.network.FProtocol;
import com.common.ui.FBaseActivity;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.utils.VersionInfoUtils;
import com.ihujia.hujia.MainApplication;
import com.ihujia.hujia.R;
import com.ihujia.hujia.main.controller.MainActivity;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.UpdateEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.update.controller.UpdateActiviy;
import com.ihujia.hujia.utils.CommonTools;
import com.ihujia.hujia.utils.ExitManager;
import com.ihujia.hujia.utils.InputMethodUtil;

import java.util.IdentityHashMap;

import cn.jpush.android.api.JPushInterface;

/**
 * @author songxudong
 */
public class BaseActivity extends FBaseActivity implements IActivityHelper {

	public static final String EXTRA_FROM = "extra_from";
	public static final String EXTRA_ID = "extra_id";
	public static final int REQUEST_UPDATE_VERSION_CODE = -3;
	public static final String REQUEST_NET_SUCCESS = "0";//网络请求成功
	public static final int REQUEST_PERMISSION_CODE = 0x1;

	protected Resources res;
	protected View mLayoutLoading;
	protected ImageView mImgLoading;
	protected TextView mTxtCardEmpty;
	protected TextView mTxtLoadingEmpty;
	protected TextView mTxtLoadingRetry;
	protected ImageView mImgLoadingRetry;
	protected ImageView mImgLoadingEmpty;
	private AlertDialog wjkAlertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitManager.instance.addActivity(this);
		res = getResources();
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	public void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method, IdentityHashMap<String, String> postParameters) {
		if (postParameters == null) {
			postParameters = new IdentityHashMap<>();
		}
		postParameters.put("channel", MainApplication.getAppMetaData(this, MainApplication.CHANNEL_KEY));
		super.requestHttpData(path, requestCode, method, postParameters);
	}

	/**
	 * 检查更新
	 */
	protected void checkUpdateVersion() {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("version_code", String.valueOf(VersionInfoUtils.getVersionCode(this)));
		requestHttpData(Constants.Urls.URL_POST_CHECK_UPDATE, REQUEST_UPDATE_VERSION_CODE, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		if (REQUEST_UPDATE_VERSION_CODE == requestCode) {
			closeProgressDialog();
			UpdateEntity update = Parsers.getUpdate(data);
			if (update != null) {
				if (REQUEST_NET_SUCCESS.equals(update.getResultCode())) {
					String version = update.getNewVersion() == null ? "" : update.getNewVersion();
					String url = update.getDownloadUrl() == null ? "" : update.getDownloadUrl();
					update.setType(0);
					UpdateActiviy.startUpdateActiviy(this, version, url, update.getType());
				} else if ("10101015".equals(update.getResultCode())) {
					if (!(this instanceof MainActivity)) {
						ToastUtil.shortShow(this, update.getResultMsg());
					}
				} else {
					ToastUtil.shortShow(this, update.getResultMsg());
				}
			} else {
				ToastUtil.shortShow(this, "检查更新失败");
			}
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		if (REQUEST_UPDATE_VERSION_CODE == requestCode) {
			closeProgressDialog();
		}
		ToastUtil.shortShow(this, errorMessage);
	}

	protected void showAlertDialog(String title,
	                               @NonNull String message,
	                               boolean cancelable,
	                               @NonNull String okText,
	                               @NonNull View.OnClickListener onOkListener) {
		View view = LayoutInflater.from(this).inflate(R.layout.wjk_alert_dialog, null);
		wjkAlertDialog = new AlertDialog.Builder(this).setView(view).create();
		wjkAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		TextView txtTitle = (TextView) view.findViewById(R.id.alert_title);
		TextView txtMessage = (TextView) view.findViewById(R.id.alert_message);
		TextView btnOne = (TextView) view.findViewById(R.id.alert_btn_one);
		View llBtn = view.findViewById(R.id.alert_btn_ll);
		if (StringUtil.isEmpty(title)) {
			txtTitle.setVisibility(View.GONE);
		} else {
			txtTitle.setText(title);
		}
		txtMessage.setText(message);
		llBtn.setVisibility(View.GONE);
		btnOne.setVisibility(View.VISIBLE);
		btnOne.setText(okText);
		btnOne.setOnClickListener(onOkListener);
		wjkAlertDialog.setCancelable(cancelable);
		wjkAlertDialog.show();
		setAlertDialogWidth();

	}

	protected void showAlertDialog(String title,
	                               @NonNull String message,
	                               @NonNull String cancelText,
	                               @NonNull String okText,
	                               @NonNull View.OnClickListener onCancelListener,
	                               @NonNull View.OnClickListener onOkListener) {
		showAlertDialog(title, message, true, cancelText, okText, onCancelListener, onOkListener);
	}

	protected void showAlertDialog(String title,
	                               @NonNull String message,
	                               boolean cancelable,
	                               @NonNull String cancelText,
	                               @NonNull String okText,
	                               @NonNull View.OnClickListener onCancelListener,
	                               @NonNull View.OnClickListener onOkListener) {
		View view = LayoutInflater.from(this).inflate(R.layout.wjk_alert_dialog, null);
		wjkAlertDialog = new AlertDialog.Builder(this).setView(view).create();
		wjkAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		TextView txtTitle = (TextView) view.findViewById(R.id.alert_title);
		TextView txtMessage = (TextView) view.findViewById(R.id.alert_message);
		TextView btnCancel = (TextView) view.findViewById(R.id.alert_btn_cancel);
		TextView btnOk = (TextView) view.findViewById(R.id.alert_btn_ok);
		if (StringUtil.isEmpty(title)) {
			txtTitle.setVisibility(View.GONE);
		} else {
			txtTitle.setText(title);
		}
		txtMessage.setText(message);
		if (StringUtil.isEmpty(cancelText)) {
			btnCancel.setVisibility(View.GONE);
		} else {
			btnCancel.setText(cancelText);
			btnCancel.setOnClickListener(onCancelListener);
		}
		btnOk.setText(okText);
		btnOk.setOnClickListener(onOkListener);
		wjkAlertDialog.setCancelable(cancelable);
		wjkAlertDialog.show();
		setAlertDialogWidth();
	}

	protected void showAlertDialog(String title,
	                               @NonNull String message,
	                               @NonNull String cancelText,
	                               @NonNull String okText,
	                               @NonNull View.OnClickListener onCancelListener,
	                               @NonNull final AlertEtClickListenner onOkListener) {
		View view = LayoutInflater.from(this).inflate(R.layout.wjk_alert_dialog, null);
		wjkAlertDialog = new AlertDialog.Builder(this).setView(view).create();
		wjkAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		TextView txtTitle = (TextView) view.findViewById(R.id.alert_title);
		TextView txtMessage = (TextView) view.findViewById(R.id.alert_message);
		TextView btnCancel = (TextView) view.findViewById(R.id.alert_btn_cancel);
		TextView btnOk = (TextView) view.findViewById(R.id.alert_btn_ok);
		TextView tvEditTextTitle = (TextView) view.findViewById(R.id.alert_edittext_title);
		View llAlertEditText = view.findViewById(R.id.alert_edittext_ll);
		final EditText etAlert = (EditText) view.findViewById(R.id.alert_et);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodUtil.showInput(BaseActivity.this, etAlert);
			}
		}, 100);

		if (StringUtil.isEmpty(title)) {
			txtTitle.setVisibility(View.GONE);
		} else {
			txtTitle.setText(title);
		}
		txtMessage.setVisibility(View.GONE);
		llAlertEditText.setVisibility(View.VISIBLE);

		tvEditTextTitle.setText(message);

		if (StringUtil.isEmpty(cancelText)) {
			btnCancel.setVisibility(View.GONE);
		} else {
			btnCancel.setText(cancelText);
			btnCancel.setOnClickListener(onCancelListener);
		}

		btnOk.setText(okText);
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onOkListener.onClick(v, etAlert);
			}
		});
		wjkAlertDialog.show();
		wjkAlertDialog.setCancelable(true);
		setAlertDialogWidth();
	}

	public interface AlertEtClickListenner {
		void onClick(View v, EditText editText);
	}

	protected void showAlertDialog(String title,
	                               @NonNull String message,
	                               @NonNull String okText,
	                               @NonNull View.OnClickListener onOkListener) {
		View view = LayoutInflater.from(this).inflate(R.layout.wjk_alert_dialog, null);
		wjkAlertDialog = new AlertDialog.Builder(this).setView(view).create();
		wjkAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		TextView txtTitle = (TextView) view.findViewById(R.id.alert_title);
		TextView txtMessage = (TextView) view.findViewById(R.id.alert_message);
		TextView btnOne = (TextView) view.findViewById(R.id.alert_btn_one);
		View llBtn = view.findViewById(R.id.alert_btn_ll);
		if (StringUtil.isEmpty(title)) {
			txtTitle.setVisibility(View.GONE);
		} else {
			txtTitle.setText(title);
		}
		txtMessage.setText(message);
		llBtn.setVisibility(View.GONE);
		btnOne.setVisibility(View.VISIBLE);
		btnOne.setText(okText);
		btnOne.setOnClickListener(onOkListener);
		wjkAlertDialog.show();
		setAlertDialogWidth();
	}

	private void setAlertDialogWidth() {
		WindowManager.LayoutParams params = wjkAlertDialog.getWindow().getAttributes();
		params.width = CommonTools.dp2px(this, 270f);
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		wjkAlertDialog.getWindow().setAttributes(params);
	}

	protected void showAlertDialog(String title,
	                               @NonNull SpannableStringBuilder message,
	                               @NonNull String okText,
	                               @NonNull View.OnClickListener onOkListener) {
		View view = LayoutInflater.from(this).inflate(R.layout.wjk_alert_dialog, null);
		wjkAlertDialog = new AlertDialog.Builder(this).setView(view).create();
		wjkAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		TextView txtTitle = (TextView) view.findViewById(R.id.alert_title);
		TextView txtMessage = (TextView) view.findViewById(R.id.alert_message);
		TextView btnOne = (TextView) view.findViewById(R.id.alert_btn_one);
		View llBtn = view.findViewById(R.id.alert_btn_ll);

		if (StringUtil.isEmpty(title)) {
			txtTitle.setVisibility(View.GONE);
		} else {
			txtTitle.setText(title);
		}
		txtMessage.setText(message);
		llBtn.setVisibility(View.GONE);
		btnOne.setVisibility(View.VISIBLE);
		btnOne.setText(okText);
		btnOne.setOnClickListener(onOkListener);
		wjkAlertDialog.show();
		setAlertDialogWidth();
	}

	protected void closeDialog() {
		if (wjkAlertDialog != null) {
			wjkAlertDialog.dismiss();
		}
	}

	protected void initLoadingView(View.OnClickListener listener) {
		mLayoutLoading = findViewById(R.id.loading_layout);
		mImgLoading = (ImageView) findViewById(R.id.loading_img_anim);
		mTxtLoadingEmpty = (TextView) findViewById(R.id.loading_txt_empty);
		mTxtLoadingRetry = (TextView) findViewById(R.id.loading_txt_retry);
		mImgLoadingRetry = (ImageView) findViewById(R.id.loading_img_refresh);
		mImgLoadingEmpty = (ImageView) findViewById(R.id.loading_img_empty);
		mTxtCardEmpty = (TextView) findViewById(R.id.loading_btn_card_empty);

		Animation operatingAnim = AnimationUtils.loadAnimation(this, com.common.R.anim.load_operate);
		LinearInterpolator linearInterpolator = new LinearInterpolator();
		operatingAnim.setInterpolator(linearInterpolator);
		if (operatingAnim != null) {
			mImgLoading.startAnimation(operatingAnim);
		}

		if (mTxtCardEmpty != null) {
			mTxtCardEmpty.setOnClickListener(listener);
			mTxtCardEmpty.setClickable(false);
		}
		if (mLayoutLoading != null) {
			mLayoutLoading.setOnClickListener(listener);
			mLayoutLoading.setClickable(false);
		}
	}

	protected void setLoadingStatus(LoadingStatus status) {
		if (mLayoutLoading == null || mImgLoading == null || mImgLoadingEmpty == null
				|| mImgLoadingRetry == null || mTxtLoadingEmpty == null || mTxtLoadingRetry == null) {
			return;
		}
		switch (status) {
			case LOADING: {
				mLayoutLoading.setClickable(false);
				mLayoutLoading.setVisibility(View.VISIBLE);
				mImgLoading.setVisibility(View.VISIBLE);
				mImgLoadingEmpty.setVisibility(View.GONE);
				mImgLoadingRetry.setVisibility(View.GONE);
				mTxtLoadingEmpty.setVisibility(View.GONE);
				mTxtLoadingRetry.setVisibility(View.GONE);
				mTxtCardEmpty.setVisibility(View.GONE);
				mTxtCardEmpty.setClickable(false);
				break;
			}
			case EMPTY: {
				mTxtCardEmpty.setClickable(false);
				mLayoutLoading.setClickable(false);
				mLayoutLoading.setVisibility(View.VISIBLE);
				mImgLoading.setVisibility(View.GONE);
				mImgLoading.clearAnimation();
				mImgLoadingEmpty.setVisibility(View.VISIBLE);
				mTxtLoadingEmpty.setVisibility(View.VISIBLE);
				mImgLoadingRetry.setVisibility(View.GONE);
				mTxtLoadingRetry.setVisibility(View.GONE);
				mTxtCardEmpty.setVisibility(View.GONE);
				break;
			}
			case RETRY: {
				mTxtCardEmpty.setClickable(false);
				mLayoutLoading.setClickable(true);
				mLayoutLoading.setVisibility(View.VISIBLE);
				mImgLoading.setVisibility(View.GONE);
				mImgLoading.clearAnimation();
				mImgLoadingEmpty.setVisibility(View.GONE);
				mTxtLoadingEmpty.setVisibility(View.GONE);
				mImgLoadingRetry.setVisibility(View.VISIBLE);
				mTxtLoadingRetry.setVisibility(View.VISIBLE);
				mTxtCardEmpty.setVisibility(View.GONE);
				break;
			}
			case GONE: {
				mTxtCardEmpty.setClickable(false);
				mLayoutLoading.setClickable(false);
				mLayoutLoading.setVisibility(View.GONE);
				mImgLoading.setVisibility(View.GONE);
				mImgLoading.clearAnimation();
				mTxtLoadingEmpty.setVisibility(View.GONE);
				mTxtLoadingRetry.setVisibility(View.GONE);
				mImgLoadingEmpty.setVisibility(View.GONE);
				mImgLoadingRetry.setVisibility(View.GONE);
				mTxtCardEmpty.setVisibility(View.GONE);
				break;
			}
		}
	}

	public enum LoadingStatus {
		LOADING,
		EMPTY,
		RETRY,
		GONE
	}

	@Override
	public void onDestroy() {
		ExitManager.instance.remove(this);
		super.onDestroy();
	}
}
