package com.ihujia.hujia.network.entities;

import com.common.utils.StringUtil;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zww on 2017/6/28.
 * 店铺装修
 */

public class StoreRenovationEntity {

    @SerializedName("first_pic")
    private String firstPic;
    @SerializedName("goods_count")
    private String goodsCount;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("store_pic")
    private String storeLogo;
    @SerializedName("store_type")
    private String storeType;
    @SerializedName("store_url")
    private String storeUrl;

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }

    public String getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
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

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getStoreType() {
        return storeType;
    }

    public String getStoreTypeName() {
        //1:品牌旗舰店 2：品牌专卖店 3：品类专营店 4:认证个人店 、、、
        String typeName = "";
        if (!StringUtil.isEmpty(storeType)) {
            switch (storeType) {
                case "1":
                    typeName = "品牌旗舰店";
                    break;
                case "2":
                    typeName = "品牌专卖店";
                    break;
                case "3":
                    typeName = "品类专营店";
                    break;
                case "4":
                    typeName = "认证个人店";
                    break;
            }
        }
        return typeName;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }
}
