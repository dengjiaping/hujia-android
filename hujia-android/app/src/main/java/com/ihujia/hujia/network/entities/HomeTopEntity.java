package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuzhichao on 2017/2/14.
 * 首页上半部分
 */
public class HomeTopEntity extends Entity {

	@SerializedName("ad_list")
	private ADEntity adEntity;
	@SerializedName("brand_list")
	private List<BrandEntity> brandEntities;
	@SerializedName("category_list")
	private List<CategoryEntity> categoryEntities;
	@SerializedName("pic_list")
	private List<ActivityEntity> activityEntities;

	public ADEntity getAdEntity() {
		return adEntity;
	}

	public void setAdEntity(ADEntity adEntity) {
		this.adEntity = adEntity;
	}

	public List<BrandEntity> getBrandEntities() {
		return brandEntities;
	}

	public void setBrandEntities(List<BrandEntity> brandEntities) {
		this.brandEntities = brandEntities;
	}

	public List<CategoryEntity> getCategoryEntities() {
		return categoryEntities;
	}

	public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
		this.categoryEntities = categoryEntities;
	}

	public List<ActivityEntity> getActivityEntities() {
		return activityEntities;
	}

	public void setActivityEntities(List<ActivityEntity> activityEntities) {
		this.activityEntities = activityEntities;
	}
}
