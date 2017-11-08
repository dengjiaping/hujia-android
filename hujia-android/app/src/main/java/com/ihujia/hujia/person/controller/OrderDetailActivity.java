package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.home.controller.ConfirmChooseActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.CommitOrderEntity;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.ListDialogPageEntity;
import com.ihujia.hujia.network.entities.OrderDetailPageEntity;
import com.ihujia.hujia.network.entities.OrderItemEntity;
import com.ihujia.hujia.network.entities.OrderStoreEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.otherstore.controller.StoreRenovationActivity;
import com.ihujia.hujia.person.adapter.OrderDetailStoreAdapter;
import com.ihujia.hujia.utils.ListDialogDatasUtil;
import com.ihujia.hujia.utils.TimeUtil;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/21.
 * 订单详情
 */

public class OrderDetailActivity extends ToolBarActivity implements View.OnClickListener {

    private static final int REQUEST_ORDER_DETAIL = 0X01;
    private static final int REQUEST_CANCEL_ORDER = 0X02;
    private static final int REQUEST_QUERY_ORDER = 0X03;
    private static final int REQUEST_PAY_TPYE = 0X04;
    private static final int REQUEST_TO_PAY = 0x05;
    private static final int REQUEST_DELETE_ORDER = 0x06;

    @ViewInject(R.id.order_detail_list)
    private ListView orderList;
    @ViewInject(R.id.order_detail_paytime_info)
    private TextView orderPaytimeInfo;
    @ViewInject(R.id.order_detail_paying_time)
    private TextView orderPaytime;
    @ViewInject(R.id.order_detail_button1)
    private TextView orderButton1;
    @ViewInject(R.id.order_detail_button2)
    private TextView orderButton2;
    @ViewInject(R.id.order_detail_bottom_layout)
    private RelativeLayout orderBottomLayout;

    private OrderItemEntity entity;
    private String status;
    private String orderId;
    private TextView orderNumber;
    private TextView orderState;
    private TextView orderUsername;
    private TextView orderPhone;
    private TextView orderAddress;
    private TextView goodsPrice;
    private TextView orderCoupon;
    private TextView orderFreight;
    private TextView orderRelpay;
    private TextView orderTime;
    private OrderDetailPageEntity<OrderStoreEntity> orderDetailPageEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_act_order_detail);
        setLeftTitle(getString(R.string.person_order_detail));
        ViewInjectUtils.inject(this);
        entity = getIntent().getParcelableExtra(OrderListFragment.EXTRA_ORDER_ITEM);
        status = entity.getState();
        entity.setState(status);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initView() {
        View topView = getLayoutInflater().inflate(R.layout.person_orderdetail_top, null);
        orderNumber = (TextView) topView.findViewById(R.id.order_item_store);
        orderNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        orderState = (TextView) topView.findViewById(R.id.order_item_state);
        orderUsername = (TextView) topView.findViewById(R.id.order_detail_username);
        orderPhone = (TextView) topView.findViewById(R.id.order_detail_phone);
        orderAddress = (TextView) topView.findViewById(R.id.order_detail_address);
        View bottomView = getLayoutInflater().inflate(R.layout.person_orderdetail_bottom, null);
        goodsPrice = (TextView) bottomView.findViewById(R.id.order_detail_goods_price);
        orderCoupon = (TextView) bottomView.findViewById(R.id.order_detail_goods_coupon);
        orderFreight = (TextView) bottomView.findViewById(R.id.order_detail_goods_freight);
        orderRelpay = (TextView) bottomView.findViewById(R.id.order_detail_goods_re1pay);
        orderTime = (TextView) bottomView.findViewById(R.id.order_detail_time);
        orderList.addHeaderView(topView);
        orderList.addFooterView(bottomView);
    }

    private void loadData() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put(Constants.USERID, UserCenter.getUserId(this));
        orderId = entity.getOrderStoreId();
        params.put("order_id", orderId);
        requestHttpData(Constants.Urls.URL_POST_ORDER_DETAIL, REQUEST_ORDER_DETAIL, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void success(int requestCode, String data) {
        super.success(requestCode, data);
        closeProgressDialog();
        switch (requestCode) {
            case REQUEST_ORDER_DETAIL:
                orderDetailPageEntity = Parsers.getOrderDetail(data);
                if (orderDetailPageEntity != null) {
                    if (REQUEST_NET_SUCCESS.equals(orderDetailPageEntity.getResultCode())) {
                        orderNumber.setText(getString(R.string.person_order_order_number, orderDetailPageEntity.getOrderId()));
                        orderState.setText(orderDetailPageEntity.getStatusName());
                        orderUsername.setText(orderDetailPageEntity.getUserName());
                        orderPhone.setText(orderDetailPageEntity.getUserPhone());
                        orderAddress.setText(getString(R.string.person_order_detail_address, orderDetailPageEntity.getUserAddress()));

                        goodsPrice.setText(getString(R.string.price, orderDetailPageEntity.getGoodsPrice()));
                        orderCoupon.setText(getString(R.string.price_minus, orderDetailPageEntity.getCouponPrice()));
                        orderFreight.setText(getString(R.string.price_add, orderDetailPageEntity.getRevelingPrice()));
                        orderRelpay.setText(getString(R.string.price, orderDetailPageEntity.getOrderTotal()));
                        orderTime.setText(getString(R.string.person_order_create_time, orderDetailPageEntity.getCreatTime()));

                        initButton();
                        if (!StringUtil.isEmpty(orderDetailPageEntity.getRemainTime())) {
                            int countDownTime = Integer.parseInt(orderDetailPageEntity.getRemainTime());
                            if (countDownTime > 0) {
                                orderPaytime.setVisibility(View.VISIBLE);
                                new CountDownTimer(countDownTime * 1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        orderPaytime.setText(TimeUtil.getFormatTime2(millisUntilFinished / 1000));
                                    }

                                    @Override
                                    public void onFinish() {
                                        orderState.setText(getString(R.string.person_order_status_close));
                                        orderBottomLayout.setVisibility(View.GONE);
                                        cancel();
                                    }
                                }.start();
                            } else {
                                orderPaytimeInfo.setVisibility(View.GONE);
                            }
                        } else {
                            orderPaytimeInfo.setVisibility(View.GONE);
                        }
                        List<OrderStoreEntity> storeEntities = orderDetailPageEntity.getOrderStoreEntities();
                        if (storeEntities != null && storeEntities.size() > 0) {
                            OrderDetailStoreAdapter adapter = new OrderDetailStoreAdapter(this, storeEntities, this);
                            orderList.setAdapter(adapter);
                        }
                    } else {
                        ToastUtil.shortShow(this, orderDetailPageEntity.getResultMsg());
                    }
                }
                break;
            case REQUEST_CANCEL_ORDER:
                Entity entity = Parsers.getResult(data);
                if (BaseActivity.REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
                    orderState.setText(getString(R.string.person_order_status_close));
                    orderBottomLayout.setVisibility(View.GONE);
                } else {
                    ToastUtil.shortShow(this, entity.getResultMsg());
                }
                break;
            case REQUEST_QUERY_ORDER:
                Entity entity2 = Parsers.getResult(data);
                if (BaseActivity.REQUEST_NET_SUCCESS.equals(entity2.getResultCode())) {
                    orderState.setText(getString(R.string.person_order_status_finished));
                    orderBottomLayout.setVisibility(View.GONE);
                } else {
                    ToastUtil.shortShow(this, entity2.getResultMsg());
                }
                break;
            case REQUEST_PAY_TPYE:
                CommitOrderEntity commitOrderEntity = Parsers.getCommitSuccess(data);
                if (REQUEST_NET_SUCCESS.equals(commitOrderEntity.getResultCode())) {
                    Intent intent = new Intent(this, ConfirmChooseActivity.class);
                    intent.putExtra(ConfirmChooseActivity.EXTRA_COMMIT_SUCCESS_ENTITY, commitOrderEntity);
                    startActivityForResult(intent, REQUEST_TO_PAY);
                } else {
                    ToastUtil.shortShow(this, commitOrderEntity.getResultMsg());
                }
                break;
            case REQUEST_DELETE_ORDER:
                Entity deleteEntity = Parsers.getResult(data);
                if (BaseActivity.REQUEST_NET_SUCCESS.equals(deleteEntity.getResultCode())) {
                    finish();
                } else {
                    ToastUtil.shortShow(this, deleteEntity.getResultMsg());
                }
                break;
        }
    }

    private void initButton() {
        status = entity.getState();
        if (String.valueOf(OrderListActivity.ORDERTYPE_PAYING).equals(status)) {//待支付
            orderButton1.setVisibility(View.VISIBLE);
            orderButton2.setVisibility(View.VISIBLE);
            orderPaytimeInfo.setVisibility(View.VISIBLE);
            orderButton1.setText("取消订单");
            orderButton2.setText("立即付款");
            orderButton2.setTextColor(ContextCompat.getColor(this, R.color.primary_color_red));
            orderButton2.setBackground(ContextCompat.getDrawable(this, R.drawable.person_order_button_bg_red));
        } else if (String.valueOf(OrderListActivity.ORDERTYPE_DELIVER).equals(status)) {//待发货
            orderButton1.setVisibility(View.GONE);
            orderButton2.setVisibility(View.VISIBLE);
            orderPaytimeInfo.setVisibility(View.GONE);
            orderButton2.setText("申请退款");
            orderButton2.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
            orderButton2.setBackground(ContextCompat.getDrawable(this, R.drawable.person_order_button_bg));
        } else if (String.valueOf(OrderListActivity.ORDERTYPE_GETING).equals(status)) {//待收货
            orderButton1.setVisibility(View.VISIBLE);
            orderButton2.setVisibility(View.VISIBLE);
            orderPaytimeInfo.setVisibility(View.GONE);
            orderButton1.setText("查看物流");
            orderButton2.setText("确认收货");
            orderButton2.setTextColor(ContextCompat.getColor(this, R.color.primary_color_red));
            orderButton2.setBackground(ContextCompat.getDrawable(this, R.drawable.person_order_button_bg_red));
        } else if (String.valueOf(OrderListActivity.ORDERTYPE_FINISH).equals(status)) {//已完成
            orderButton1.setVisibility(View.VISIBLE);
            orderButton2.setVisibility(View.VISIBLE);
            orderPaytimeInfo.setVisibility(View.GONE);
            orderButton1.setText("删除订单");
            orderButton2.setText("退货/退款");
            orderButton2.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
            orderButton2.setBackground(ContextCompat.getDrawable(this, R.drawable.person_order_button_bg));
        } else if (String.valueOf(OrderListActivity.ORDERTYPE_CLOSE).equals(status)) {
            orderBottomLayout.setVisibility(View.GONE);
//			orderButton1.setVisibility(View.GONE);
//			orderButton2.setVisibility(View.GONE);
//			orderPaytimeInfo.setVisibility(View.GONE);
//			orderButton2.setText("删除订单");
//			orderButton2.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
//			orderButton2.setBackground(ContextCompat.getDrawable(this, R.drawable.person_order_button_bg));
        }
        orderButton1.setOnClickListener(this);
        orderButton2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_detail_button1:
                if (!StringUtil.isEmpty(status)) {
                    if (String.valueOf(OrderListActivity.ORDERTYPE_PAYING).equals(status)) {//待支付 取消订单
                        ListDialogPageEntity pageEntity = ListDialogDatasUtil.createOrderDatas();
                        ListDialogActivity.startListDialogActivity(this, pageEntity);
                    } else if (String.valueOf(OrderListActivity.ORDERTYPE_GETING).equals(status)) {//待收货 查看物流
                        LogisticsDetailActivity.startLogisticsDetailActivity(this, orderDetailPageEntity.getOrderId());
                    } else if (String.valueOf(OrderListActivity.ORDERTYPE_FINISH).equals(status)) {//已完成 删除订单
                        showProgressDialog();
                        IdentityHashMap<String, String> params = new IdentityHashMap<>();
                        params.put("order_id", orderDetailPageEntity.getOrderId());
                        params.put("user_id", UserCenter.getUserId(this));
                        requestHttpData(Constants.Urls.URL_POST_DELETE_ORDER, REQUEST_DELETE_ORDER, FProtocol.HttpMethod.POST, params);
                    }
                }
                break;
            case R.id.order_detail_button2:
                if (!StringUtil.isEmpty(status)) {
                    if (String.valueOf(OrderListActivity.ORDERTYPE_PAYING).equals(status)) {//待支付 立即付款
                        ToastUtil.shortShow(this, "立即付款");
                        IdentityHashMap<String, String> params = new IdentityHashMap<>();
                        params.put("order_id", orderDetailPageEntity.getOrderId());
                        params.put("order_total", orderDetailPageEntity.getOrderPrice());
                        requestHttpData(Constants.Urls.URL_POST_GET_PAYTYPE, REQUEST_PAY_TPYE, FProtocol.HttpMethod.POST, params);
                    } else if (String.valueOf(OrderListActivity.ORDERTYPE_DELIVER).equals(status)) {//待发货 申请退款
                        Intent intent = new Intent(this, ToRefundActivity.class);
                        intent.putExtra(OrderListFragment.EXTRA_ORDER_ITEM, orderDetailPageEntity);
                        intent.putExtra(EXTRA_FROM, ToRefundActivity.EXTRA_FROM_DELIVER);
                        startActivity(intent);
                    } else if (String.valueOf(OrderListActivity.ORDERTYPE_GETING).equals(status)) {//待收货 确认收货
                        showAlertDialog(
                                getString(R.string.setting_hint_text),
                                getString(R.string.order_confirm_message),
                                getString(R.string.cancel),
                                getString(R.string.button_ok),
                                view1 -> closeDialog(),
                                view12 -> {
                                    queryOrder();
                                    closeDialog();
                                });
                    } else if (String.valueOf(OrderListActivity.ORDERTYPE_FINISH).equals(status)) {//已完成 退货/退款
                        Intent intent = new Intent(this, ToRefundActivity.class);
                        intent.putExtra(OrderListFragment.EXTRA_ORDER_ITEM, orderDetailPageEntity);
                        intent.putExtra(EXTRA_FROM, ToRefundActivity.EXTRA_FROM_FINISHED);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.confirm_order_store_layout:
                String storeId = (String) view.getTag();
                String storeName = (String) view.getTag();
                StoreRenovationActivity.startStoreRenovationActivity(this, storeId, storeName);
                break;
        }
    }

    /**
     * 确认收货
     */
    private void queryOrder() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put(Constants.USERID, UserCenter.getUserId(this));
        params.put("order_id", orderId);
        requestHttpData(Constants.Urls.URL_POST_QUERY_ORDER, REQUEST_QUERY_ORDER, FProtocol.HttpMethod.POST, params);
    }

    private void cancelOrder(String cancelReason) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put(Constants.USERID, UserCenter.getUserId(this));
        params.put("order_id", orderDetailPageEntity.getOrderId());
        params.put("status", orderDetailPageEntity.getStatus());
        params.put("reason", cancelReason);
        requestHttpData(Constants.Urls.URL_POST_CANCEL_ORDER, REQUEST_CANCEL_ORDER, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_TO_PAY:
                    loadData();
                    break;
                case ListDialogActivity.REQUEST_CHOOSE_ITEM:
                    String cancelReason = data.getStringExtra(ListDialogActivity.REQUEST_ITEM_CONTENT);
                    cancelOrder(cancelReason);
                    break;
            }
        }
    }
}
