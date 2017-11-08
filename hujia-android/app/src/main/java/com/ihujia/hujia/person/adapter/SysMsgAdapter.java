package com.ihujia.hujia.person.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.MessageEntity;

import java.util.List;

/**
 * Created by liuzhichao on 2017/2/26.
 * 系统消息
 */
public class SysMsgAdapter extends BaseRecycleAdapter<MessageEntity> {

	private View.OnClickListener onClickListener;

	public SysMsgAdapter(List<MessageEntity> datas, View.OnClickListener onClickListener) {
		super(datas);
		this.onClickListener = onClickListener;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.item_sys_msg);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		MessageEntity messageEntity = getItemData(position);
		holder.setText(R.id.tv_sys_msg_date, messageEntity.getDate());
		holder.setText(R.id.tv_sys_msg_title, messageEntity.getName());
		holder.setText(R.id.tv_sys_msg_content, messageEntity.getContent());
		holder.setTag(R.id.cv_sys_msg_detail, messageEntity);
		holder.setOnClickListener(R.id.cv_sys_msg_detail, onClickListener);
	}
}
