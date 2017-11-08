package com.ihujia.hujia.home.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.utils.ViewInjectUtils;

/**
 * Created by zhaoweiwei on 2017/2/13.
 * 发票信息
 */

public class ReceiptActivity extends ToolBarActivity implements View.OnClickListener {
	@ViewInject(R.id.receipt_status_no)
	private RadioButton receiptNo;
	@ViewInject(R.id.receipt_status_yes)
	private RadioButton receiptYes;
	@ViewInject(R.id.receipt_layout_content)
	private LinearLayout receiptLayout;
	@ViewInject(R.id.receipt_input)
	private EditText receiptInput;
	@ViewInject(R.id.receipt_ok)
	private TextView receiptOk;

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_receipt);
		ViewInjectUtils.inject(this);
		intView();
	}

	private void intView() {
		setLeftTitle(getString(R.string.receipt_title));
		if (receiptNo.isChecked()) {
			receiptLayout.setVisibility(View.GONE);
		}

		intent = getIntent();
		receiptNo.setOnClickListener(this);
		receiptYes.setOnClickListener(this);
		receiptOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.receipt_status_no:
				receiptLayout.setVisibility(View.GONE);
				break;
			case R.id.receipt_status_yes:
				receiptLayout.setVisibility(View.VISIBLE);
				break;
			case R.id.receipt_ok:
				if (receiptYes.isChecked()) {
					String receiptTitle = receiptInput.getText().toString();
					if (StringUtil.isEmpty(receiptTitle)) {
						ToastUtil.shortShow(this, getString(R.string.receipt_input_head));
					} else {
						intent.putExtra("receiptTitle", receiptTitle);
						setResult(RESULT_OK, intent);
						finish();
					}
				}
				break;
		}
	}
}
