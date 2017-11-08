package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/2/22.
 */

public class CommitOrderEntity implements Parcelable{
	@SerializedName("account_balance")
	private String accountBalance;
	@SerializedName("order_id")
	private String OrderId;
	@SerializedName("order_name")
	private String OrderName;
	@SerializedName("order_total")
	private String OrderTotal;
	@SerializedName("pay_type")
	private String payType;
	@SerializedName("result_code")
	private String resultCode;
	@SerializedName("result_desc")
	private String resultMsg;


	protected CommitOrderEntity(Parcel in) {
		accountBalance = in.readString();
		OrderId = in.readString();
		OrderName = in.readString();
		OrderTotal = in.readString();
		payType = in.readString();
		resultCode = in.readString();
		resultMsg = in.readString();
	}

	public static final Creator<CommitOrderEntity> CREATOR = new Creator<CommitOrderEntity>() {
		@Override
		public CommitOrderEntity createFromParcel(Parcel in) {
			return new CommitOrderEntity(in);
		}

		@Override
		public CommitOrderEntity[] newArray(int size) {
			return new CommitOrderEntity[size];
		}
	};

	public String getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getOrderName() {
		return OrderName;
	}

	public void setOrderName(String orderName) {
		OrderName = orderName;
	}

	public String getOrderTotal() {
		return OrderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		OrderTotal = orderTotal;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(accountBalance);
		dest.writeString(OrderId);
		dest.writeString(OrderName);
		dest.writeString(OrderTotal);
		dest.writeString(payType);
		dest.writeString(resultCode);
		dest.writeString(resultMsg);
	}
}
