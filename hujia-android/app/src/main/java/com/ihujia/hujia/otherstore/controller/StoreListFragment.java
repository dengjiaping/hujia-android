package com.ihujia.hujia.otherstore.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.base.BaseFragment;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.OtherStoreEntity;
import com.ihujia.hujia.network.entities.PagesEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.otherstore.adapter.StoreListAdapter;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by zww on 2017/6/27.
 * 店铺tab
 */

public class StoreListFragment extends BaseFragment implements View.OnClickListener {
    private static final int REQUEST_NET_OTHER_STORE_LIST = 10;
    private static final int REQUEST_NET_OTHER_STORE_LIST_MORE = 20;

    private View view;
    private FootLoadingListView rrvOtherStoreList;
    private StoreListAdapter adapter;
    private List<OtherStoreEntity> storeEntities;


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frag_other_store, null);
            initView();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initView() {
        view.findViewById(R.id.left_button).setVisibility(View.GONE);
        TextView toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.main_tab_store));
        toolbarTitle.setVisibility(View.VISIBLE);
        rrvOtherStoreList = (FootLoadingListView) view.findViewById(R.id.rrv_other_store_list);
        rrvOtherStoreList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData(false);
    }

    private void loadData(boolean isMore) {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        int requestCode = REQUEST_NET_OTHER_STORE_LIST;
        int page = 1;
        if (isMore) {
            requestCode = REQUEST_NET_OTHER_STORE_LIST_MORE;
            page = adapter.getPage() + 1;
        }
        params.put(Constants.PAGENUM, String.valueOf(page));
        params.put(Constants.PAGESIZE, Constants.PAGE_SIZE_10);
        requestHttpData(Constants.Urls.URL_POST_OTHER_STORE_LIST, requestCode, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void success(int requestCode, String data) {
        rrvOtherStoreList.setOnRefreshComplete();
        Entity result = Parsers.getResult(data);
        if (BaseActivity.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
            PagesEntity<OtherStoreEntity> pageEntity = Parsers.getOtherStoreList(data);
            if (pageEntity != null) {
                storeEntities = pageEntity.getDatas();
                if (pageEntity.getDatas() != null && pageEntity.getDatas().size() > 0) {
                    switch (requestCode) {
                        case REQUEST_NET_OTHER_STORE_LIST:
                            adapter = new StoreListAdapter(getActivity(), storeEntities,this);
                            rrvOtherStoreList.setAdapter(adapter);
                            if (pageEntity.getTotalPage() > adapter.getPage()) {
                                rrvOtherStoreList.setCanAddMore(true);
                            } else {
                                rrvOtherStoreList.setCanAddMore(false);
                            }
                            break;
                        case REQUEST_NET_OTHER_STORE_LIST_MORE:
                            adapter.addDatas(storeEntities);
                            adapter.notifyDataSetChanged();
                            if (pageEntity.getTotalPage() <= adapter.getPage()) {
                                rrvOtherStoreList.setCanAddMore(false);
                            }
                            break;
                    }
                }
            }
        } else {
            ToastUtil.shortShow(getActivity(), result.getResultMsg());
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        rrvOtherStoreList.setOnRefreshComplete();
        ToastUtil.shortShow(getActivity(), errorMessage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_other_store_layout:
                OtherStoreEntity otherStoreEntity = (OtherStoreEntity) v.getTag(R.id.tag_store_item);
                if (otherStoreEntity != null) {
                    StoreRenovationActivity.startStoreRenovationActivity(
                            getActivity(),
                            otherStoreEntity.getId(),
                            otherStoreEntity.getName());
                }
                break;
        }
    }

}
