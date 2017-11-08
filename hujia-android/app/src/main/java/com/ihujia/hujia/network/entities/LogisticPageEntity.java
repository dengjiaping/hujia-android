package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/3/10.
 */

public class LogisticPageEntity<T> extends PagesEntity<T> {
	@SerializedName("logistics_name")
	private String logisticName;
	@SerializedName("logistics_no")
	private String logisticNumber;
	@SerializedName("logistics_phone")
	private String logisticPhone;
	@SerializedName("logistics_list")
	private List<LogisticEntity> logisticEntities;

	public String getLogisticName() {
		return logisticName;
	}

	public void setLogisticName(String logisticName) {
		this.logisticName = logisticName;
	}

	public String getLogisticNumber() {
		return logisticNumber;
	}

	public void setLogisticNumber(String logisticNumber) {
		this.logisticNumber = logisticNumber;
	}

	public String getLogisticPhone() {
		return logisticPhone;
	}

	public void setLogisticPhone(String logisticPhone) {
		this.logisticPhone = logisticPhone;
	}

	public List<LogisticEntity> getLogisticEntities() {
		return logisticEntities;
	}

	public void setLogisticEntities(List<LogisticEntity> logisticEntities) {
		this.logisticEntities = logisticEntities;
	}
}
