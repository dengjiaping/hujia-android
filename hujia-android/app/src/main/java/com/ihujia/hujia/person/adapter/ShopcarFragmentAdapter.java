package com.ihujia.hujia.person.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.network.entities.GoodsAttrEntity;
import com.ihujia.hujia.network.entities.ShopCarGoodEntity;
import com.ihujia.hujia.network.entities.ShopCarStoreEntity;
import com.ihujia.hujia.person.shopcarInterface.CheckInterface;
import com.ihujia.hujia.person.shopcarInterface.ModifyCountInterface;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.NumberFormatUtil;
import com.ihujia.hujia.utils.ViewInjectUtils;
import com.ihujia.hujia.widget.AddMinuLayout;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/2/10.
 * 购物车店铺adapter
 */
public class ShopcarFragmentAdapter extends BaseExpandableListAdapter {

	private List<ShopCarStoreEntity> shopCarStoreEntities;
	private View.OnClickListener onClickListener;
	private Context context;
	private CheckInterface checkInterface;
	private ModifyCountInterface modifyCountInterface;
	private int page;

	public ShopcarFragmentAdapter(List<ShopCarStoreEntity> shopCarStoreEntities, Context context, View.OnClickListener onClickListener) {
		this.shopCarStoreEntities = shopCarStoreEntities;
		this.onClickListener = onClickListener;
		this.context = context;
	}

	public void setCheckInterface(CheckInterface checkInterface) {
		this.checkInterface = checkInterface;
	}

	public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
		this.modifyCountInterface = modifyCountInterface;
	}

	@Override
	public int getGroupCount() {
		return shopCarStoreEntities.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return shopCarStoreEntities.get(groupPosition).getEntities().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return shopCarStoreEntities.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return shopCarStoreEntities.get(groupPosition).getEntities().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupViewHolder groupViewHolder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.person_shopcar_store_list_item, null);
			groupViewHolder = new GroupViewHolder(convertView);
			convertView.setTag(groupViewHolder);
		} else {
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}

		ShopCarStoreEntity storeEntity = (ShopCarStoreEntity) getGroup(groupPosition);
		groupViewHolder.storeSelect.setChecked(storeEntity.isChecked());
		groupViewHolder.storeSelect.setOnClickListener(v -> {
			storeEntity.setChecked(groupViewHolder.storeSelect.isChecked());
			checkInterface.checkGroup(groupPosition, groupViewHolder.storeSelect.isChecked());
		});

		groupViewHolder.storeName.setText(storeEntity.getStoreName());
		groupViewHolder.shopStoreLayout.setTag(storeEntity.getStoreId());
		groupViewHolder.shopStoreLayout.setOnClickListener(onClickListener);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder childViewHolder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.person_shopcar_list_item, null);
			childViewHolder = new ChildViewHolder(convertView);
			convertView.setTag(childViewHolder);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
		ShopCarGoodEntity clothEntity = (ShopCarGoodEntity) getChild(groupPosition, childPosition);
		childViewHolder.clothEdit.setOnClickListener(v -> {
			childViewHolder.normalLayout.setVisibility(View.GONE);
			childViewHolder.editLayout.setVisibility(View.VISIBLE);
			childViewHolder.editOperate.setVisibility(View.VISIBLE);
			childViewHolder.clothEdit.setVisibility(View.GONE);
		});
		childViewHolder.editFinish.setOnClickListener(view -> {
			clothEntity.setCount(childViewHolder.addMinuLayout.getResult());
			childViewHolder.normalLayout.setVisibility(View.VISIBLE);
			childViewHolder.editLayout.setVisibility(View.GONE);
			childViewHolder.editOperate.setVisibility(View.GONE);
			childViewHolder.clothEdit.setVisibility(View.VISIBLE);
			modifyCountInterface.fixFinish(groupPosition, childPosition);
		});

		ImageUtils.setSmallImg(childViewHolder.orderItemImg, clothEntity.getGoodsPic());
		ImageUtils.setSmallImg(childViewHolder.shopcarItemEditImg, clothEntity.getGoodsPic());
		childViewHolder.clothSelect.setChecked(clothEntity.isChecked());
		childViewHolder.normalName.setText(clothEntity.getGoodsName());
		String color = null;
		String size = null;
		List<GoodsAttrEntity> attrEntities = clothEntity.getAttrList();
		if (attrEntities != null && attrEntities.size() > 0) {
			for (GoodsAttrEntity entity : attrEntities) {
				if (entity != null) {
					if (entity.getAttrName().contains("尺码")) {
						size = entity.getAttrValue();
					} else if (entity.getAttrName().contains("颜色")) {
						color = entity.getAttrValue();
					}
				}
			}
		}
		childViewHolder.normalColor.setText(context.getString(R.string.shopcar_color, color));
		childViewHolder.normalSize.setText(context.getString(R.string.shopcar_size, size));
		childViewHolder.editColor.setText(context.getString(R.string.shopcar_color, color));
		childViewHolder.editSize.setText(context.getString(R.string.shopcar_size, size));
		childViewHolder.normalPrice.setText(context.getString(R.string.price,  NumberFormatUtil.formatMoney(clothEntity.getGoodsPrice())));
		childViewHolder.normalPrice.setTextColor(context.getResources().getColor(R.color.primary_color_red));
		childViewHolder.normalNum.setText(context.getString(R.string.num, clothEntity.getCount()));
		childViewHolder.addMinuLayout.setResult(clothEntity.getCount());
		childViewHolder.addMinuLayout.setLimitNum(1, NumberFormatUtil.string2Int(clothEntity.getGoodsNum()));
		//颜色尺寸编辑图标的点击事件，弹出编辑界面
		childViewHolder.editColor.setTag(clothEntity);
		childViewHolder.editColor.setOnClickListener(onClickListener);
		childViewHolder.editSize.setTag(clothEntity);
		childViewHolder.editSize.setOnClickListener(onClickListener);
		childViewHolder.editFix.setTag(clothEntity);
		childViewHolder.editFix.setOnClickListener(onClickListener);

		//选择
		childViewHolder.clothSelect.setOnClickListener(v -> {
			clothEntity.setChecked(childViewHolder.clothSelect.isChecked());
			checkInterface.checkChild(groupPosition, childPosition, childViewHolder.clothSelect.isChecked());
		});
		//删除
		childViewHolder.editDelete.setOnClickListener(v -> {
			AlertDialog alert = new AlertDialog.Builder(context).create();
			alert.setTitle("操作提示");
			alert.setMessage("您确定要将这些商品从购物车中移除吗？");
			alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
					(dialog, which) -> {});
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
					(dialog, which) -> modifyCountInterface.childDelete(groupPosition, childPosition));
			alert.show();

		});
		childViewHolder.normalLayout.setTag(clothEntity.getGoodsId());
		childViewHolder.normalLayout.setOnClickListener(onClickListener);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	/**
	 * 使某个组处于编辑状态
	 * <p/>
	 * groupPosition组的位置
	 */
	class GroupViewClick implements View.OnClickListener {
		private int groupPosition;
		private Button edtor;
		private ShopCarStoreEntity group;

		public GroupViewClick(int groupPosition, Button edtor, ShopCarStoreEntity group) {
			this.groupPosition = groupPosition;
			this.edtor = edtor;
			this.group = group;
		}

		@Override
		public void onClick(View v) {
			int groupId = v.getId();
			if (groupId == edtor.getId()) {
				if (group.isChecked()) {
					group.setChecked(false);
				} else {
					group.setChecked(true);

				}
				notifyDataSetChanged();
			}
		}
	}

	public void addDatas(List<ShopCarStoreEntity> storeEntities) {
		shopCarStoreEntities.addAll(storeEntities);
		page++;
	}

	public int getPage() {
		return page;
	}

	private class GroupViewHolder {
		@ViewInject(R.id.shopcar_store_layout)
		private LinearLayout shopStoreLayout;
		@ViewInject(R.id.shopcar_store_checked)
		private CheckBox storeSelect;
		@ViewInject(R.id.shopcar_store_name)
		private TextView storeName;

		GroupViewHolder(View view) {
			ViewInjectUtils.inject(this, view);
		}
	}

	private class ChildViewHolder {
		@ViewInject(R.id.order_item_img)
		private SimpleDraweeView orderItemImg;
		@ViewInject(R.id.shopcar_item_edit_img)
		private SimpleDraweeView shopcarItemEditImg;
		@ViewInject(R.id.shopcar_item_checked)
		private CheckBox clothSelect;
		@ViewInject(R.id.shopcar_item_normal_operate)
		private TextView clothEdit;
		@ViewInject(R.id.shopcar_item_edit_operate)
		private LinearLayout editOperate;//编辑布局
		@ViewInject(R.id.shopcar_item_edit_finish)
		private TextView editFinish;
		@ViewInject(R.id.shopcar_item_edit_delete)
		private TextView editDelete;
		@ViewInject(R.id.shopcar_item_normal)
		private RelativeLayout normalLayout;//正常状态
		@ViewInject(R.id.order_item_name)
		private TextView normalName;
		@ViewInject(R.id.order_item_color)
		private TextView normalColor;
		@ViewInject(R.id.order_item_size)
		private TextView normalSize;
		@ViewInject(R.id.order_item_price)
		private TextView normalPrice;
		@ViewInject(R.id.order_item_num)
		private TextView normalNum;
		@ViewInject(R.id.shopcar_item_edit)
		private LinearLayout editLayout;//编辑状态
		@ViewInject(R.id.shopcar_item_edit_color)
		private TextView editColor;//编辑状态的颜色
		@ViewInject(R.id.shopcar_item_edit_size)
		private TextView editSize;//编辑状态的尺寸
		@ViewInject(R.id.shopcar_item_edit_fix)
		private ImageView editFix;//编辑图标
		@ViewInject(R.id.shopcar_item_num_change)
		private AddMinuLayout addMinuLayout;

		ChildViewHolder(View view) {
			ViewInjectUtils.inject(this, view);
		}
	}
}
