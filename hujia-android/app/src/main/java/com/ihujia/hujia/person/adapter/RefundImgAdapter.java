package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.common.utils.BitmapUtil;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.utils.CommonTools;
import com.ihujia.hujia.utils.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhaoweiwei on 2017/3/22.
 */

public class RefundImgAdapter extends RecyclerView.Adapter<RefundImgAdapter.MyViewHolder> {
	private Context context;
	private List<String> urls;
	private View.OnClickListener onClickListener;

	public RefundImgAdapter(Context context, List<String> urls, View.OnClickListener onClickListener) {
		this.context = context;
		this.urls = urls;
		this.onClickListener = onClickListener;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.torefund_photo_item, null);
		MyViewHolder viewHolder = new MyViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		if (urls.size() == 1) {
			holder.photo.setImageURI("");
			holder.delete.setVisibility(View.GONE);
			holder.photo.setOnClickListener(onClickListener);
		} else {
			if (StringUtil.isEmpty(urls.get(position))) {
				holder.photo.setImageURI("");
				holder.delete.setVisibility(View.GONE);
				holder.photo.setOnClickListener(onClickListener);
			} else {
				holder.delete.setVisibility(View.VISIBLE);
				ImageUtils.setSmallImg(holder.photo, urls.get(position));
				String path = urls.get(position);
				File file = new File(path);
				if (file.exists()) {
					try {
						Bitmap bitmap = BitmapUtil.revitionImageSize(path, CommonTools.dp2px(context, 60), CommonTools.dp2px(context, 60));
						holder.photo.setImageBitmap(bitmap);
					} catch (IOException e) {
						e.printStackTrace();
						ToastUtil.shortShow(context, "文件压缩失败");
					}
				}
			}
		}

		holder.delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				urls.remove(position);
				if (urls.size()<4 && !StringUtil.isEmpty(urls.get(urls.size()-1))) {
					urls.add("");
				}
				notifyDataSetChanged();
			}
		});
	}

	@Override
	public int getItemCount() {
		return urls.size();
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		private SimpleDraweeView photo;
		private ImageView delete;

		public MyViewHolder(View itemView) {
			super(itemView);
			photo = (SimpleDraweeView) itemView.findViewById(R.id.torefund_photo_item_img);
			delete = (ImageView) itemView.findViewById(R.id.torefund_photo_item_delete);
		}
	}
}
