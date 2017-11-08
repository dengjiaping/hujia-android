package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.IntegralEntity;
import com.ihujia.hujia.network.entities.IntegralExchangeEntity;

import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/25.
 */

public class IntegralExchangeAdapter extends BaseAdapterNew<IntegralExchangeEntity> {
	private Context context;
	public IntegralExchangeAdapter(Context context, List<IntegralExchangeEntity> mDatas) {
		super(context, mDatas);
		this.context = context;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.integral_exchange_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		IntegralExchangeEntity entity = getItem(position);
		LinearLayout integralLayout = ViewHolder.get(convertView,R.id.integral_exchange_item);
		TextView money = ViewHolder.get(convertView,R.id.integral_exchange_money);
		TextView integral = ViewHolder.get(convertView,R.id.integral_exchange_integral);
		money.setText(entity.getExchangeMoney());
		integral.setText(entity.getExchangeIntegral());
		boolean isChecked = entity.isChecked();
		if (isChecked) {
			integralLayout.setBackgroundResource(R.drawable.person_charge_useable);
			money.setTextColor(context.getResources().getColor(R.color.primary_color_red));
			integral.setTextColor(context.getResources().getColor(R.color.primary_color_red));
		} else {
			integralLayout.setBackgroundResource(R.drawable.person_charge_unuseable);
			money.setTextColor(context.getResources().getColor(R.color.person_check_order_textcolor));
			integral.setTextColor(context.getResources().getColor(R.color.person_check_order_textcolor));
		}
	}
}
