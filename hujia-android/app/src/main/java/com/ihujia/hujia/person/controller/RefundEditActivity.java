package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.ListDialogEntity;
import com.ihujia.hujia.network.entities.ListDialogPageEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.utils.InputUtil;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2017/3/26.
 * 填写邮寄物流信息
 */

public class RefundEditActivity extends ToolBarActivity implements View.OnClickListener {
	private static final int REQUEST_REFUND_LOGISTIC_UNITS = 0X01;
	private static final int REQUEST_REFUND_LOGISTIC_COMMIT = 0X02;

	@ViewInject(R.id.refund_edit_logistic_unit)
	private TextView logisticUnit;
	@ViewInject(R.id.refund_edit_logistic_number)
	private EditText logisticNumber;
	@ViewInject(R.id.refund_edit_commit)
	private TextView commit;

	private String refundId;
	private String logistic;
	private String logisticId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_refund_edit);
		ViewInjectUtils.inject(this);
		setLeftTitle(getString(R.string.courier_info));
		InputUtil.editIsEmpty(commit, logisticNumber);
		refundId = getIntent().getStringExtra("refundId");
		logisticUnit.setOnClickListener(this);
		commit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.refund_edit_logistic_unit:
				showProgressDialog();
				IdentityHashMap<String, String> params = new IdentityHashMap<>();
				requestHttpData(Constants.Urls.URL_POST_LOGISTIC_COMPANY, REQUEST_REFUND_LOGISTIC_UNITS, FProtocol.HttpMethod.POST, params);
				break;
			case R.id.refund_edit_commit:
				String logisticNo = logisticNumber.getText().toString();
				showProgressDialog();
				IdentityHashMap<String, String> params1 = new IdentityHashMap<>();
				params1.put(Constants.USERID, UserCenter.getUserId(this));
				params1.put("refund_id", refundId);
				params1.put("logistics_id", logisticId);
				params1.put("logistics_no", logisticNo);
				params1.put("invoice_name", logistic);
				requestHttpData(Constants.Urls.URL_POST_COMMIT_LOGISTIC, REQUEST_REFUND_LOGISTIC_COMMIT, FProtocol.HttpMethod.POST, params1);
				break;
		}
	}

	@Override
	public void success(int requestCode, String data) {
		super.success(requestCode, data);
		closeProgressDialog();
		switch (requestCode) {
			case REQUEST_REFUND_LOGISTIC_UNITS:
				ListDialogPageEntity<ListDialogEntity> pagesEntity = Parsers.getLogisticUnit(data);
				if (pagesEntity != null) {
					List<ListDialogEntity> logisticUnits = pagesEntity.getEntities();
					if (logisticUnits != null && logisticUnits.size() > 0) {
						ListDialogActivity.startListDialogActivity(this, pagesEntity);
					}
				}
				break;
			case REQUEST_REFUND_LOGISTIC_COMMIT:
				Entity entity = Parsers.getResult(data);
				if (entity != null) {
					if (REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
						setResult(RESULT_OK);
						finish();
					} else {
						ToastUtil.shortShow(this, entity.getResultMsg());
					}
				}
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			switch (requestCode) {
				case ListDialogActivity.REQUEST_CHOOSE_ITEM:
					logistic = data.getStringExtra(ListDialogActivity.REQUEST_ITEM_CONTENT);
					logisticId = data.getStringExtra(ListDialogActivity.REQUEST_ITEM_ID);
					logisticUnit.setText(logistic);
					logisticUnit.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
					break;
			}
		}
	}
}
