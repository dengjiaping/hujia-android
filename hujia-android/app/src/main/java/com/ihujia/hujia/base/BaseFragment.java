package com.ihujia.hujia.base;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.interfaces.IActivityHelper;
import com.common.network.FProtocol;
import com.common.ui.FBaseFragment;
import com.common.utils.StringUtil;
import com.ihujia.hujia.MainApplication;
import com.ihujia.hujia.R;
import com.ihujia.hujia.utils.CommonTools;

import java.util.IdentityHashMap;

public class BaseFragment extends FBaseFragment implements IActivityHelper {

	protected View mLayoutLoading;
	protected ImageView mImgLoading;
	protected TextView mTxtCardEmpty;
	protected TextView mTxtLoadingEmpty;
	protected TextView mTxtLoadingRetry;
	protected ImageView mImgLoadingRetry;
	protected ImageView mImgLoadingEmpty;
	private AlertDialog wjkAlertDialog;

	private String baseTitle = "";

	public String getTitle() {
		return baseTitle;
	}

	public void setTitle(int titleId) {
		baseTitle = getString(titleId);
	}

	public void setTitle(String title) {
		baseTitle = title;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("baseTitle", baseTitle);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			baseTitle = savedInstanceState.getString("baseTitle", getTitle());
		}
	}

	@Override
	public void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method, IdentityHashMap<String, String> postParameters) {
		if (postParameters == null) {
			postParameters = new IdentityHashMap<>();
		}
		postParameters.put("channel", MainApplication.getAppMetaData(getActivity(), MainApplication.CHANNEL_KEY));
		super.requestHttpData(path, requestCode, method, postParameters);
	}

	protected void initLoadingView(View.OnClickListener listener, View parentView) {
		mLayoutLoading = parentView.findViewById(R.id.loading_layout);
		mImgLoading = (ImageView) parentView.findViewById(R.id.loading_img_anim);
		mTxtLoadingEmpty = (TextView) parentView.findViewById(R.id.loading_txt_empty);
		mTxtLoadingRetry = (TextView) parentView.findViewById(R.id.loading_txt_retry);
		mImgLoadingRetry = (ImageView) parentView.findViewById(R.id.loading_img_refresh);
		mImgLoadingEmpty = (ImageView) parentView.findViewById(R.id.loading_img_empty);
		mTxtCardEmpty = (TextView) parentView.findViewById(R.id.loading_btn_card_empty);

		Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), com.common.R.anim.load_operate);
		LinearInterpolator linearInterpolator = new LinearInterpolator();
		operatingAnim.setInterpolator(linearInterpolator);
		if (operatingAnim!=null) {
			mImgLoading.startAnimation(operatingAnim);
		}

		mTxtCardEmpty.setOnClickListener(listener);
		mLayoutLoading.setOnClickListener(listener);
		mLayoutLoading.setClickable(false);
		mTxtCardEmpty.setClickable(false);
	}

	protected void showAlertDialog(String title,
	                               @NonNull String message,
	                               boolean cancelable,
	                               @NonNull String cancelText,
	                               @NonNull String okText,
	                               @NonNull View.OnClickListener onCancelListener,
	                               @NonNull View.OnClickListener onOkListener) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.wjk_alert_dialog, null);
		wjkAlertDialog = new AlertDialog.Builder(getActivity()).setView(view).create();
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

	private void setAlertDialogWidth() {
		WindowManager.LayoutParams params = wjkAlertDialog.getWindow().getAttributes();
		params.width = CommonTools.dp2px(getActivity(), 270f);
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		wjkAlertDialog.getWindow().setAttributes(params);
	}

	protected void closeDialog() {
		if (wjkAlertDialog != null) {
			wjkAlertDialog.dismiss();
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

	public static enum LoadingStatus {
		INIT,
		LOADING,
		EMPTY,
		RETRY,
		GONE
	}
}
