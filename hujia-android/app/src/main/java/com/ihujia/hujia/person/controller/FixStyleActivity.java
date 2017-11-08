package com.ihujia.hujia.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.AnalysisUtil;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.FixSizeAndColorEntity;
import com.ihujia.hujia.network.entities.PagesEntity;
import com.ihujia.hujia.network.entities.ProductAttrEntity;
import com.ihujia.hujia.network.entities.ProductDetailEntity;
import com.ihujia.hujia.network.entities.ShopCarFixEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.flowlayout.FixStyleSizeAdapter;
import com.ihujia.hujia.person.flowlayout.FlowTagLayout;
import com.ihujia.hujia.person.flowlayout.OnTagSelectListener;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.ViewInjectUtils;
import com.ihujia.hujia.widget.AddMinuLayout;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2016/12/28.
 * 商品修改颜色尺寸
 */
public class FixStyleActivity extends BaseActivity implements View.OnClickListener, OnTagSelectListener {

	public static final String EXTRA_FROM = "extra_from";
	public static final String EXTRA_ENTITY = "extra_entity";
	public static final String EXTRA_FLAG = "extra_flag";
	public static final int FROM_SHOPCAR = 0x11;
	public static final int FROM_PRODUCT_DETAIL = 0x22;
	public static final int FLAG_SHOPCAR = 0x1;
	public static final int FLAG_BUY = 0x2;
	private static final int REQUEST_FIX_ATTR_SIZE_CODE = 0X01;//根据尺寸获取数据
	private static final int REQUEST_FIX_ATTR_COLOR_CODE = 0X02;//根据颜色获取数据

	@ViewInject(R.id.product_fix_img)
	private SimpleDraweeView productImg;
	@ViewInject(R.id.product_fix_name)
	private TextView productName;
	@ViewInject(R.id.product_fix_price)
	private TextView productPrice;
	@ViewInject(R.id.product_fix_old_price)
	private TextView productOldPrice;
	@ViewInject(R.id.product_fix_num)
	private TextView productNum;
	@ViewInject(R.id.product_fix_attr1)
	private TextView productAttr1;
	@ViewInject(R.id.product_fix_attr2)
	private TextView productAttr2;
	@ViewInject(R.id.product_fix_size)
	private FlowTagLayout productSize;
	@ViewInject(R.id.product_fix_color)
	private FlowTagLayout productColor;
	@ViewInject(R.id.product_fix_num_change)
	private AddMinuLayout fixNum;
	@ViewInject(R.id.shopcar_fix_ok)
	private TextView fixOk;
	@ViewInject(R.id.product_fix_close)
	private ImageView productClose;

	private String sizeId;
	private String colorId;
	private ProductAttrEntity sizeEntity;
	private ProductAttrEntity colorEntity;
	private FixStyleSizeAdapter sizeAdapter, colorAdapter;
	private boolean isAfterRequestSize, isAfterRequestColor;
	private List<FixSizeAndColorEntity> requestSizeEntity;
	private List<FixSizeAndColorEntity> requestColorEntity;
	private int oldSizePosition = -1, oldColorPosition = -1;
	private String mainKey;
	private String skuId;
	private int from;
	private List<ProductAttrEntity> attrEntities;
	private String goodsId;
	private String selectColor, selectSize;
	private int maxBuyNum;
	private int flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_act_shopcar_fixstyle);
		ViewInjectUtils.inject(this);
		initStyle();
		initView();
		fixOk.setOnClickListener(this);
		productClose.setOnClickListener(this);
	}

	private void initView() {
		from = getIntent().getIntExtra(EXTRA_FROM, 0X11);
		flag = getIntent().getIntExtra(EXTRA_FLAG, 0);
		if (FROM_PRODUCT_DETAIL == from) {
			ProductDetailEntity productEntity = getIntent().getParcelableExtra(EXTRA_ENTITY);

			if (productEntity != null) {
				goodsId = productEntity.getProductId();
				if (productEntity.getPicList() != null && productEntity.getPicList().size() > 0) {
					String url = productEntity.getPicList().get(0).getUrl();
					if (!StringUtil.isEmpty(url)) {
						ImageUtils.setSmallImg(productImg, url);
					}
				}
				productName.setText(productEntity.getProductName());
				productPrice.setText(getString(R.string.product_detail_old_price, productEntity.getProductPrice()));

				String oldPrice = getString(R.string.product_detail_old_price, productEntity.getProductOldPrice());
				SpannableStringBuilder spanText = new SpannableStringBuilder(oldPrice);
				spanText.setSpan(new StrikethroughSpan(), 3, oldPrice.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				productOldPrice.setText(spanText);

				productNum.setText(getString(R.string.product_fix_stock_num, productEntity.getProductNum()));
				attrEntities = productEntity.getAttrEntities();
				setSizeAndColor();
			}
		} else if (FROM_SHOPCAR == from) {
			ShopCarFixEntity shopCarFixEntity = getIntent().getParcelableExtra(EXTRA_ENTITY);

			if (shopCarFixEntity != null) {
				goodsId = shopCarFixEntity.getGoodsId();
				String url = shopCarFixEntity.getGoodsPic();
				if (!StringUtil.isEmpty(url)) {
					ImageUtils.setSmallImg(productImg, url);
				}
				productName.setText(shopCarFixEntity.getGoodsName());
				productPrice.setText(getString(R.string.product_detail_old_price, shopCarFixEntity.getGoodsPrice()));

				String oldPrice = getString(R.string.product_detail_old_price, shopCarFixEntity.getGoodsOldPrice());
				SpannableStringBuilder spanText = new SpannableStringBuilder(oldPrice);
				spanText.setSpan(new StrikethroughSpan(), 3, oldPrice.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				productOldPrice.setText(spanText);

				productNum.setText(getString(R.string.product_fix_stock_num, shopCarFixEntity.getGoodsStock()));
				attrEntities = shopCarFixEntity.getAttrList();
				setSizeAndColor();
			}
		}
	}

	private void setSizeAndColor() {
		sizeEntity = attrEntities.get(0);
		colorEntity = attrEntities.get(1);

		productAttr1.setText(sizeEntity.getAttrName());
		sizeAdapter = new FixStyleSizeAdapter(this);
		productSize.setAdapter(sizeAdapter);
		productSize.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
		initSizeDatas(sizeEntity, sizeAdapter);
		productSize.setOnTagSelectListener(this);

		productAttr2.setText(colorEntity.getAttrName());
		colorAdapter = new FixStyleSizeAdapter(this);
		productColor.setAdapter(colorAdapter);
		productColor.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
		initSizeDatas(colorEntity, colorAdapter);
		productColor.setOnTagSelectListener(this);
	}

	private void initSizeDatas(ProductAttrEntity entity, FixStyleSizeAdapter adapter) {
		final List<String> attrDatas = new ArrayList<>();
		for (int i = 0; i < entity.getAttrList().size(); i++) {
			attrDatas.add(entity.getAttrList().get(i).getAttrValue());
		}
		adapter.onlyAddAll(attrDatas);
	}

	private void initStyle() {
		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams wl = window.getAttributes();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		this.onWindowAttributesChanged(wl);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.actionsheet_dialog_out);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.shopcar_fix_ok:
				if (FLAG_BUY == flag) {
					AnalysisUtil.onEvent(this, "buy_chenggong");
				} else if (FLAG_SHOPCAR == flag) {
					AnalysisUtil.onEvent(this, "gwc_chenggong");
				}
				Intent intent = getIntent();
				String num = fixNum.getResult();
				int numInt = Integer.parseInt(num);
				if (!StringUtil.isEmpty(skuId)) {
					if (numInt > maxBuyNum) {
						ToastUtil.shortShow(this, getString(R.string.product_fix_error_num));
					} else {
						if (FROM_PRODUCT_DETAIL == from) {
							intent.putExtra("productNum", num);
							intent.putExtra("skuId", skuId);
						} else if (FROM_SHOPCAR == from) {
							boolean isFix = !StringUtil.isEmpty(skuId) && !StringUtil.isEmpty(selectColor) && !StringUtil.isEmpty(selectSize);
							intent.putExtra("productNum", num);
							intent.putExtra("skuId", skuId);
							intent.putExtra("color", selectColor);
							intent.putExtra("size", selectSize);
							intent.putExtra("isFix", isFix);
						}
						setResult(RESULT_OK, intent);
						finish();
					}
				} else {
					ToastUtil.shortShow(this, getString(R.string.product_fix_no_color_or_size));
				}
				break;
			case R.id.product_fix_close:
				finish();
				break;
		}
	}

	@Override
	public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
		switch (parent.getId()) {
			case R.id.product_fix_size:
				if (selectedList != null && selectedList.size() > 0) {
					int sizePosition = selectedList.get(0);
					if (isAfterRequestSize) {
						if (requestSizeEntity != null && requestSizeEntity.size() > sizePosition) {
							sizeId = requestSizeEntity.get(sizePosition).getValueId();
							selectSize = requestSizeEntity.get(sizePosition).getValueName();
						}
					} else {
						sizeId = sizeEntity.getAttrList().get(sizePosition).getAttrId();
						selectSize = sizeEntity.getAttrList().get(sizePosition).getAttrValue();
					}
					if (StringUtil.isEmpty(colorId)) {//选择sizeId的尺寸时，如果未选择颜色，以尺寸获取数据
						getAttrData(colorId, sizeId, REQUEST_FIX_ATTR_SIZE_CODE);
					} else if (!StringUtil.isEmpty(colorId) && oldSizePosition != sizePosition && mainKey.equals("size")) {
						getAttrData("", sizeId, REQUEST_FIX_ATTR_SIZE_CODE);
					} else {//选择sizeId的尺寸时，如果已经选择颜色，就直接从已经获取到的数据中取出图片价格库存等显示。
						FixSizeAndColorEntity entity = requestSizeEntity.get(sizePosition);
						setHadData(entity);
					}
					oldSizePosition = sizePosition;
				} else {
					sizeId = "";//取消选择某个尺寸时，如果没有选中的颜色，就回归初始状态；如果有选择的颜色，就以选中的颜色获取数据
					if (!StringUtil.isEmpty(colorId)) {
						getAttrData(colorId, sizeId, REQUEST_FIX_ATTR_COLOR_CODE);
					} else {
						setSizeAndColor();
					}
				}
				break;
			case R.id.product_fix_color:
				if (selectedList != null && selectedList.size() > 0) {
					int colorPosition = selectedList.get(0);
					if (isAfterRequestColor) {
						if (requestColorEntity!=null && requestColorEntity.size()>0) {
							colorId = requestColorEntity.get(colorPosition).getValueId();
							selectColor = requestColorEntity.get(colorPosition).getValueName();
						}
					} else {
						colorId = colorEntity.getAttrList().get(colorPosition).getAttrId();
						selectColor = colorEntity.getAttrList().get(colorPosition).getAttrValue();
					}
					if (StringUtil.isEmpty(sizeId)) {
						getAttrData(colorId, sizeId, REQUEST_FIX_ATTR_COLOR_CODE);
					} else if (!StringUtil.isEmpty(sizeId) && oldColorPosition != colorPosition && "color".equals(mainKey)) {
						getAttrData(colorId, "", REQUEST_FIX_ATTR_COLOR_CODE);
					} else {
						if (requestColorEntity != null) {
							FixSizeAndColorEntity entity = requestColorEntity.get(colorPosition);
							setHadData(entity);
						}
					}
					oldColorPosition = colorPosition;
				} else {
					colorId = "";
					if (!StringUtil.isEmpty(sizeId)) {
						getAttrData(colorId, sizeId, REQUEST_FIX_ATTR_SIZE_CODE);
					} else {
						setSizeAndColor();
					}
				}
				break;
		}
	}

	private void setHadData(FixSizeAndColorEntity entity) {
		//String url = requestSizeEntity.get(sizePosition).//设置图片
		//设置价格
		productPrice.setText(getString(R.string.product_detail_old_price, entity.getSellPrice()));
		//设置原价
		if (StringUtil.isEmpty(entity.getOldPrice())) {
			productOldPrice.setVisibility(View.GONE);
		} else {
			productOldPrice.setVisibility(View.VISIBLE);
			String oldPrice = getString(R.string.product_detail_old_price, entity.getOldPrice());
			SpannableStringBuilder spanText = new SpannableStringBuilder(oldPrice);
			spanText.setSpan(new StrikethroughSpan(), 3, oldPrice.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			productOldPrice.setText(spanText);
		}

		String stockNum = entity.getStockNum();
		if (!StringUtil.isEmpty(stockNum)) {
			maxBuyNum = Integer.parseInt(stockNum);
			productNum.setText(getString(R.string.product_fix_stock_num, stockNum));
		}
		skuId = entity.getSkuId();
	}

	private void getAttrData(String colorId, String sizeId, int requestFixAttrColorCode) {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("goods_id", goodsId);
		params.put("color_id", colorId);
		params.put("size_id", sizeId);
		requestHttpData(Constants.Urls.URL_POST_FIX_ATTR, requestFixAttrColorCode, FProtocol.HttpMethod.POST, params);
		skuId = "";
	}

	@Override
	public void success(int requestCode, String data) {
		super.success(requestCode, data);
		closeProgressDialog();
		switch (requestCode) {
			case REQUEST_FIX_ATTR_COLOR_CODE://以颜色获取的数据，展示尺寸
				PagesEntity<FixSizeAndColorEntity> fixSizeEntity = Parsers.getSizeOrColor(data);
				if (fixSizeEntity != null) {
					requestSizeEntity = fixSizeEntity.getDatas();
					List<String> onlySizeEntity = new ArrayList<>();
					if (requestSizeEntity != null && requestSizeEntity.size() > 0) {
						for (FixSizeAndColorEntity sizeEntity : requestSizeEntity) {
							onlySizeEntity.add(sizeEntity.getValueName());
						}
						sizeAdapter.clearAndAddAll(onlySizeEntity);
					} else {
						sizeAdapter.clearAll();
					}
					isAfterRequestSize = true;
					mainKey = "color";
				}
				break;
			case REQUEST_FIX_ATTR_SIZE_CODE://以尺寸获取的数据，展示颜色
				PagesEntity<FixSizeAndColorEntity> fixColorEntity = Parsers.getSizeOrColor(data);
				if (fixColorEntity != null) {
					requestColorEntity = fixColorEntity.getDatas();
					List<String> onlyColorEntity = new ArrayList<>();
					if (requestColorEntity != null && requestColorEntity.size() > 0) {
						for (FixSizeAndColorEntity colorEntity : requestColorEntity) {
							onlyColorEntity.add(colorEntity.getValueName());
						}
						colorAdapter.clearAndAddAll(onlyColorEntity);
					} else {
						colorAdapter.clearAll();
					}
					isAfterRequestColor = true;
					mainKey = "size";
				}
				break;
		}
	}
}
