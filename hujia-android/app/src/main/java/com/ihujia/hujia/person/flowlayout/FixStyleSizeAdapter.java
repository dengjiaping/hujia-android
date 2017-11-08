package com.ihujia.hujia.person.flowlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihujia.hujia.R;


/**
 * 尺寸流布局
 */
public class FixStyleSizeAdapter extends FlowLayoutAdapter<String> {

	private final Context mContext;

	public FixStyleSizeAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.person_fixstyle_size_item, null);
		TextView textView = (TextView) view.findViewById(R.id.shopcar_fixstyle_size_tv);
		String t = mDataList.get(position);
		textView.setText(t);
		return view;
	}
}
