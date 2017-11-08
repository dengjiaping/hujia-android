package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuzhichao on 2017/1/6.
 * 分类
 */
public class CategoryEntity {

	@SerializedName("category_id")
	private String id;
	@SerializedName("category_name")
	private String name;
	@SerializedName("category_pic")
	private String imgUrl;
	@SerializedName("child_list")
	private List<CategoryEntity> categoryEntities;

	public CategoryEntity(String id, String name, String imgUrl) {
		this.id = id;
		this.name = name;
		this.imgUrl = imgUrl;
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

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<CategoryEntity> getCategoryEntities() {
		return categoryEntities;
	}

	public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
		this.categoryEntities = categoryEntities;
	}
}
