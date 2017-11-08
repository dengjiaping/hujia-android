package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.AddressEntity;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.PagesEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.adapter.AddressAdapter;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/25.
 * 地址管理
 */
public class AddressMangerActivity extends ToolBarActivity implements View.OnClickListener {

	public static final String EXTRA_ADDRESS_ENTITY = "extra_address_entity";
	public static final String EXTRA_ADDRESS_TYPE = "extra_address_type";
	public static final String EXTRA_TYPE_ADD = "extra_type_add";
	public static final String EXTRA_TYPE_EDIT = "extra_type_edit";
	private static final int REQUEST_NET_ADDRESS = 0x01;
	private static final int REQUEST_NET_ADDRESS_MORE = 0x02;
	private static final int REQUEST_NET_FIX_ADDRESS = 0x03;
	private static final int REQUEST_NET_DELETE_ADDRESS = 0x04;
	private static final int REQUEST_ADD_NEW_ADDRESS = 0x05;
	private static final int REQUEST_EDIT_NEW_ADDRESS = 0x06;

	@ViewInject(R.id.address_list)
	private FootLoadingListView addressList;
	@ViewInject(R.id.address_add_new)
	private TextView addressAddNew;

	private List<AddressEntity> entities;
	private AddressAdapter addressAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_address);
		ViewInjectUtils.inject(this);
		initView();
		loadData(false);
	}

	private void initView() {
		setLeftTitle(getString(R.string.person_my_address));
		addressAddNew.setOnClickListener(this);
		initLoadingView(this);
		setLoadingStatus(LoadingStatus.LOADING);
		addressList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData(false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData(true);
			}
		});
		addressList.setOnItemClickListener((parent, view, position, id) -> {
			Intent intent = getIntent();
			if (1 == intent.getIntExtra(EXTRA_FROM, 0)) {
				intent.putExtra("addressEntity", entities.get(position));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private void loadData(boolean isMore) {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put(Constants.USERID, UserCenter.getUserId(this));
		int request = REQUEST_NET_ADDRESS;
		int page = 1;
		if (isMore) {
			page = addressAdapter.getPage() + 1;
			request = REQUEST_NET_ADDRESS_MORE;
		}
		params.put(Constants.PAGENUM, String.valueOf(page));
		params.put(Constants.PAGESIZE, Constants.PAGE_SIZE_10);
		requestHttpData(Constants.Urls.URL_POST_ADDRESS_LIST, request, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.address_add_new:
				startActivityForResult(new Intent(this, EditAddressActivity.class).putExtra(EXTRA_ADDRESS_TYPE, EXTRA_TYPE_ADD), REQUEST_ADD_NEW_ADDRESS);
				break;
			case R.id.address_item_default:
				AddressEntity defaultEntity = (AddressEntity) view.getTag();
				setDefaultAddress(defaultEntity);
				break;
			case R.id.address_item_edit:
				AddressEntity editEntity = (AddressEntity) view.getTag();
				Intent editIntent = new Intent(this, EditAddressActivity.class);
				editIntent.putExtra(EXTRA_ADDRESS_TYPE, EXTRA_TYPE_EDIT);
				editIntent.putExtra(EXTRA_ADDRESS_ENTITY, editEntity);
				startActivityForResult(editIntent, REQUEST_EDIT_NEW_ADDRESS);
				break;
			case R.id.address_item_delete:
				AddressEntity deleteEntity = (AddressEntity) view.getTag();
				deleteAddress(deleteEntity.getReciveId());
				break;
			case R.id.loading_layout:
				loadData(false);
				break;
		}
	}

	private void setDefaultAddress(AddressEntity defaultEntity) {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put(Constants.USERID, UserCenter.getUserId(this));
		params.put("receiving_id", defaultEntity.getReciveId());
		params.put("consignee", defaultEntity.getUserName());
		params.put("contacts", defaultEntity.getUserPhone());
		params.put("province", defaultEntity.getProvinceId());
		params.put("city", defaultEntity.getCityId());
		params.put("district", defaultEntity.getDistrictId());
		params.put("address", defaultEntity.getAddress());
		params.put("is_default", "1");
		params.put("operation_type", "2");
		requestHttpData(Constants.Urls.URL_POST_UPDATE_ADDRESS, REQUEST_NET_FIX_ADDRESS, FProtocol.HttpMethod.POST, params);
	}

	/**
	 * @param addressId 删除收货地址的id
	 */
	private void deleteAddress(String addressId) {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("receiving_id", addressId);
		params.put(Constants.USERID, UserCenter.getUserId(this));
		requestHttpData(Constants.Urls.URL_POST_DELETE_ADDRESS, REQUEST_NET_DELETE_ADDRESS, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		setLoadingStatus(LoadingStatus.GONE);
		addressList.setOnRefreshComplete();
		switch (requestCode) {
			case REQUEST_NET_ADDRESS:
				PagesEntity<AddressEntity> pagesEntity = Parsers.getAddress(data);
				if (pagesEntity != null) {
					if (REQUEST_NET_SUCCESS.equals(pagesEntity.getResultCode())) {
						entities = pagesEntity.getDatas();
						if (entities != null && entities.size() > 0) {
							addressAdapter = new AddressAdapter(this, entities, this);
							addressList.setAdapter(addressAdapter);
							if (pagesEntity.getTotalPage() > addressAdapter.getTotalPage()) {
								addressList.setCanAddMore(true);
							} else {
								addressList.setCanAddMore(false);
							}
						} else {
							setLoadingStatus(LoadingStatus.EMPTY);
							mImgLoadingEmpty.setImageResource(R.drawable.loading_address_empty);
							mTxtLoadingEmpty.setText(getString(R.string.empty_loading_address));
						}
					} else {
						ToastUtil.shortShow(this, pagesEntity.getResultMsg());
					}
				}
				break;
			case REQUEST_NET_ADDRESS_MORE:
				PagesEntity<AddressEntity> pagesEntityMore = Parsers.getAddress(data);
				if (pagesEntityMore != null) {
					if (REQUEST_NET_SUCCESS.equals(pagesEntityMore.getResultCode())) {
						if (pagesEntityMore.getDatas() != null && pagesEntityMore.getDatas().size() > 0) {
							addressAdapter.addDatas(pagesEntityMore.getDatas());
							addressAdapter.notifyDataSetChanged();
							if (pagesEntityMore.getTotalPage() > addressAdapter.getTotalPage()) {
								addressList.setCanAddMore(true);
							} else {
								addressList.setCanAddMore(false);
							}
						}
					} else {
						ToastUtil.shortShow(this, pagesEntityMore.getResultMsg());
					}
				}
				break;
			case REQUEST_NET_DELETE_ADDRESS:
				setResult(RESULT_CANCELED, new Intent().putExtra("needRefresh", true));
			case REQUEST_NET_FIX_ADDRESS:
				Entity result = Parsers.getResult(data);
				if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
					loadData(false);
				} else {
					ToastUtil.shortShow(this, result.getResultMsg());
				}
				break;
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		ToastUtil.shortShow(this, errorMessage);
		if (REQUEST_NET_ADDRESS == requestCode || REQUEST_NET_ADDRESS_MORE == requestCode) {
			setLoadingStatus(LoadingStatus.RETRY);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			switch (requestCode) {
				case REQUEST_EDIT_NEW_ADDRESS:
					setResult(RESULT_CANCELED, new Intent().putExtra("needRefresh", true));
				case REQUEST_ADD_NEW_ADDRESS:
					loadData(false);
					break;
			}
		}
	}
}
