package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2016/12/19.
 */

public class RecommendItemEntity {
	@SerializedName("goods_id")
	private String id;
	@SerializedName("pic_url")
	private String imgUrl;
	@SerializedName("brand_name")
	private String brandName;
	@SerializedName("goods_name")
	private String clothName;
	@SerializedName("price")
	private String salePrice;
	private boolean isShowDelete;

	public RecommendItemEntity(String imgUrl, String brandName, String clothName, String salePrice) {
		this.imgUrl = imgUrl;
		this.brandName = brandName;
		this.clothName = clothName;
		this.salePrice = salePrice;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getClothName() {
		return clothName;
	}

	public void setClothName(String clothName) {
		this.clothName = clothName;
	}

	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	public boolean isShowDelete() {
		return isShowDelete;
	}

	public void setShowDelete(boolean showDelete) {
		isShowDelete = showDelete;
	}
}
