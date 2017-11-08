package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.OrderItemEntity;
import com.ihujia.hujia.network.entities.RefundDetailEntity;
import com.ihujia.hujia.network.entities.RefundDetailPageEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.adapter.RefundDetailAdapter;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2017/1/11.
 * 退货详情（处理进度）
 */

public class RefundDetailActivity extends ToolBarActivity implements View.OnClickListener {
	private static final int REQUEST_REFUND_DETAIL = 0X01;
	private static final int REQUEST_GET_LOGISTIC = 0X02;
	private static final int REQUEST_CANCEL_REFUND = 0X03;

	@ViewInject(R.id.refund_detail_number)
	private TextView refundNumber;
	@ViewInject(R.id.refund_detail_status)
	private TextView refundStatus;
	@ViewInject(R.id.refund_detail_time)
	private TextView refundTime;
	@ViewInject(R.id.refund_detail_list)
	private ListView refundList;
	@ViewInject(R.id.refund_detail_to_edit)
	private TextView refundToEdit;

	private OrderItemEntity entity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_refund_detail);
		setLeftTitle(getString(R.string.handle_progress));
		ViewInjectUtils.inject(this);
		initView();
		loadData();
	}

	private void initView() {
		entity = getIntent().getParcelableExtra("refundEntity");
		refundNumber.setText(getString(R.string.person_refund_number, entity.getRefundId()));
		refundTime.setText(getString(R.string.person_refund_time, entity.getRefundTime()));
	}

	private void loadData() {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("user_id", UserCenter.getUserId(this));
		params.put("refund_id", entity.getRefundId());
		params.put("type", entity.getType());
		requestHttpData(Constants.Urls.URL_POST_REFUND_PROGRESS, REQUEST_REFUND_DETAIL, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		super.success(requestCode, data);
		closeProgressDialog();
		switch (requestCode) {
			case REQUEST_REFUND_DETAIL:
				RefundDetailPageEntity<RefundDetailEntity> pagesEntity = Parsers.getRefundDetail(data);
				if (pagesEntity != null) {
					//设置状态
					String status = pagesEntity.getStatus();
					if (StringUtil.isEmpty(status)) {
						refundStatus.setText(getString(R.string.person_info_is_not_set));
					} else {
						refundStatus.setText(pagesEntity.getStatusName());
						if ("22".equals(status)) {
							refundToEdit.setVisibility(View.VISIBLE);
							refundToEdit.setOnClickListener(this);
						} else {
							refundToEdit.setVisibility(View.GONE);
						}

						if ("21".equals(status) || "22".equals(status) || "31".equals(status)) {
							setRightText(getString(R.string.refund_cancel));
							rightText.setOnClickListener(this);
						}
					}
					//设置物流进度
					if (REQUEST_NET_SUCCESS.equals(pagesEntity.getResultCode())) {
						List<RefundDetailEntity> entities = pagesEntity.getDatas();
						if (entities != null && entities.size() > 0) {
							RefundDetailAdapter adapter = new RefundDetailAdapter(this, entities);
							refundList.setAdapter(adapter);
						}
					} else {
						ToastUtil.shortShow(this, pagesEntity.getResultMsg());
					}
				}
				break;
			case REQUEST_CANCEL_REFUND:
				Entity entity = Parsers.getResult(data);
				if (entity != null) {
					if (REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
						loadData();
					} else {
						ToastUtil.shortShow(this, entity.getResultMsg());
					}
				}
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.refund_detail_to_edit:
				startActivityForResult(new Intent(this, RefundEditActivity.class).putExtra("refundId", entity.getRefundId()), REQUEST_GET_LOGISTIC);
				break;
			case R.id.rigth_text:
				showAlertDialog("", getString(R.string.refund_confirm_cancel), getString(R.string.button_ok), getString(R.string.let_me_think), v1 -> {
					showProgressDialog();
					IdentityHashMap<String, String> params = new IdentityHashMap<>();
					params.put("user_id", UserCenter.getUserId(RefundDetailActivity.this));
					params.put("refund_id", entity.getRefundId());
					requestHttpData(Constants.Urls.URL_POST_CANCEL_REFUND, REQUEST_CANCEL_REFUND, FProtocol.HttpMethod.POST, params);
					closeDialog();
				}, v1 -> closeDialog());
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			switch (requestCode) {
				case REQUEST_GET_LOGISTIC:
					loadData();
					break;
			}
		}
	}
}
