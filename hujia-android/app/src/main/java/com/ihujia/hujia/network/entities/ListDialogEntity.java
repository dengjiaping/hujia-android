package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/2/27.
 * 列表式弹窗
 */

public class ListDialogEntity implements Parcelable{
	@SerializedName("logisticsId")
	private String reasonId;
	@SerializedName("logisticsName")
	private String reasonContent;
	private boolean isSelected;

	public ListDialogEntity(String reasonId, String reasonContent) {
		this.reasonId = reasonId;
		this.reasonContent = reasonContent;
	}

	protected ListDialogEntity(Parcel in) {
		reasonId = in.readString();
		reasonContent = in.readString();
		isSelected = in.readByte() != 0;
	}

	public static final Creator<ListDialogEntity> CREATOR = new Creator<ListDialogEntity>() {
		@Override
		public ListDialogEntity createFromParcel(Parcel in) {
			return new ListDialogEntity(in);
		}

		@Override
		public ListDialogEntity[] newArray(int size) {
			return new ListDialogEntity[size];
		}
	};

	public String getReasonId() {
		return reasonId;
	}

	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}

	public String getReasonContent() {
		return reasonContent;
	}

	public void setReasonContent(String reasonContent) {
		this.reasonContent = reasonContent;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(reasonId);
		dest.writeString(reasonContent);
		dest.writeByte((byte) (isSelected ? 1 : 0));
	}
}
