package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/3/30.
 */

public class RefundDetailPageEntity<T> extends PagesEntity<T> {
	@SerializedName("status")
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusName() {
		String statusName = "";
		switch (status) {
			//20正常  21 等待商家确认  22  等待买家邮寄  23 等待商家收货  24  等待商家退款 25  退款完成  26 拒绝退款  27 退款关闭   28 退款失败
			case "21":
			case "31":
				statusName = "等待商家确认";
				break;
			case "22":
				statusName = "等待买家邮寄";
				break;
			case "23":
				statusName = "等待商家收货";
				break;
			case "24":
				statusName = "等待商家退款";
				break;
			case "25":
			case "32":
				statusName = "退款完成";
				break;
			case "26":
			case "33":
				statusName = "拒绝退款";
				break;
			case "27":
			case "34":
				statusName = "退款关闭";
				break;
			case "28":
				statusName = "退款失败";
				break;
		}
		return statusName;
	}
}
