package com.ihujia.hujia.home.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.AnalysisUtil;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.home.adapter.ConfirmOrderAdapter;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.AddressEntity;
import com.ihujia.hujia.network.entities.CommitOrderEntity;
import com.ihujia.hujia.network.entities.ConfirmOrderEntity;
import com.ihujia.hujia.network.entities.CouponEntity;
import com.ihujia.hujia.network.entities.ShopCarGoodEntity;
import com.ihujia.hujia.network.entities.ShopCarStoreEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.controller.AddressMangerActivity;
import com.ihujia.hujia.utils.ExitManager;
import com.ihujia.hujia.utils.NumberFormatUtil;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2017/1/10.
 * 确认订单
 */

public class ConfirmOrderActivity extends ToolBarActivity implements View.OnClickListener {

    public static final int EXTRA_FROM_CONFIRM = 1;
    public static final int EXTRA_FROM_PRODUCT_DETAIL = 2;
    private static final int COUPON_REQUEST_CODE = 0x02;
    public static final int RECEIPT_REQUEST_CODE = 0X03;
    public static final int USERINFO_REQUEST_CODE = 0X04;
    private static final int REQUEST_CREAT_ORDER_CODE = 0X05;
    private static final int REQUEST_COMMIT_ORDER_CODE = 0X06;

    @ViewInject(R.id.order_confirm_list)
    private ListView confirmList;
    @ViewInject(R.id.order_confirm_commit)
    private TextView confirmCommit;
    @ViewInject(R.id.confirm_all_price)
    private TextView confirmAllPrice;
    @ViewInject(R.id.confirm_coupon_minus)
    private TextView confirmCouponMinus;

    private ConfirmOrderAdapter adapter;
    private String cartList;
    private TextView userName;
    private TextView userPhone;
    private TextView userAddress;
    private double price;
    private String receiveId, templateId;
    private double orderPrice;
    private int from;
    private String productNum;
    private String skuId;
    private String storeId;
    private double logisticsFee;
    private ShopCarStoreEntity couponStoreEntity;//与优惠券选择有关的item
    private double couponAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExitManager.instance.addBuyNowActivity(this);
        setContentView(R.layout.act_confirm_order);
        ViewInjectUtils.inject(this);
        setLeftTitle(getString(R.string.confirm_order));
        initView();
        loadData();
    }

    private void initView() {
        from = getIntent().getIntExtra(EXTRA_FROM, 0);
        if (EXTRA_FROM_PRODUCT_DETAIL == from) {
            productNum = getIntent().getStringExtra("productNum");
            skuId = getIntent().getStringExtra("skuId");
            storeId = getIntent().getStringExtra("storeId");
        }

        confirmCommit.setOnClickListener(this);

        View topView = getLayoutInflater().inflate(R.layout.confirm_order_top, null);
        RelativeLayout userInfoLayout = (RelativeLayout) topView.findViewById(R.id.confirm_userinfo_layout);
        userInfoLayout.setOnClickListener(this);
        userName = (TextView) topView.findViewById(R.id.confirm_username);
        userPhone = (TextView) topView.findViewById(R.id.confirm_userphone);
        userAddress = (TextView) topView.findViewById(R.id.confirm_address);
        confirmList.addHeaderView(topView);
        Intent intent = getIntent();
        cartList = intent.getStringExtra("cartList");
        confirmCommit.setEnabled(false);
    }

    private void loadData() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put(Constants.USERID, UserCenter.getUserId(this));
        params.put("receiving_id", receiveId);
        if (EXTRA_FROM_PRODUCT_DETAIL == from) {
            params.put("sku_id", skuId);
            params.put("sore_id", storeId);
            params.put("goods_num", productNum);
            requestHttpData(Constants.Urls.URL_POST_BUY_NOW, REQUEST_CREAT_ORDER_CODE, FProtocol.HttpMethod.POST, params);
        } else {
            params.put("cart_list", cartList);
            requestHttpData(Constants.Urls.URL_POST_CREAT_ORDER, REQUEST_CREAT_ORDER_CODE, FProtocol.HttpMethod.POST, params);
        }
    }

    @Override
    public void success(int requestCode, String data) {
        super.success(requestCode, data);
        closeProgressDialog();
        switch (requestCode) {
            case REQUEST_CREAT_ORDER_CODE:
                confirmCommit.setEnabled(true);
                ConfirmOrderEntity<ShopCarStoreEntity> confirmOrderEntity = Parsers.getConfirmOrder(data);
                if (confirmOrderEntity != null) {
                    if (REQUEST_NET_SUCCESS.equals(confirmOrderEntity.getResultCode())) {
                        List<ShopCarStoreEntity> storeEntities = confirmOrderEntity.getStoreEntities();
                        if (storeEntities != null && storeEntities.size() > 0) {
                            adapter = new ConfirmOrderAdapter(this, confirmOrderEntity.getStoreEntities(), this);
                            confirmList.setAdapter(adapter);
                            setGetInfo(confirmOrderEntity.getUserName(), confirmOrderEntity.getUserPhone(), confirmOrderEntity.getUserAddress());
                            templateId = confirmOrderEntity.getTemplateId();
                            receiveId = confirmOrderEntity.getReceiveId();

                            price = 0.0d;
                            couponAmount = 0.0d;
                            logisticsFee = 0.0d;
                            for (ShopCarStoreEntity shopcarStoreEntity : storeEntities) {
                                caculateProductPrice(shopcarStoreEntity);
                                caculateCoupon(shopcarStoreEntity.getCouponAmount());
                                caculateLogisticFee(shopcarStoreEntity.getLogisticFee());
                            }
                            updateAllPrice();
                        }
                    } else {
                        ToastUtil.shortShow(this, confirmOrderEntity.getResultMsg());
                    }
                }
                break;
            case REQUEST_COMMIT_ORDER_CODE:
                CommitOrderEntity commitOrderEntity = Parsers.getCommitSuccess(data);
                if (REQUEST_NET_SUCCESS.equals(commitOrderEntity.getResultCode())) {
                    startActivity(new Intent(this, ConfirmChooseActivity.class).putExtra(ConfirmChooseActivity.EXTRA_COMMIT_SUCCESS_ENTITY, commitOrderEntity));
                    setResult(RESULT_OK);
                    finish();
                } else {
                    ToastUtil.shortShow(this, commitOrderEntity.getResultMsg());
                }
                break;
        }
    }

    private void setGetInfo(String user, String phone, String address) {
        userName.setText(getString(R.string.confirm_order_get_user, user));
        userPhone.setText(getString(R.string.confirm_order_get_phone, phone));
        userAddress.setText(getString(R.string.person_order_detail_address, address));
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        super.mistake(requestCode, status, errorMessage);
        closeProgressDialog();
        ToastUtil.shortShow(this, errorMessage);
    }

    private void caculateProductPrice(ShopCarStoreEntity shopcarStoreEntity) {
        for (ShopCarGoodEntity shopcarGoodEntity : shopcarStoreEntity.getEntities()) {
            price += Double.parseDouble(shopcarGoodEntity.getCount()) * Double.parseDouble(shopcarGoodEntity.getGoodsPrice());
        }
    }

    private void caculateCoupon(String couponValue) {
        if (!StringUtil.isEmpty(couponValue)) {
            couponAmount += Double.parseDouble(couponValue);
        }
        if (couponAmount <= 0) {
            confirmCouponMinus.setVisibility(View.GONE);
        } else {
            setCouponMinu(couponAmount);
        }
    }

    private void caculateLogisticFee(String logisticFee) {
        if (!StringUtil.isEmpty(logisticFee)) {
            logisticsFee += Double.parseDouble(logisticFee);
        }
    }

    private void updateAllPrice() {
        orderPrice = price - couponAmount;
        orderPrice += logisticsFee;
        String allPrice = getString(R.string.confirm_order_allprice, NumberFormatUtil.formatMoney(orderPrice));
        SpannableStringBuilder spanText = new SpannableStringBuilder(allPrice);
        spanText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary_color_red)), 3, allPrice.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        confirmAllPrice.setText(spanText);
    }

    private void setCouponMinu(double couponAmount) {
        confirmCouponMinus.setVisibility(View.VISIBLE);
        String couponMinus = getString(R.string.confirm_order_coupon_minus, String.valueOf(couponAmount));
        SpannableStringBuilder spanText = new SpannableStringBuilder(couponMinus);
        spanText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary_color_red)), 7, couponMinus.length() - 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        confirmCouponMinus.setText(spanText);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_confirm_commit:
                AnalysisUtil.onEvent(this, "dd_tijiao");
                showProgressDialog();
                IdentityHashMap<String, String> params = new IdentityHashMap<>();
                params.put("user_id", UserCenter.getUserId(this));
                params.put("cart_list", cartList);
                params.put("coupon_id", "");
                params.put("template_id", templateId);
                params.put("receiving_id", receiveId);
                params.put("order_total", NumberFormatUtil.formatMoney(orderPrice));
                if (EXTRA_FROM_PRODUCT_DETAIL == from) {//立即购买
                    params.put("type", "2");
                    params.put("sku_id", skuId);
                    params.put("goods_num", productNum);
                    params.put("store_id", storeId);
                } else {
                    params.put("type", "1");
                }
                requestHttpData(Constants.Urls.URL_POST_COMMIT_ORDER, REQUEST_COMMIT_ORDER_CODE, FProtocol.HttpMethod.POST, params);
                break;
            case R.id.confirm_userinfo_layout:
                Intent userInfoIntent = new Intent(this, AddressMangerActivity.class);
                userInfoIntent.putExtra(EXTRA_FROM, 1);
                startActivityForResult(userInfoIntent, USERINFO_REQUEST_CODE);
                break;
            case R.id.confirm_order_coupon_layout:
                loadData();
                /*couponStoreEntity = (ShopCarStoreEntity) view.getTag();
				Intent couponIntent = new Intent(this, MyCouponActivity.class);
				couponIntent.putExtra(EXTRA_FROM, EXTRA_FROM_CONFIRM);
				couponIntent.putExtra("storeId", couponStoreEntity.getStoreId());
				startActivityForResult(couponIntent, COUPON_REQUEST_CODE);*/
                break;
            case R.id.confirm_order_receipt_layout://选择发票
                Intent receiptIntent = new Intent(this, ReceiptActivity.class);
//				receiptIntent.putExtra()//发票抬头
                startActivityForResult(receiptIntent, RECEIPT_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case COUPON_REQUEST_CODE:
                    CouponEntity couponEntity = data.getParcelableExtra("couponEntity");
                    couponStoreEntity.setCouponAmount(couponEntity.getValue());
                    couponStoreEntity.setCouponName(couponEntity.getName());
                    couponStoreEntity.setCouponId(couponEntity.getCouponId());
                    adapter.notifyDataSetChanged();
                    caculateCoupon(couponEntity.getValue());
                    updateAllPrice();
                    break;
                case RECEIPT_REQUEST_CODE:
                    break;
                case USERINFO_REQUEST_CODE:
                    AddressEntity addressEntity = data.getParcelableExtra("addressEntity");
                    setGetInfo(addressEntity.getUserName(), addressEntity.getUserPhone(), addressEntity.getProvinceName() + addressEntity.getCityName() + addressEntity.getDistrictName());
                    receiveId = addressEntity.getReciveId();
                    loadData();
                    break;
            }
        } else {
            if (data != null && USERINFO_REQUEST_CODE == requestCode && data.getBooleanExtra("needRefresh", false)) {
                loadData();
            }
        }
    }
}
