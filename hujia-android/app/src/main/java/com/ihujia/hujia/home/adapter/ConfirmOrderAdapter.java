package com.ihujia.hujia.home.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.common.utils.StringUtil;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.ShopCarGoodEntity;
import com.ihujia.hujia.network.entities.ShopCarStoreEntity;
import com.ihujia.hujia.person.adapter.OrderDetailAdapter;
import com.ihujia.hujia.utils.ListViewUtil;
import com.ihujia.hujia.utils.NumberFormatUtil;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/2/12.
 * 确认订单店铺列表
 */

public class ConfirmOrderAdapter extends BaseAdapterNew<ShopCarStoreEntity> {
	private Context context;
	private View.OnClickListener onClickListener;

	public ConfirmOrderAdapter(Context context, List<ShopCarStoreEntity> mDatas, View.OnClickListener onClickListener) {
		super(context, mDatas);
		this.context = context;
		this.onClickListener = onClickListener;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.confirm_order_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		ShopCarStoreEntity entity = getItem(position);

		TextView storeName = ViewHolder.get(convertView, R.id.confirm_order_store_name);
		ListView orderList = ViewHolder.get(convertView, R.id.confirm_order_list);
		TextView orderNum = ViewHolder.get(convertView, R.id.confirm_order_num);
		TextView orderPrice = ViewHolder.get(convertView, R.id.confirm_order_price);
		TextView logistics = ViewHolder.get(convertView, R.id.confirm_order_express);
		RelativeLayout couponLayout = ViewHolder.get(convertView, R.id.confirm_order_coupon_layout);
		TextView coupon = ViewHolder.get(convertView, R.id.confirm_order_coupon);

		if (entity != null) {
			if (!StringUtil.isEmpty(entity.getStoreName())) {
				storeName.setText(entity.getStoreName());
			}
			storeName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.person_order_store, 0, 0, 0);
			if (entity.getEntities() != null) {
				OrderDetailAdapter adapter = new OrderDetailAdapter(context, entity.getEntities());//店铺下的商品列表
				orderList.setAdapter(adapter);
				ListViewUtil.setListViewHeightBasedOnChildren(orderList);
			}
			int num = 0;
			double price = 0.0d;
			String logisticFee = entity.getLogisticFee();
			for (ShopCarGoodEntity shopGoodEntity : entity.getEntities()) {
				num += Integer.parseInt(shopGoodEntity.getCount());
				price += Double.parseDouble(shopGoodEntity.getCount()) * Double.parseDouble(shopGoodEntity.getGoodsPrice());
			}
			if (!StringUtil.isEmpty(logisticFee) || "0".equals(logisticFee)) {
				price += Double.parseDouble(logisticFee);
				logistics.setText(context.getString(R.string.price, entity.getLogisticFee()));
			} else {
				logistics.setText(context.getString(R.string.confirm_order_logistic_free));

			}

			String couponAmount = entity.getCouponAmount();
			String couponName = entity.getCouponName();
			if (StringUtil.isEmpty(couponAmount) || "0".equals(couponAmount)) {
				coupon.setText(context.getString(R.string.confirm_order_no_coupon));
				coupon.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				couponLayout.setEnabled(false);
			} else {
				coupon.setText(couponName);
				price -= Double.parseDouble(couponAmount);
				couponLayout.setTag(entity);
				couponLayout.setOnClickListener(onClickListener);
			}
			orderNum.setText(context.getString(R.string.confirm_order_goodsnum, String.valueOf(num)));
			String storePrice = context.getString(R.string.confirm_order_goodsprice, NumberFormatUtil.formatMoney(price));
			SpannableStringBuilder spanText = new SpannableStringBuilder(storePrice);
			spanText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.primary_color_red)), 3, storePrice.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			orderPrice.setText(spanText);
		}
	}
}
