package com.ihujia.hujia.custom.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.common.utils.DeviceUtil;
import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/2/23.
 * 自定义相机预览界面
 */
public class PhotoPreviewActivity extends BaseActivity implements View.OnClickListener {

	public static final int RESULT_CLOSE = 1;
	private static final String EXTRA_PHOTO_PATH = "extra_photo_path";

	@ViewInject(R.id.iv_photo_close)
	private View ivPhotoClose;
	@ViewInject(R.id.sdv_photo_content)
	private SimpleDraweeView sdvPhotoContent;
	@ViewInject(R.id.iv_photo_cancel)
	private View ivPhotoCancel;
	@ViewInject(R.id.iv_photo_confirm)
	private View ivPhotoConfirm;

	public static void startPhotoPreviewActivity(Activity activity, int requestCode, String photoPath) {
		Intent intent = new Intent(activity, PhotoPreviewActivity.class);
		intent.putExtra(EXTRA_PHOTO_PATH, photoPath);
		activity.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_photo_preview);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		String photoPath = getIntent().getStringExtra(EXTRA_PHOTO_PATH);
		if (!TextUtils.isEmpty(photoPath)) {
			photoPath = "file://" + photoPath;
			DraweeController controller = Fresco.newDraweeControllerBuilder()
					.setImageRequest(ImageRequestBuilder.newBuilderWithSource(Uri.parse(photoPath))
							.setResizeOptions(new ResizeOptions(DeviceUtil.getWidth(this), DeviceUtil.getHeight(this)))
							.build())
					.build();
			sdvPhotoContent.setController(controller);
		}
		ivPhotoClose.setOnClickListener(this);
		ivPhotoCancel.setOnClickListener(this);
		ivPhotoConfirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_photo_close:
				setResult(RESULT_CLOSE);
				finish();
				break;
			case R.id.iv_photo_cancel:
				setResult(Activity.RESULT_CANCELED);
				finish();
				break;
			case R.id.iv_photo_confirm:
				setResult(Activity.RESULT_OK);
				finish();
				break;
		}
	}
}
