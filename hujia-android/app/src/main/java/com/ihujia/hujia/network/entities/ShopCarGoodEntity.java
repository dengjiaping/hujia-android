package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/2/18.
 * 购物车商品
 */
public class ShopCarGoodEntity implements Parcelable {

	@SerializedName("store_id")
	private String storeId;
	@SerializedName("goods_id")
	private String goodsId;
	@SerializedName("goods_name")
	private String goodsName;
	@SerializedName("goods_status")
	private String goodsStatus;//商品状态 0 正常；2，退款中；3，已退款,4、拒绝
	@SerializedName("goods_number")
	private String goodsNum;
	@SerializedName("head_pic")
	private String goodsPic;
	@SerializedName("sku_id")
	private String skuId;
	@SerializedName("price")
	private String goodsPrice;
	@SerializedName("count")
	private String count;//加get/set,加序列化字段,修改实际取值
	@SerializedName("cart_id")
	private String shopcarId;
	@SerializedName("brand_name")
	private String brandName;
	@SerializedName("refund_total")
	private String refundPrice;
	@SerializedName("logistics_cost")
	private String logisticPrice;
	@SerializedName("attribute_list")
	private List<GoodsAttrEntity> attrList;

	private boolean checked;


	protected ShopCarGoodEntity(Parcel in) {
		storeId = in.readString();
		goodsId = in.readString();
		goodsName = in.readString();
		goodsStatus = in.readString();
		goodsNum = in.readString();
		goodsPic = in.readString();
		skuId = in.readString();
		goodsPrice = in.readString();
		count = in.readString();
		shopcarId = in.readString();
		brandName = in.readString();
		refundPrice = in.readString();
		logisticPrice = in.readString();
		attrList = in.createTypedArrayList(GoodsAttrEntity.CREATOR);
		checked = in.readByte() != 0;
	}

	public static final Creator<ShopCarGoodEntity> CREATOR = new Creator<ShopCarGoodEntity>() {
		@Override
		public ShopCarGoodEntity createFromParcel(Parcel in) {
			return new ShopCarGoodEntity(in);
		}

		@Override
		public ShopCarGoodEntity[] newArray(int size) {
			return new ShopCarGoodEntity[size];
		}
	};

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
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

	public String getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(String goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public String getGoodsPic() {
		return goodsPic;
	}

	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getShopcarId() {
		return shopcarId;
	}

	public void setShopcarId(String shopcarId) {
		this.shopcarId = shopcarId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(String refundPrice) {
		this.refundPrice = refundPrice;
	}

	public String getLogisticPrice() {
		return logisticPrice;
	}

	public void setLogisticPrice(String logisticPrice) {
		this.logisticPrice = logisticPrice;
	}

	public List<GoodsAttrEntity> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<GoodsAttrEntity> attrList) {
		this.attrList = attrList;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(storeId);
		dest.writeString(goodsId);
		dest.writeString(goodsName);
		dest.writeString(goodsStatus);
		dest.writeString(goodsNum);
		dest.writeString(goodsPic);
		dest.writeString(skuId);
		dest.writeString(goodsPrice);
		dest.writeString(count);
		dest.writeString(shopcarId);
		dest.writeString(brandName);
		dest.writeString(refundPrice);
		dest.writeString(logisticPrice);
		dest.writeTypedList(attrList);
		dest.writeByte((byte) (checked ? 1 : 0));
	}
}
