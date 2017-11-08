package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/2/16.
 */

public class ShopCarNumEntity {
	@SerializedName("cart_count")
	private String shopcarNum;
	@SerializedName("result_code")
	private String resultCode;
	@SerializedName("result_desc")
	private String resultDesc;

	public String getShopcarNum() {
		return shopcarNum;
	}

	public void setShopcarNum(String shopcarNum) {
		this.shopcarNum = shopcarNum;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
}
