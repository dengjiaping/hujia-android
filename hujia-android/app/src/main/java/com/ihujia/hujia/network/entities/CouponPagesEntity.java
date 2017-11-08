package com.ihujia.hujia.network.entities;

/**
 * Created by zhaoweiwei on 2017/2/14.
 */

public class CouponPagesEntity<T> extends PagesEntity<T>{
	private String unUsedNum;
	private String usedNum;
	private String overDueNum;

	public String getUnUsedNum() {
		return unUsedNum;
	}

	public void setUnUsedNum(String unUsedNum) {
		this.unUsedNum = unUsedNum;
	}

	public String getUsedNum() {
		return usedNum;
	}

	public void setUsedNum(String usedNum) {
		this.usedNum = usedNum;
	}

	public String getOverDueNum() {
		return overDueNum;
	}

	public void setOverDueNum(String overDueNum) {
		this.overDueNum = overDueNum;
	}
}
