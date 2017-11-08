package com.ihujia.hujia.otherstore.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.RefreshRecyclerView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.base.BaseFragment;
import com.ihujia.hujia.home.controller.ProductDetailActivity;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.OtherStoreDetailEntity;
import com.ihujia.hujia.network.entities.ProductEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.store.adapter.ProductAdapter;

import java.util.IdentityHashMap;

/**
 * Created by liuzhichao on 2017/2/10.
 * 第三方店铺详情-分类商品
 */
public class ProductFragment extends BaseFragment implements RefreshRecyclerView.OnRefreshAndLoadMoreListener, View.OnClickListener {

	private static final int REQUEST_NET_PRODUCT_LIST = 10;
	private static final int REQUEST_NET_PRODUCT_LIST_MORE = 20;
	public static final int TYPE_RECOMMEND = 1;
	public static final int TYPE_HOT= 2;
	public static final int TYPE_ALL = 3;

	private RefreshRecyclerView rrvFragProductList;

	private int type;//1-推荐商品、2-热销商品、3-全部商品
	private String id;
	private ProductAdapter adapter;

	public void setArgs(int type, String id) {
		this.type = type;
		this.id = id;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_product, null);
		rrvFragProductList = (RefreshRecyclerView) view.findViewById(R.id.rrv_frag_product_list);
		rrvFragProductList.setHasFixedSize(true);
		rrvFragProductList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
		rrvFragProductList.setMode(RefreshRecyclerView.Mode.BOTH);
		rrvFragProductList.setOnRefreshAndLoadMoreListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadData(false);
	}

	private void loadData(boolean isMore) {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		int page = 1;
		int requestCode = REQUEST_NET_PRODUCT_LIST;
		if (isMore) {
			page = adapter.getPage() + 1;
			requestCode = REQUEST_NET_PRODUCT_LIST_MORE;
		}
		params.put("store_id", id);
		params.put("goods_type", String.valueOf(type));
		params.put(Constants.PAGENUM, String.valueOf(page));
		params.put(Constants.PAGESIZE, Constants.PAGE_SIZE_20);
		requestHttpData(Constants.Urls.URL_POST_OTHER_STORE_DETAIL, requestCode, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		rrvFragProductList.resetStatus();
		Entity result = Parsers.getResult(data);
		if (BaseActivity.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case REQUEST_NET_PRODUCT_LIST:{
					OtherStoreDetailEntity otherStoreDetail = Parsers.getOtherStoreDetail(data);
					if (otherStoreDetail != null) {
						((OtherStoreDetailActivity) getActivity()).updateHead(otherStoreDetail);
						adapter = new ProductAdapter(getActivity(),otherStoreDetail.getDatas(), this,2);
						rrvFragProductList.setAdapter(adapter);
						if (otherStoreDetail.getTotalPage() > 1) {
							rrvFragProductList.setCanAddMore(true);
						} else {
							rrvFragProductList.setCanAddMore(false);
						}
					}
					break;
				}
				case REQUEST_NET_PRODUCT_LIST_MORE:{
					OtherStoreDetailEntity otherStoreDetail = Parsers.getOtherStoreDetail(data);
					if (otherStoreDetail != null) {
						((OtherStoreDetailActivity) getActivity()).updateHead(otherStoreDetail);
						adapter.addDatas(otherStoreDetail.getDatas());
						if (otherStoreDetail.getTotalPage() <= adapter.getPage()) {
							rrvFragProductList.setCanAddMore(false);
						}
					}
					break;
				}
			}
		} else {
			ToastUtil.shortShow(getActivity(), result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		rrvFragProductList.resetStatus();
		ToastUtil.shortShow(getActivity(), errorMessage);
	}

	@Override
	public void onRefresh() {
		loadData(false);
	}

	@Override
	public void onLoadMore() {
		loadData(true);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.home_recommend_layout) {
			ProductEntity productEntity = (ProductEntity) v.getTag();
			if (productEntity != null) {
				ProductDetailActivity.startProductDetailActivity(getActivity(), productEntity.getId());
			}
		}
	}
}
