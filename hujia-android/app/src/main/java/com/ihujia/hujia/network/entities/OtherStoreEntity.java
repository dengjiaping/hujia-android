package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/2/8.
 * 第三方店铺
 */
public class OtherStoreEntity {

    @SerializedName("store_id")
    private String id;
    @SerializedName("first_pic")
    private String cover;
    @SerializedName("store_name")
    private String name;
    @SerializedName("store_pic")
    private String logo;
    @SerializedName("goods_count")
    private String stock;
    @SerializedName("store_url")
    private String storeUrl;
    @SerializedName("store_type")
    private String storeType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
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
}
