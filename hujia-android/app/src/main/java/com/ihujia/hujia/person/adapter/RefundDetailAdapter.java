package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.common.utils.StringUtil;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.RefundDetailEntity;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/3/26.
 * 退款进度
 */

public class RefundDetailAdapter extends BaseAdapterNew<RefundDetailEntity> {
    private Context context;

    public RefundDetailAdapter(Context context, List<RefundDetailEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.person_item_refund;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        RefundDetailEntity entity = getItem(position);
        TextView whose = ViewHolder.get(convertView, R.id.refund_item_whose);
        TextView time = ViewHolder.get(convertView, R.id.refund_item_time);
        TextView title = ViewHolder.get(convertView, R.id.refund_item_title);
        TextView content = ViewHolder.get(convertView, R.id.refund_item_content);

        if (entity != null) {
            String name = entity.getWhose();
            whose.setText(name);
            if ("买家".equals(name)) {
                whose.setCompoundDrawablesWithIntrinsicBounds(R.drawable.refund_customer, 0, 0, 0);
            } else {
                whose.setCompoundDrawablesWithIntrinsicBounds(R.drawable.refund_merchant, 0, 0, 0);
            }
            time.setText(entity.getTime());
            if (!StringUtil.isEmpty(entity.getTitle())) {
                title.setText(context.getString(R.string.person_refund_detail_title, entity.getTitle()));
            }
            if (!StringUtil.isEmpty(entity.getContent())) {
                content.setText(context.getString(R.string.person_refund_detail_content, entity.getContent()));
            }
        }
    }
}
