package com.ihujia.hujia.person.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.base.BaseFragment;
import com.ihujia.hujia.home.controller.ConfirmChooseActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.CommitOrderEntity;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.ListDialogPageEntity;
import com.ihujia.hujia.network.entities.OrderItemEntity;
import com.ihujia.hujia.network.entities.PagesEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.adapter.OrderListAdapter;
import com.ihujia.hujia.utils.ListDialogDatasUtil;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/21.
 * 订单列表
 */

public class OrderListFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
	public static final String EXTRA_ORDER_ITEM = "extra_order_item";
	private static final int REQUEST_ORDER_CODE = 0X01;
	private static final int REQUEST_ORDER_CODE_MORE = 0X02;
	private static final int REQUEST_CANCEL_ORDER = 0x03;
	private static final int REQUEST_QUERY_ORDER = 0x04;
	private static final int REQUEST_GET_PAYTYPE = 0X05;

	private int state;
	private FootLoadingListView loadingListView;
	private List<OrderItemEntity> entities;
	private OrderListAdapter adapter;
	private String operateOrderId;
	private String operateStatus;

	public void setArgs(int state) {
		this.state = state;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.person_frag_order, null);
		initLoadingView(this, view);
		setLoadingStatus(LoadingStatus.LOADING);
		loadingListView = (FootLoadingListView) view.findViewById(R.id.order_list);
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
		loadingListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadData(false);
	}

	private void loadData(boolean isMore) {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		int page = 1;
		int request = REQUEST_ORDER_CODE;
		if (isMore) {
			page = adapter.getPage() + 1;
			request = REQUEST_ORDER_CODE_MORE;
		}
		params.put("user_id", UserCenter.getUserId(getActivity()));
		params.put("order_status", String.valueOf(state));
		params.put(Constants.PAGENUM, String.valueOf(page));
		params.put(Constants.PAGESIZE, Constants.PAGE_SIZE_10);
		requestHttpData(Constants.Urls.URL_POST_ORDER, request, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		super.success(requestCode, data);
		closeProgressDialog();
		setLoadingStatus(LoadingStatus.GONE);
		loadingListView.setOnRefreshComplete();
		switch (requestCode) {
			case REQUEST_ORDER_CODE:
				PagesEntity<OrderItemEntity> pagesEntity = Parsers.getPageOrder(data);
				if (null != pagesEntity) {
					if (BaseActivity.REQUEST_NET_SUCCESS.equals(pagesEntity.getResultCode())) {
						entities = pagesEntity.getDatas();
						if (entities != null && entities.size() > 0) {
							adapter = new OrderListAdapter(getActivity(), entities, this);
							loadingListView.setAdapter(adapter);
							if (pagesEntity.getTotalPage() > adapter.getPage()) {
								loadingListView.setCanAddMore(true);
							} else {
								loadingListView.setCanAddMore(false);
							}
						} else {
							setLoadingStatus(LoadingStatus.EMPTY);
							mImgLoadingEmpty.setImageResource(R.drawable.loading_order_empty);
							mTxtLoadingEmpty.setText(getString(R.string.empty_loading_order));
						}
					} else {
						ToastUtil.shortShow(getActivity(), pagesEntity.getResultMsg());
					}
				}
				break;
			case REQUEST_ORDER_CODE_MORE:
				PagesEntity<OrderItemEntity> pagesEntityMore = Parsers.getPageOrder(data);
				if (null != pagesEntityMore) {
					if (BaseActivity.REQUEST_NET_SUCCESS.equals(pagesEntityMore.getResultCode())) {
						if (pagesEntityMore.getDatas().size() > 0) {
							adapter.addDatas(pagesEntityMore.getDatas());
							adapter.notifyDataSetChanged();
							if (pagesEntityMore.getTotalPage() > adapter.getPage()) {
								loadingListView.setCanAddMore(true);
							} else {
								loadingListView.setCanAddMore(false);
							}
						}
					} else {
						ToastUtil.shortShow(getActivity(), pagesEntityMore.getResultMsg());
					}
				}
				break;
			case REQUEST_CANCEL_ORDER:
			case REQUEST_QUERY_ORDER:
				Entity entity = Parsers.getResult(data);
				if (BaseActivity.REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
					loadData(false);
				} else {
					ToastUtil.shortShow(getActivity(), entity.getResultMsg());
				}
				break;
			case REQUEST_GET_PAYTYPE:
				CommitOrderEntity commitOrderEntity = Parsers.getCommitSuccess(data);
				if (BaseActivity.REQUEST_NET_SUCCESS.equals(commitOrderEntity.getResultCode())) {
					startActivity(new Intent(getActivity(), ConfirmChooseActivity.class).putExtra(ConfirmChooseActivity.EXTRA_COMMIT_SUCCESS_ENTITY, commitOrderEntity));
				} else {
					ToastUtil.shortShow(getActivity(), commitOrderEntity.getResultMsg());
				}
				break;
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		super.mistake(requestCode, status, errorMessage);
		closeProgressDialog();
		loadingListView.setOnRefreshComplete();
		if (REQUEST_ORDER_CODE == requestCode || REQUEST_ORDER_CODE_MORE == requestCode) {
			setLoadingStatus(LoadingStatus.RETRY);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.order_item_button1:
				//只有待收货订单显示此按钮，此点击事件只有查看物流
				OrderItemEntity entity = (OrderItemEntity) view.getTag();
				operateOrderId = entity.getOrderStoreId();
				LogisticsDetailActivity.startLogisticsDetailActivity(getActivity(), operateOrderId);
				break;
			case R.id.order_item_button2:
				//代付款和待收货订单显示该按钮
				OrderItemEntity entity2 = (OrderItemEntity) view.getTag();
				operateStatus = entity2.getState();
				operateOrderId = entity2.getOrderStoreId();
				if (String.valueOf(OrderListActivity.ORDERTYPE_PAYING).equals(operateStatus)) {//待支付
					IdentityHashMap<String, String> params = new IdentityHashMap<>();
					params.put("order_id", operateOrderId);
					params.put("order_total", entity2.getAllPrice());
					requestHttpData(Constants.Urls.URL_POST_GET_PAYTYPE, REQUEST_GET_PAYTYPE, FProtocol.HttpMethod.POST, params);
					//立即付款
				} else if (String.valueOf(OrderListActivity.ORDERTYPE_DELIVER).equals(operateStatus)) {//待发货
					ListDialogPageEntity pageEntity = ListDialogDatasUtil.createOrderDatas();
					ListDialogActivity.startListDialogActivity(getActivity(), pageEntity);
				} else if (String.valueOf(OrderListActivity.ORDERTYPE_GETING).equals(operateStatus)) {//待收货
					//确认收货
					IdentityHashMap<String, String> params = new IdentityHashMap<>();
					params.put(Constants.USERID, UserCenter.getUserId(getActivity()));
					params.put("order_id", operateOrderId);
					requestHttpData(Constants.Urls.URL_POST_QUERY_ORDER, REQUEST_QUERY_ORDER, FProtocol.HttpMethod.POST, params);
				}
				break;
			case R.id.loading_layout:
				loadData(false);
				setLoadingStatus(LoadingStatus.GONE);
				showProgressDialog();
				break;
		}
	}

	private void cancelOrder() {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put(Constants.USERID, UserCenter.getUserId(getActivity()));
		params.put("order_id", operateOrderId);
		params.put("status", operateStatus);
		requestHttpData(Constants.Urls.URL_POST_CANCEL_ORDER, REQUEST_CANCEL_ORDER, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
		intent.putExtra(EXTRA_ORDER_ITEM, entities.get(i));
		startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK == resultCode) {
			switch (requestCode) {
				case ListDialogActivity.REQUEST_CHOOSE_ITEM:
					cancelOrder();
					break;
			}
		}
	}
}
