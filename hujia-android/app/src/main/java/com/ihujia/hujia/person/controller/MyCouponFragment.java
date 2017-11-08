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
import android.widget.RadioButton;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.base.BaseFragment;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.CouponEntity;
import com.ihujia.hujia.network.entities.CouponPagesEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.adapter.CouponListAdapter;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/23.
 * 优惠券分类列表
 */
public class MyCouponFragment extends BaseFragment implements View.OnClickListener {

	private static final int REQUEST_COPON_CODE = 0X01;
	private static final int REQUEST_COPON_CODE_MORE = 0X02;

	private int state;
	private int from;

	private CouponListAdapter adapter;
	private FootLoadingListView loadingListView;
	private RadioButton couponUnused, couponUsed, couponOverdue;
	private List<CouponEntity> entities;

	public void setArgs(int state, int from, RadioButton couponUnused, RadioButton couponUsed, RadioButton couponOverdue) {
		this.state = state;
		this.from = from;
		this.couponUnused = couponUnused;
		this.couponUsed = couponUsed;
		this.couponOverdue = couponOverdue;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.person_frag_coupon, null);
		initLoadingView(this, view);
		loadingListView = (FootLoadingListView) view.findViewById(R.id.coupon_list);
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
		loadData(false);
		setLoadingStatus(LoadingStatus.LOADING);
		if (MyCouponActivity.COUPONTYPE_UNUSED == state) {
			loadingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					Intent intent = getActivity().getIntent();
					intent.putExtra("couponEntity",entities.get(i));
					getActivity().setResult(Activity.RESULT_OK,intent);
					getActivity().finish();
				}
			});
		}
		return view;
	}

	private void loadData(boolean isMore) {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		int page = 1;
		int request = REQUEST_COPON_CODE;
		if (isMore) {
			page = adapter.getPage() + 1;
			request = REQUEST_COPON_CODE_MORE;
		}
		params.put("user_id", UserCenter.getUserId(getActivity()));
		params.put("coupon_status", String.valueOf(state));
		params.put(Constants.PAGENUM, String.valueOf(page));
		params.put(Constants.PAGESIZE, Constants.PAGE_SIZE_10);
		requestHttpData(Constants.Urls.URL_POST_COUPON, request, FProtocol.HttpMethod.POST, params);
	}

	public void refresh() {
		loadData(false);
	}

	@Override
	public void success(int requestCode, String data) {
		super.success(requestCode, data);
		loadingListView.setOnRefreshComplete();
		setLoadingStatus(LoadingStatus.GONE);
		switch (requestCode) {
			case REQUEST_COPON_CODE:
				CouponPagesEntity<CouponEntity> pagesEntity = Parsers.getPageCoupon(data);
				if (BaseActivity.REQUEST_NET_SUCCESS.equals(pagesEntity.getResultCode())) {
					entities = pagesEntity.getDatas();
					if (entities != null && entities.size() > 0) {
						adapter = new CouponListAdapter(getActivity(), entities);
						loadingListView.setAdapter(adapter);
						changedNum(pagesEntity.getUnUsedNum(), pagesEntity.getUsedNum(), pagesEntity.getOverDueNum());
						if (pagesEntity.getTotalPage() > adapter.getPage()) {
							loadingListView.setCanAddMore(true);
						} else {
							loadingListView.setCanAddMore(false);
						}
					} else {
						setLoadingStatus(LoadingStatus.EMPTY);
						mImgLoadingEmpty.setImageResource(R.drawable.loading_coupon_empty);
						mTxtLoadingEmpty.setText(getString(R.string.empty_loading_coupon));
					}
				} else {
					ToastUtil.shortShow(getActivity(), pagesEntity.getResultMsg());
				}
				break;
			case REQUEST_COPON_CODE_MORE:
				CouponPagesEntity<CouponEntity> pagesEntityMore = Parsers.getPageCoupon(data);
				if (BaseActivity.REQUEST_NET_SUCCESS.equals(pagesEntityMore.getResultCode())) {
					entities.addAll(pagesEntityMore.getDatas());
					if (pagesEntityMore.getDatas() != null && pagesEntityMore.getDatas().size() > 0) {
						adapter.addDatas(pagesEntityMore.getDatas());
						adapter.notifyDataSetChanged();
						changedNum(pagesEntityMore.getUnUsedNum(), pagesEntityMore.getUsedNum(), pagesEntityMore.getOverDueNum());
						if (pagesEntityMore.getTotalPage() > adapter.getPage()) {
							loadingListView.setCanAddMore(true);
						} else {
							loadingListView.setCanAddMore(false);
						}
					}
				} else {
					ToastUtil.shortShow(getActivity(), pagesEntityMore.getResultMsg());
				}
				break;
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		super.mistake(requestCode, status, errorMessage);
		setLoadingStatus(LoadingStatus.RETRY);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.loading_layout:
				loadData(false);
				break;
		}
	}

	public void changedNum(String unUsedNum, String usedNum, String overDueNum) {
		couponUnused.setText(getString(R.string.person_coupon_title_unused, unUsedNum));
		couponUsed.setText(getString(R.string.person_coupon_title_used, usedNum));
		couponOverdue.setText(getString(R.string.person_coupon_title_overdue, overDueNum));
	}
}
