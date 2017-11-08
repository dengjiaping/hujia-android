package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuzhichao on 2017/2/13.
 * 筛选分类
 */
public class ConditionEntity extends Entity {

	@SerializedName("brand_list")
	private List<BrandEntity> brandEntities;
	@SerializedName("price_list")
	private List<PriceEntity> priceEntities;
	@SerializedName("category_list")
	private List<CategoryEntity> categoryEntities;

	public List<BrandEntity> getBrandEntities() {
		return brandEntities;
	}

	public void setBrandEntities(List<BrandEntity> brandEntities) {
		this.brandEntities = brandEntities;
	}

	public List<PriceEntity> getPriceEntities() {
		return priceEntities;
	}

	public void setPriceEntities(List<PriceEntity> priceEntities) {
		this.priceEntities = priceEntities;
	}

	public List<CategoryEntity> getCategoryEntities() {
		return categoryEntities;
	}

	public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
		this.categoryEntities = categoryEntities;
	}
}
