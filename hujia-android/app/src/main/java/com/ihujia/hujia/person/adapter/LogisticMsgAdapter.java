package com.ihujia.hujia.person.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.MessageEntity;
import com.ihujia.hujia.utils.ImageUtils;

import java.util.List;

/**
 * Created by liuzhichao on 2017/2/26.
 * 物流通知
 */
public class LogisticMsgAdapter extends BaseRecycleAdapter<MessageEntity> {

	private View.OnClickListener onClickListener;

	public LogisticMsgAdapter(List<MessageEntity> datas, View.OnClickListener onClickListener) {
		super(datas);
		this.onClickListener = onClickListener;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.item_logistic_msg);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		MessageEntity messageEntity = getItemData(position);
		holder.setText(R.id.tv_logistic_msg_date, messageEntity.getDate());
		holder.setText(R.id.tv_logistic_msg_status, messageEntity.getName());
		holder.setText(R.id.tv_logistic_msg_title, messageEntity.getProductName());
		holder.setText(R.id.tv_logistic_msg_content, holder.getContext().getString(R.string.person_order_order_number, messageEntity.getOrderNo()));
		ImageUtils.setSmallImg(holder.getView(R.id.sdv_logistic_msg_img), messageEntity.getImage());
		holder.setTag(R.id.cv_logistic_msg_detail, messageEntity);
		holder.setOnClickListener(R.id.cv_logistic_msg_detail, onClickListener);
	}
}
