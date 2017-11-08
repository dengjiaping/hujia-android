package com.ihujia.hujia.custom.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.common.network.FProtocol;
import com.common.utils.BitmapUtil;
import com.common.utils.DeviceUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.utils.PermissionUtils;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by liuzhichao on 2017/2/23.
 * 自定义相机界面
 */
public class CustomCameraActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener {

	private static final int REQUEST_ACT_PICK_IMAGE = 10;
	private static final int REQUEST_NET_UPLOAD_AVATAR = 20;
	private static final int REQUEST_ACT_PHOTO_PREVIEW = 30;
	private static final int REQUEST_PERMISSION_TAKE_PHOTO = 40;
	private static final int REQUEST_PERMISSION_PICK_PHOTO = 50;

	@ViewInject(R.id.iv_camera_close)
	private View ivCameraClose;
	@ViewInject(R.id.iv_camera_switch)
	private View ivCameraSwitch;
	@ViewInject(R.id.iv_camera_take)
	private View ivCameraTake;
	@ViewInject(R.id.iv_camera_pick)
	private View ivCameraPick;
	@ViewInject(R.id.sv_camera_preview)
	private SurfaceView svCameraPreview;

	private SurfaceHolder holder;
	private Camera camera;
	private int screenWidth;
	private int screenHeight;
	private int cameraPosition;//1后置0前置
	private String filePath;

	public static void startCustomCameraActivity(Activity activity, int requestCode) {
		activity.startActivityForResult(new Intent(activity, CustomCameraActivity.class), requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.act_custom_camera);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		screenWidth = DeviceUtil.getWidth(this);
		screenHeight = DeviceUtil.getHeight(this);

		holder = svCameraPreview.getHolder();
		holder.addCallback(this);
		ivCameraClose.setOnClickListener(this);
		ivCameraSwitch.setOnClickListener(this);
		ivCameraTake.setOnClickListener(this);
		ivCameraPick.setOnClickListener(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		if (camera == null) {
			for (int camIdx =0;camIdx< Camera.getNumberOfCameras();camIdx++) {
				Camera.getCameraInfo(camIdx,cameraInfo);
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
					try {
						camera = Camera.open(camIdx);
						camera.setPreviewDisplay(holder);
						setCameraParams(screenWidth, screenHeight);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		camera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (camera != null) {
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_camera_close:
				finish();
				break;
			case R.id.iv_camera_switch:
				switchCamera();
				break;
			case R.id.iv_camera_take:
				if (PermissionUtils.isGetPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
					ivCameraTake.setClickable(false);
					takePhoto();
				} else {
					PermissionUtils.secondRequest(this, REQUEST_PERMISSION_TAKE_PHOTO, Manifest.permission.READ_EXTERNAL_STORAGE);
				}
				break;
			case R.id.iv_camera_pick:
				if (PermissionUtils.isGetPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
					Intent intent = new Intent(Intent.ACTION_PICK);
					intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(intent, REQUEST_ACT_PICK_IMAGE);
				} else {
					PermissionUtils.secondRequest(this, REQUEST_PERMISSION_PICK_PHOTO, Manifest.permission.READ_EXTERNAL_STORAGE);
				}
				break;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (grantResults.length > 0 && Manifest.permission.READ_EXTERNAL_STORAGE.equals(permissions[0]) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			if (REQUEST_PERMISSION_TAKE_PHOTO == requestCode) {
				ivCameraTake.setClickable(false);
				takePhoto();
			} else if (REQUEST_PERMISSION_PICK_PHOTO == requestCode) {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, REQUEST_ACT_PICK_IMAGE);
			}
		} else {
			ToastUtil.shortShow(this, getString(R.string.get_permission_failed));
		}
	}

	private void switchCamera() {
		int cameraCount;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

		for (int i = 0; i < cameraCount; i++) {
			Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
			if (cameraPosition == 1) {
				//现在是后置，变更为前置
				//cameraInfo.facing代表摄像头的方位，CAMERA_FACING_FRONT前置  CAMERA_FACING_BACK后置
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
					camera.stopPreview();//停掉原来摄像头的预览
					camera.release();//释放资源
					camera = null;//取消原来摄像头
					camera = Camera.open(i);//打开当前选中的摄像头
					try {
						camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
					} catch (IOException e) {
						e.printStackTrace();
					}
					setCameraParams(screenWidth, screenHeight);//重新设置参数
					camera.startPreview();//开始预览
					cameraPosition = 0;
					break;
				}
			} else {
				//现在是前置， 变更为后置
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
					camera.stopPreview();//停掉原来摄像头的预览
					camera.release();//释放资源
					camera = null;//取消原来摄像头
					camera = Camera.open(i);//打开当前选中的摄像头
					try {
						camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
					} catch (IOException e) {
						e.printStackTrace();
					}
					setCameraParams(screenWidth, screenHeight);//重新设置参数
					camera.startPreview();//开始预览
					cameraPosition = 1;
					break;
				}
			}
		}
	}

	private void setCameraParams(int width, int height) {
		Camera.Parameters parameters = camera.getParameters();
		// 获取摄像头支持的PictureSize列表
		List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();

		/*从列表中选取合适的分辨率*/
		Camera.Size picSize = getProperSize(pictureSizeList, ((float) height / width));
		if (null == picSize) {
			picSize = parameters.getPictureSize();
		}
		// 根据选出的PictureSize重新设置SurfaceView大小
		float w = picSize.width;
		float h = picSize.height;
		parameters.setPictureSize(picSize.width, picSize.height);
		svCameraPreview.setLayoutParams(new FrameLayout.LayoutParams((int) (height * (h / w)), height));

		// 获取摄像头支持的PreviewSize列表
		List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();

		Camera.Size preSize = getProperSize(previewSizeList, ((float) height) / width);
		if (null != preSize) {
			parameters.setPreviewSize(preSize.width, preSize.height);
		}

		parameters.setJpegQuality(100); // 设置照片质量
		if (parameters.getSupportedFocusModes().contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
			parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
		}

		camera.cancelAutoFocus();//自动对焦。
		camera.setDisplayOrientation(90);// 设置PreviewDisplay的方向，效果就是将捕获的画面旋转多少度显示
		camera.setParameters(parameters);
	}

	/**
	 * 从列表中选取合适的分辨率
	 * 默认w:h = 4:3
	 * <p>注意：这里的w对应屏幕的height
	 * h对应屏幕的width<p/>
	 */
	private Camera.Size getProperSize(List<Camera.Size> pictureSizeList, float screenRatio) {
		Camera.Size result = null;
		for (Camera.Size size : pictureSizeList) {
			float currentRatio = ((float) size.width) / size.height;
			if (currentRatio - screenRatio == 0) {
				result = size;
				break;
			}
		}

		if (null == result) {
			for (Camera.Size size : pictureSizeList) {
				float curRatio = ((float) size.width) / size.height;
				if (curRatio == 4f / 3) {// 默认w:h = 4:3
					result = size;
					break;
				}
			}
		}
		return result;
	}

	private void takePhoto() {
		camera.takePicture(null, null, (data, camera1) -> {
			//创建jpeg图片回调数据对象
			Bitmap bm = null;
			try {
				// 获得图片
				if (data.length > 10 * 1024 * 1024) {//图片大于10M取1/4
					bm = BitmapUtil.byteToBitmap(data, 4);
				} else if (data.length < 1024 * 1024) {//图片小于1M取原大小
					bm = BitmapUtil.byteToBitmap(data, 1);
				} else {//其他的取1/2
					bm = BitmapUtil.byteToBitmap(data, 2);
				}
				//压缩图片到1M以下
				bm = BitmapUtil.revitionImageSize(bm, 1000);

				//预览图片旋转了90度，保存图片同样要旋转90度
				//后置摄像头旋转90度，前置摄像头需要旋转270度
				//前置摄像头被水平翻转了，需要调整
				if (1 == cameraPosition) {
					bm = BitmapUtil.rotateBitmap(bm, 90);
				} else {
					bm = BitmapUtil.rotateBitmap(bm, 270);
					bm = BitmapUtil.flipBitmap(bm, false);
				}

				//保存图片,100%的质量
				filePath = BitmapUtil.saveBitmap(this, 100, bm);
				PhotoPreviewActivity.startPhotoPreviewActivity(this, REQUEST_ACT_PHOTO_PREVIEW, filePath);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bm != null) {
					bm.recycle();// 回收bitmap空间
				}
				camera.stopPreview();// 关闭预览
				camera.startPreview();// 开启预览
			}
		});
	}

	private void uploadCustomAvatar(String path) {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("head_portrait", BitmapUtil.bitmapToString(path, 350, 450));
		params.put(Constants.USERID, UserCenter.getUserId(this));
		requestHttpData(Constants.Urls.URL_POST_CUSTOM_UPDATE_AVATAR, REQUEST_NET_UPLOAD_AVATAR, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			if (REQUEST_NET_UPLOAD_AVATAR == requestCode) {
				setResult(RESULT_OK, new Intent().putExtra("picUrl", Parsers.getAvatarUrl(data)));
				finish();
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (REQUEST_ACT_PHOTO_PREVIEW == requestCode) {
			switch (resultCode) {
				case RESULT_OK:
					checkPhotoFile(filePath);
					break;
				case RESULT_CANCELED:
					ivCameraTake.setClickable(true);
					break;
				case PhotoPreviewActivity.RESULT_CLOSE:
					finish();
					break;
			}
		} else if (REQUEST_ACT_PICK_IMAGE == requestCode) {
			if (data != null) {
				Uri uri = data.getData();
				if (uri != null) {
					filePath = getRealPathFromURI(uri);
					checkPhotoFile(filePath);
				}
			}
		}
	}

	private void checkPhotoFile(String filePath) {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if (file.exists()) {
				uploadCustomAvatar(filePath);
			} else {
				ToastUtil.shortShow(this, getString(R.string.custom_picture_not_found));
			}
		}
	}

	private String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = {MediaStore.Images.Media.DATA};
			Cursor cursor = managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

	@Override
	public void onDestroy() {
		surfaceDestroyed(holder);
		super.onDestroy();
	}
}
