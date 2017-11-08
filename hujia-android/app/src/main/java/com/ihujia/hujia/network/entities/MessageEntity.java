package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/2/25.
 * 消息
 */
public class MessageEntity implements Parcelable {

	@SerializedName("message_title")
	private String name;
	@SerializedName("update_date")
	private String date;
	@SerializedName("message_content")
	private String content;
	@SerializedName("goods_name")
	private String productName;
	@SerializedName("order_id")
	private String orderNo;
	@SerializedName("head_pic")
	private String image;
	@SerializedName("message_time")
	private String messageTime;
	@SerializedName("content")
	private String assetContent;

	protected MessageEntity(Parcel in) {
		name = in.readString();
		date = in.readString();
		content = in.readString();
		productName = in.readString();
		orderNo = in.readString();
		image = in.readString();
		messageTime = in.readString();
		assetContent = in.readString();
	}

	public static final Creator<MessageEntity> CREATOR = new Creator<MessageEntity>() {
		@Override
		public MessageEntity createFromParcel(Parcel in) {
			return new MessageEntity(in);
		}

		@Override
		public MessageEntity[] newArray(int size) {
			return new MessageEntity[size];
		}
	};

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}

	public String getAssetContent() {
		return assetContent;
	}

	public void setAssetContent(String assetContent) {
		this.assetContent = assetContent;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(name);
		parcel.writeString(date);
		parcel.writeString(content);
		parcel.writeString(productName);
		parcel.writeString(orderNo);
		parcel.writeString(image);
		parcel.writeString(messageTime);
		parcel.writeString(assetContent);
	}
}
