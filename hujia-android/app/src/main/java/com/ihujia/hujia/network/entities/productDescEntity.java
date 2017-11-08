package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/1/9.
 */

public class ProductDescEntity {
	@SerializedName("extend_name")
	private String describeType;
	@SerializedName("extend_value")
	private String describeDetails;

	public ProductDescEntity(String describeType, String describeDetails) {
		this.describeType = describeType;
		this.describeDetails = describeDetails;
	}

	public String getDescribeType() {
		return describeType;
	}

	public void setDescribeType(String describeType) {
		this.describeType = describeType;
	}

	public String getDescribeDetails() {
		return describeDetails;
	}

	public void setDescribeDetails(String describeDetails) {
		this.describeDetails = describeDetails;
	}
}
