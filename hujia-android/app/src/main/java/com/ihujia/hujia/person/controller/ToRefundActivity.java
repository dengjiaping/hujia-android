package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.home.controller.ProductDetailActivity;
import com.ihujia.hujia.network.entities.OrderDetailPageEntity;
import com.ihujia.hujia.network.entities.OrderStoreEntity;
import com.ihujia.hujia.network.entities.ShopCarGoodEntity;
import com.ihujia.hujia.person.adapter.ToRefundAdapter;
import com.ihujia.hujia.utils.CommonTools;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/1/5.
 * 可退款商品列表
 */

public class ToRefundActivity extends ToolBarActivity implements View.OnClickListener {
	public static final int EXTRA_FROM_FINISHED =0x01;
	public static final int EXTRA_FROM_DELIVER =0x02;

	private String orderId;
	private int from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_to_refund);
		from = getIntent().getIntExtra(EXTRA_FROM,EXTRA_FROM_FINISHED);
		if (from == EXTRA_FROM_DELIVER) {
			setLeftTitle(getString(R.string.person_order_apply_refund_money));
		} else {
			setLeftTitle(getString(R.string.person_order_apply_refund));
		}
		initView();
	}

	private void initView() {
		ListView listView = (ListView) findViewById(R.id.torefund_list);
		OrderDetailPageEntity<OrderStoreEntity> orderDetailPageEntity = getIntent().getParcelableExtra(OrderListFragment.EXTRA_ORDER_ITEM);
		List<OrderStoreEntity> storeEntities = orderDetailPageEntity.getOrderStoreEntities();
		OrderStoreEntity entity = storeEntities.get(0);//获取店铺数据
		ToRefundAdapter adapter = new ToRefundAdapter(this, entity.getGoodsList(), this);
		listView.setAdapter(adapter);

		//订单号和订单状态
		View topView = getLayoutInflater().inflate(R.layout.person_order_item_title, null);
		TextView orderNum = (TextView) topView.findViewById(R.id.order_item_store);
		orderNum.setCompoundDrawablesWithIntrinsicBounds(R.drawable.person_order_store, 0, 0, 0);
		TextView orderStatus = (TextView) topView.findViewById(R.id.order_item_state);
		orderId = orderDetailPageEntity.getOrderId();
		orderNum.setText(getString(R.string.person_order_order_number, orderId));
		orderStatus.setText(orderDetailPageEntity.getStatusName());
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, CommonTools.dp2px(this, 44));
		topView.setLayoutParams(params);
		listView.addHeaderView(topView);
		listView.setOnItemClickListener((parent, view, position, id) -> ProductDetailActivity.startProductDetailActivity(ToRefundActivity.this, entity.getGoodsList().get(position-1).getGoodsId()));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.order_item_customer_service:
				if (from == EXTRA_FROM_DELIVER) {
					Intent intent = new Intent(this, ApplyRefundActivity.class);
					ShopCarGoodEntity entity = (ShopCarGoodEntity) view.getTag();
					intent.putExtra("product",entity);
					intent.putExtra("orderId",orderId);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(this, ApplyServiceActivity.class);
					ShopCarGoodEntity entity = (ShopCarGoodEntity) view.getTag();
					intent.putExtra("product",entity);
					intent.putExtra("orderId",orderId);
					startActivity(intent);
				}
				break;
		}
	}
}
