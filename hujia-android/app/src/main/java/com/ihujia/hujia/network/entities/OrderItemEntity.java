package com.ihujia.hujia.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/21.
 */

public class OrderItemEntity implements Parcelable {
    @SerializedName("order_store_id")
    private String orderStoreId;
    @SerializedName("status")
    private String state;
    @SerializedName("order_total")
    private String allPrice;
    @SerializedName("goods_num")
    private String allNum;//单个订单内商品总数
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("refund_id")
    private String refundId;
    @SerializedName("refund_time")
    private String refundTime;
    @SerializedName("type")
    private String type;//1 退款退货  2  仅退款
    @SerializedName("goods_list")
    private List<ShopCarGoodEntity> goodsList;


    protected OrderItemEntity(Parcel in) {
        orderStoreId = in.readString();
        state = in.readString();
        allPrice = in.readString();
        allNum = in.readString();
        storeId = in.readString();
        storeName = in.readString();
        refundId = in.readString();
        refundTime = in.readString();
        type = in.readString();
        goodsList = in.createTypedArrayList(ShopCarGoodEntity.CREATOR);
    }

    public static final Creator<OrderItemEntity> CREATOR = new Creator<OrderItemEntity>() {
        @Override
        public OrderItemEntity createFromParcel(Parcel in) {
            return new OrderItemEntity(in);
        }

        @Override
        public OrderItemEntity[] newArray(int size) {
            return new OrderItemEntity[size];
        }
    };

    public String getOrderStoreId() {
        return orderStoreId;
    }

    public void setOrderStoreId(String orderStoreId) {
        this.orderStoreId = orderStoreId;
    }

    public String getState() {
        return state;
    }

    public String getStatusName() {
        String statusName = "";
        switch (state) {
            case "1":
                statusName = "待支付";
                break;
            case "2":
                statusName = "待发货";
                break;
            case "3":
                statusName = "待收货";
                break;
            case "4":
                statusName = "已完成";
                break;
            case "9":
                statusName = "已关闭";
                break;
            //20正常  21 等待商家确认  22  等待买家邮寄  23 等待商家收货  24  等待商家退款 25  退款完成  26 拒绝退款  27 退款关闭   28 退款中（退货退款的状态）
            // 31 等待商家确认 32 退款完成 33 拒绝退款 34 关闭订单 35 退款中（仅退款的状态）
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
            case "35":
                statusName = "退款中";
                break;
        }
        return statusName;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(String allPrice) {
        this.allPrice = allPrice;
    }

    public String getAllNum() {
        return allNum;
    }

    public void setAllNum(String allNum) {
        this.allNum = allNum;
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

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ShopCarGoodEntity> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<ShopCarGoodEntity> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderStoreId);
        dest.writeString(state);
        dest.writeString(allPrice);
        dest.writeString(allNum);
        dest.writeString(storeId);
        dest.writeString(storeName);
        dest.writeString(refundId);
        dest.writeString(refundTime);
        dest.writeString(type);
        dest.writeTypedList(goodsList);
    }
}
