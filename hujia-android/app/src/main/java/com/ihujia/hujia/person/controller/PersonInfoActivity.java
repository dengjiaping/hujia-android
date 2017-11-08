package com.ihujia.hujia.person.controller;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.BitmapUtil;
import com.common.utils.FileUtil;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.UserEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.adapter.SelectPhotoAdapter;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.PermissionUtils;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.io.File;
import java.util.Calendar;
import java.util.IdentityHashMap;

import static com.common.utils.BitmapUtil.IMAGE_CACHE_DIR;

/**
 * Created by zhaoweiwei on 2016/12/20.
 * 个人信息修改
 */

public class PersonInfoActivity extends ToolBarActivity implements View.OnClickListener {

	private static final int REQUEST_CAMERA_CODE = 100;//拍照
	private static final int REQUEST_PHOTO_CODE = 200;//相册
	private static final int RESPONSE_NAME = 0X01;//设置用户名
	private static final int RESPONSE_ADDRESS = 0X02;//设置地址
	private static final int REQUEST_UPDATE_USER_INFO = 0X03;

	private static final int REQUEST_CAMERA_PERMISSION_CODE = 0x04;
	private static final int REQUEST_STORAGE_PERMISSION_CODE = 0x05;

	@ViewInject(R.id.person_info_head)
	private LinearLayout personInfoHead;
	@ViewInject(R.id.person_info_avatar)
	private ImageView personInfoAvatar;
	@ViewInject(R.id.person_info_username_ll)
	private LinearLayout personUsernameLL;
	@ViewInject(R.id.person_info_username)
	private TextView personUsername;
	@ViewInject(R.id.person_info_gender_ll)
	private LinearLayout personInfoGenderLL;
	@ViewInject(R.id.person_info_gender)
	private TextView personInfoGender;
	@ViewInject(R.id.person_info_birthday_ll)
	private LinearLayout personInfoBirthdayLL;
	@ViewInject(R.id.person_info_birthday)
	private TextView personInfoBirthday;
	@ViewInject(R.id.person_info_location_ll)
	private LinearLayout personInfoLocationLL;
	@ViewInject(R.id.person_info_location)
	private TextView personInfoLocation;

	private String path;
	private String birthday;
	private String sex; //0 男 1 女
	private String name;
	private String address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_info);
		ViewInjectUtils.inject(this);
		setLeftTitle(getString(R.string.person_my_info));
		initClickListener();
		initView();
	}

	private void initView() {
		UserEntity userInfo = UserCenter.getUserInfo(this);
		if (userInfo != null) {
			path = userInfo.getAvatar() == null ? "" : userInfo.getAvatar();
			name = userInfo.getNickName();
			sex = userInfo.getSex();
			birthday = userInfo.getBirthday();
			address = userInfo.getProvince() + userInfo.getCity() + userInfo.getDistrict();
			setData();
		}
	}

	private void setData() {
		if (!TextUtils.isEmpty(path) && !path.contains("?")) {
			ImageUtils.setSmallImg(personInfoAvatar, path);
		}

		personUsername.setText(name);

		if ("0".equals(sex)) {
			personInfoGender.setText(getString(R.string.person_info_sex_man));
		} else if ("1".equals(sex)) {
			personInfoGender.setText(getString(R.string.person_info_sex_woman));
		} else {
			personInfoGender.setText(res.getString(R.string.person_info_is_not_set));
		}

		if (StringUtil.isEmpty(birthday)) {
			personInfoBirthday.setText(res.getString(R.string.person_info_is_not_set));
		} else {
			personInfoBirthday.setText(birthday);
		}

		if (!StringUtil.isEmpty(address)) {
			personInfoLocation.setText(address);
		} else {
			personInfoLocation.setText(res.getString(R.string.person_info_is_not_set));
		}
	}

	private void initClickListener() {
		personInfoHead.setOnClickListener(this);
		personUsernameLL.setOnClickListener(this);
		personInfoGenderLL.setOnClickListener(this);
		personInfoBirthdayLL.setOnClickListener(this);
		personInfoLocationLL.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.person_info_head:
				showHeadDialog();
				break;
			case R.id.person_info_username_ll:
				startActivityForResult(new Intent(this, FixNameActivity.class), RESPONSE_NAME);
				break;
			case R.id.person_info_gender_ll:
				showGenderDialog();
				break;
			case R.id.person_info_birthday_ll:
				showDataDialog();
				break;
			case R.id.person_info_location_ll:
				startActivityForResult(new Intent(this, SelectCityActivity.class), RESPONSE_ADDRESS);
				break;
		}
	}

	private void showHeadDialog() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setAdapter(new SelectPhotoAdapter(this), (dialog, which) -> {
			if (0 == which) {
				if (PermissionUtils.isGetPermission(PersonInfoActivity.this, Manifest.permission.CAMERA)) {
					openCamera();
				} else {
					PermissionUtils.secondRequest(PersonInfoActivity.this, REQUEST_CAMERA_PERMISSION_CODE, Manifest.permission.CAMERA);
				}
			} else {
				if (PermissionUtils.isGetPermission(PersonInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
					openPhotos();
				} else {
					PermissionUtils.secondRequest(PersonInfoActivity.this, REQUEST_STORAGE_PERMISSION_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
				}
			}
			dialog.dismiss();
		});
		AlertDialog alertDialog1 = builder1.create();
		alertDialog1.setCanceledOnTouchOutside(true);
		alertDialog1.show();
	}

	private void openPhotos() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent, REQUEST_PHOTO_CODE);
	}

	private void openCamera() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = FileUtil.getDiskCacheFile(PersonInfoActivity.this, IMAGE_CACHE_DIR);
		file = new File(file.getPath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, REQUEST_CAMERA_CODE);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case REQUEST_CAMERA_PERMISSION_CODE:
				if (grantResults.length > 0 && Manifest.permission.CAMERA.equals(permissions[0]) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					openCamera();
				} else {
					ToastUtil.shortShow(this, getString(R.string.person_permission_photo_failed));
				}
				break;
			case REQUEST_STORAGE_PERMISSION_CODE:
				if (grantResults.length > 0 && Manifest.permission.CAMERA.equals(permissions[0]) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					openPhotos();
				} else {
					ToastUtil.shortShow(this, getString(R.string.person_permission_album_failed));
				}
				break;
		}
	}

	private void showGenderDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(res.getString(R.string.person_info_sex_hint));
		builder.setItems(new String[]{res.getString(R.string.person_info_sex_man), res.getString(R.string.person_info_sex_woman)}, (dialog, which) -> {
			if (0 == which) {
				sex = "0";
			} else if (1 == which) {
				sex = "1";
			} else {
				sex = "";
			}
			updateUserInfo("", "", sex, "", "", "", "");
			dialog.dismiss();
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	private void showDataDialog() {
		Calendar calendar = Calendar.getInstance();
		//不添加DateSet回调，通过自己处理确定事件获取日期，兼容4.x系统
		int birthdayYear = 1990;
		int birthdayMonth = 1;
		int birthdayDay = 1;
		if (!StringUtil.isEmpty(birthday)) {
			String birthdayData = "";
			for (int i = 0; i < birthday.length(); i++) {
				birthdayData = birthday.replace("年", ",");
				birthdayData = birthdayData.replace("月", ",");
				birthdayData = birthdayData.replace("日", ",");
			}
			birthdayData = birthdayData.replace("-",",");//兼容以“-”分割的生日
			String[] tempBirthday = birthdayData.split(",");
			birthdayYear = Integer.parseInt(tempBirthday[0]);
			birthdayMonth = Integer.parseInt(tempBirthday[1]);
			birthdayDay = Integer.parseInt(tempBirthday[2]);
		}

		final DatePickerDialog datePickerDialog = new DatePickerDialog(this, null, birthdayYear, birthdayMonth - 1, birthdayDay);
		datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getString(R.string.button_ok), (dialog, which) -> {
			DatePicker datePicker = datePickerDialog.getDatePicker();
			int year = datePicker.getYear();
			int month = datePicker.getMonth() + 1;
			int day = datePicker.getDayOfMonth();
			StringBuilder monthStr = new StringBuilder(),dayStr = new StringBuilder();
			if (month<10) {
				monthStr = monthStr.append("0").append(month);
			}
			if (day<10) {
				dayStr = dayStr.append("0").append(day);
			}
			birthday = year + "年" + monthStr + "月" + dayStr + "日";
			updateUserInfo("", "", "", birthday, "", "", "");
		});
		datePickerDialog.setCancelable(true);
		datePickerDialog.setCanceledOnTouchOutside(true);
		DatePicker datePicker = datePickerDialog.getDatePicker();
		datePicker.setMaxDate(calendar.getTimeInMillis());
		datePickerDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			switch (requestCode) {
				case REQUEST_PHOTO_CODE:
					Uri uri = data.getData();
					if (uri != null) {
						path = getRealPathFromURI(uri);
					}
					String headImg = BitmapUtil.bitmapToString(path, 300, 300);
					updateUserInfo(headImg, "", "", "", "", "", "");
					break;
				case REQUEST_CAMERA_CODE:
					String headImg1 = BitmapUtil.bitmapToString(path, 300, 300);
					updateUserInfo(headImg1, "", "", "", "", "", "");
					break;
				case RESPONSE_NAME:
					name = data.getStringExtra("name");
					updateUserInfo("", name, "", "", "", "", "");
					break;
				case RESPONSE_ADDRESS:
					String provinceName = data.getStringExtra("provinceName");
					String province = data.getStringExtra("provinceId");
					String cityName = data.getStringExtra("cityName");
					String city = data.getStringExtra("cityId");
					String destrictName = data.getStringExtra("districtName");
					String district = data.getStringExtra("districtId");

					address = provinceName + cityName + destrictName;
					updateUserInfo("", "", "", "", province, city, district);
					break;
			}
		}
	}

	private void updateUserInfo(String headImg, String userName, String sex, String birthday, String province, String city, String district) {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("head_portrait", headImg);
		params.put("nick_name", userName);
		params.put("sex", sex);
		params.put("birthday", birthday);
		params.put("province", province);
		params.put("city", city);
		params.put("district", district);
		params.put(Constants.USERID, UserCenter.getUserId(this));
		requestHttpData(Constants.Urls.URL_POST_PERSON_INFO, REQUEST_UPDATE_USER_INFO, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		super.success(requestCode, data);
		closeProgressDialog();
		UserEntity user = Parsers.getUser(data);
		if (user != null) {
			if (REQUEST_NET_SUCCESS.equals(user.getResultCode())) {
				UserCenter.saveUserInfo(this, user);
				initView();
				setData();
			} else {
				ToastUtil.shortShow(this, user.getResultMsg());
			}
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		super.mistake(requestCode, status, errorMessage);
		closeProgressDialog();
		ToastUtil.shortShow(this,errorMessage);
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
}
