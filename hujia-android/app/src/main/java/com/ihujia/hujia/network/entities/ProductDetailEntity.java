package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by zhaoweiwei on 2017/2/15.
 */

public class ProductDetailEntity implements Parcelable {
    @SerializedName("goods_id")
    private String productId;
    @SerializedName("goods_name")
    private String productName;
    @SerializedName("goods_number")
    private String productNum;
    @SerializedName("pic_list")
    private ArrayList<ProductPicEntity> picList;
    @SerializedName("goods_price")
    private String productPrice;
    @SerializedName("market_price")
    private String productOldPrice;
    @SerializedName("store_icon")
    private String storeLogo;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("store_goods_count")
    private String storeDesc;
    @SerializedName("link_url")
    private String productDetail;
    @SerializedName("is_colletion")
    private Boolean isCollect;//0 未收藏  1 已收藏
    @SerializedName("store_url")
    private String storeUrl;
    @SerializedName("store_type")
    private String storeType;
    @SerializedName("extend_list")
    private ArrayList<ProductDescEntity> descEntities;
    @SerializedName("attribute_list")
    private ArrayList<ProductAttrEntity> attrEntities;

    protected ProductDetailEntity(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        productNum = in.readString();
        picList = in.createTypedArrayList(ProductPicEntity.CREATOR);
        productPrice = in.readString();
        productOldPrice = in.readString();
        storeLogo = in.readString();
        storeId = in.readString();
        storeName = in.readString();
        storeDesc = in.readString();
        productDetail = in.readString();
        isCollect = in.readByte() != 0;
        storeUrl = in.readString();
        storeType = in.readString();
        attrEntities = in.createTypedArrayList(ProductAttrEntity.CREATOR);
    }

    public static final Creator<ProductDetailEntity> CREATOR = new Creator<ProductDetailEntity>() {
        @Override
        public ProductDetailEntity createFromParcel(Parcel in) {
            return new ProductDetailEntity(in);
        }

        @Override
        public ProductDetailEntity[] newArray(int size) {
            return new ProductDetailEntity[size];
        }
    };

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public ArrayList<ProductPicEntity> getPicList() {
        return picList;
    }

    public void setPicList(ArrayList<ProductPicEntity> picList) {
        this.picList = picList;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductOldPrice() {
        return productOldPrice;
    }

    public void setProductOldPrice(String productOldPrice) {
        this.productOldPrice = productOldPrice;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreDesc() {
        return storeDesc;
    }

    public void setStoreDesc(String storeDesc) {
        this.storeDesc = storeDesc;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public ArrayList<ProductDescEntity> getDescEntities() {
        return descEntities;
    }

    public void setDescEntities(ArrayList<ProductDescEntity> descEntities) {
        this.descEntities = descEntities;
    }

    public ArrayList<ProductAttrEntity> getAttrEntities() {
        return attrEntities;
    }

    public void setAttrEntities(ArrayList<ProductAttrEntity> attrEntities) {
        this.attrEntities = attrEntities;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(productNum);
        dest.writeTypedList(picList);
        dest.writeString(productPrice);
        dest.writeString(productOldPrice);
        dest.writeString(storeLogo);
        dest.writeString(storeId);
        dest.writeString(storeName);
        dest.writeString(storeDesc);
        dest.writeString(productDetail);
        dest.writeByte((byte) (isCollect ? 1 : 0));
        dest.writeString(storeUrl);
        dest.writeString(storeType);
        dest.writeTypedList(attrEntities);
    }
}
