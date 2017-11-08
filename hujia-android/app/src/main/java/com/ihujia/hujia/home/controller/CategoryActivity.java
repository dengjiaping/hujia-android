package com.ihujia.hujia.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.home.adapter.SecondCategoryAdapter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.CategoryEntity;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.store.controller.ProductActivity;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.List;

import static com.ihujia.hujia.store.controller.ProductActivity.FROM_HOME_CATEGORY;

/**
 * Created by liuzhichao on 2017/1/6.
 * 分类
 */
public class CategoryActivity extends ToolBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

	private static final int REQUEST_NET_CATEGORY_LIST = 10;

	@ViewInject(R.id.lv_category_main)
	private ListView lvCategoryMain;
	@ViewInject(R.id.lv_category_detail)
	private ListView lvCategoryDetail;

	private List<CategoryEntity> categoryList;
	private int preCategory = 0;
	private SecondCategoryAdapter secondCategoryAdapter;

	public static void startCategoryActivity(Context context) {
		Intent intent = new Intent(context, CategoryActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_category);
		ViewInjectUtils.inject(this);
		initView();
		loadData();
	}

	private void initView() {
		setLeftTitle(getString(R.string.category_name_text));

		//默认选中第一个
		lvCategoryMain.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
			View firstView = lvCategoryMain.getChildAt(preCategory);
			if (firstView != null) {
				firstView.setSelected(true);
			}
		});

		lvCategoryMain.setOnItemClickListener(this);
	}

	private void loadData() {
		showProgressDialog();
		requestHttpData(Constants.Urls.URL_POST_CATEGORY_LIST, REQUEST_NET_CATEGORY_LIST, FProtocol.HttpMethod.POST, null);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			if (REQUEST_NET_CATEGORY_LIST == requestCode) {
				categoryList = Parsers.getCategoryList(data);
				if (categoryList != null) {
					List<String> mainCategories = new ArrayList<>();
					for (CategoryEntity categoryEntity : categoryList) {
						mainCategories.add(categoryEntity.getName());
					}
					ArrayAdapter<String> leftAdapter = new ArrayAdapter<>(this, R.layout.item_category, mainCategories);
					lvCategoryMain.setAdapter(leftAdapter);

					List<CategoryEntity> categoryEntities = new ArrayList<>();
					CategoryEntity categoryEntity = categoryList.get(0);
					if (categoryEntity != null) {
						categoryEntities.addAll(categoryEntity.getCategoryEntities());
					}
					secondCategoryAdapter = new SecondCategoryAdapter(this, categoryEntities, this);
					lvCategoryDetail.setAdapter(secondCategoryAdapter);
				} else {
					ToastUtil.shortShow(this, getString(R.string.no_data_text));
				}
			}
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		preCategory = position;
		if (categoryList != null) {
			CategoryEntity categoryEntity = categoryList.get(position);
			List<CategoryEntity> categoryEntities = new ArrayList<>();
			if (categoryEntity != null) {
				categoryEntities.addAll(categoryEntity.getCategoryEntities());
			}
			secondCategoryAdapter = new SecondCategoryAdapter(this, categoryEntities, this);
			lvCategoryDetail.setAdapter(secondCategoryAdapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_third_category:
				CategoryEntity categoryEntity = (CategoryEntity) v.getTag();
				if (categoryEntity != null) {
					ProductActivity.startProductActivity(this, FROM_HOME_CATEGORY, categoryEntity.getName(), categoryEntity.getId());
				}
				break;
		}
	}
}
