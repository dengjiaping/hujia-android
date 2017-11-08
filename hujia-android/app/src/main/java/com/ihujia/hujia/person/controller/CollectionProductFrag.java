package com.ihujia.hujia.person.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.RefreshRecyclerView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.base.BaseFragment;
import com.ihujia.hujia.home.controller.ProductDetailActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.PagesEntity;
import com.ihujia.hujia.network.entities.RecommendItemEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.adapter.CollectionAdapter;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/25.
 * 我的收藏
 */

public class CollectionProductFrag extends BaseFragment implements View.OnClickListener, RefreshRecyclerView.OnRefreshAndLoadMoreListener {
	private static final int REQUEST_COLLECTION_CODE = 0X01;
	private static final int REQUEST_COLLECTION_CODE_MORE = 0X02;
	private static final int REQUEST_COLLECTION_DELETE_CODE = 0X03;

	@ViewInject(R.id.collection_recycleview)
	private RefreshRecyclerView collectionRecycleView;

	private CollectionAdapter adapter;
	private int deletePosition;
	private boolean isEnable;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.person_frag_collection_product, null);
		ViewInjectUtils.inject(this, view);
		initLoadingView(this, view);
		initView();
		loadData(false);
		setLoadingStatus(LoadingStatus.LOADING);
		return view;
	}

	private void initView() {
		collectionRecycleView.setHasFixedSize(true);
		collectionRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
		collectionRecycleView.setMode(RefreshRecyclerView.Mode.BOTH);
		collectionRecycleView.setOnRefreshAndLoadMoreListener(this);
	}

	private void loadData(boolean isMore) {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		int page = 1;
		int request = REQUEST_COLLECTION_CODE;
		if (isMore) {
			page = adapter.getPage() + 1;
			request = REQUEST_COLLECTION_CODE_MORE;
		}
		params.put("user_id", UserCenter.getUserId(getActivity()));
		params.put(Constants.PAGENUM, String.valueOf(page));
		params.put(Constants.PAGESIZE, Constants.PAGE_SIZE_20);
		requestHttpData(Constants.Urls.URL_POST_COLLECTION, request, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.cloth_item_delete:
				showAlertDialog("提示", "确定取消该收藏吗？", false, getString(R.string.cancel), getString(R.string.button_ok), v -> closeDialog(), v -> {
					deletePosition = (int) view.getTag();
					RecommendItemEntity deleteEntity = adapter.getDatas().get(deletePosition);
					showProgressDialog();
					IdentityHashMap<String, String> params = new IdentityHashMap<>();
					params.put("user_id", UserCenter.getUserId(getActivity()));
					params.put("goods_id", deleteEntity.getId());
					requestHttpData(Constants.Urls.URL_POST_COLLECTION_DELETE, REQUEST_COLLECTION_DELETE_CODE, FProtocol.HttpMethod.POST, params);
					closeDialog();
				});
				break;
			case R.id.home_recommend_layout:
				RecommendItemEntity entity = (RecommendItemEntity) view.getTag();
				if (entity != null) {
					ProductDetailActivity.startProductDetailActivity(getActivity(), entity.getId());
				} else {
					ToastUtil.shortShow(getActivity(), "数据异常");
				}
				break;
			case R.id.loading_layout:
				loadData(false);
				break;
		}
	}

	@Override
	public void success(int requestCode, String data) {
		super.success(requestCode, data);
		setLoadingStatus(LoadingStatus.GONE);
		switch (requestCode) {
			case REQUEST_COLLECTION_CODE:
				collectionRecycleView.resetStatus();
				PagesEntity<RecommendItemEntity> pagesEntity = Parsers.getPageCollection(data);
				if (null != pagesEntity) {
					if (BaseActivity.REQUEST_NET_SUCCESS.equals(pagesEntity.getResultCode())) {
						if (pagesEntity.getDatas().size() > 0) {
							List<RecommendItemEntity> commendItemEntities = pagesEntity.getDatas();
							adapter = new CollectionAdapter(getActivity(), commendItemEntities, this);
							collectionRecycleView.setAdapter(adapter);
							if (pagesEntity.getTotalPage() > adapter.getPage()) {
								collectionRecycleView.setCanAddMore(true);
							} else {
								collectionRecycleView.setCanAddMore(false);
							}
							isEnable = true;
//							setEditStatus("编辑");
						} else {
							setLoadingStatus(LoadingStatus.EMPTY);
							mImgLoadingEmpty.setImageResource(R.drawable.loading_collection_empty);
							mTxtLoadingEmpty.setText(getString(R.string.empty_loading_collection));
							isEnable = false;
//							setEditStatus("编辑");
						}
					} else {
						ToastUtil.shortShow(getActivity(), pagesEntity.getResultMsg());
					}
				}
				break;
			case REQUEST_COLLECTION_CODE_MORE:
				collectionRecycleView.resetStatus();
				PagesEntity<RecommendItemEntity> pagesEntity1 = Parsers.getPageCollection(data);
				if (null != pagesEntity1) {
					if (BaseActivity.REQUEST_NET_SUCCESS.equals(pagesEntity1.getResultCode())) {
						if (pagesEntity1.getDatas().size() > 0) {
							adapter.addDatas(pagesEntity1.getDatas());
							adapter.notifyDataSetChanged();
							if (pagesEntity1.getTotalPage() > adapter.getPage()) {
								collectionRecycleView.setCanAddMore(true);
							} else {
								collectionRecycleView.setCanAddMore(false);
							}
						}
					} else {
						ToastUtil.shortShow(getActivity(), pagesEntity1.getResultMsg());
					}
				}
				break;
			case REQUEST_COLLECTION_DELETE_CODE:
				closeProgressDialog();
				Entity entity = Parsers.getResult(data);
				if (BaseActivity.REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
					adapter.getDatas().remove(deletePosition);
					adapter.notifyDataSetChanged();
					if (adapter.getDatas().size() < 1) {
						setLoadingStatus(LoadingStatus.EMPTY);
						mImgLoadingEmpty.setImageResource(R.drawable.loading_collection_empty);
						mTxtLoadingEmpty.setText(getString(R.string.empty_loading_collection));
						isEnable = false;
//						setEditStatus("编辑");
						((MyCollectionActivity)getActivity()).setEdit();
					}
				} else {
					ToastUtil.shortShow(getActivity(), entity.getResultMsg());
				}
				break;
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		if (REQUEST_COLLECTION_CODE == requestCode || REQUEST_COLLECTION_CODE_MORE == requestCode) {
			collectionRecycleView.resetStatus();
		}
		setLoadingStatus(LoadingStatus.RETRY);
	}

	@Override
	public void onRefresh() {
		loadData(false);
	}

	@Override
	public void onLoadMore() {
		loadData(true);
	}

//	private void setEditStatus(String content) {
//		TextView collectionEdit = (TextView) getActivity().findViewById(R.id.collection_edit);
//		collectionEdit.setText(content);
//		if (isEnable) {
//			collectionEdit.setEnabled(true);
//		} else {
//			collectionEdit.setEnabled(false);
//		}
//	}

	public void showDelete(boolean isShowDelete) {
		if (isEnable) {
			if (isShowDelete) {
				adapter.showDelete(true);
				collectionRecycleView.setMode(RefreshRecyclerView.Mode.DISABLED);
			} else {
				adapter.showDelete(false);
				collectionRecycleView.setMode(RefreshRecyclerView.Mode.BOTH);
			}
		}
	}

	public boolean getEnable(){
		return isEnable;
	}
}
