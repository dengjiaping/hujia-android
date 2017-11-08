package com.ihujia.hujia.person.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.network.entities.ListDialogEntity;
import com.ihujia.hujia.network.entities.ListDialogPageEntity;
import com.ihujia.hujia.person.adapter.OrderCancelReasonAdapter;
import com.ihujia.hujia.utils.CommonTools;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/2/27.
 */

public class ListDialogActivity extends BaseActivity implements View.OnClickListener {
	public static final int REQUEST_CHOOSE_ITEM = 0X01;
	public static final String REQUEST_ITEM_CONTENT = "content";
	public static final String REQUEST_ITEM_ID = "contentId";

	@ViewInject(R.id.order_cancel_cancel)
	private TextView orderCancel;
	@ViewInject(R.id.order_cancel_ok)
	private TextView orderOk;
	@ViewInject(R.id.order_cancel_list)
	private ListView orderCancelList;

	private String cancelReason;
	private List<ListDialogEntity> entities;
	private String logisticId;

	public static void startListDialogActivity(Activity activity, ListDialogPageEntity pagesEntity) {
		Intent intent = new Intent(activity, ListDialogActivity.class);
		intent.putExtra("entity", pagesEntity);
		activity.startActivityForResult(intent,REQUEST_CHOOSE_ITEM);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_order_cancel_dialog);
		ViewInjectUtils.inject(this);
		initStyle();
		orderCancel.setOnClickListener(this);
		orderOk.setOnClickListener(this);
		ListDialogPageEntity pageEntity = getIntent().getParcelableExtra("entity");
		entities = pageEntity.getEntities();
		if (entities!=null && entities.size()>0) {
			setDatas();
		}
	}

	private void setDatas() {
		OrderCancelReasonAdapter adapter = new OrderCancelReasonAdapter(this, entities);
		orderCancelList.setAdapter(adapter);
		orderCancelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				for (ListDialogEntity entity : entities) {
					entity.setSelected(false);
				}
				entities.get(position).setSelected(true);
				adapter.notifyDataSetChanged();
				cancelReason = entities.get(position).getReasonContent();
				logisticId = entities.get(position).getReasonId();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.order_cancel_cancel:
				finish();
				break;
			case R.id.order_cancel_ok:
				Intent intent = getIntent();
				intent.putExtra(REQUEST_ITEM_CONTENT,cancelReason);
				intent.putExtra(REQUEST_ITEM_ID, logisticId);
				setResult(RESULT_OK,intent);
				finish();
				break;
		}
	}

	private void initStyle() {
		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams wl = window.getAttributes();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = CommonTools.dp2px(this, 284);
		this.onWindowAttributesChanged(wl);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.actionsheet_dialog_out);
	}
}
