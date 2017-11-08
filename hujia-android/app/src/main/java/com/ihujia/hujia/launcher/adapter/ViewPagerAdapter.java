package com.ihujia.hujia.launcher.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihujia.hujia.R;
import com.ihujia.hujia.base.adapter.BasePagerAdapter;
import com.ihujia.hujia.main.controller.MainActivity;
import com.ihujia.hujia.utils.ConfigUtils;

import java.util.List;

/**
 * Created by liuzhichao on 2016/12/16.
 * 引导页
 */
public class ViewPagerAdapter extends BasePagerAdapter<View> {

	private Context context;

	public ViewPagerAdapter(List<View> datas, Activity activity) {
		super(datas);
		this.context = activity;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = datas.get(position);
		container.addView(view);

		TextView btnStart = (TextView) view.findViewById(R.id.guide_btn_start);
		if (position == datas.size() - 1) {
			btnStart.setVisibility(View.VISIBLE);
			btnStart.setOnClickListener(v -> {
				setGuided();
				goHome();
			});
		} else {
			btnStart.setVisibility(View.GONE);
		}
		return view;
	}

	private void goHome() {
		context.startActivity(new Intent(context, MainActivity.class));
	}

	/**
	 * method desc：设置已经引导过了，下次启动不用再次引导
	 */
	private void setGuided() {
		ConfigUtils.setShowGuide(context, false);
	}
}
