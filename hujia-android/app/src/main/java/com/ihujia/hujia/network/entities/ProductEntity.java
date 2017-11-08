package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/1/5.
 * 商品
 */
public class ProductEntity {

	@SerializedName("goods_id")
	private String id;
	@SerializedName("pic_url")
	private String picUrl;
	@SerializedName("brand_name")
	private String brand;
	@SerializedName("goods_name")
	private String name;
	@SerializedName("price")
	private String price;
	@SerializedName("sku_id")
	private String skuId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
}
