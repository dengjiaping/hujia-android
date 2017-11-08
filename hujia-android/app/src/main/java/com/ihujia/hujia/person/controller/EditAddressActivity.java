package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.AddressEntity;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.utils.InputUtil;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.IdentityHashMap;

/**
 * Created by zhaoweiwei on 2016/12/26.
 * 添加/修改收货地址
 */
public class EditAddressActivity extends ToolBarActivity implements View.OnClickListener {

	private static final int REQUEST_NET_UPDATE_ADDRESS = 10;
	private static final int RESOPNSE_SELECT_DISTRICT = 0X01;

	@ViewInject(R.id.edit_address_user)
	private EditText editAddressUser;
	@ViewInject(R.id.edit_address_phone)
	private EditText editAddressPhone;
	@ViewInject(R.id.edit_address_select_area)
	private RelativeLayout editAddressSelect;
	@ViewInject(R.id.edit_address_area)
	private TextView editAddressArea;
	@ViewInject(R.id.edit_address_detail)
	private EditText editAddressDetail;
	@ViewInject(R.id.edit_address_default)
	private TextView editAddressDefault;
	@ViewInject(R.id.edit_address_save)
	private TextView editAddressSave;

	private boolean isDefault;
	private String type;
	private String districtId;
	private String cityId;
	private String provinceId;
	private String receiveingId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_edit_address);
		ViewInjectUtils.inject(this);
		type = getIntent().getStringExtra(AddressMangerActivity.EXTRA_ADDRESS_TYPE);
		initView();
	}

	private void initView() {
		if (AddressMangerActivity.EXTRA_TYPE_EDIT.equals(type)) {
			AddressEntity entity = getIntent().getParcelableExtra(AddressMangerActivity.EXTRA_ADDRESS_ENTITY);
			receiveingId = entity.getReciveId();
			provinceId = entity.getProvinceId();
			cityId = entity.getCityId();
			districtId = entity.getDistrictId();
			editAddressUser.setText(entity.getUserName());
			editAddressPhone.setText(entity.getUserPhone());
			editAddressDetail.setText(entity.getAddress());
			editAddressArea.setText(entity.getProvinceName()+entity.getCityName()+entity.getDistrictName());
			if (entity.isDefault()) {
				editAddressDefault.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
				editAddressDefault.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.list_check_checked, 0, 0, 0);
			} else {
				editAddressDefault.setTextColor(ContextCompat.getColor(this, R.color.person_check_order_textcolor));
				editAddressDefault.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.list_check_normal, 0, 0, 0);
			}
			editAddressSave.setEnabled(true);
		}
		setLeftTitle(getString(R.string.address_edit));
		InputUtil.editIsEmpty(editAddressSave, editAddressUser, editAddressPhone, editAddressDetail);
		editAddressSelect.setOnClickListener(this);
		editAddressDefault.setOnClickListener(this);
		editAddressSave.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.edit_address_select_area:
				startActivityForResult(new Intent(this, SelectCityActivity.class), RESOPNSE_SELECT_DISTRICT);
				break;
			case R.id.edit_address_default:
				isDefault = !isDefault;
				if (isDefault) {
					editAddressDefault.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
					editAddressDefault.setCompoundDrawablesWithIntrinsicBounds(R.drawable.list_check_checked, 0, 0, 0);
				} else {
					editAddressDefault.setTextColor(ContextCompat.getColor(this, R.color.person_check_order_textcolor));
					editAddressDefault.setCompoundDrawablesWithIntrinsicBounds(R.drawable.list_check_normal, 0, 0, 0);
				}
				break;
			case R.id.edit_address_save:
				String phoneNumber = editAddressPhone.getText().toString();
				if (phoneNumber.length() == 11) {
					updateAddress(phoneNumber);
				} else {
					ToastUtil.shortShow(this,getString(R.string.address_input_real_phone));
				}
				break;
		}
	}

	/**
	 * 添加/修改收货地址
	 */
	private void updateAddress(String phone) {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("receiving_id", receiveingId);
		params.put("consignee", editAddressUser.getText().toString());
		params.put("contacts", phone);
		params.put("province", provinceId);
		params.put("city", cityId);
		params.put("district", districtId);
		params.put("address", editAddressDetail.getText().toString());
		if (isDefault) {
			params.put("is_default", "1");//1：是，0：不是
		} else {
			params.put("is_default", "0");//1：是，0：不是
		}
		if (AddressMangerActivity.EXTRA_TYPE_EDIT.equals(type)) {
			params.put("operation_type", "2");//1新增，2修改
		} else if (AddressMangerActivity.EXTRA_TYPE_ADD.equals(type)) {
			params.put("operation_type", "1");//1新增，2修改
		}
		params.put(Constants.USERID, UserCenter.getUserId(this));
		requestHttpData(Constants.Urls.URL_POST_UPDATE_ADDRESS, REQUEST_NET_UPDATE_ADDRESS, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			ToastUtil.shortShow(this, getString(R.string.address_alter_success));
			setResult(RESULT_OK);
			finish();
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		ToastUtil.shortShow(this, errorMessage);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			switch (requestCode) {
				case RESOPNSE_SELECT_DISTRICT:
					String provinceName = data.getStringExtra("provinceName");
					provinceId = data.getStringExtra("provinceId");
					String cityName = data.getStringExtra("cityName");
					cityId = data.getStringExtra("cityId");
					String destrictName = data.getStringExtra("districtName");
					districtId = data.getStringExtra("districtId");
					editAddressArea.setText(provinceName + cityName + destrictName);
					break;
			}
		}
	}
}
