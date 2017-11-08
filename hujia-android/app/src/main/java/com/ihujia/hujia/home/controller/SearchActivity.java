package com.ihujia.hujia.home.controller;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.utils.AnalysisUtil;
import com.common.utils.PreferencesUtils;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.home.adapter.HistorySearchAdapter;
import com.ihujia.hujia.network.Constants;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.parser.Parsers;
import com.ihujia.hujia.person.flowlayout.FlowTagLayout;
import com.ihujia.hujia.person.flowlayout.HotSearchAdapter;
import com.ihujia.hujia.store.controller.ProductActivity;
import com.ihujia.hujia.utils.ViewInjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhaoweiwei on 2017/1/6.
 * 搜索
 */
public class SearchActivity extends BaseActivity {

	private final static int REQUEST_NET_HOT_SEARCH = 20;
	private final static int SEARCH_HISTORY_COUNT = 10;
	private final static String SEARCH_PRE_KEY_HISTORY = "search_pre_key_history";

	@ViewInject(R.id.home_search_back)
	private View homeSearchBack;
	@ViewInject(R.id.home_search_edit)
	private EditText searchEdit;
	@ViewInject(R.id.home_search_start)
	private View homeSearchStart;
	@ViewInject(R.id.search_hot_search)
	private FlowTagLayout hotSearch;
	@ViewInject(R.id.search_history)
	private ListView historySearch;
	@ViewInject(R.id.search_clean_history)
	private View searchCleanHistory;

	private HotSearchAdapter sizeAdapter;
	private List<String> historyDatas = new ArrayList<>();
	private List<String> hotDatas;
	private HistorySearchAdapter historyAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_act_search);
		ViewInjectUtils.inject(this);
		initView();
		loadData();
	}

	private void initView() {
		homeSearchBack.setOnClickListener(v -> finish());
		homeSearchStart.setOnClickListener(v -> {
			AnalysisUtil.onEvent(this, "out_search");
			search();
		});
		//历史搜索
		historyAdapter = new HistorySearchAdapter(this, historyDatas);
		historySearch.setAdapter(historyAdapter);

		sizeAdapter = new HotSearchAdapter(this);

		hotSearch.setAdapter(sizeAdapter);
		hotSearch.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_NONE);
		hotSearch.setOnTagClickListener((parent, view, position) -> {
			searchEdit.setText(hotDatas.get(position));
			searchEdit.setSelection(hotDatas.get(position).length());
			search();
		});

		historySearch.setOnItemClickListener((parent, view, position, id) -> {
			searchEdit.setText(historyDatas.get(position));
			searchEdit.setSelection(historyDatas.get(position).length());
			search();
		});
		//软键盘搜索
		searchEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
			if (EditorInfo.IME_ACTION_SEARCH == i) {
				search();
			}
			return false;
		});
		searchCleanHistory.setOnClickListener(v -> {
			PreferencesUtils.removeSharedPreferenceByKey(this, SEARCH_PRE_KEY_HISTORY);
			historyAdapter.clear();
			searchCleanHistory.setVisibility(View.GONE);
		});
	}

	private void loadData() {
		requestHttpData(Constants.Urls.URL_POST_HOT_SEARCH, REQUEST_NET_HOT_SEARCH, FProtocol.HttpMethod.POST, null);
	}

	private void search() {
		String content = searchEdit.getText().toString().trim();
		setSearchStoreHistory(content);
		ProductActivity.startProductActivity(this, content, 0, "");
	}

	@Override
	protected void onResume() {
		super.onResume();
		getSearchStoreHistory();
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			if (REQUEST_NET_HOT_SEARCH == requestCode) {
				hotDatas = Parsers.getHotSearchList(data);
				if (hotDatas.size() > 0) {
					sizeAdapter.onlyAddAll(hotDatas);
				}
			}
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		ToastUtil.shortShow(this, errorMessage);
	}

	public void setSearchStoreHistory(String history) {
		StringBuilder historyStr = new StringBuilder();
		if (historyDatas != null) {
			if (historyDatas.contains(history)) {
				historyDatas.remove(history);
			}
			if (historyDatas.size() >= SEARCH_HISTORY_COUNT) {
				historyDatas.remove(historyDatas.size() - 1);
			}
			historyStr.append(history);
			for (String str : historyDatas) {
				historyStr.append(",");
				historyStr.append(str);
			}
			historyDatas.add(0, history);
		}
		PreferencesUtils.putString(this, SEARCH_PRE_KEY_HISTORY, historyStr.toString());
	}

	public void getSearchStoreHistory() {
		String historyStr = PreferencesUtils.getString(this, SEARCH_PRE_KEY_HISTORY);
		if (historyDatas != null) {
			historyDatas.clear();
			List<String> historys = new ArrayList<>();
			if (!StringUtil.isEmpty(historyStr)) {
				Collections.addAll(historys, historyStr.split(","));
				for (int i = 0; i < historys.size() && i < SEARCH_HISTORY_COUNT; i++) {
					historyDatas.add(historys.get(i));
					historyAdapter.notifyDataSetChanged();
				}
			}
			if (historys.size() > 0) {
				searchCleanHistory.setVisibility(View.VISIBLE);
			} else {
				searchCleanHistory.setVisibility(View.GONE);
			}
		}
	}
}
