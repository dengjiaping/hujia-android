package com.ihujia.hujia.otherstore.controller;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.RefreshRecyclerView;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.base.BaseFragment;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.OtherStoreEntity;
import com.ihujia.hujia.network.entities.PagesEntity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.otherstore.adapter.OtherStoreAdapter;
import com.ihujia.hujia.utils.CommonTools;

import java.util.IdentityHashMap;

/**
 * Created by liuzhichao on 2017/2/6.
 * 第三方店铺tab
 */
public class OtherStoreFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_NET_OTHER_STORE_LIST = 10;
    private static final int REQUEST_NET_OTHER_STORE_LIST_MORE = 20;

    private View view;
    private RefreshRecyclerView rrvOtherStoreList;
    private OtherStoreAdapter adapter;

    @Nullable
    @Override
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

        rrvOtherStoreList = (RefreshRecyclerView) view.findViewById(R.id.rrv_other_store_list);
        rrvOtherStoreList.setHasFixedSize(true);
        rrvOtherStoreList.setMode(RefreshRecyclerView.Mode.BOTH);
        rrvOtherStoreList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rrvOtherStoreList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int headerSize = rrvOtherStoreList.getHeaderSize();//头部数量
                int pos = parent.getChildLayoutPosition(view) - headerSize;//减去头部后的下标位置
                if (pos < 0) {//头部
                    return;
                }

                outRect.top = CommonTools.dp2px(getActivity(), 10);
            }
        });
        rrvOtherStoreList.setOnRefreshAndLoadMoreListener(new RefreshRecyclerView.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                loadData(false);
            }

            @Override
            public void onLoadMore() {
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
        rrvOtherStoreList.resetStatus();
        Entity result = Parsers.getResult(data);
        if (BaseActivity.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
            switch (requestCode) {
                case REQUEST_NET_OTHER_STORE_LIST:
                    PagesEntity<OtherStoreEntity> otherStoreEntityPagesEntity = Parsers.getOtherStoreList(data);
                    adapter = new OtherStoreAdapter(getActivity(), otherStoreEntityPagesEntity.getDatas(), this);
                    rrvOtherStoreList.setAdapter(adapter);
                    if (otherStoreEntityPagesEntity.getTotalPage() > adapter.getPage()) {
                        rrvOtherStoreList.setCanAddMore(true);
                    } else {
                        rrvOtherStoreList.setCanAddMore(false);
                    }
                    break;
                case REQUEST_NET_OTHER_STORE_LIST_MORE:
                    PagesEntity<OtherStoreEntity> pagesEntity = Parsers.getOtherStoreList(data);
                    adapter.addDatas(pagesEntity.getDatas());
                    adapter.notifyDataSetChanged();
                    if (pagesEntity.getTotalPage() <= adapter.getPage()) {
                        rrvOtherStoreList.setCanAddMore(false);
                    }
                    break;
            }
        } else {
            ToastUtil.shortShow(getActivity(), result.getResultMsg());
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        rrvOtherStoreList.resetStatus();
        ToastUtil.shortShow(getActivity(), errorMessage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_other_store_layout:
                OtherStoreEntity otherStoreEntity = (OtherStoreEntity) v.getTag();
                if (otherStoreEntity != null) {
                    String storeUrl = otherStoreEntity.getStoreUrl();
                    StoreRenovationActivity.startStoreRenovationActivity(getActivity(), otherStoreEntity.getId(), otherStoreEntity.getName());
                }
                break;
        }
    }
}
