package com.ihujia.hujia.home.controller;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.AnalysisUtil;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.home.adapter.ProductDescribeAdapter;
import com.ihujia.hujia.home.utils.BannerImageLoader;
import com.ihujia.hujia.login.controller.LoginActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.ProductDescEntity;
import com.ihujia.hujia.network.entities.ProductDetailEntity;
import com.ihujia.hujia.network.entities.ProductPicEntity;
import com.ihujia.hujia.network.entities.ShareEntity;
import com.ihujia.hujia.network.entities.ShopCarNumEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.otherstore.controller.StoreRenovationActivity;
import com.ihujia.hujia.person.controller.FixStyleActivity;
import com.ihujia.hujia.person.controller.MyShopCarActivity;
import com.ihujia.hujia.utils.CommonShareUtil;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.LayoutUtil;
import com.ihujia.hujia.utils.NumberFormatUtil;
import com.ihujia.hujia.utils.ViewInjectUtils;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2017/1/8.
 * 商品详情
 */

public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final int INTENT_RESULT_FIX_BUY = 1;
    private static final int INTENT_RESULT_FIX_SHOPCAR = 11;
    public static final String EXTRA_FROM = "extra_from";
    private static final int REQUEST_PRODUCT_DETAIL_CODE = 0x02;
    private static final int REQUEST_PRODUCT_ADD_COLLECT = 0x03;
    private static final int REQUEST_PRODUCT_CANCEL_COLLECT = 0x04;
    private static final int REQUEST_PRODUCT_ADD_SHOPCAR = 0x05;
    private static final int REQUEST_GET_SHOPCAR_NUM = 0x06;
    private static final int REQUEST_NET_SHARE_INFO = 0x07;

    @ViewInject(R.id.product_detail_layout)
    private RelativeLayout productLayout;
    @ViewInject(R.id.left_button)
    private ImageView leftButton;
    @ViewInject(R.id.toolbar_left_title)
    private TextView title;
    @ViewInject(R.id.right_button1)
    private ImageView titleShopcar;
    @ViewInject(R.id.right_text)
    private TextView shopcarNum;
    @ViewInject(R.id.right_button2)
    private ImageView titleShare;
    @ViewInject(R.id.goods_describe_view)
    private View goodsDescribeView;
    @ViewInject(R.id.product_detail_good_describe)
    private TextView goodDescribeTitle;
    @ViewInject(R.id.product_describe_list)
    private ListView describeList;
    @ViewInject(R.id.product_detail_collect_layout)
    private LinearLayout productCollectLayout;
    @ViewInject(R.id.product_detail_collection)
    private ImageView productCollect;
    @ViewInject(R.id.product_detail_shopcar)
    private TextView productShopcar;
    @ViewInject(R.id.product_detail_buy)
    private TextView productBuy;
    @ViewInject(R.id.product_detail_name)
    private TextView productName;
    @ViewInject(R.id.product_detail_price)
    private TextView productPrice;
    @ViewInject(R.id.product_detail_old_price)
    private TextView productOldPrice;
    @ViewInject(R.id.product_detail_store)
    private RelativeLayout storeLayout;
    @ViewInject(R.id.product_detail_store_img)
    private SimpleDraweeView storeImg;
    @ViewInject(R.id.product_detail_store_name)
    private TextView storeName;
    @ViewInject(R.id.product_detail_store_stock)
    private TextView storeStock;
    @ViewInject(R.id.product_detail_banner)
    private Banner banner;
    @ViewInject(R.id.product_detail_web)
    private WebView webview;

    private int i = 0;//购物车数量
    private ProductDetailEntity productDetailEntity;
    private String id;
    private boolean isCollect;

    public static void startProductDetailActivity(Context context, String id) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(EXTRA_ID, id);
        context.startActivity(intent);
    }

    public static void startProductDetailActivity(Context context, String id, int flag) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(EXTRA_ID, id);
        intent.addFlags(flag);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_product_detail);
        ViewInjectUtils.inject(this);
        title.setText(getString(R.string.product_title));
        id = getIntent().getStringExtra(EXTRA_ID);
        loadData();
        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserCenter.isLogin(this)) {
            getShopcarNum();
        }
    }

    private void loadData() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("user_id", UserCenter.getUserId(this));
        params.put("goods_id", id);
        requestHttpData(Constants.Urls.URL_POST_PRODUCT_DETAIL, REQUEST_PRODUCT_DETAIL_CODE, FProtocol.HttpMethod.POST, params);
    }

    private void getShopcarNum() {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("user_id", UserCenter.getUserId(this));
        requestHttpData(Constants.Urls.URL_POST_GET_SHOPCAR_NUM, REQUEST_GET_SHOPCAR_NUM, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void success(int requestCode, String data) {
        closeProgressDialog();
        Entity result = Parsers.getResult(data);
        if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
            switch (requestCode) {
                case REQUEST_PRODUCT_DETAIL_CODE:
                    productDetailEntity = Parsers.getProductDetail(data);
                    if (productDetailEntity != null) {
                        List<ProductDescEntity> describeEntities = productDetailEntity.getDescEntities();
                        if (describeEntities != null && describeEntities.size() > 0) {
                            ProductDescribeAdapter adapter = new ProductDescribeAdapter(this, describeEntities);
                            describeList.setAdapter(adapter);
                            goodDescribeTitle.setVisibility(View.VISIBLE);
                            goodsDescribeView.setVisibility(View.VISIBLE);
                        } else {
                            goodDescribeTitle.setVisibility(View.GONE);
                            goodsDescribeView.setVisibility(View.GONE);
                        }
                        setData();
                    }
                    break;
                case REQUEST_PRODUCT_ADD_COLLECT:
                    Entity addEntity = Parsers.getResult(data);
                    if (REQUEST_NET_SUCCESS.equals(addEntity.getResultCode())) {
                        productCollect.setEnabled(true);
                        productCollect.setImageResource(R.drawable.product_detail_collect_checked);
                        ToastUtil.shortShow(this, getString(R.string.product_favorite_success));
                    } else {
                        ToastUtil.shortShow(this, addEntity.getResultMsg());
                    }
                    break;
                case REQUEST_PRODUCT_CANCEL_COLLECT:
                    Entity cancelEntity = Parsers.getResult(data);
                    if (REQUEST_NET_SUCCESS.equals(cancelEntity.getResultCode())) {
                        productCollect.setEnabled(true);
                        productCollect.setImageResource(R.drawable.product_detail_collect);
                        ToastUtil.shortShow(this, getString(R.string.product_favorite_cancel));
                    } else {
                        ToastUtil.shortShow(this, cancelEntity.getResultMsg());
                    }
                    break;
                case REQUEST_PRODUCT_ADD_SHOPCAR:
                    Entity entity = Parsers.getResult(data);
                    if (REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
                        getShopcarNum();
                    } else {
                        ToastUtil.shortShow(this, entity.getResultMsg());
                    }
                    break;
                case REQUEST_GET_SHOPCAR_NUM:
                    ShopCarNumEntity shopCarNumEntity = Parsers.getShopCarNum(data);
                    if (REQUEST_NET_SUCCESS.equals(shopCarNumEntity.getResultCode())) {
                        String shopCarNum = shopCarNumEntity.getShopcarNum();
                        if (StringUtil.isEmpty(shopCarNum) || "0".equals(shopCarNum)) {
                            shopcarNum.setVisibility(View.GONE);
                        } else {
                            shopcarNum.setVisibility(View.VISIBLE);
                            shopcarNum.setText(shopCarNum);
                        }
                    } else {
                        ToastUtil.shortShow(this, shopCarNumEntity.getResultDesc());
                    }
                    break;
                case REQUEST_NET_SHARE_INFO:
                    ShareEntity share = Parsers.getShare(data);
                    if (share != null) {
                        CommonShareUtil.share(this, share.getDesc(), share.getTitle(), share.getImg(), share.getUrl());
                    }
                    break;
            }
        } else {
            if (REQUEST_PRODUCT_DETAIL_CODE == requestCode&& "10000001".equals(result.getResultCode())) {
                finish();
            }
            ToastUtil.shortShow(this, result.getResultMsg());
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        super.mistake(requestCode, status, errorMessage);
        closeProgressDialog();
    }

    private void setData() {
        initBanner(productDetailEntity);
        productName.setText(productDetailEntity.getProductName());
        productPrice.setText(getString(R.string.price, NumberFormatUtil.formatMoney(productDetailEntity.getProductPrice())));

        if (!StringUtil.isEmpty(productDetailEntity.getProductOldPrice())) {
            productOldPrice.setVisibility(View.VISIBLE);
            String oldPrice = getString(R.string.product_detail_old_price, NumberFormatUtil.formatMoney(productDetailEntity.getProductOldPrice()));
            SpannableStringBuilder spanText = new SpannableStringBuilder(oldPrice);
            spanText.setSpan(new StrikethroughSpan(), 3, oldPrice.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            productOldPrice.setText(spanText);
        } else {
            productOldPrice.setVisibility(View.GONE);
        }

        String url = productDetailEntity.getStoreLogo();
        if (!StringUtil.isEmpty(url)) {
            ImageUtils.setSmallImg(storeImg, url);
        }
        storeName.setText(productDetailEntity.getStoreName());
        storeStock.setText(getString(R.string.product_detail_sale_num, productDetailEntity.getStoreDesc()));
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webview.loadUrl(productDetailEntity.getProductDetail());
        isCollect = productDetailEntity.isCollect();
        if (isCollect) {
            productCollect.setImageResource(R.drawable.product_detail_collect_checked);
        } else {
            productCollect.setImageResource(R.drawable.product_detail_collect);
        }
    }

    /**
     * 商品详情设置轮播图
     **/
    private void initBanner(ProductDetailEntity productDetailEntity) {
        LayoutUtil.setHeightAsWidth(this, banner, 0, 0);
        List<ProductPicEntity> productPicEntities = productDetailEntity.getPicList();
        List<String> bannerUrls = new ArrayList<>();
        if (productPicEntities != null && productPicEntities.size() > 0) {
            for (int j = 0; j < productPicEntities.size(); j++) {
                bannerUrls.add(productDetailEntity.getPicList().get(j).getUrl());
            }
        }
        if (bannerUrls.size() > 0) {
            banner.setImageLoader(new BannerImageLoader());
            banner.setImages(bannerUrls);
            banner.isAutoPlay(false);
            banner.start();
        }
    }

    private void initClick() {
        leftButton.setOnClickListener(this);
        title.setOnClickListener(this);
        titleShare.setOnClickListener(this);
        titleShopcar.setOnClickListener(this);
        productBuy.setOnClickListener(this);
        productCollectLayout.setOnClickListener(this);
        productShopcar.setOnClickListener(this);
        storeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (UserCenter.isLogin(this)) {
            switch (view.getId()) {
                case R.id.left_button:
                case R.id.toolbar_left_title:
                    finish();
                    break;
                case R.id.product_detail_store://店铺详情
                    if (productDetailEntity != null) {
                        StoreRenovationActivity.startStoreRenovationActivity(
                                this,
                                productDetailEntity.getStoreId(),
                                productDetailEntity.getStoreName());
                    }
                    break;
                case R.id.right_button2://分享
                    AnalysisUtil.onEvent(this, "xq_fenxiang");
                    loadShareInfo();
                    break;
                case R.id.right_button1://购物车
                    AnalysisUtil.onEvent(this, "xq_gwc1");
                    startActivity(new Intent(this, MyShopCarActivity.class));
                    break;
                case R.id.product_detail_collect_layout://收藏
                    AnalysisUtil.onEvent(this, "xq_shoucang");
                    if (productDetailEntity != null) {
                        productCollect.setEnabled(false);
                        isCollect = !isCollect;
                        setIsCollect();
                    }
                    break;
                case R.id.product_detail_shopcar://加入购物车
                    AnalysisUtil.onEvent(this, "xq_gwc2");
                    Intent shopcarIntent = new Intent(this, FixStyleActivity.class);
                    shopcarIntent.putExtra(EXTRA_FROM, FixStyleActivity.FROM_PRODUCT_DETAIL);
                    shopcarIntent.putExtra(FixStyleActivity.EXTRA_FLAG, FixStyleActivity.FLAG_SHOPCAR);
                    shopcarIntent.putExtra(FixStyleActivity.EXTRA_ENTITY, productDetailEntity);
                    startActivityForResult(shopcarIntent, INTENT_RESULT_FIX_SHOPCAR);
                    break;
                case R.id.product_detail_buy://立即购买
                    AnalysisUtil.onEvent(this, "xq_buy");
                    Intent buyIntent = new Intent(this, FixStyleActivity.class);
                    buyIntent.putExtra(EXTRA_FROM, FixStyleActivity.FROM_PRODUCT_DETAIL);
                    buyIntent.putExtra(FixStyleActivity.EXTRA_FLAG, FixStyleActivity.FLAG_BUY);
                    buyIntent.putExtra(FixStyleActivity.EXTRA_ENTITY, productDetailEntity);
                    startActivityForResult(buyIntent, INTENT_RESULT_FIX_BUY);
                    break;
            }
        } else {
            switch (view.getId()) {
                case R.id.left_button:
                case R.id.toolbar_left_title:
                    finish();
                    break;
                case R.id.product_detail_store://店铺详情
                    StoreRenovationActivity.startStoreRenovationActivity(
                            this,
                            productDetailEntity.getStoreId(),
                            productDetailEntity.getStoreName());
                    break;
                case R.id.right_button2://分享
                    AnalysisUtil.onEvent(this, "xq_fenxiang");
                    loadShareInfo();
                    break;
                case R.id.right_button1://购物车
                    AnalysisUtil.onEvent(this, "xq_gwc1");
                    startActivity(new Intent(this, LoginActivity.class));
                    break;
                case R.id.product_detail_collect_layout://收藏
                    AnalysisUtil.onEvent(this, "xq_shoucang");
                    startActivity(new Intent(this, LoginActivity.class));
                    break;
                case R.id.product_detail_collection:
                    startActivity(new Intent(this, LoginActivity.class));
                    break;
                case R.id.product_detail_shopcar://加入购物车
                    AnalysisUtil.onEvent(this, "xq_gwc2");
                    startActivity(new Intent(this, LoginActivity.class));
                    break;
                case R.id.product_detail_buy://立即购买
                    AnalysisUtil.onEvent(this, "xq_buy");
                    startActivity(new Intent(this, LoginActivity.class));
                    break;
            }
        }
    }

    private void loadShareInfo() {
        showProgressDialog();
        requestHttpData(Constants.Urls.URL_POST_SHARE_INFO, REQUEST_NET_SHARE_INFO, FProtocol.HttpMethod.POST, null);
    }

    private void setIsCollect() {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("user_id", UserCenter.getUserId(this));
        params.put("goods_id", productDetailEntity.getProductId());
        if (isCollect) {
            requestHttpData(Constants.Urls.URL_POST_PRODUCT_ADD_COLLECT, REQUEST_PRODUCT_ADD_COLLECT, FProtocol.HttpMethod.POST, params);
        } else {
            requestHttpData(Constants.Urls.URL_POST_COLLECTION_DELETE, REQUEST_PRODUCT_CANCEL_COLLECT, FProtocol.HttpMethod.POST, params);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case INTENT_RESULT_FIX_BUY:
                    String productNum = data.getStringExtra("productNum");
                    String skuId1 = data.getStringExtra("skuId");
                    Intent intent = new Intent(this, ConfirmOrderActivity.class);
                    intent.putExtra(EXTRA_FROM, ConfirmOrderActivity.EXTRA_FROM_PRODUCT_DETAIL);
                    intent.putExtra("productNum", productNum);
                    intent.putExtra("skuId", skuId1);
                    intent.putExtra("storeId", productDetailEntity.getStoreId());
                    startActivity(intent);
                    break;
                case INTENT_RESULT_FIX_SHOPCAR:
                    String count = data.getStringExtra("productNum");
                    String skuId = data.getStringExtra("skuId");
                    if (!StringUtil.isEmpty(skuId) && Integer.parseInt(count) > 0) {
                        IdentityHashMap<String, String> params = new IdentityHashMap<>();
                        params.put("user_id", UserCenter.getUserId(this));
                        params.put("store_id", productDetailEntity.getStoreId());
                        params.put("goods_id", productDetailEntity.getProductId());
                        params.put("sku_id", skuId);
                        params.put("goods_count", count);
                        requestHttpData(Constants.Urls.URL_POST_PRODUCT_ADD_SHOPCAR, REQUEST_PRODUCT_ADD_SHOPCAR, FProtocol.HttpMethod.POST, params);
                    }
                    break;
            }
        }
    }

    /**
     * 把商品添加到购物车的动画效果
     */
    private void addCart(TextView iv) {
//      一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        final ImageView goods = new ImageView(ProductDetailActivity.this);
        goods.setImageResource(R.drawable.list_check_checked);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
        productLayout.addView(goods, params);

//        二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = {0, 0};
//		int[] parentLocation = new int[2];
//		productLayout.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        iv.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        titleShopcar.getLocationInWindow(endLoc);

//        三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0] + iv.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] - iv.getHeight() / 2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + titleShopcar.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

//        四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo(startX, (startY - toY) / 2, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        PathMeasure mPathMeasure = new PathMeasure(path, false);

        //属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(400);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            // 当插值计算进行时，获取中间的每个值，
            // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
            float value = (Float) animation.getAnimatedValue();
            // ★★★★★获取当前点坐标封装到mCurrentPosition
            // boolean getPosTan(float distance, float[] pos, float[] tan) ：
            // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
            // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
            float[] mCurrentPosition = new float[2];
            mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
            // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
            goods.setTranslationX(mCurrentPosition[0]);
            goods.setTranslationY(mCurrentPosition[1]);
        });
//      五、 开始执行动画
        valueAnimator.start();

//      六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 购物车的数量加1
                i++;
                shopcarNum.setVisibility(View.VISIBLE);
                shopcarNum.setText(String.valueOf(i));
                // 把移动的图片imageview从父布局里移除
                productLayout.removeView(goods);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
