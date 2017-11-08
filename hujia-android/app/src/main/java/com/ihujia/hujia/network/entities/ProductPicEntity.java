package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/2/15.
 */

public class ProductPicEntity implements Parcelable{
	@SerializedName("pic_url")
	private String url;

	protected ProductPicEntity(Parcel in) {
		url = in.readString();
	}

	public static final Creator<ProductPicEntity> CREATOR = new Creator<ProductPicEntity>() {
		@Override
		public ProductPicEntity createFromParcel(Parcel in) {
			return new ProductPicEntity(in);
		}

		@Override
		public ProductPicEntity[] newArray(int size) {
			return new ProductPicEntity[size];
		}
	};

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(url);
	}
}
