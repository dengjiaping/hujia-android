package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/2/28.
 * 订单详情实体
 */

public class OrderDetailPageEntity<T> extends PagesEntity<T> implements Parcelable{
	@SerializedName("order_id")
	private String orderId;
	@SerializedName("status")
	private String status;
	@SerializedName("consignee")
	private String userName;
	@SerializedName("contacts")
	private String userPhone;
	@SerializedName("address")
	private String userAddress;
	@SerializedName("order_price")
	private String goodsPrice; //商品金额
	@SerializedName("amount")
	private String couponPrice; // 优惠券金额
	@SerializedName("reveling_price")
	private String revelingPrice; // 运费
	@SerializedName("order_price")
	private String orderPrice; // 订单最终金额
	@SerializedName("create_date")
	private String creatTime; // 创建订单时间
	@SerializedName("order_out_itme")
	private String remainTime; //剩余时间
	@SerializedName("order_total")
	private String orderTotal;
	@SerializedName("order_store_list")
	private List<OrderStoreEntity> orderStoreEntities;

	public OrderDetailPageEntity() {
	}

	protected OrderDetailPageEntity(Parcel in) {
		orderId = in.readString();
		status = in.readString();
		userName = in.readString();
		userPhone = in.readString();
		userAddress = in.readString();
		goodsPrice = in.readString();
		couponPrice = in.readString();
		revelingPrice = in.readString();
		orderPrice = in.readString();
		creatTime = in.readString();
		remainTime = in.readString();
		orderTotal = in.readString();
		orderStoreEntities = in.createTypedArrayList(OrderStoreEntity.CREATOR);
	}

	public static final Creator<OrderDetailPageEntity> CREATOR = new Creator<OrderDetailPageEntity>() {
		@Override
		public OrderDetailPageEntity createFromParcel(Parcel in) {
			return new OrderDetailPageEntity(in);
		}

		@Override
		public OrderDetailPageEntity[] newArray(int size) {
			return new OrderDetailPageEntity[size];
		}
	};

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusName(){
		String statusName = "";
		switch (status) {
			case "1":
				statusName = "待支付";
				break;
			case "2":
				statusName = "待发货";
				break;
			case "3":
				statusName = "待收货";
				break;
			case "4":
				statusName = "已完成";
				break;
			case "9":
				statusName = "已关闭";
				break;
		}
		return statusName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(String couponPrice) {
		this.couponPrice = couponPrice;
	}

	public String getRevelingPrice() {
		return revelingPrice;
	}

	public void setRevelingPrice(String revelingPrice) {
		this.revelingPrice = revelingPrice;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public String getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(String remainTime) {
		this.remainTime = remainTime;
	}

	public String getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}

	public List<OrderStoreEntity> getOrderStoreEntities() {
		return orderStoreEntities;
	}

	public void setOrderStoreEntities(List<OrderStoreEntity> orderStoreEntities) {
		this.orderStoreEntities = orderStoreEntities;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(orderId);
		parcel.writeString(status);
		parcel.writeString(userName);
		parcel.writeString(userPhone);
		parcel.writeString(userAddress);
		parcel.writeString(goodsPrice);
		parcel.writeString(couponPrice);
		parcel.writeString(revelingPrice);
		parcel.writeString(orderPrice);
		parcel.writeString(creatTime);
		parcel.writeString(remainTime);
		parcel.writeString(orderTotal);
		parcel.writeTypedList(orderStoreEntities);
	}
}
