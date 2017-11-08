package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.common.utils.StringUtil;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.CouponEntity;

import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/23.
 * 优惠券adapter
 */
public class CouponListAdapter extends BaseAdapterNew<CouponEntity> {
    private Resources res;
    private Context context;

    public CouponListAdapter(Context context, List<CouponEntity> mDatas) {
        super(context, mDatas);
        res = context.getResources();
        this.context = context;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.person_coupon_item;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        CouponEntity entity = getItem(position);

        RelativeLayout layout = ViewHolder.get(convertView, R.id.coupon_item_layout);
        TextView value = ViewHolder.get(convertView, R.id.coupon_item_value);
        TextView requirements = ViewHolder.get(convertView, R.id.coupon_item_requirements);
        TextView name = ViewHolder.get(convertView, R.id.coupon_item_name);
        TextView validity = ViewHolder.get(convertView, R.id.coupon_item_validity);
        ImageView status = ViewHolder.get(convertView, R.id.coupon_item_status);

        if (entity != null) {
            if (!StringUtil.isEmpty(entity.getValue())) {
                String couponAmount = context.getString(R.string.product_price,entity.getValue());
                SpannableStringBuilder ssb = new SpannableStringBuilder(couponAmount);
                ssb.setSpan(new AbsoluteSizeSpan(20, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                value.setText(ssb);
            }
            if (!StringUtil.isEmpty(entity.getFullAmount())) {
                requirements.setVisibility(View.VISIBLE);
                requirements.setText(res.getString(R.string.person_coupon_full_amount, entity.getFullAmount()));
            } else {
                requirements.setVisibility(View.GONE);
            }
            name.setText(entity.getName());
            validity.setText(res.getString(R.string.person_coupon_use_data, entity.getStartData(), entity.getEndData()));
            if ("1".equals(entity.getState())) {//未使用
                status.setVisibility(View.GONE);
                layout.setBackgroundResource(R.drawable.coupon_useable_bg);
            } else if ("2".equals(entity.getState())) {//已使用
                status.setVisibility(View.VISIBLE);
                layout.setBackgroundResource(R.drawable.coupon_unuseable_bg);
                status.setImageResource(R.drawable.person_coupon_used);
            } else if ("3".equals(entity.getState())) {//已过期
                status.setVisibility(View.VISIBLE);
                layout.setBackgroundResource(R.drawable.coupon_unuseable_bg);
                status.setImageResource(R.drawable.person_coupon_overdue);
            } else {
                status.setVisibility(View.GONE);
            }
        }
    }
}
