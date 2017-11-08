package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/2/9.
 * 购物车店铺
 */
public class ShopCarStoreEntity implements Parcelable {

	private boolean isChecked;
	@SerializedName("store_id")
	private String storeId;
	@SerializedName("store_name")
	private String storeName;
	@SerializedName("goods_num")
	private String goodsNum;
	@SerializedName("order_total")
	private String orderTotal;
	@SerializedName("logistics_fee")
	private String logisticFee;//配送
	@SerializedName("logistics_id")
	private String logisticId;
	@SerializedName("coupon_id")
	private String couponId;
	@SerializedName("amount")
	private String couponAmount;
	@SerializedName("coupon_name")
	private String couponName;
	@SerializedName("goods_list")
	private List<ShopCarGoodEntity> entities;

	protected ShopCarStoreEntity(Parcel in) {
		isChecked = in.readByte() != 0;
		storeId = in.readString();
		storeName = in.readString();
		goodsNum = in.readString();
		orderTotal = in.readString();
		logisticFee = in.readString();
		logisticId = in.readString();
		couponId = in.readString();
		couponAmount = in.readString();
		couponName = in.readString();
		entities = in.createTypedArrayList(ShopCarGoodEntity.CREATOR);
	}

	public static final Creator<ShopCarStoreEntity> CREATOR = new Creator<ShopCarStoreEntity>() {
		@Override
		public ShopCarStoreEntity createFromParcel(Parcel in) {
			return new ShopCarStoreEntity(in);
		}

		@Override
		public ShopCarStoreEntity[] newArray(int size) {
			return new ShopCarStoreEntity[size];
		}
	};

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}

	public String getLogisticFee() {
		return logisticFee;
	}

	public void setLogisticFee(String logisticFee) {
		this.logisticFee = logisticFee;
	}

	public String getLogisticId() {
		return logisticId;
	}

	public void setLogisticId(String logisticId) {
		this.logisticId = logisticId;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(String couponAmount) {
		this.couponAmount = couponAmount;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public List<ShopCarGoodEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<ShopCarGoodEntity> entities) {
		this.entities = entities;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (isChecked ? 1 : 0));
		dest.writeString(storeId);
		dest.writeString(storeName);
		dest.writeString(goodsNum);
		dest.writeString(orderTotal);
		dest.writeString(logisticFee);
		dest.writeString(logisticId);
		dest.writeString(couponId);
		dest.writeString(couponAmount);
		dest.writeString(couponName);
		dest.writeTypedList(entities);
	}
}
