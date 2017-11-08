package com.ihujia.hujia.network.entities;

/**
 * Created by liuzhichao on 2017/1/13.
 * 筛选-品牌或者价格
 */
public class BrandOrPriceEntity {

	private String id;
	private String name;

	public BrandOrPriceEntity(String id, String name) {
		this.id = id;
		this.name = name;
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
}
