package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.main.controller.MainActivity;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.OrderItemEntity;
import com.ihujia.hujia.network.entities.PagesEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.adapter.CustomerServiceAdapter;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/31.
 * 退款售后(列表)界面
 */

public class RefundListActivity extends ToolBarActivity implements View.OnClickListener {
	private static final int REQUEST_NET_REFUND = 0x01;
	private static final int REQUEST_NET_REFUND_MORE = 0x02;
	private FootLoadingListView loadingListView;
	private CustomerServiceAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_frag_order);
		setLeftTitle(getString(R.string.person_refund_after_sale));
		initLoadingView(this);
		setLoadingStatus(LoadingStatus.LOADING);
		loadingListView = (FootLoadingListView) findViewById(R.id.order_list);
		loadingListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData(false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData(true);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData(false);
	}

	private void loadData(boolean isMore) {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		int page = 1;
		int request = REQUEST_NET_REFUND;
		if (isMore) {
			page = adapter.getPage() + 1;
			request = REQUEST_NET_REFUND_MORE;
		}
		params.put("user_id", UserCenter.getUserId(this));
		params.put(Constants.PAGENUM, String.valueOf(page));
		params.put(Constants.PAGESIZE, Constants.PAGE_SIZE_10);
		requestHttpData(Constants.Urls.URL_POST_REFUND_LIST, request, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		super.success(requestCode, data);
		closeProgressDialog();
		setLoadingStatus(LoadingStatus.GONE);
		loadingListView.setOnRefreshComplete();
		PagesEntity<OrderItemEntity> pagesEntity = Parsers.getPageOrder(data);
		if (pagesEntity != null) {
			if (REQUEST_NET_SUCCESS.equals(pagesEntity.getResultCode())) {
				List<OrderItemEntity> entities = pagesEntity.getDatas();
				switch (requestCode) {
					case REQUEST_NET_REFUND:
						if (entities != null && entities.size() > 0) {
							adapter = new CustomerServiceAdapter(this, entities, this);
							loadingListView.setAdapter(adapter);
							if (pagesEntity.getTotalPage() > adapter.getPage()) {
								loadingListView.setCanAddMore(true);
							} else {
								loadingListView.setCanAddMore(false);
							}
						} else {
							setLoadingStatus(LoadingStatus.EMPTY);
							mImgLoadingEmpty.setImageResource(R.drawable.loading_refund_empty);
							mTxtLoadingEmpty.setText(getString(R.string.empty_loading_refund));
						}
						break;
					case REQUEST_NET_REFUND_MORE:
						if (entities != null && entities.size() > 0) {
							adapter.addDatas(entities);
							adapter.notifyDataSetChanged();
							if (pagesEntity.getTotalPage() > adapter.getPage()) {
								loadingListView.setCanAddMore(true);
							} else {
								loadingListView.setCanAddMore(false);
							}
						}
						break;
				}
			}else {
				ToastUtil.shortShow(this, pagesEntity.getResultMsg());
			}
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		super.mistake(requestCode, status, errorMessage);
		closeProgressDialog();
		setLoadingStatus(LoadingStatus.RETRY);
		ToastUtil.shortShow(this, errorMessage);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.loading_layout:
				setLoadingStatus(LoadingStatus.GONE);
				showProgressDialog();
				loadData(false);
				break;
			case R.id.order_item_progress_query:
				OrderItemEntity entity = (OrderItemEntity) view.getTag();
				startActivity(new Intent(this, RefundDetailActivity.class).putExtra("refundEntity",entity));
				break;
			case R.id.left_button:
				toPersonFragment();
				break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			toPersonFragment();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void toPersonFragment() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(MainActivity.EXTRA_WHICH_TAB, 4);
		startActivity(intent);
	}
}
