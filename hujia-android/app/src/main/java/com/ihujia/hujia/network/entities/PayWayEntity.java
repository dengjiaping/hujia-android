package com.ihujia.hujia.network.entities;

/**
 * Created by zhaoweiwei on 2017/1/10.
 */

public class PayWayEntity {
	private String name;
	private String type;//1 支付宝 2 微信 3 贵宾卡支付
	private boolean checked;

	public PayWayEntity(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
