package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/2/13.
 */

public class PersonEntity {


	@SerializedName("result_code")
	private String resultCode;
	@SerializedName("result_desc")
	private String resultDesc;
	@SerializedName("news_num")
	private String newsNum;
	@SerializedName("pending_payment")
	private String payingNum;//代付款
	@SerializedName("deliver_goods")
	private String deliverNum;//待发货
	@SerializedName("take_delivery")
	private String gettingNum;//待收货
	@SerializedName("refund_num")
	private String refundNum;//退款
	@SerializedName("coupon_num")
	private String couponNum;//优惠券
	@SerializedName("collection_num")
	private String collectionNum;//收藏

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getNewsNum() {
		return newsNum;
	}

	public void setNewsNum(String newsNum) {
		this.newsNum = newsNum;
	}

	public String getPayingNum() {
		return payingNum;
	}

	public void setPayingNum(String payingNum) {
		this.payingNum = payingNum;
	}

	public String getDeliverNum() {
		return deliverNum;
	}

	public void setDeliverNum(String deliverNum) {
		this.deliverNum = deliverNum;
	}

	public String getGettingNum() {
		return gettingNum;
	}

	public void setGettingNum(String gettingNum) {
		this.gettingNum = gettingNum;
	}

	public String getRefundNum() {
		return refundNum;
	}

	public void setRefundNum(String refundNum) {
		this.refundNum = refundNum;
	}

	public String getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(String couponNum) {
		this.couponNum = couponNum;
	}

	public String getCollectionNum() {
		return collectionNum;
	}

	public void setCollectionNum(String collectionNum) {
		this.collectionNum = collectionNum;
	}
}
