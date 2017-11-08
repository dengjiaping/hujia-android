package com.ihujia.hujia.person.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.AnalysisUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.PullToRefreshBase;
import com.common.widget.PullToRefreshExpandableListView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.base.BaseFragment;
import com.ihujia.hujia.home.controller.ConfirmOrderActivity;
import com.ihujia.hujia.home.controller.ProductDetailActivity;
import com.ihujia.hujia.login.utils.UserCenter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.GoodsAttrEntity;
import com.ihujia.hujia.network.entities.PagesEntity;
import com.ihujia.hujia.network.entities.ShopCarFixEntity;
import com.ihujia.hujia.network.entities.ShopCarGoodEntity;
import com.ihujia.hujia.network.entities.ShopCarStoreEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.otherstore.controller.StoreRenovationActivity;
import com.ihujia.hujia.person.adapter.ShopcarFragmentAdapter;
import com.ihujia.hujia.person.shopcarInterface.CheckInterface;
import com.ihujia.hujia.person.shopcarInterface.ModifyCountInterface;
import com.ihujia.hujia.utils.NumberFormatUtil;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zhaoweiwei on 2017/2/10.
 * 购物车
 */
public class ShopCarFragment extends BaseFragment implements View.OnClickListener, CheckInterface, ModifyCountInterface {

    private static final int REQUEST_SHOPCAR_CODE = 0X01;
    private static final int REQUEST_SHOPCAR_CODE_MORE = 0x11;
    private static final int REQUEST_SHOPCAR_DELETE = 0X02;
    private static final int REQUEST_SHOPCAR_FIX = 0X03;
    private static final int REQUEST_SHOPCAR_FINISH = 0X04;
    private static final int RESOPNSE_FIX = 0X05;
    private static final int RESOPNSE_CONFIRM_ORDER = 0X06;

    @ViewInject(R.id.shopcar_title)
    private RelativeLayout shopcarTitle;
    @ViewInject(R.id.left_button)
    private ImageView backButton;
    @ViewInject(R.id.toolbar_title)
    private TextView title;
    @ViewInject(R.id.shopcar_list)
    private PullToRefreshExpandableListView loadingListView;
    @ViewInject(R.id.shopcar_select_all)
    private CheckBox selectAll;
    @ViewInject(R.id.shopcar_sum)
    private TextView shopSumPrice;
    @ViewInject(R.id.shopcar_to_pay)
    private TextView shopToPay;

    private List<ShopCarStoreEntity> storeEntities;
    private double totalPrice;
    private int totalCount;
    private ShopcarFragmentAdapter adapter;
    private int deleteGroupPosition, deleteChildPosition;
    private ShopCarGoodEntity shopCarGoodFixEntity;
    private StringBuilder cartList;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_frag_shopcar, null);
        ViewInjectUtils.inject(this, view);
        initLoadingView(this, view);
        setLoadingStatus(LoadingStatus.LOADING);
        initView();
        if (UserCenter.isLogin(getActivity())) {
            loadData(false);
        }
        return view;
    }

    private void initView() {
        shopcarTitle.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        title.setText(getString(R.string.person_shopcar_name));
        loadingListView.getRefreshableView().setGroupIndicator(null);
        selectAll.setOnClickListener(this);
        shopSumPrice.setOnClickListener(this);
        shopToPay.setOnClickListener(this);
        setSelectResult();
        loadingListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                loadData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                loadData(true);
            }
        });
    }

    private void loadData(boolean isMore) {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("user_id", UserCenter.getUserId(getActivity()));
        int request = REQUEST_SHOPCAR_CODE;
        if (isMore) {
            page++;
            request = REQUEST_SHOPCAR_CODE_MORE;
        }
        params.put(Constants.PAGENUM, String.valueOf(page));
        params.put(Constants.PAGESIZE, Constants.PAGE_SIZE_10);
        requestHttpData(Constants.Urls.URL_POST_MY_SHOPCAR, request, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void onResume() {
        super.onResume();
        calculate();
    }

    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        cartList = new StringBuilder();
        if (storeEntities != null && storeEntities.size() > 0) {
            for (int i = 0; i < storeEntities.size(); i++) {
                ShopCarStoreEntity group = storeEntities.get(i);
                List<ShopCarGoodEntity> childs = group.getEntities();
                for (int j = 0; j < childs.size(); j++) {
                    ShopCarGoodEntity product = childs.get(j);
                    if (product.isChecked()) {
                        totalCount++;
                        totalPrice += (Double.parseDouble(product.getGoodsPrice()) * Double.parseDouble(product.getCount()));
                        cartList.append(product.getShopcarId());
                        cartList.append(",");
                    }
                }
            }
        } else {
            totalCount = 0;
            totalPrice = 0.0d;
        }
        setSelectResult();
    }

    @Override
    public void success(int requestCode, String data) {
        super.success(requestCode, data);
        loadingListView.onRefreshComplete();
        closeProgressDialog();
        setLoadingStatus(LoadingStatus.GONE);
        switch (requestCode) {
            case REQUEST_SHOPCAR_CODE:
                PagesEntity<ShopCarStoreEntity> pagesEntity = Parsers.getShopCarStore(data);
                if (pagesEntity != null) {
                    if (BaseActivity.REQUEST_NET_SUCCESS.equals(pagesEntity.getResultCode())) {
                        storeEntities = pagesEntity.getDatas();
                        if (storeEntities != null && storeEntities.size() > 0) {
                            adapter = new ShopcarFragmentAdapter(storeEntities, getActivity(), this);
                            loadingListView.getRefreshableView().setAdapter(adapter);
                            for (int i = 0; i < adapter.getGroupCount(); i++) {
                                loadingListView.getRefreshableView().expandGroup(i);
                            }
                            adapter.setCheckInterface(this);// 关键步骤1,设置复选框接口
                            adapter.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
                            if (adapter.getPage() < pagesEntity.getTotalPage()) {
                                loadingListView.setCanAddMore(true);
                            } else {
                                loadingListView.setCanAddMore(false);
                            }
                            doCheckAll();
                        } else {
                            setLoadingStatus(LoadingStatus.EMPTY);
                            mImgLoadingEmpty.setImageResource(R.drawable.loading_shopcar_empty);
                            mTxtLoadingEmpty.setText(getString(R.string.empty_loading_shopcar));
                            selectAll.setChecked(false);
                            calculate();
                        }
                    } else {
                        ToastUtil.shortShow(getActivity(), pagesEntity.getResultMsg());
                    }
                }
                break;
            case REQUEST_SHOPCAR_CODE_MORE:
                PagesEntity<ShopCarStoreEntity> pagesEntity1 = Parsers.getShopCarStore(data);
                if (pagesEntity1 != null) {
                    if (BaseActivity.REQUEST_NET_SUCCESS.equals(pagesEntity1.getResultCode())) {
                        List<ShopCarStoreEntity> storeEntities1 = pagesEntity1.getDatas();
                        if (storeEntities1 != null && storeEntities1.size() > 0) {
                            adapter.addDatas(storeEntities1);
                            adapter.notifyDataSetChanged();
                            for (int i = 0; i < adapter.getGroupCount(); i++) {
                                loadingListView.getRefreshableView().expandGroup(i);
                            }
                            adapter.setCheckInterface(this);// 关键步骤1,设置复选框接口
                            adapter.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
                        } else {
                            setLoadingStatus(LoadingStatus.EMPTY);
                        }
                        if (adapter.getPage() < pagesEntity1.getTotalPage()) {
                            loadingListView.setCanAddMore(true);
                        } else {
                            loadingListView.setCanAddMore(false);
                        }
                    } else {
                        ToastUtil.shortShow(getActivity(), pagesEntity1.getResultMsg());
                    }
                }
                break;
            case REQUEST_SHOPCAR_DELETE:
                Entity entity = Parsers.getResult(data);
                if (BaseActivity.REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
                    storeEntities.get(deleteGroupPosition).getEntities().remove(deleteChildPosition);
                    List<ShopCarGoodEntity> childs = storeEntities.get(deleteGroupPosition).getEntities();
                    if (childs.size() == 0) {
                        storeEntities.remove(deleteGroupPosition);
                    }
                    if (storeEntities.size() == 0) {
                        setLoadingStatus(LoadingStatus.EMPTY);
                        mImgLoadingEmpty.setImageResource(R.drawable.loading_shopcar_empty);
                        mTxtLoadingEmpty.setText(getString(R.string.empty_loading_shopcar));
                        selectAll.setChecked(false);
                        calculate();
                    }
                    adapter.notifyDataSetChanged();
                    calculate();
                } else {
                    ToastUtil.shortShow(getActivity(), entity.getResultMsg());
                }
                break;
            case REQUEST_SHOPCAR_FIX:
                ShopCarFixEntity shopCarFixEntity = Parsers.getShopCarFix(data);
                if (BaseActivity.REQUEST_NET_SUCCESS.equals(shopCarFixEntity.getResultCode())) {
                    Intent fixIntent = new Intent(getActivity(), FixStyleActivity.class);
                    fixIntent.putExtra(FixStyleActivity.EXTRA_ENTITY, shopCarFixEntity);
                    fixIntent.putExtra(FixStyleActivity.EXTRA_FROM, FixStyleActivity.FROM_SHOPCAR);
                    startActivityForResult(fixIntent, RESOPNSE_FIX);
                } else {
                    ToastUtil.shortShow(getActivity(), shopCarFixEntity.getResultMsg());
                }
                break;
            case REQUEST_SHOPCAR_FINISH:
                Entity finishEntity = Parsers.getResult(data);
                if (BaseActivity.REQUEST_NET_SUCCESS.equals(finishEntity.getResultCode())) {
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.shortShow(getActivity(), finishEntity.getResultMsg());
                }
                break;
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        super.mistake(requestCode, status, errorMessage);
        loadingListView.onRefreshComplete();
        closeProgressDialog();
        if (REQUEST_SHOPCAR_CODE == requestCode || REQUEST_SHOPCAR_CODE_MORE == requestCode) {
            setLoadingStatus(LoadingStatus.RETRY);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopcar_store_layout:
                String storeId = (String) v.getTag();
                StoreRenovationActivity.startStoreRenovationActivity(getActivity(), storeId, "店铺详情");
                break;
            case R.id.shopcar_item_edit_color:
            case R.id.shopcar_item_edit_size:
            case R.id.shopcar_item_edit_fix:
                shopCarGoodFixEntity = (ShopCarGoodEntity) v.getTag();
                showProgressDialog();
                IdentityHashMap<String, String> params = new IdentityHashMap<>();
                params.put("goods_id", shopCarGoodFixEntity.getGoodsId());
                params.put("sku_id", shopCarGoodFixEntity.getSkuId());
                requestHttpData(Constants.Urls.URL_POST_PRODUCT_FIX_SHOPCAR, REQUEST_SHOPCAR_FIX, FProtocol.HttpMethod.POST, params);
                break;
            case R.id.shopcar_store_checked:
                int groupPosition = (int) v.getTag();
                ShopCarStoreEntity checkStoreEntity = storeEntities.get(groupPosition);
                checkStoreEntity.setChecked(!checkStoreEntity.isChecked());
                for (ShopCarGoodEntity childEntity : checkStoreEntity.getEntities()) {
                    childEntity.setChecked(checkStoreEntity.isChecked());
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.shopcar_item_normal:
                String goodsId = (String) v.getTag();
                ProductDetailActivity.startProductDetailActivity(getActivity(), goodsId);
                break;
            case R.id.shopcar_select_all:
                if (storeEntities != null && storeEntities.size() > 0) {
                    doCheckAll();
                } else {
                    selectAll.setChecked(false);
                    ToastUtil.shortShow(getActivity(), getString(R.string.empty_loading_shopcar));
                }
                break;
            case R.id.shopcar_to_pay:
                AnalysisUtil.onEvent(getActivity(), "guc_jiesuan");
                if (totalCount > 0) {
                    Intent payIntent = new Intent(getActivity(), ConfirmOrderActivity.class);
                    payIntent.putExtra("cartList", cartList.toString());
                    startActivityForResult(payIntent, RESOPNSE_CONFIRM_ORDER);
                } else {
                    ToastUtil.shortShow(getActivity(), getString(R.string.person_shopcar_least_choose));
                }
                break;
            case R.id.loading_layout:
                loadData(false);
                setLoadingStatus(LoadingStatus.GONE);
                showProgressDialog();
                break;
        }
    }

    private void setSelectResult() {
        Resources res = getActivity().getResources();
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(getString(R.string.confirm_order_allprice, NumberFormatUtil.formatMoney(totalPrice)));
        stringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.primary_color)), 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        shopSumPrice.setText(stringBuilder);
        shopToPay.setText(res.getString(R.string.person_shopcar_topay, String.valueOf(totalCount)));
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        ShopCarStoreEntity storeEntity = storeEntities.get(groupPosition);
        List<ShopCarGoodEntity> childs = storeEntity.getEntities();
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setChecked(isChecked);
        }
        if (isAllCheck()) {
            selectAll.setChecked(true);// 全选
        } else {
            selectAll.setChecked(false);// 反选
        }
        adapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        ShopCarStoreEntity storeEntity = storeEntities.get(groupPosition);
        List<ShopCarGoodEntity> childs = storeEntity.getEntities();
        for (int i = 0; i < childs.size(); i++) {
            if (childs.get(i).isChecked() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        if (allChildSameState) {
            storeEntity.setChecked(isChecked);
        } else {
            storeEntity.setChecked(false);
        }

        if (isAllCheck()) {
            selectAll.setChecked(true);// 全选
        } else {
            selectAll.setChecked(false);// 反选
        }
        adapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void childDelete(int groupPosition, int childPosition) {
        this.deleteChildPosition = childPosition;
        this.deleteGroupPosition = groupPosition;
        String shopcarId = storeEntities.get(groupPosition).getEntities().get(childPosition).getShopcarId();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("user_id", UserCenter.getUserId(getActivity()));
        params.put("cart_id", shopcarId);
        requestHttpData(Constants.Urls.URL_POST_PRODUCT_DELETE_SHOPCAR, REQUEST_SHOPCAR_DELETE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void fixFinish(int groupPosition, int childPosition) {
        ShopCarGoodEntity finishEntity = storeEntities.get(groupPosition).getEntities().get(childPosition);
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("user_id", UserCenter.getUserId(getActivity()));
        params.put("cart_id", finishEntity.getShopcarId());
        params.put("goods_id", finishEntity.getGoodsId());
        params.put("goods_count", finishEntity.getCount());
        params.put("sku_id", finishEntity.getSkuId());
        requestHttpData(Constants.Urls.URL_POST_PRODUCT_FINISH_SHOPCAR, REQUEST_SHOPCAR_FINISH, FProtocol.HttpMethod.POST, params);
    }

    private boolean isAllCheck() {
        for (ShopCarStoreEntity group : storeEntities) {
            if (!group.isChecked())
                return false;
        }
        return true;
    }

    /**
     * 全选与反选
     */
    private void doCheckAll() {
        for (int i = 0; i < storeEntities.size(); i++) {
            storeEntities.get(i).setChecked(selectAll.isChecked());
            ShopCarStoreEntity group = storeEntities.get(i);
            List<ShopCarGoodEntity> childs = group.getEntities();
            for (int j = 0; j < childs.size(); j++) {
                childs.get(j).setChecked(selectAll.isChecked());
            }
        }
        adapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void onStop() {
        super.onStop();
        selectAll.setChecked(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case RESOPNSE_FIX:
                    boolean isFix = data.getBooleanExtra("isFix", false);
                    String count = data.getStringExtra("productNum");
                    String skuId = data.getStringExtra("skuId");
                    String color = data.getStringExtra("color");
                    String size = data.getStringExtra("size");
                    if (isFix) {
                        shopCarGoodFixEntity.setSkuId(skuId);
                        shopCarGoodFixEntity.setCount(count);
                        List<GoodsAttrEntity> attrEntities = shopCarGoodFixEntity.getAttrList();
                        for (GoodsAttrEntity entity : attrEntities) {
                            if (getString(R.string.person_order_size_name).equals(entity.getAttrName())) {
                                entity.setAttrValue(size);
                            } else if (getString(R.string.person_order_color_name).equals(entity.getAttrName())) {
                                entity.setAttrValue(color);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case RESOPNSE_CONFIRM_ORDER:
                    storeEntities = null;
                    loadData(false);
                    selectAll.setChecked(false);
                    totalCount = 0;
                    totalPrice = 0.0d;
                    setSelectResult();
                    break;
            }
        }
    }
}
