package com.ihujia.hujia.person.controller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.BitmapUtil;
import com.common.utils.FileUtil;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.GoodsAttrEntity;
import com.ihujia.hujia.network.entities.ListDialogPageEntity;
import com.ihujia.hujia.network.entities.ShopCarGoodEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.adapter.RefundImgAdapter;
import com.ihujia.hujia.person.adapter.SelectPhotoAdapter;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.ListDialogDatasUtil;
import com.ihujia.hujia.utils.PermissionUtils;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import static com.common.utils.BitmapUtil.IMAGE_CACHE_DIR;

/**
 * Created by zhaoweiwei on 2017/1/3.
 * 售后申请（维修，退货，退款）
 */

public class ApplyServiceActivity extends ToolBarActivity implements TextWatcher, View.OnTouchListener, View.OnClickListener {
	private static final int REQUEST_CAMERA_CODE = 100;//拍照
	private static final int REQUEST_PHOTO_CODE = 200;//相册
	private static final int REQUEST_NET_COMMIT_REFUND = 0X01;
	private static final int REQUEST_CAMERA_PERMISSION_CODE = 0x04;
	private static final int REQUEST_STORAGE_PERMISSION_CODE = 0x05;


	@ViewInject(R.id.order_item_img)
	private SimpleDraweeView clothImg;
	@ViewInject(R.id.order_item_name)
	private TextView clothName;
	@ViewInject(R.id.order_item_color)
	private TextView clothColor;
	@ViewInject(R.id.order_item_size)
	private TextView clothSize;
	@ViewInject(R.id.order_item_price)
	private TextView clothPrice;
	@ViewInject(R.id.order_item_num)
	private TextView clothNum;
	@ViewInject(R.id.service_apply_fix)
	private RadioButton applyFix;
	@ViewInject(R.id.service_apply_return_goods)
	private RadioButton applyReturnGoods;
	@ViewInject(R.id.service_apply_refund)
	private RadioButton applyRefund;
	@ViewInject(R.id.service_apply_reason)
	private TextView reason;
	@ViewInject(R.id.service_apply_maxprice)
	private TextView maxPrice;
	@ViewInject(R.id.customer_service_describe)
	private EditText describe;
	@ViewInject(R.id.customer_service_limit)
	private TextView limit;
	@ViewInject(R.id.customer_service_photo)
	private RecyclerView photo;
	@ViewInject(R.id.customer_service_next)
	private TextView next;

	private String path;
	private List<String> urls;
	private RefundImgAdapter adapter;
	private ShopCarGoodEntity entity;
	private String orderId;
	private StringBuilder refundPic;
	private String refundReason;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_apply_service);
		ViewInjectUtils.inject(this);
		setLeftTitle("售后申请");
		initView();
		initPhotoLayout();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	private void initView() {
		entity = getIntent().getParcelableExtra("product");
		orderId = getIntent().getStringExtra("orderId");
		if (entity != null) {
			ImageUtils.setSmallImg(clothImg, entity.getGoodsPic());
			String goodSize = null, goodsColor = null;
			List<GoodsAttrEntity> attrEntities = entity.getAttrList();
			for (GoodsAttrEntity attrEntity : attrEntities) {
				if ("尺码".equals(attrEntity.getAttrName())) {
					goodSize = attrEntity.getAttrValue();
				} else if ("颜色".equals(attrEntity.getAttrName())) {
					goodsColor = attrEntity.getAttrValue();
				}
			}
			String goodsPrice = entity.getGoodsPrice();
			String refundPrice = entity.getRefundPrice();
			clothName.setText(entity.getGoodsName());
			clothColor.setText(getString(R.string.shopcar_color, goodsColor));
			clothSize.setText(getString(R.string.shopcar_size, goodSize));
			clothPrice.setText(getString(R.string.price, goodsPrice));
			clothNum.setText(getString(R.string.num, entity.getCount()));
			SpannableStringBuilder stringBuilder = new SpannableStringBuilder(getString(R.string.person_refund_maxprice, refundPrice));
			stringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary_color_red)), 0, goodsPrice.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			maxPrice.setText(stringBuilder);
		}
		applyFix.setOnClickListener(this);
		applyReturnGoods.setOnClickListener(this);
		applyRefund.setOnClickListener(this);
		reason.setOnClickListener(this);
		describe.addTextChangedListener(this);
		describe.setOnTouchListener(this);
		next.setOnClickListener(this);
	}

	private void initPhotoLayout() {
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		photo.setLayoutManager(linearLayoutManager);
		//设置适配器
		urls = new ArrayList<>();
		urls.add("");
		adapter = new RefundImgAdapter(this, urls, this);
		photo.setAdapter(adapter);
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	}

	@Override
	public void afterTextChanged(Editable editable) {
		int length = describe.getText().length();
		if (length >= 200) {
			limit.setText(getString(R.string.person_order_refund_content_size, "200"));
			ToastUtil.shortShow(this, "最多只能输入200个字哦");
		} else {
			limit.setText(getString(R.string.person_order_refund_content_size, String.valueOf(length)));
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		//触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
		if ((view.getId() == R.id.customer_service_describe && canVerticalScroll(describe))) {
			view.getParent().requestDisallowInterceptTouchEvent(true);
			if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
				view.getParent().requestDisallowInterceptTouchEvent(false);
			}
		}
		return false;
	}

	/**
	 * EditText竖直方向是否可以滚动
	 *
	 * @param editText 需要判断的EditText
	 * @return true：可以滚动   false：不可以滚动
	 */
	private boolean canVerticalScroll(EditText editText) {
		//滚动的距离
		int scrollY = editText.getScrollY();
		//控件内容的总高度
		int scrollRange = editText.getLayout().getHeight();
		//控件实际显示的高度
		int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
		//控件内容总高度与实际显示高度的差值
		int scrollDifference = scrollRange - scrollExtent;

		if (scrollDifference == 0) {
			return false;
		}

		return (scrollY > 0) || (scrollY < scrollDifference - 1);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.service_apply_reason:
				ListDialogPageEntity pageEntity = ListDialogDatasUtil.createRefundDatas();
				ListDialogActivity.startListDialogActivity(this, pageEntity);
				break;
			case R.id.torefund_photo_item_img:
				ToastUtil.shortShow(this, "点击此处添加图片");
				showHeadDialog();
				break;
			case R.id.customer_service_next:
				if (StringUtil.isEmpty(refundReason)) {
					ToastUtil.shortShow(this, "请选择退货原因！！！");
				} else {
					if (describe.getText().length() > 9) {
						if (!StringUtil.isEmpty(orderId) && !StringUtil.isEmpty(refundReason) && !StringUtil.isEmpty(entity.getSkuId())) {
							showProgressDialog();
							IdentityHashMap<String, String> params = new IdentityHashMap<>();
							params.put("user_id", UserCenter.getUserId(this));
							params.put("order_id", orderId);
							params.put("refund_reason", refundReason);
							params.put("reason", describe.getText().toString());
							params.put("sku_id", entity.getSkuId());
							if (refundPic != null) {
								params.put("refund_pic", refundPic.toString());
							}
							requestHttpData(Constants.Urls.URL_POST_REFUND, REQUEST_NET_COMMIT_REFUND, FProtocol.HttpMethod.POST, params);
						} else {
							ToastUtil.shortShow(this, "申请信息不完全");
						}
					} else {
						ToastUtil.shortShow(this, "问题描述不少于10个字！！！");
					}
				}
				break;
		}
	}

	@Override
	public void success(int requestCode, String data) {
		super.success(requestCode, data);
		closeProgressDialog();
		Entity entity = Parsers.getResult(data);
		if (entity != null) {
			if (REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
				startActivity(new Intent(this, RefundSuccessActivity.class));
			} else {
				ToastUtil.shortShow(this, entity.getResultMsg());
			}
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		super.mistake(requestCode, status, errorMessage);
		closeProgressDialog();
		ToastUtil.shortShow(this, errorMessage);
	}

	private void showHeadDialog() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setAdapter(new SelectPhotoAdapter(this), (dialog, which) -> {
			if (0 == which) {
				if (PermissionUtils.isGetPermission(ApplyServiceActivity.this, Manifest.permission.CAMERA)) {
					openCamera();
				} else {
					PermissionUtils.secondRequest(ApplyServiceActivity.this, REQUEST_CAMERA_PERMISSION_CODE, Manifest.permission.CAMERA);
				}
			} else {
				if (PermissionUtils.isGetPermission(ApplyServiceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
					openPhotos();
				} else {
					PermissionUtils.secondRequest(ApplyServiceActivity.this, REQUEST_STORAGE_PERMISSION_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
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
		File file = FileUtil.getDiskCacheFile(ApplyServiceActivity.this, IMAGE_CACHE_DIR);
		file = new File(file.getPath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, REQUEST_CAMERA_CODE);
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
					urls.add(0, path);
					if (urls.size() > 4) {
						urls.remove(4);
					}
					refundPic = new StringBuilder();
					String netPath = BitmapUtil.bitmapToString(path, 300, 300);
					refundPic.append(netPath);
					refundPic.append(",");
					adapter.notifyDataSetChanged();
					break;
				case ListDialogActivity.REQUEST_CHOOSE_ITEM:
					refundReason = data.getStringExtra(ListDialogActivity.REQUEST_ITEM_CONTENT);
					reason.setText(refundReason);
					reason.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
					break;
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
}
