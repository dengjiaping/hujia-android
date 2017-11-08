package com.ihujia.hujia.network.entities;

/**
 * Created by zhaoweiwei on 2016/12/25.
 */

public class IntegralExchangeEntity {
	private String exchangeMoney;
	private String exchangeIntegral;
	private boolean checked;

	public IntegralExchangeEntity(String exchangeMoney, String exchangeIntegral, boolean available) {
		this.exchangeMoney = exchangeMoney;
		this.exchangeIntegral = exchangeIntegral;
		this.checked = available;
	}

	public String getExchangeMoney() {
		return exchangeMoney;
	}

	public void setExchangeMoney(String exchangeMoney) {
		this.exchangeMoney = exchangeMoney;
	}

	public String getExchangeIntegral() {
		return exchangeIntegral;
	}

	public void setExchangeIntegral(String exchangeIntegral) {
		this.exchangeIntegral = exchangeIntegral;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
