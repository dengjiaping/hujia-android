package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/2/12.
 */

public class ConfirmOrderEntity<T> extends PagesEntity<T> {
	@SerializedName("receiving_id")
	private String receiveId;
	@SerializedName("consignee")
	private String userName;
	@SerializedName("contacts")
	private String userPhone;
	@SerializedName("address")
	private String userAddress;
	@SerializedName("template_id")
	private String templateId;//配送方式
	@SerializedName("store_list")
	private List<ShopCarStoreEntity> storeEntities;

	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
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

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public List<ShopCarStoreEntity> getStoreEntities() {
		return storeEntities;
	}

	public void setStoreEntities(List<ShopCarStoreEntity> storeEntities) {
		this.storeEntities = storeEntities;
	}
}
