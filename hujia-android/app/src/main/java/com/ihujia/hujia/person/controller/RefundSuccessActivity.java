package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;

/**
 * Created by zhaoweiwei on 2017/1/4.
 * 售后申请成功
 */

public class RefundSuccessActivity extends ToolBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_service_success);
		mBtnTitleLeft.setVisibility(View.GONE);
		TextView textView = (TextView) findViewById(R.id.service_success_text);
		TextView describe = (TextView) findViewById(R.id.service_success_text_describe);
		int from = getIntent().getIntExtra(EXTRA_FROM, 0);
		if (1 == from) {
			setLeftTitle(getString(R.string.person_order_apply_refund_money));
			textView.setText(getString(R.string.person_refund_success_money_text));
			SpannableStringBuilder stringBuilder = new SpannableStringBuilder(getString(R.string.person_refund_success_money_describe));
			stringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary_color_red)), 5, 9, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			describe.setText(stringBuilder);
		} else {
			setLeftTitle(getString(R.string.person_order_apply_refund));
			textView.setText(getString(R.string.person_refund_success_text));
		}
		findViewById(R.id.service_success_close).setOnClickListener(v -> {
			startActivity(new Intent(RefundSuccessActivity.this, RefundListActivity.class));
			finish();
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode && event.getRepeatCount() == 0) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
