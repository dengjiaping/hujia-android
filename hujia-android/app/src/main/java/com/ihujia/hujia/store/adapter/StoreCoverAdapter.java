package com.ihujia.hujia.store.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.base.adapter.BasePagerAdapter;

import java.util.List;

/**
 * Created by liuzhichao on 2017/1/5.
 * 店铺详情封面
 */
public class StoreCoverAdapter extends BasePagerAdapter<String> {

	public StoreCoverAdapter(List<String> datas) {
		super(datas);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = generateView(container.getContext(), datas.get(position));
		container.addView(view);
		return view;
	}

	private View generateView(Context context, String url) {
		SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		simpleDraweeView.setLayoutParams(lp);
		simpleDraweeView.setImageURI(url);
		return simpleDraweeView;
	}
}
