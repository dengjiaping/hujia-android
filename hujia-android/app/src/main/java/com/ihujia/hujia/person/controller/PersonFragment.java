package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseFragment;
import com.ihujia.hujia.login.controller.LoginActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.PersonEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.IdentityHashMap;

/**
 * Created by liuzhichao on 2016/12/16.
 * 我的
 */
public class PersonFragment extends BaseFragment implements View.OnClickListener {
	private static final int REQUEST_PERSON_CODE = 0x01;

	@ViewInject(R.id.person_main_setting)
	private ImageView personSetting;
	@ViewInject(R.id.person_main_message)
	private FrameLayout personMessage;
	@ViewInject(R.id.person_message_dot)
	private TextView personMessageDot;
	@ViewInject(R.id.person_main_avatar)
	private SimpleDraweeView personAvatar;
	@ViewInject(R.id.person_main_phone)
	private TextView personPhone;
	@ViewInject(R.id.person_all_order)
	private RelativeLayout personAllOrder;
	//textview为订单类型
	@ViewInject(R.id.person_order_paying)
	private TextView personOrderPaying;
	@ViewInject(R.id.person_order_geting)
	private TextView personOrderGetting;
	@ViewInject(R.id.person_order_deliver)
	private TextView personOrderDeliver;
	@ViewInject(R.id.person_order_refund)
	private TextView personOrderRefund;
	//textview为网格布局中的元素
	@ViewInject(R.id.person_frag_shopping)
	private TextView personShoppingcar;
	@ViewInject(R.id.person_frag_downline_pay)
	private TextView personDownLinePay;
	@ViewInject(R.id.person_frag_password_set)
	private TextView personPasswordSet;
	@ViewInject(R.id.person_frag_vip_card)
	private TextView personVipCard;
	@ViewInject(R.id.person_frag_integral)
	private TextView personIntegral;
	//	@ViewInject(R.id.person_frag_coupon)
//	private TextView personCoupon;
//	@ViewInject(R.id.person_frag_collection)
//	private TextView personCollection;
//	@ViewInject(R.id.person_frag_address_manager)
//	private TextView personAddressManager;
//	@ViewInject(R.id.person_frag_about_us)
//	private TextView personAboutUs;
	//layout 为网格布局，用于设置宽高相等
	@ViewInject(R.id.person_order_paying_view)
	private FrameLayout orderPayingView;
	@ViewInject(R.id.person_order_deliver_view)
	private FrameLayout orderDeliverView;
	@ViewInject(R.id.person_order_geting_view)
	private FrameLayout orderGetingView;
	@ViewInject(R.id.person_order_refund_view)
	private FrameLayout orderRefundView;
	//列表布局中的元素及更多按钮
	@ViewInject(R.id.person_collection)
	private RelativeLayout personCollection;
	@ViewInject(R.id.person_collection_more)
	private TextView personCollectionMore;
	@ViewInject(R.id.person_coupon)
	private RelativeLayout personCoupon;
	@ViewInject(R.id.person_coupon_more)
	private TextView personCouponMore;
	@ViewInject(R.id.person_address)
	private RelativeLayout personAddressManager;
	@ViewInject(R.id.person_about_us)
	private RelativeLayout personAboutUs;

	private TextView orderPayingDot;
	private TextView orderDeliverDot;
	private TextView orderGetingDot;
	private TextView orderRefundDot;
	private View view;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.person_frag, null);
			ViewInjectUtils.inject(this, view);
			initOnclickListener();
			initView();
//		setLayoutHeight();
		}
		ViewGroup mViewParent = (ViewGroup) view.getParent();
		if (mViewParent != null) {
			mViewParent.removeView(view);
		}
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();
		if (UserCenter.isLogin(getActivity())) {
			ImageUtils.setSmallImg(personAvatar, UserCenter.getUserAvatar(getActivity()));
			personPhone.setText(UserCenter.getNickName(getActivity()));
		} else {
			personAvatar.setImageResource(R.drawable.default_avatar_bg);
			personPhone.setText(getString(R.string.person_click_login));
			setDotText("0", personMessageDot);
			setDotText("0", orderPayingDot);
			setDotText("0", orderDeliverDot);
			setDotText("0", orderGetingDot);
			setDotText("0", orderRefundDot);
			personCollectionMore.setText("");
			personCouponMore.setText("");
		}
	}

	private void initView() {
		orderPayingDot = (TextView) orderPayingView.findViewById(R.id.person_order_red_dot);
		orderDeliverDot = (TextView) orderDeliverView.findViewById(R.id.person_order_red_dot);
		orderGetingDot = (TextView) orderGetingView.findViewById(R.id.person_order_red_dot);
		orderRefundDot = (TextView) orderRefundView.findViewById(R.id.person_order_red_dot);
	}

	private void loadData() {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("user_id", UserCenter.getUserId(getActivity()));
		requestHttpData(Constants.Urls.URL_POST_PERSON, REQUEST_PERSON_CODE, FProtocol.HttpMethod.POST, params);
	}

	private void initOnclickListener() {
		personSetting.setOnClickListener(this);
		personMessage.setOnClickListener(this);
		personAvatar.setOnClickListener(this);
		personPhone.setOnClickListener(this);
		personAllOrder.setOnClickListener(this);
		personOrderPaying.setOnClickListener(this);
		personOrderGetting.setOnClickListener(this);
		personOrderDeliver.setOnClickListener(this);
		personOrderRefund.setOnClickListener(this);
		personShoppingcar.setOnClickListener(this);
		personDownLinePay.setOnClickListener(this);
		personPasswordSet.setOnClickListener(this);
		personVipCard.setOnClickListener(this);
		personIntegral.setOnClickListener(this);
		personCoupon.setOnClickListener(this);
		personCollection.setOnClickListener(this);
		personAddressManager.setOnClickListener(this);
		personAboutUs.setOnClickListener(this);
	}

	@Override
	public void success(int requestCode, String data) {
		super.success(requestCode, data);
		PersonEntity entity = Parsers.getPersonOrderNum(data);
		if (entity == null) {
			return;
		}
		String newsNum = entity.getNewsNum();
		String payingNum = entity.getPayingNum();
		String deliverNum = entity.getDeliverNum();
		String gettingNum = entity.getGettingNum();
		String refundNum = entity.getRefundNum();
		String couponNum = entity.getCouponNum();
		String collectionNum = entity.getCollectionNum();

		//设置未读消息数量和订单数量
		setDotText(newsNum, personMessageDot);
		setDotText(payingNum, orderPayingDot);
		setDotText(deliverNum, orderDeliverDot);
		setDotText(gettingNum, orderGetingDot);
		setDotText(refundNum, orderRefundDot);

		//设置我的收藏数据
		if (!StringUtil.isEmpty(collectionNum)) {
			if ("0".equals(collectionNum)) {
				personCollectionMore.setText("");
			} else if (Integer.parseInt(collectionNum) > 99) {
				personCollectionMore.setText("99+");
			} else {
				personCollectionMore.setText(collectionNum);
			}
		}

		//设置我的优惠券数量
		if (!StringUtil.isEmpty(couponNum)) {
			if ("0".equals(couponNum)) {
				personCouponMore.setText("");
			} else if (Integer.parseInt(couponNum) > 99) {
				personCouponMore.setText("99+");
			} else {
				personCouponMore.setText(couponNum);
			}
		}
	}

	private void setDotText(String num, TextView view) {
		if (!StringUtil.isEmpty(num)) {
			if ("0".equals(num)) {
				view.setVisibility(View.GONE);
			} else if (Integer.parseInt(num) > 99) {
				view.setVisibility(View.VISIBLE);
				view.setText("99+");
			} else {
				view.setVisibility(View.VISIBLE);
				view.setText(num);
			}
		}
	}

	@Override
	public void onClick(View view) {
		if (UserCenter.isLogin(getActivity())) {
			switch (view.getId()) {
				//设置
				case R.id.person_main_setting:
					startActivity(new Intent(getActivity(), SettingActivity.class));
					break;
				//消息
				case R.id.person_main_message:
					startActivity(new Intent(getActivity(), MessageActivity.class));
					break;
				//个人信息
				case R.id.person_main_phone:
				case R.id.person_main_avatar:
					startActivity(new Intent(getActivity(), PersonInfoActivity.class));
					break;
				//全部订单
				case R.id.person_all_order:
					Intent intentAll = new Intent(getActivity(), OrderListActivity.class);
					intentAll.putExtra(OrderListActivity.EXTRA_ORDERTYPE, OrderListActivity.ORDERTYPE_ALL);
					startActivity(intentAll);
					break;
				//待付款
				case R.id.person_order_paying:
					Intent intentPaying = new Intent(getActivity(), OrderListActivity.class);
					intentPaying.putExtra(OrderListActivity.EXTRA_ORDERTYPE, OrderListActivity.ORDERTYPE_PAYING);
					startActivity(intentPaying);
					break;
				//待发货
				case R.id.person_order_deliver:
					Intent intentFinished = new Intent(getActivity(), OrderListActivity.class);
					intentFinished.putExtra(OrderListActivity.EXTRA_ORDERTYPE, OrderListActivity.ORDERTYPE_DELIVER);
					startActivity(intentFinished);
					break;
				//待收货
				case R.id.person_order_geting:
					Intent intentGetting = new Intent(getActivity(), OrderListActivity.class);
					intentGetting.putExtra(OrderListActivity.EXTRA_ORDERTYPE, OrderListActivity.ORDERTYPE_GETING);
					startActivity(intentGetting);
					break;
				//退款/售后
				case R.id.person_order_refund:
					startActivity(new Intent(getActivity(), RefundListActivity.class));
					break;
				//我的购物车
				case R.id.person_frag_shopping:
					startActivity(new Intent(getActivity(), MyShopCarActivity.class));
					break;
				//线下支付
				case R.id.person_frag_downline_pay:
					startActivity(new Intent(getActivity(), DownlinePayActivity.class));
					break;
				//支付密码设置
				case R.id.person_frag_password_set:
					startActivity(new Intent(getActivity(), SetPasswordActivity.class));
					break;
				//我的贵宾卡
				case R.id.person_frag_vip_card:
					startActivity(new Intent(getActivity(), MyVipCardActivity.class));
					break;
				//我的积分
				case R.id.person_frag_integral:
					startActivity(new Intent(getActivity(), IntegralActivity.class));
					break;
				//我的优惠券
				case R.id.person_coupon:
					startActivity(new Intent(getActivity(), MyCouponActivity.class));
					break;
				//我的收藏
				case R.id.person_collection:
					startActivity(new Intent(getActivity(), MyCollectionActivity.class));
					break;
				//地址管理
				case R.id.person_address:
					startActivity(new Intent(getActivity(), AddressMangerActivity.class));
					break;
				//关于我们
				case R.id.person_about_us:
					startActivity(new Intent(getActivity(), AboutUsActivity.class));
					break;
			}
		} else {
			switch (view.getId()) {
				//设置
				case R.id.person_main_setting:
					startActivity(new Intent(getActivity(), SettingActivity.class));
					break;
				//关于我们
				case R.id.person_about_us:
					startActivity(new Intent(getActivity(), AboutUsActivity.class));
					break;
				//消息
				case R.id.person_main_message:
					//个人信息
				case R.id.person_main_phone:
				case R.id.person_main_avatar:
					//全部订单
				case R.id.person_all_order:
					//待付款
				case R.id.person_order_paying:
					//待发货
				case R.id.person_order_deliver:
					//待收货
				case R.id.person_order_geting:
					//退款/售后
				case R.id.person_order_refund:
					//我的购物车
				case R.id.person_frag_shopping:
					//线下支付
				case R.id.person_frag_downline_pay:
					//支付密码设置
				case R.id.person_frag_password_set:
					//我的贵宾卡
				case R.id.person_frag_vip_card:
					//我的积分
				case R.id.person_frag_integral:
					//我的优惠券
				case R.id.person_coupon:
					//我的收藏
				case R.id.person_collection:
					//地址管理
				case R.id.person_address:
					LoginActivity.startLoginActivity(getActivity());
					break;
			}
		}
	}
}
