package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.common.network.FProtocol;
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
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.ListDialogDatasUtil;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2017/4/20.
 * 申请退款(待发货订单)
 */

public class ApplyRefundActivity extends ToolBarActivity implements View.OnClickListener {
	private static final int REQUEST_NET_COMMIT_REFUND_MONEY = 0x01;

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

	@ViewInject(R.id.refund_apply_reason)
	private TextView refundReason;
	@ViewInject(R.id.refund_apply_maxprice)
	private TextView refundPrice;
	@ViewInject(R.id.refund_apply_commit)
	private TextView refundCommit;

	private String reason;
	private String orderId;
	private String skuId;
	private String payPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_apply_refund);
		ViewInjectUtils.inject(this);
		setLeftTitle("申请退款");
		initView();
	}

	private void initView() {
		ShopCarGoodEntity entity = getIntent().getParcelableExtra("product");
		skuId = entity.getSkuId();
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
			payPrice = entity.getRefundPrice();
			String logisticPrice = entity.getLogisticPrice();
			clothName.setText(entity.getGoodsName());
			clothColor.setText(getString(R.string.shopcar_color, goodsColor));
			clothSize.setText(getString(R.string.shopcar_size, goodSize));
			clothPrice.setText(getString(R.string.price, goodsPrice));
			clothNum.setText(getString(R.string.num, entity.getCount()));
			SpannableStringBuilder stringBuilder = new SpannableStringBuilder(getString(R.string.person_refund_price, payPrice, payPrice, logisticPrice));
			stringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary_color_red)), 0, goodsPrice.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			refundPrice.setText(stringBuilder);
		}
		refundReason.setOnClickListener(this);
		refundCommit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.refund_apply_reason:
				ListDialogPageEntity pageEntity = ListDialogDatasUtil.createOrderDatas();
				ListDialogActivity.startListDialogActivity(this, pageEntity);
				break;
			case R.id.refund_apply_commit:
				if (StringUtil.isEmpty(reason)) {
					ToastUtil.shortShow(this, "请选择退货原因！！！");
				} else {
					if (!StringUtil.isEmpty(orderId) && !StringUtil.isEmpty(reason) && !StringUtil.isEmpty(payPrice) && !StringUtil.isEmpty(skuId)) {
						showProgressDialog();
						IdentityHashMap<String, String> params = new IdentityHashMap<>();
						params.put("user_id", UserCenter.getUserId(this));
						params.put("order_id", orderId);
						params.put("refund_reason", reason);
						params.put("refund_total", payPrice);
						params.put("sku_id", skuId);
						requestHttpData(Constants.Urls.URL_POST_REFUND_MONEY, REQUEST_NET_COMMIT_REFUND_MONEY, FProtocol.HttpMethod.POST, params);
					} else {
						ToastUtil.shortShow(this, "申请信息不完全");
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
				startActivity(new Intent(this, RefundSuccessActivity.class).putExtra(EXTRA_FROM, 1));
				finish();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			switch (requestCode) {
				case ListDialogActivity.REQUEST_CHOOSE_ITEM:
					reason = data.getStringExtra(ListDialogActivity.REQUEST_ITEM_CONTENT);
					refundReason.setText(reason);
					refundReason.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
					break;
			}
		}
	}
}
