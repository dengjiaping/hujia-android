package com.ihujia.hujia.store.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.DeviceUtil;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.RefreshRecyclerView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.ToolBarActivity;
import com.ihujia.hujia.home.adapter.SecondCategoryAdapter;
import com.ihujia.hujia.home.controller.ProductDetailActivity;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.BrandEntity;
import com.ihujia.hujia.network.entities.CategoryEntity;
import com.ihujia.hujia.network.entities.ConditionEntity;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.PagesEntity;
import com.ihujia.hujia.network.entities.PriceEntity;
import com.ihujia.hujia.network.entities.ProductEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.store.adapter.ProductAdapter;
import com.ihujia.hujia.utils.CommonTools;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by liuzhichao on 2017/1/6.
 * 商品列表
 */
public class ProductActivity extends ToolBarActivity implements RefreshRecyclerView.OnRefreshAndLoadMoreListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private static final int REQUEST_NET_CONDITION_LIST = 10;
    private static final int REQUEST_NET_PRODUCT_LIST = 20;
    private static final int REQUEST_NET_PRODUCT_LIST_MORE = 30;
    public static final int FROM_HOME_BOTTOM = 1;
    public static final int FROM_HOME_CATEGORY = 2;
    public static final int FROM_BRAND_DETAIL = 3;
    public static final int FROM_STORE_DETAIL = 4;
    private int popHeight;
    private static final String EXTRA_CONDITION = "extra_condition";
    private static final String EXTRA_CATEGORY_NAME = "extra_category_name";
    private static final String EXTRA_CATEGORY_ID = "extra_category_id";
    private static final String EXTRA_STRORE_ID = "extra_store_id";
    private static final String EXTRA_BRAND_ID = "extra_brand_id";
    private static final String EXTRA_BRAND_NAME = "extra_brand_name";

    @ViewInject(R.id.ll_product_filter)
    private View llProductFilter;
    @ViewInject(R.id.fl_product_brand)
    private View flProductBrand;
    @ViewInject(R.id.fl_product_category)
    private View flProductCategory;
    @ViewInject(R.id.fl_product_price)
    private View flProductPrice;
    @ViewInject(R.id.tv_product_brand)
    private TextView tvProductBrand;
    @ViewInject(R.id.tv_product_category)
    private TextView tvProductCategory;
    @ViewInject(R.id.tv_product_price)
    private TextView tvProductPrice;
    @ViewInject(R.id.rrv_product_list)
    private RefreshRecyclerView rrvProductList;

    private float firstX = -1;
    private float firstY = -1;
    private int preBrand = 0;
    private int prePrice = 0;
    private int preCategory = 0;
    private int tempCategory;
    private List<BrandEntity> brandEntities;
    private List<PriceEntity> priceEntities;
    private ProductAdapter adapter;
    private String brandId = "";
    private String categoryId = "";
    private String minPrice = "";
    private String maxPrice = "";
    private List<CategoryEntity> categoryEntities;
    private PopupWindow popupWindow;
    private String condition;
    private int from;
    private String recommendId;
    private String storeId;
    private ListView lvCategoryDetail;
    private ListView lvCategoryMain;

    public static void startProductActivity(Context context, int from, String storeId) {
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra(EXTRA_FROM, from);
        intent.putExtra(EXTRA_STRORE_ID, storeId);
        context.startActivity(intent);
    }

    public static void startProductActivity(Context context, int from, String categoryName, String categoryId) {
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra(EXTRA_FROM, from);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        context.startActivity(intent);
    }

    public static void startProductActivity(Context context, String brandId, String brandName, int from) {
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra(EXTRA_FROM, from);
        intent.putExtra(EXTRA_BRAND_ID, brandId);
        intent.putExtra(EXTRA_BRAND_NAME, brandName);
        context.startActivity(intent);
    }

    public static void startProductActivity(Context context, String condition, int from, String recommendId) {
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra(EXTRA_CONDITION, condition);
        intent.putExtra(EXTRA_FROM, from);
        intent.putExtra(EXTRA_ID, recommendId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_product);
        ViewInjectUtils.inject(this);
        initLoadingView(this);
        setLoadingStatus(LoadingStatus.LOADING);
        initView();
        initData();
    }

    private void initView() {
        condition = getIntent().getStringExtra(EXTRA_CONDITION);
        if (StringUtil.isEmpty(condition)) {
            setLeftTitle(getString(R.string.product_list_title));
        } else {
            setLeftTitle(condition);
        }
        //不要在属性上初始化，因为有些东西没有初始化，高度获取不正确
        popHeight = DeviceUtil.getHeight(this) - CommonTools.getStatusBarHeight(this) - DeviceUtil.dp_to_px(this, 88);
        from = getIntent().getIntExtra(EXTRA_FROM, 0);
        recommendId = getIntent().getStringExtra(EXTRA_ID);

        mImgLoadingEmpty.setImageResource(R.drawable.ic_search_empty);
        mTxtLoadingEmpty.setText(getString(R.string.product_search_nothing));
        if (FROM_HOME_BOTTOM == from) {
            llProductFilter.setVisibility(View.GONE);
        } else if (FROM_HOME_CATEGORY == from) {
            categoryId = getIntent().getStringExtra(EXTRA_CATEGORY_ID);
            tvProductCategory.setText(getIntent().getStringExtra(EXTRA_CATEGORY_NAME));
        } else if (FROM_BRAND_DETAIL == from) {
            brandId = getIntent().getStringExtra(EXTRA_BRAND_ID);
            String brandName = getIntent().getStringExtra(EXTRA_BRAND_NAME);
            tvProductBrand.setText(brandName);
        } else if (FROM_STORE_DETAIL == from) {
            storeId = getIntent().getStringExtra(EXTRA_STRORE_ID);
        }

        rrvProductList.setHasFixedSize(true);
        rrvProductList.setMode(RefreshRecyclerView.Mode.BOTH);
        rrvProductList.setLayoutManager(new GridLayoutManager(this, 2));
        rrvProductList.setOnRefreshAndLoadMoreListener(this);

        flProductBrand.setOnClickListener(this);
        flProductCategory.setOnClickListener(this);
        flProductPrice.setOnClickListener(this);
    }

    private void initData() {
        requestHttpData(Constants.Urls.URL_POST_CONDITION_LIST, REQUEST_NET_CONDITION_LIST, FProtocol.HttpMethod.POST, null);
        loadData(false);
    }

    private void loadData(boolean isMore) {
        int requestCode = REQUEST_NET_PRODUCT_LIST;
        int pageNum = 1;
        if (isMore && adapter != null) {
            requestCode = REQUEST_NET_PRODUCT_LIST_MORE;
            pageNum = adapter.getPage() + 1;
        }
        String requestUrl;
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        if (FROM_HOME_BOTTOM == from) {
            requestUrl = Constants.Urls.URL_POST_RECOMMEND_MORE;
            params.put("floor_id", recommendId);
        } else {
            requestUrl = Constants.Urls.URL_POST_PRODUCT_LIST;
            params.put("category_id", categoryId);
            params.put("brand_id", brandId);
            params.put("brand_name", "");
            params.put("condition", condition);
            params.put("min_price", minPrice);
            params.put("max_price", maxPrice);
            params.put("store_id", storeId);
        }
        params.put(Constants.PAGENUM, String.valueOf(pageNum));
        params.put(Constants.PAGESIZE, Constants.PAGE_SIZE_20);
        requestHttpData(requestUrl, requestCode, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void success(int requestCode, String data) {
        Entity result = Parsers.getResult(data);
        if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
            switch (requestCode) {
                case REQUEST_NET_CONDITION_LIST:
                    ConditionEntity condition = Parsers.getCondition(data);
                    brandEntities = condition.getBrandEntities();
                    if (brandEntities == null) {
                        brandEntities = new ArrayList<>();
                    }
                    if (brandEntities.size()>1) {
                        brandEntities.add(0, new BrandEntity("", getString(R.string.product_all_brand), ""));
                    }
                    tvProductBrand.setText(brandEntities.get(0).getName());
                    if (FROM_BRAND_DETAIL == from) {
                        for (int i = 0; i < brandEntities.size(); i++) {
                            if (brandId.equals(brandEntities.get(i).getId())) {
                                preBrand = i;
                                break;//结束循环
                            }
                        }
                    }
                    priceEntities = condition.getPriceEntities();
                    categoryEntities = condition.getCategoryEntities();
                    if (categoryEntities == null) {
                        categoryEntities = new ArrayList<>();
                    }
                    CategoryEntity categoryEntity = new CategoryEntity("", getString(R.string.product_all_category), "");
                    categoryEntity.setCategoryEntities(new ArrayList<>());
                    categoryEntities.add(0, categoryEntity);
                    if (FROM_HOME_CATEGORY == from) {
                        first:
                        for (int i = 0; i < categoryEntities.size(); i++) {
                            if (categoryId.equals(categoryEntities.get(i).getId())) {
                                //跳转过来的分类id是一级分类，直接设置默认选中
                                preCategory = i;
                                break;
                            } else {
                                //判断id是属于二级分类中的，设置其一级分类位置为默认选中
                                List<CategoryEntity> categoryEntityList = categoryEntities.get(i).getCategoryEntities();
                                for (int j = 0; j < categoryEntityList.size(); j++) {
                                    if (categoryId.equals(categoryEntityList.get(j).getId())) {
                                        preCategory = i;
                                        break first;
                                    } else {
                                        //判断id是属于三级分类的，设置其一级分类位置为默认选中
                                        List<CategoryEntity> thirdCategoryEntities = categoryEntityList.get(j).getCategoryEntities();
                                        for (int k = 0; k < thirdCategoryEntities.size(); k++) {
                                            if (categoryId.equals(thirdCategoryEntities.get(k).getId())) {
                                                preCategory = i;
                                                break first;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //初始化临时位置为默认位置，防止点开popWindow但不选择时被dismiss监听给重置了
                        tempCategory = preCategory;
                    }
                    break;
                case REQUEST_NET_PRODUCT_LIST: {
                    rrvProductList.resetStatus();
                    PagesEntity<ProductEntity> pageProduct = Parsers.getPageProduct(data);
                    List<ProductEntity> productEntities = pageProduct.getDatas();
                    if (productEntities != null && productEntities.size() > 0) {
                        setLoadingStatus(LoadingStatus.GONE);
                        adapter = new ProductAdapter(this, productEntities, this, 1);
                        rrvProductList.setAdapter(adapter);
                        if (pageProduct.getTotalPage() > 1) {
                            rrvProductList.setCanAddMore(true);
                        } else {
                            rrvProductList.setCanAddMore(false);
                        }
                    } else {
                        setLoadingStatus(LoadingStatus.EMPTY);
                    }
                    break;
                }
                case REQUEST_NET_PRODUCT_LIST_MORE: {
                    rrvProductList.resetStatus();
                    PagesEntity<ProductEntity> pageProduct = Parsers.getPageProduct(data);
                    List<ProductEntity> productEntities = pageProduct.getDatas();
                    if (productEntities != null && productEntities.size() > 0) {
                        adapter.addDatas(productEntities);
                        if (pageProduct.getTotalPage() <= adapter.getPage()) {
                            rrvProductList.setCanAddMore(false);
                        }
                    } else {
                        ToastUtil.shortShow(this, getString(R.string.no_data_text));
                    }
                    break;
                }
            }
        } else {
            if (REQUEST_NET_PRODUCT_LIST == requestCode || REQUEST_NET_PRODUCT_LIST_MORE == requestCode) {
                rrvProductList.resetStatus();
            }
            if (REQUEST_NET_PRODUCT_LIST == requestCode) {
                setLoadingStatus(LoadingStatus.EMPTY);
            }
            ToastUtil.shortShow(this, result.getResultMsg());
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        if (REQUEST_NET_PRODUCT_LIST == requestCode || REQUEST_NET_PRODUCT_LIST_MORE == requestCode) {
            rrvProductList.resetStatus();
        }
        if (REQUEST_NET_PRODUCT_LIST == requestCode) {
            setLoadingStatus(LoadingStatus.RETRY);
        }
        ToastUtil.shortShow(this, errorMessage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loading_layout:
                loadData(false);
                break;
            case R.id.fl_product_brand:
                if (brandEntities != null && brandEntities.size() > 0) {
                    List<String> brandList = new ArrayList<>();
                    for (BrandEntity brand : brandEntities) {
                        brandList.add(brand.getName());
                    }
                    showPopupSingle(brandList, true).showAsDropDown(flProductBrand);
                }
                break;
            case R.id.fl_product_category:
                popupWindow = showPopupCategory();
                popupWindow.showAsDropDown(flProductBrand);
                break;
            case R.id.fl_product_price:
                if (priceEntities != null && priceEntities.size() > 0) {
                    List<String> priceList = new ArrayList<>();
                    priceList.add(getString(R.string.product_all_price));
                    for (int i = 0; i < priceEntities.size(); i++) {
                        PriceEntity priceEntity = priceEntities.get(i);
                        String priceText = "￥" + priceEntity.getMin();
                        if (StringUtil.isEmpty(priceEntity.getMax())) {
                            priceText += getString(R.string.product_filter_above);
                        } else {
                            priceText += ("-" + priceEntity.getMax());
                        }
                        priceList.add(priceText);
                    }
                    showPopupSingle(priceList, false).showAsDropDown(flProductBrand);
                }
                break;
            case R.id.home_recommend_layout:
                ProductEntity productEntity = (ProductEntity) v.getTag();
                if (productEntity != null) {
                    ProductDetailActivity.startProductDetailActivity(this, productEntity.getId());
                }
                break;
            case R.id.tv_third_category:
                CategoryEntity categoryEntity = (CategoryEntity) v.getTag();
                if (categoryEntity != null) {
                    tvProductCategory.setText(categoryEntity.getName());
                    categoryId = categoryEntity.getId();
                    tempCategory = preCategory;//每次选择了子分类后将临时分类位置修改为一级分类的位置
                }
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                loadData(false);
                break;
        }
    }

    private PopupWindow showPopupSingle(List<String> datas, boolean isBrand) {
        if (isBrand) {
            tvProductBrand.setTextColor(ContextCompat.getColor(this, R.color.primary_color_red));
            tvProductBrand.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_red, 0);
        } else {
            tvProductPrice.setTextColor(ContextCompat.getColor(this, R.color.primary_color_red));
            tvProductPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_red, 0);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.popup_single_layout, null);
        ListView listView = (ListView) view.findViewById(R.id.lv_popup_single);
        listView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            View preChild;
            if (isBrand) {
                preChild = listView.getChildAt(preBrand);
            } else {
                preChild = listView.getChildAt(prePrice);
            }
            if (preChild != null) {
                preChild.setSelected(true);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.pop_item_brand, datas);

        PopupWindow pop = new PopupWindow(view, DeviceUtil.getWidth(this), popHeight);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, v, position, id) -> {
            if (isBrand) {
                if (position != preBrand) {
                    preBrand = position;
                    brandId = brandEntities.get(position).getId();
                    String name = getSimpleText(datas.get(position));
                    tvProductBrand.setText(name);
                    loadData(false);
                }
            } else {
                if (position != prePrice) {
                    prePrice = position;
                    minPrice = priceEntities.get(position).getMin();
                    maxPrice = priceEntities.get(position).getMax();
                    String name = getSimpleText(datas.get(position));
                    tvProductPrice.setText(name);
                    loadData(false);
                }
            }
            if (pop.isShowing()) {
                pop.dismiss();
            }
        });

        pop.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setTouchInterceptor((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    firstX = event.getX();
                    firstY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float x = event.getX();
                    float y = event.getY();
                    float dX = Math.abs(x - firstX);
                    float dY = Math.abs(y - firstY);
                    if (dX < 10 && dY < 10 && y > listView.getHeight()) {
                        if (pop.isShowing()) {
                            pop.dismiss();
                        }
                        firstX = -1;
                        firstY = -1;
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    firstX = -1;
                    firstY = -1;
                    break;
            }
            return false;
        });
        pop.setOnDismissListener(() -> {
            if (isBrand) {
                tvProductBrand.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
                tvProductBrand.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_black, 0);
            } else {
                tvProductPrice.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
                tvProductPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_black, 0);
            }
        });
        return pop;
    }

    @NonNull
    private String getSimpleText(String name) {
        if (name.length() > 4) {
            name = (name.substring(0, 4) + "...");
        }
        return name;
    }

    private PopupWindow showPopupCategory() {
        tvProductCategory.setTextColor(ContextCompat.getColor(this, R.color.primary_color_red));
        tvProductCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_red, 0);

        View view = LayoutInflater.from(this).inflate(R.layout.act_category, null);
        lvCategoryMain = (ListView) view.findViewById(R.id.lv_category_main);
        ViewGroup.LayoutParams layoutParams = lvCategoryMain.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        lvCategoryMain.setLayoutParams(layoutParams);
        lvCategoryDetail = (ListView) view.findViewById(R.id.lv_category_detail);
        ViewGroup.LayoutParams lp = lvCategoryDetail.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        lvCategoryDetail.setLayoutParams(lp);

        //默认选中第一个
        lvCategoryMain.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            View firstView = lvCategoryMain.getChildAt(preCategory);
            if (firstView != null) {
                firstView.setSelected(true);
            }
        });

        if (categoryEntities != null && categoryEntities.size() > 0) {
            List<String> mainCategories = new ArrayList<>();
            for (CategoryEntity categoryEntity : categoryEntities) {
                mainCategories.add(categoryEntity.getName());
            }
            ArrayAdapter<String> leftAdapter = new ArrayAdapter<>(this, R.layout.item_category, mainCategories);
            lvCategoryMain.setAdapter(leftAdapter);

            SecondCategoryAdapter secondCategoryAdapter = new SecondCategoryAdapter(this, categoryEntities.get(preCategory).getCategoryEntities(), this);
            lvCategoryDetail.setAdapter(secondCategoryAdapter);
        }

        lvCategoryMain.setOnItemClickListener(this);

        PopupWindow pop = new PopupWindow(view, DeviceUtil.getWidth(this), popHeight);
        pop.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setOnDismissListener(() -> {
            preCategory = tempCategory;//每次popWindow隐藏时将默认一级分类位置重置
            tvProductCategory.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
            tvProductCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_black, 0);
        });
        return pop;
    }

    @Override
    public void onRefresh() {
        loadData(false);
    }

    @Override
    public void onLoadMore() {
        loadData(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tempCategory = preCategory;//每次选择一级分类时，将上一次的一级分类位置记录到临时分类位置中去
        preCategory = position;
        if (categoryEntities != null && categoryEntities.size() > 0) {
            CategoryEntity categoryEntity = categoryEntities.get(position);
            if ("".equals(categoryEntity.getId()) && getString(R.string.product_all_category).equals(categoryEntity.getName())) {
                tempCategory = position;
                tvProductCategory.setText(categoryEntity.getName());
                categoryId = categoryEntity.getId();
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                loadData(false);
            } else {
                List<CategoryEntity> categoryEntityList = categoryEntity.getCategoryEntities();
                SecondCategoryAdapter secondCategoryAdapter = new SecondCategoryAdapter(this, categoryEntityList, this);
                lvCategoryDetail.setAdapter(secondCategoryAdapter);
            }
        }
    }
}
