package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2016/12/25.
 */

public class AddressEntity implements Parcelable{
	@SerializedName("receiving_id")
	private String reciveId;
	@SerializedName("consignee")
	private String userName;
	@SerializedName("contacts")
	private String userPhone;
	@SerializedName("province")
	private String provinceId;
	@SerializedName("province_name")
	private String provinceName;
	@SerializedName("city")
	private String cityId;
	@SerializedName("city_name")
	private String cityName;
	@SerializedName("district")
	private String districtId;
	@SerializedName("district_name")
	private String districtName;
	@SerializedName("address")
	private String address;
	@SerializedName("is_default")
	private Boolean isDefault;


	protected AddressEntity(Parcel in) {
		reciveId = in.readString();
		userName = in.readString();
		userPhone = in.readString();
		provinceId = in.readString();
		provinceName = in.readString();
		cityId = in.readString();
		cityName = in.readString();
		districtId = in.readString();
		districtName = in.readString();
		address = in.readString();
		isDefault = in.readByte() != 0;
	}

	public static final Creator<AddressEntity> CREATOR = new Creator<AddressEntity>() {
		@Override
		public AddressEntity createFromParcel(Parcel in) {
			return new AddressEntity(in);
		}

		@Override
		public AddressEntity[] newArray(int size) {
			return new AddressEntity[size];
		}
	};

	public String getReciveId() {
		return reciveId;
	}

	public void setReciveId(String reciveId) {
		this.reciveId = reciveId;
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

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean aDefault) {
		isDefault = aDefault;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(reciveId);
		dest.writeString(userName);
		dest.writeString(userPhone);
		dest.writeString(provinceId);
		dest.writeString(provinceName);
		dest.writeString(cityId);
		dest.writeString(cityName);
		dest.writeString(districtId);
		dest.writeString(districtName);
		dest.writeString(address);
		dest.writeByte((byte) (isDefault ? 1 : 0));
	}
}
