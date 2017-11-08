package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/3/26.
 */

public class RefundDetailEntity {
	@SerializedName("name")
	private String whose;
	@SerializedName("refund_time")
	private String time;
	@SerializedName("title")
	private String title;
	@SerializedName("content")
	private String content;

	public String getWhose() {
		return whose;
	}

	public void setWhose(String whose) {
		this.whose = whose;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
