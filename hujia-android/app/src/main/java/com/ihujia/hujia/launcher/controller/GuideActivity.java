package com.ihujia.hujia.launcher.controller;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.launcher.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhichao on 2016/12/16.
 * 引导页
 */
public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

	private ViewPager guideViewpager;
	private LinearLayout guideDot;
	private ImageView[] dots;
	private int currentIndex;
	private final int picId [] = new int[]{R.drawable.launcher_img_guide_1, R.drawable.launcher_img_guide_2,
			R.drawable.launcher_img_guide_3,R.drawable.launcher_img_guide_4};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_guide);
		initView();
		initDot();
	}

	private void initView() {
		guideViewpager = (ViewPager) findViewById(R.id.guide_viewpager);
		guideDot = (LinearLayout) findViewById(R.id.guide_dot);

		List<View> views = new ArrayList<>();
		for (int guidePic : picId) {
			View view = View.inflate(this, R.layout.item_guide_viewpager, null);
			ImageView guideItemPic = (ImageView) view.findViewById(R.id.guide_img);
			guideItemPic.setBackgroundResource(guidePic);
			views.add(view);
		}
		ViewPagerAdapter adapter = new ViewPagerAdapter(views, this);
		guideViewpager.setAdapter(adapter);
		guideViewpager.addOnPageChangeListener(this);
	}

	private void initDot() {
		dots = new ImageView[picId.length];

		// 循环取得小点图片
		for (int i = 0; i < dots.length; i++) {
			dots[i] = new ImageView(this);
			dots[i].setLayoutParams(new ViewGroup.LayoutParams(22, 22));
			dots[i].setId(i);
			dots[i].setBackgroundResource(R.drawable.guide_dot_icon);
			dots[i].setPadding(0, 0, 0, 0);
			dots[i].setEnabled(false);
			//添加焦点图间的间隔
			View view = new View(this);
			view.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
			guideDot.addView(dots[i]);
			guideDot.addView(view);
		}

		currentIndex = 0;
		dots[currentIndex].setBackgroundResource(R.drawable.guide_dot_icon_selected);
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > dots.length - 1 || currentIndex == position) {
			return;
		}

		currentIndex = position;
		// 循环取得小点图片
		for (int i = 0; i < dots.length; i++) {
			if (i == currentIndex) {
				dots[i].setBackgroundResource(R.drawable.guide_dot_icon_selected);
			} else {
				dots[i].setBackgroundResource(R.drawable.guide_dot_icon);
			}
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		// 设置底部小点选中状态
		if (position == dots.length - 1) {
			guideDot.setVisibility(View.GONE);
		} else {
			guideDot.setVisibility(View.VISIBLE);
		}
		setCurrentDot(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}
}
