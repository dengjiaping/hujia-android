package com.ihujia.hujia.person.adapter;

import android.view.ViewGroup;

import com.common.utils.StringUtil;
import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.MessageEntity;

import java.util.List;

/**
 * Created by liuzhichao on 2017/2/26.
 * 我的资产
 */
public class AssetsMsgAdapter extends BaseRecycleAdapter<MessageEntity> {

    public AssetsMsgAdapter(List<MessageEntity> datas) {
        super(datas);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(parent, R.layout.item_assets_msg);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        MessageEntity messageEntity = getItemData(position);

        String[] contentArray = new String[]{"", ""};
        String content = messageEntity.getAssetContent();
        if (!StringUtil.isEmpty(content)) {
            contentArray = content.split(";");
        }
        holder.setText(R.id.tv_assets_msg_date, messageEntity.getMessageTime());
        holder.setText(R.id.tv_assets_msg_title, messageEntity.getName());
        holder.setText(R.id.tv_assets_msg_content, contentArray[0]);
        holder.setText(R.id.tv_assets_msg_amount, contentArray[1]);
    }
}
