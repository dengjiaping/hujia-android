package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/2/16.
 */

public class GoodsAttrEntity implements Parcelable{
	@SerializedName("value_id")
	private String attrId;
	@SerializedName("attr_name")
	private String attrName;
	@SerializedName("value_name")
	private String attrValue;

	protected GoodsAttrEntity(Parcel in) {
		attrId = in.readString();
		attrName = in.readString();
		attrValue = in.readString();
	}

	public static final Creator<GoodsAttrEntity> CREATOR = new Creator<GoodsAttrEntity>() {
		@Override
		public GoodsAttrEntity createFromParcel(Parcel in) {
			return new GoodsAttrEntity(in);
		}

		@Override
		public GoodsAttrEntity[] newArray(int size) {
			return new GoodsAttrEntity[size];
		}
	};

	public String getAttrId() {
		return attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(attrId);
		dest.writeString(attrName);
		dest.writeString(attrValue);
	}
}
