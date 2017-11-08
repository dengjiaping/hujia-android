package com.ihujia.hujia.person.controller;

import android.os.Bundle;
import android.widget.TextView;

import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.utils.InputUtil;
import com.ihujia.hujia.utils.ViewInjectUtils;
import com.ihujia.hujia.widget.ClearEditText;

/**
 * Created by zhaoweiwei on 2017/1/9.
 * 修改昵称
 */

public class FixNameActivity extends ToolBarActivity {
	@ViewInject(R.id.fixname_edit)
	private ClearEditText edit;
	@ViewInject(R.id.fixname_button)
	private TextView buttonOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_fix_name);
		ViewInjectUtils.inject(this);
		setLeftTitle(getString(R.string.person_alter_nickname));
		InputUtil.editIsEmpty(buttonOk, edit);
		buttonOk.setOnClickListener(v -> {
			String name = edit.getText().toString();
			if (String_length(name) < 1 || String_length(name) > 20) {
				ToastUtil.shortShow(FixNameActivity.this, getString(R.string.person_input_real_nickname));
			} else {
				setResult(RESULT_OK, getIntent().putExtra("name", name));
				finish();
			}
		});
	}

	public int String_length(String value) {
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return valueLength;
	}
}
