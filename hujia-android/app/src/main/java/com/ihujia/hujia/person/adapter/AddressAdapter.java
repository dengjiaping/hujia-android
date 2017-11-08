package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.AddressEntity;

import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/25.
 */

public class AddressAdapter extends BaseAdapterNew<AddressEntity> {
	private View.OnClickListener onClickListener;
	private Resources resources;
	private static final String TAG = "AddressAdapter";

	public AddressAdapter(Context context, List<AddressEntity> mDatas, View.OnClickListener onClickListener) {
		super(context, mDatas);
		resources = context.getResources();
		this.onClickListener = onClickListener;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.person_address_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		AddressEntity entity = getItem(position);
		TextView userName = ViewHolder.get(convertView, R.id.address_item_username);
		TextView phone = ViewHolder.get(convertView, R.id.address_item_phone);
		TextView address = ViewHolder.get(convertView, R.id.address_item_address);
		TextView defaultAddress = ViewHolder.get(convertView, R.id.address_item_default);
		TextView edit = ViewHolder.get(convertView, R.id.address_item_edit);
		TextView deleteAddress = ViewHolder.get(convertView, R.id.address_item_delete);
		userName.setText(entity.getUserName());
		phone.setText(entity.getUserPhone());
		address.setText(entity.getAddress());

		edit.setTag(entity);
		edit.setOnClickListener(onClickListener);

		deleteAddress.setTag(entity);
		deleteAddress.setOnClickListener(onClickListener);

		//1是默认 0不是默认
		if (entity.isDefault()) {
			defaultAddress.setTextColor(resources.getColor(R.color.primary_color));
			defaultAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.list_check_checked, 0, 0, 0);
		} else {
			defaultAddress.setTextColor(resources.getColor(R.color.person_check_order_textcolor));
			defaultAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.list_check_normal, 0, 0, 0);
			defaultAddress.setTag(entity);
			defaultAddress.setOnClickListener(onClickListener);
		}
		userName.setText(entity.getUserName());
		phone.setText(entity.getUserPhone());
		address.setText(entity.getProvinceName() + entity.getCityName() + entity.getDistrictName() + entity.getAddress());
	}
}
