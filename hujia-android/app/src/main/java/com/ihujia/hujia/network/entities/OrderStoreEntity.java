package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/2/26.
 */

public class OrderStoreEntity implements Parcelable{
	@SerializedName("order_store_id")
	private String orderStoreId;
	@SerializedName("store_id")
	private String storeId;
	@SerializedName("store_name")
	private String storeName;
	@SerializedName("goods_list")
	private List<ShopCarGoodEntity> goodsList;

	protected OrderStoreEntity(Parcel in) {
		orderStoreId = in.readString();
		storeId = in.readString();
		storeName = in.readString();
		goodsList = in.createTypedArrayList(ShopCarGoodEntity.CREATOR);
	}

	public static final Creator<OrderStoreEntity> CREATOR = new Creator<OrderStoreEntity>() {
		@Override
		public OrderStoreEntity createFromParcel(Parcel in) {
			return new OrderStoreEntity(in);
		}

		@Override
		public OrderStoreEntity[] newArray(int size) {
			return new OrderStoreEntity[size];
		}
	};

	public String getOrderStoreId() {
		return orderStoreId;
	}

	public void setOrderStoreId(String orderStoreId) {
		this.orderStoreId = orderStoreId;
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

	public List<ShopCarGoodEntity> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<ShopCarGoodEntity> goodsList) {
		this.goodsList = goodsList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(orderStoreId);
		dest.writeString(storeId);
		dest.writeString(storeName);
		dest.writeTypedList(goodsList);
	}
}
