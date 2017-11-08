package com.ihujia.hujia.home.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.AnalysisUtil;
import com.common.utils.ToastUtil;
import com.common.view.GridViewForInner;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ihujia.hujia.BuildConfig;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseFragment;
import com.ihujia.hujia.base.WebViewActivity;
import com.ihujia.hujia.brand.controller.BrandActivity;
import com.ihujia.hujia.brand.controller.BrandDetailActivity;
import com.ihujia.hujia.home.adapter.GalleryAdapter;
import com.ihujia.hujia.home.adapter.HomeRecommendAdapter;
import com.ihujia.hujia.home.adapter.HomeTypeAdapter;
import com.ihujia.hujia.home.utils.BannerImageLoader;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.ADEntity;
import com.ihujia.hujia.network.entities.ActivityEntity;
import com.ihujia.hujia.network.entities.BrandEntity;
import com.ihujia.hujia.network.entities.CategoryEntity;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.HomeRecommendEntity;
import com.ihujia.hujia.network.entities.HomeTopEntity;
import com.ihujia.hujia.network.entities.ProductEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.otherstore.controller.StoreRenovationActivity;
import com.ihujia.hujia.store.controller.ProductActivity;
import com.ihujia.hujia.utils.ImageUtils;
import com.ihujia.hujia.utils.LayoutUtil;
import com.ihujia.hujia.utils.PermissionUtils;
import com.ihujia.hujia.utils.ViewInjectUtils;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import static com.ihujia.hujia.base.BaseActivity.REQUEST_NET_SUCCESS;
import static com.ihujia.hujia.base.BaseActivity.REQUEST_PERMISSION_CODE;

/**
 * Created by liuzhichao on 2016/12/16.
 * 首页
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_NET_HOME_TOP = 10;
    private static final int REQUEST_NET_HOME_BOTTOM = 20;

    @ViewInject(R.id.home_recommend_list)
    private FootLoadingListView listView;
    @ViewInject(R.id.home_scan)
    private ImageView homeScan;
    @ViewInject(R.id.home_search)
    private RelativeLayout homeSearch;
    @ViewInject(R.id.home_search_img)
    private ImageView homeSearchImg;
    @ViewInject(R.id.home_search_text)
    private TextView homeSearchText;
    @ViewInject(R.id.tv_home_retry)
    private View tvHomeRetry;
    @ViewInject(R.id.home_layout)
    private View homeLayout;
    private View view;
    private GridViewForInner homeType;
    private RecyclerView homeBrand;
    private Banner banner;

    private List<ActivityEntity> activityEntities;
    private List<CategoryEntity> categoryEntities;
    private List<BrandEntity> brandEntities;
    private boolean topSuccess;
    private boolean bottomSuccess;
    private FrameLayout homeAd;
    private ADEntity adEntity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.home_frag, null);
            ViewInjectUtils.inject(this, view);
            initTopView(inflater);
        }
        ViewGroup mViewParent = (ViewGroup) view.getParent();
        if (mViewParent != null) {
            mViewParent.removeView(view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    /*初始化listview头部*/
    private void initTopView(LayoutInflater inflater) {
        View topView = inflater.inflate(R.layout.home_top_layout, null);
        banner = (Banner) topView.findViewById(R.id.home_banner);
        homeType = (GridViewForInner) topView.findViewById(R.id.home_grid);
        homeBrand = (RecyclerView) topView.findViewById(R.id.home_brand_list);
        homeAd = (FrameLayout) topView.findViewById(R.id.home_ad);
        View homeBrandMore = topView.findViewById(R.id.home_brand_more);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.getRefreshableView().addHeaderView(topView);
        listView.setOnRefreshListener(refreshView -> loadData());
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstChild = view.getChildAt(0);
                if (firstChild != null && firstVisibleItem <= 1) {
                    int dAlpha;
                    int distance = -firstChild.getTop();
                    float total = 200;
                    if (distance > 20) {
                        //设置白色并开始渐变
                        homeLayout.setBackgroundColor(Color.WHITE);
                        homeSearch.setBackgroundResource(R.drawable.home_search_black_bg);
                        homeScan.setImageResource(R.drawable.home_scan_black);
                        homeSearchImg.setImageResource(R.drawable.home_search_black_icon);
                        homeSearchText.setTextColor(getResources().getColor(R.color.person_check_order_textcolor));

                        float result = (distance - 20) / total;
                        dAlpha = 25 + (int) (230 * result);
                        dAlpha = dAlpha > 250 ? 250 : dAlpha;
                        homeLayout.getBackground().mutate().setAlpha(dAlpha);
                    } else {
                        homeLayout.setBackgroundResource(R.drawable.home_title_bg);
                        homeSearch.setBackgroundResource(R.drawable.home_search_white_bg);
                        homeScan.setImageResource(R.drawable.home_scan_white);
                        homeSearchImg.setImageResource(R.drawable.home_search_white_icon);
                        homeSearchText.setTextColor(Color.WHITE);
                    }
                }
            }
        });

        homeScan.setOnClickListener(this);
        homeSearch.setOnClickListener(this);
        tvHomeRetry.setOnClickListener(this);
        homeBrandMore.setOnClickListener(this);
    }

    private void loadData() {
        requestHttpData(Constants.Urls.URL_POST_HOME_TOP, REQUEST_NET_HOME_TOP, FProtocol.HttpMethod.POST, null);
        requestHttpData(Constants.Urls.URL_POST_HOME_BOTTOM, REQUEST_NET_HOME_BOTTOM, FProtocol.HttpMethod.POST, null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //扫描
            case R.id.home_scan:
                AnalysisUtil.onEvent(getActivity(), "erweima");
                if (PermissionUtils.isGetPermission(getActivity(), Manifest.permission.CAMERA)) {
                    prepareScan();
                } else {
                    PermissionUtils.secondRequest(this, REQUEST_PERMISSION_CODE, Manifest.permission.CAMERA);
                }
                break;
            //搜索
            case R.id.home_search:
                AnalysisUtil.onEvent(getActivity(), "search");
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.home_brand_more:
                AnalysisUtil.onEvent(getActivity(), "sy_gd1");
                BrandActivity.startBrandActivity(getActivity());
                break;
            case R.id.home_commend_more:
                AnalysisUtil.onEvent(getActivity(), "sy_gd" + view.getTag(R.id.home_recommend));
                HomeRecommendEntity homeRecommendEntity = (HomeRecommendEntity) view.getTag();
                if (homeRecommendEntity != null) {
                    ProductActivity.startProductActivity(getActivity(), "", ProductActivity.FROM_HOME_BOTTOM, homeRecommendEntity.getId());
                }
                break;
            case R.id.recommend_cloth1:
            case R.id.recommend_cloth2:
            case R.id.recommend_cloth3:
                AnalysisUtil.onEvent(getActivity(), "sy_tj" + view.getTag(R.id.home_recommend));
                ProductEntity productEntity = (ProductEntity) view.getTag();
                ProductDetailActivity.startProductDetailActivity(getActivity(), productEntity.getId());
                break;
            case R.id.sdv_home_ad1:
                AnalysisUtil.onEvent(getActivity(), "sy_ad1");
                activityJump((ActivityEntity) view.getTag());
                break;
            case R.id.sdv_home_ad2:
                AnalysisUtil.onEvent(getActivity(), "sy_ad2");
                activityJump((ActivityEntity) view.getTag());
                break;
            case R.id.sdv_home_ad3:
                AnalysisUtil.onEvent(getActivity(), "sy_ad3");
                activityJump((ActivityEntity) view.getTag());
                break;
            case R.id.sdv_home_ad4:
                AnalysisUtil.onEvent(getActivity(), "sy_ad4");
                activityJump((ActivityEntity) view.getTag());
                break;
            case R.id.sdv_home_ad5:
                AnalysisUtil.onEvent(getActivity(), "sy_ad5");
                activityJump((ActivityEntity) view.getTag());
                break;
            case R.id.tv_home_retry:
                loadData();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_PERMISSION_CODE == requestCode) {
            if (grantResults.length > 0 && Manifest.permission.CAMERA.equals(permissions[0]) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                prepareScan();
            } else {
                ToastUtil.shortShow(getActivity(), getString(R.string.get_permission_failed));
            }
        }
    }

    private void prepareScan() {
        startActivity(new Intent(getActivity(), ScanActivity.class));
    }

    @Override
    public void success(int requestCode, String data) {
        Entity result = Parsers.getResult(data);
        if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
            switch (requestCode) {
                case REQUEST_NET_HOME_TOP:
                    topSuccess = true;
                    HomeTopEntity homeTop = Parsers.getHomeTop(data);
                    if (homeTop != null) {
                        activityEntities = homeTop.getActivityEntities();
                        categoryEntities = homeTop.getCategoryEntities();
                        adEntity = homeTop.getAdEntity();
                        brandEntities = homeTop.getBrandEntities();
                        handCarousel();
                        handCategory();
                        handAD();
                        handBrand();
                    }
                    break;
                case REQUEST_NET_HOME_BOTTOM:
                    bottomSuccess = true;
                    List<HomeRecommendEntity> homeRecommendList = Parsers.getHomeRecommendList(data);
                    if (homeRecommendList != null) {
                        HomeRecommendAdapter adapter = new HomeRecommendAdapter(getActivity(), homeRecommendList, this);
                        listView.setAdapter(adapter);
                    }
                    break;
            }
        } else {
            if (REQUEST_NET_HOME_TOP == requestCode) {
                topSuccess = false;
            }
            if (REQUEST_NET_HOME_BOTTOM == requestCode) {
                bottomSuccess = false;
            }
            ToastUtil.shortShow(getActivity(), result.getResultMsg());
        }
        if (topSuccess && bottomSuccess) {
            listView.setOnRefreshComplete();
        }
        if (topSuccess || bottomSuccess) {
            listView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.GONE);
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        if (REQUEST_NET_HOME_TOP == requestCode) {
            topSuccess = false;
        }
        if (REQUEST_NET_HOME_BOTTOM == requestCode) {
            bottomSuccess = false;
        }
        if (!topSuccess && !bottomSuccess) {
            listView.setOnRefreshComplete();
            listView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
        }
        ToastUtil.shortShow(getActivity(), errorMessage);
    }

    //处理轮播图
    private void handCarousel() {
        LayoutUtil.setHeightAndWidth(getActivity(), banner, 0.6f);
        if (activityEntities != null && activityEntities.size() > 0) {
            List<String> imageUrls = new ArrayList<>();
            for (ActivityEntity activityEntity : activityEntities) {
                imageUrls.add(activityEntity.getPicUrl());
            }
            banner.setImageLoader(new BannerImageLoader());
            banner.setImages(imageUrls);
            banner.start();
            banner.setOnBannerListener(position -> {
                ActivityEntity activityEntity = activityEntities.get(position);
                AnalysisUtil.onEvent(getActivity(), "banner" + (position + 1));
                activityJump(activityEntity);
            });
        }
    }

    //处理分类
    private void handCategory() {
        if (categoryEntities != null && categoryEntities.size() > 0) {
            if (categoryEntities.size() > 7) {
                categoryEntities = categoryEntities.subList(0, 7);
                categoryEntities.add(new CategoryEntity("", getString(R.string.home_brand_more), "res://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.category_more_icon));
            }
            HomeTypeAdapter adapter = new HomeTypeAdapter(getActivity(), categoryEntities);
            homeType.setAdapter(adapter);
            homeType.setOnItemClickListener((parent, view1, position, id) -> {
                if (categoryEntities.size() > 7 && position == categoryEntities.size() - 1) {
                    AnalysisUtil.onEvent(getActivity(), "sy_fl8");
                    CategoryActivity.startCategoryActivity(getActivity());
                } else {
                    AnalysisUtil.onEvent(getActivity(), "sy_fl" + (position + 1));
                    CategoryEntity categoryEntity = categoryEntities.get(position);
                    ProductActivity.startProductActivity(getActivity(), ProductActivity.FROM_HOME_CATEGORY, categoryEntity.getName(), categoryEntity.getId());
                }
            });
        }
    }

    //处理广告
    private void handAD() {
        if (adEntity != null && adEntity.getActivityEntities() != null && adEntity.getActivityEntities().size() > 0) {
            homeAd.removeAllViews();
            List<ActivityEntity> activityEntities = adEntity.getActivityEntities();
            View adView = null;
            switch (adEntity.getType()) {
                case "app_special_ad1":
                    adView = LayoutInflater.from(getActivity()).inflate(R.layout.home_ad_layout1, homeAd);
                    break;
                case "app_special_ad2":
                    adView = LayoutInflater.from(getActivity()).inflate(R.layout.home_ad_layout2, homeAd);
                    break;
                case "app_special_ad3":
                    adView = LayoutInflater.from(getActivity()).inflate(R.layout.home_ad_layout3, homeAd);
                    break;
                case "app_special_ad4":
                    adView = LayoutInflater.from(getActivity()).inflate(R.layout.home_ad_layout4, homeAd);
                    break;
                case "app_special_ad5":
                    adView = LayoutInflater.from(getActivity()).inflate(R.layout.home_ad_layout5, homeAd);
                    break;
            }
            if (adView != null) {
                for (ActivityEntity activity : activityEntities) {
                    switch (activity.getAdNo()) {
                        case "1":
                            SimpleDraweeView sdvHomeAd1 = (SimpleDraweeView) adView.findViewById(R.id.sdv_home_ad1);
                            ImageUtils.setSmallImg(sdvHomeAd1, activity.getPicUrl());
                            sdvHomeAd1.setTag(activity);
                            sdvHomeAd1.setOnClickListener(this);
                            break;
                        case "2":
                            SimpleDraweeView sdvHomeAd2 = (SimpleDraweeView) adView.findViewById(R.id.sdv_home_ad2);
                            ImageUtils.setSmallImg(sdvHomeAd2, activity.getPicUrl());
                            sdvHomeAd2.setTag(activity);
                            sdvHomeAd2.setOnClickListener(this);
                            break;
                        case "3":
                            SimpleDraweeView sdvHomeAd3 = (SimpleDraweeView) adView.findViewById(R.id.sdv_home_ad3);
                            ImageUtils.setSmallImg(sdvHomeAd3, activity.getPicUrl());
                            sdvHomeAd3.setTag(activity);
                            sdvHomeAd3.setOnClickListener(this);
                            break;
                        case "4":
                            SimpleDraweeView sdvHomeAd4 = (SimpleDraweeView) adView.findViewById(R.id.sdv_home_ad4);
                            ImageUtils.setSmallImg(sdvHomeAd4, activity.getPicUrl());
                            sdvHomeAd4.setTag(activity);
                            sdvHomeAd4.setOnClickListener(this);
                            break;
                        case "5":
                            SimpleDraweeView sdvHomeAd5 = (SimpleDraweeView) adView.findViewById(R.id.sdv_home_ad5);
                            ImageUtils.setSmallImg(sdvHomeAd5, activity.getPicUrl());
                            sdvHomeAd5.setTag(activity);
                            sdvHomeAd5.setOnClickListener(this);
                            break;
                    }
                }
            }
        }
    }

    //处理品牌
    private void handBrand() {
        if (brandEntities != null && brandEntities.size() > 0) {
            List<String> urls = new ArrayList<>();
            for (BrandEntity brandEntity : brandEntities) {
                urls.add(brandEntity.getLogo());
            }
            //设置布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            homeBrand.setLayoutManager(linearLayoutManager);
            //设置适配器
            GalleryAdapter adapter = new GalleryAdapter(getActivity(), urls, 1);
            homeBrand.setAdapter(adapter);
            adapter.setOnItemClickListener((view1, position) -> {
                AnalysisUtil.onEvent(getActivity(), "sy_pp" + (position + 1));
                BrandDetailActivity.startBrandDetailActivity(getActivity(), brandEntities.get(position).getId());
            });
        }
    }

    //banner和ad的活动跳转
    private void activityJump(ActivityEntity activityEntity) {
        if (activityEntity == null) {
            return;
        }
        switch (activityEntity.getType()) {
            case 1:
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_URL, activityEntity.getLinkUrl());
                intent.putExtra(WebViewActivity.EXTRA_TITLE, getString(R.string.home_activity_detail));
                getActivity().startActivity(intent);
                break;
            case 2://店铺
                StoreRenovationActivity.startStoreRenovationActivity(getActivity(), activityEntity.getLinkUrl(), getString(R.string.home_store_detail));
                break;
            case 3://商品
                ProductDetailActivity.startProductDetailActivity(getActivity(), activityEntity.getLinkUrl());
                break;
        }
    }
}
