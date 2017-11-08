package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2016/12/23.
 */

public class CouponEntity implements Parcelable{
	@SerializedName("coupon_id")
	private String couponId;
	@SerializedName("coupon_amount")
	private String value;//优惠券金额
	@SerializedName("full_amount")
	private String fullAmount;//满减
	@SerializedName("coupon_name")
	private String name;//优惠券名字
	@SerializedName("start_date")
	private String startData;//开始日期
	@SerializedName("end_date")
	private String endData;//截止日期
	@SerializedName("coupon_status")
	private String state;//优惠券类型  1 未领取  2 已领取  3已使用 4 已过期
	@SerializedName("coupon_type")
	private String type;//优惠券类型、1:通用劵2:满减卷

	protected CouponEntity(Parcel in) {
		couponId = in.readString();
		value = in.readString();
		fullAmount = in.readString();
		name = in.readString();
		startData = in.readString();
		endData = in.readString();
		state = in.readString();
		type = in.readString();
	}

	public static final Creator<CouponEntity> CREATOR = new Creator<CouponEntity>() {
		@Override
		public CouponEntity createFromParcel(Parcel in) {
			return new CouponEntity(in);
		}

		@Override
		public CouponEntity[] newArray(int size) {
			return new CouponEntity[size];
		}
	};

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFullAmount() {
		return fullAmount;
	}

	public void setFullAmount(String fullAmount) {
		this.fullAmount = fullAmount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartData() {
		return startData;
	}

	public void setStartData(String startData) {
		this.startData = startData;
	}

	public String getEndData() {
		return endData;
	}

	public void setEndData(String endData) {
		this.endData = endData;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(couponId);
		dest.writeString(value);
		dest.writeString(fullAmount);
		dest.writeString(name);
		dest.writeString(startData);
		dest.writeString(endData);
		dest.writeString(state);
		dest.writeString(type);
	}
}
