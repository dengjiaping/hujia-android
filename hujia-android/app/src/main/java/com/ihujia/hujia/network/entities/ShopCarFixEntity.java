package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/2/20.
 * 购物车修改颜色尺寸
 */

public class ShopCarFixEntity implements Parcelable{

	@SerializedName("result_code")
	private String resultCode;
	@SerializedName("result_desc")
	private String resultMsg;
	@SerializedName("goods_id")
	private String goodsId;
	@SerializedName("goods_name")
	private String goodsName;
	@SerializedName("goods_number")
	private String goodsStock;//商品库存
	@SerializedName("goods_pic")
	private String goodsPic;
	@SerializedName("goods_price")
	private String goodsPrice;
	@SerializedName("market_price")
	private String goodsOldPrice;
	@SerializedName("sku_id")
	private String skuId;
	@SerializedName("store_id")
	private String storeId;
	@SerializedName("attribute_list")
	private List<ProductAttrEntity> attrList;

	protected ShopCarFixEntity(Parcel in) {
		goodsId = in.readString();
		goodsName = in.readString();
		goodsStock = in.readString();
		goodsPic = in.readString();
		goodsPrice = in.readString();
		goodsOldPrice = in.readString();
		skuId = in.readString();
		storeId = in.readString();
		attrList = in.createTypedArrayList(ProductAttrEntity.CREATOR);
	}

	public static final Creator<ShopCarFixEntity> CREATOR = new Creator<ShopCarFixEntity>() {
		@Override
		public ShopCarFixEntity createFromParcel(Parcel in) {
			return new ShopCarFixEntity(in);
		}

		@Override
		public ShopCarFixEntity[] newArray(int size) {
			return new ShopCarFixEntity[size];
		}
	};


	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsStock() {
		return goodsStock;
	}

	public void setGoodsStock(String goodsStock) {
		this.goodsStock = goodsStock;
	}

	public String getGoodsPic() {
		return goodsPic;
	}

	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getGoodsOldPrice() {
		return goodsOldPrice;
	}

	public void setGoodsOldPrice(String goodsOldPrice) {
		this.goodsOldPrice = goodsOldPrice;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public List<ProductAttrEntity> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<ProductAttrEntity> attrList) {
		this.attrList = attrList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(goodsId);
		dest.writeString(goodsName);
		dest.writeString(goodsStock);
		dest.writeString(goodsPic);
		dest.writeString(goodsPrice);
		dest.writeString(goodsOldPrice);
		dest.writeString(skuId);
		dest.writeString(storeId);
		dest.writeTypedList(attrList);
	}
}
