package com.ihujia.hujia.network.entities;

/**
 * Created by zhaoweiwei on 2016/12/22.
 */

public class IntegralEntity {
	private String value;
	private String time;
	private String type;
	private String timeInfo;

	public IntegralEntity(String value, String time, String type, String timeInfo) {
		this.value = value;
		this.time = time;
		this.type = type;
		this.timeInfo = timeInfo;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTimeInfo() {
		return timeInfo;
	}

	public void setTimeInfo(String timeInfo) {
		this.timeInfo = timeInfo;
	}
}
