package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by zhaoweiwei on 2017/2/15.
 * 商品详情 商品颜色尺寸等属性
 */

public class ProductAttrEntity implements Parcelable{
	@SerializedName("attr_name")
	private String attrName;
	@SerializedName("atrr_vlaue_list")
	private ArrayList<GoodsAttrEntity> attrList;

	protected ProductAttrEntity(Parcel in) {
		attrName = in.readString();
		attrList = in.createTypedArrayList(GoodsAttrEntity.CREATOR);
	}

	public static final Creator<ProductAttrEntity> CREATOR = new Creator<ProductAttrEntity>() {
		@Override
		public ProductAttrEntity createFromParcel(Parcel in) {
			return new ProductAttrEntity(in);
		}

		@Override
		public ProductAttrEntity[] newArray(int size) {
			return new ProductAttrEntity[size];
		}
	};

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public ArrayList<GoodsAttrEntity> getAttrList() {
		return attrList;
	}

	public void setAttrList(ArrayList<GoodsAttrEntity> attrList) {
		this.attrList = attrList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(attrName);
		dest.writeTypedList(attrList);
	}
}
