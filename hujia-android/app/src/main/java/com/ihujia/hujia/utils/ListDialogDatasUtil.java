package com.ihujia.hujia.utils;

import com.ihujia.hujia.network.entities.ListDialogEntity;
import com.ihujia.hujia.network.entities.ListDialogPageEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoweiwei on 2017/3/27.
 * 用于取消订单，退货申请的数据生成
 */

public class ListDialogDatasUtil {

	private static String[] reasons = new String[]{"质量问题", "商品成分描述不符",
			"收到商品少件/破损",
			"产地/批号/等描述不符",
			"假冒品牌",
			"卖家发错货",
			"拍错/多拍/不喜欢",
			"三无产品",
			"其他"};
	private static String[] datas = new String[]{"我不想买了" ,
			"信息填写错误，重新拍" ,
			"卖家缺货" ,
			"其他原因"};

	public static ListDialogPageEntity createOrderDatas() {
		List<ListDialogEntity> entities = new ArrayList<>();
		for (int i = 0; i < datas.length; i++) {
			ListDialogEntity entity = new ListDialogEntity(String.valueOf(i), datas[i]);
			entities.add(entity);
		}
		return new ListDialogPageEntity(entities);
	}

	public static ListDialogPageEntity createRefundDatas() {
		List<ListDialogEntity> entities = new ArrayList<>();
		for (int i = 0; i < reasons.length; i++) {
			ListDialogEntity entity = new ListDialogEntity(String.valueOf(i), reasons[i]);
			entities.add(entity);
		}
		return new ListDialogPageEntity(entities);
	}
}
