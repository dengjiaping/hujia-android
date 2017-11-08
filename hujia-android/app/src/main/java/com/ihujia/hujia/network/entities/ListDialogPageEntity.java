package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/3/27.
 */

public class ListDialogPageEntity<T> extends PagesEntity<T>implements Parcelable{
	private List<ListDialogEntity> entities;

	public ListDialogPageEntity() {
	}

	public ListDialogPageEntity(List<ListDialogEntity> entities) {
		this.entities = entities;
	}

	protected ListDialogPageEntity(Parcel in) {
		entities = in.createTypedArrayList(ListDialogEntity.CREATOR);
	}

	public static final Creator<ListDialogPageEntity> CREATOR = new Creator<ListDialogPageEntity>() {
		@Override
		public ListDialogPageEntity createFromParcel(Parcel in) {
			return new ListDialogPageEntity(in);
		}

		@Override
		public ListDialogPageEntity[] newArray(int size) {
			return new ListDialogPageEntity[size];
		}
	};

	public List<ListDialogEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<ListDialogEntity> entities) {
		this.entities = entities;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(entities);
	}
}
