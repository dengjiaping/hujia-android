package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2016/12/30.
 * 店铺
 */
public class StoreEntity {

	@SerializedName("id")
	private String id;
	@SerializedName("name")
	private String name;
	@SerializedName("address")
	private String address;
	@SerializedName("picUrl")
	private String picUrl;
	@SerializedName("distance")
	private String distance;

	public StoreEntity(String id, String name, String address, String picUrl, String distance) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.picUrl = picUrl;
		this.distance = distance;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
}
