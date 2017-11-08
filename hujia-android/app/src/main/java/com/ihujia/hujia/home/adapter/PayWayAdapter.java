package com.ihujia.hujia.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.PayWayEntity;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/1/10.
 */

public class PayWayAdapter extends BaseAdapterNew<PayWayEntity> {
	public PayWayAdapter(Context context, List<PayWayEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.pay_way_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		PayWayEntity entity = getItem(position);
		ImageView logo = ViewHolder.get(convertView, R.id.pay_way_logo);
		TextView text = ViewHolder.get(convertView, R.id.pay_way_text);
		TextView introduce = ViewHolder.get(convertView, R.id.pay_way_introduce);
		RadioButton checked = ViewHolder.get(convertView, R.id.pay_way_button);
		if (entity.isChecked()) {
			checked.setChecked(true);
		} else {
			checked.setChecked(false);
		}
		String type = entity.getType();
		if ("1".equals(type)) {//支付宝
			logo.setImageResource(R.drawable.pay_zhifubao);
			introduce.setVisibility(View.GONE);
		} else if ("2".equals(type)) {//微信
			logo.setImageResource(R.drawable.pay_wechart);
			introduce.setVisibility(View.GONE);
		} else if ("3".equals(type)) {//贵宾卡
			logo.setImageResource(R.drawable.pay_vipcard);
			introduce.setVisibility(View.VISIBLE);
			introduce.setText(getContext().getString(R.string.pay_vip_explain));
		} else {
			introduce.setVisibility(View.GONE);
		}
		text.setText(entity.getName());
	}
}
