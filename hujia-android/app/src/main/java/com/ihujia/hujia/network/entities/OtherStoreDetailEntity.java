package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/2/20.
 * 第三方店铺详情
 */
public class OtherStoreDetailEntity extends PagesEntity<ProductEntity> {

	@SerializedName("store_id")
	private String id;
	@SerializedName("first_pic")
	private String cover;
	@SerializedName("store_name")
	private String name;
	@SerializedName("store_pic")
	private String logo;
	@SerializedName("goods_count")
	private String stock;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}
}
