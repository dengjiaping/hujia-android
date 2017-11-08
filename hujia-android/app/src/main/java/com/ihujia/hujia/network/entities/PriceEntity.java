package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/2/13.
 * 筛选价格实体
 */
public class PriceEntity {

	@SerializedName("id")
	private String id;
	@SerializedName("min_price")
	private String min;
	@SerializedName("max_price")
	private String max;

	public PriceEntity() {
	}

	public PriceEntity(String id, String min, String max) {
		this.id = id;
		this.min = min;
		this.max = max;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}
}
