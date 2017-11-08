package com.ihujia.hujia.network.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ihujia.hujia.network.entities.AddressEntity;
import com.ihujia.hujia.network.entities.BrandDetailEntity;
import com.ihujia.hujia.network.entities.BrandEntity;
import com.ihujia.hujia.network.entities.CategoryEntity;
import com.ihujia.hujia.network.entities.CommitOrderEntity;
import com.ihujia.hujia.network.entities.ConditionEntity;
import com.ihujia.hujia.network.entities.ConfirmOrderEntity;
import com.ihujia.hujia.network.entities.CouponEntity;
import com.ihujia.hujia.network.entities.CouponPagesEntity;
import com.ihujia.hujia.network.entities.Entity;
import com.ihujia.hujia.network.entities.FixSizeAndColorEntity;
import com.ihujia.hujia.network.entities.HomeRecommendEntity;
import com.ihujia.hujia.network.entities.HomeTopEntity;
import com.ihujia.hujia.network.entities.ListDialogEntity;
import com.ihujia.hujia.network.entities.ListDialogPageEntity;
import com.ihujia.hujia.network.entities.LogisticEntity;
import com.ihujia.hujia.network.entities.LogisticPageEntity;
import com.ihujia.hujia.network.entities.MessageEntity;
import com.ihujia.hujia.network.entities.OrderDetailPageEntity;
import com.ihujia.hujia.network.entities.OrderItemEntity;
import com.ihujia.hujia.network.entities.OrderStoreEntity;
import com.ihujia.hujia.network.entities.OtherStoreDetailEntity;
import com.ihujia.hujia.network.entities.OtherStoreEntity;
import com.ihujia.hujia.network.entities.PagesEntity;
import com.ihujia.hujia.network.entities.PersonEntity;
import com.ihujia.hujia.network.entities.ProductDetailEntity;
import com.ihujia.hujia.network.entities.ProductEntity;
import com.ihujia.hujia.network.entities.RecommendItemEntity;
import com.ihujia.hujia.network.entities.RefundDetailEntity;
import com.ihujia.hujia.network.entities.RefundDetailPageEntity;
import com.ihujia.hujia.network.entities.ShareEntity;
import com.ihujia.hujia.network.entities.ShopCarFixEntity;
import com.ihujia.hujia.network.entities.ShopCarNumEntity;
import com.ihujia.hujia.network.entities.ShopCarStoreEntity;
import com.ihujia.hujia.network.entities.StoreRenovationEntity;
import com.ihujia.hujia.network.entities.UpdateEntity;
import com.ihujia.hujia.network.entities.UserEntity;
import com.ihujia.hujia.network.json.GsonObjectDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacktian on 15/8/30.
 * json解析
 */
public class Parsers {

	public static Gson gson = GsonObjectDeserializer.produceGson();

	/**
	 * @return 所有请求的公共部分，业务层的返回码和返回提示
	 */
	public static Entity getResult(String data) {
		Entity result = gson.fromJson(data, new TypeToken<Entity>() {}.getType());
		if (result == null) {
			result = new Entity();
		}
		return result;
	}

	/**
	 * @return 用户信息
	 */
	public static UserEntity getUser(String data) {
		return gson.fromJson(data, new TypeToken<UserEntity>(){}.getType());
	}

	/**
	 * @return 首页上半部分
	 */
	public static HomeTopEntity getHomeTop(String data) {
		return gson.fromJson(data, new TypeToken<HomeTopEntity>(){}.getType());
	}

	/**
	 * @return 首页下半部分
	 */
	public static List<HomeRecommendEntity> getHomeRecommendList(String data) {
		List<HomeRecommendEntity> homeRecommendEntities = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			homeRecommendEntities = gson.fromJson(jsonObject.optString("floor_list"), new TypeToken<List<HomeRecommendEntity>>(){}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return homeRecommendEntities;
	}

	/**
	 * @return 热门搜索
	 */
	public static List<String> getHotSearchList(String data) {
		List<String> hotSearch = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			JSONArray wordsList = jsonObject.optJSONArray("words_list");
			for (int i = 0; i < wordsList.length(); i++) {
				hotSearch.add(wordsList.optJSONObject(i).optString("hot_words"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hotSearch;
	}

	/**
	 * @return 分页品牌列表
	 */
	public static PagesEntity<BrandEntity> getPageBrand(String data) {
		PagesEntity<BrandEntity> brandEntities = new PagesEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<BrandEntity> brandList = gson.fromJson(jsonObject.optString("brand_list"), new TypeToken<List<BrandEntity>>(){}.getType());
			brandEntities.setTotalPage(jsonObject.optInt("total_page"));
			brandEntities.setDatas(brandList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return brandEntities;
	}

	/**
	 * @return 品牌详情
	 */
	public static BrandDetailEntity getBrandDetail(String data) {
		return gson.fromJson(data, new TypeToken<BrandDetailEntity>(){}.getType());
	}

	/**
	 * @return 分类列表
	 */
	public static List<CategoryEntity> getCategoryList(String data) {
		List<CategoryEntity> categoryEntities = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			categoryEntities = gson.fromJson(jsonObject.optString("category_list"), new TypeToken<List<CategoryEntity>>(){}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return categoryEntities;
	}

	/**
	 * @return 筛选分类
	 */
	public static ConditionEntity getCondition(String data) {
		return gson.fromJson(data, new TypeToken<ConditionEntity>(){}.getType());
	}

	/**
	 * @return 分页商品
	 */
	public static PagesEntity<ProductEntity> getPageProduct(String data) {
		PagesEntity<ProductEntity> pagesEntity = new PagesEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<ProductEntity> productEntities = gson.fromJson(jsonObject.optString("goods_list"), new TypeToken<List<ProductEntity>>() {}.getType());
			pagesEntity.setDatas(productEntities);
			pagesEntity.setTotalPage(jsonObject.optInt("total_page"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return pagesEntity;
	}

	/**
	 * @return 第三方店铺列表
	 */
	public static PagesEntity<OtherStoreEntity> getOtherStoreList(String data) {
		PagesEntity<OtherStoreEntity> otherStoreEntityPagesEntity = new PagesEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<OtherStoreEntity> otherStoreEntities = gson.fromJson(jsonObject.optString("store_list"), new TypeToken<List<OtherStoreEntity>>(){}.getType());
			otherStoreEntityPagesEntity.setDatas(otherStoreEntities);
			otherStoreEntityPagesEntity.setTotalPage(jsonObject.optInt("total_page"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return otherStoreEntityPagesEntity;
	}

	/**
	 * @return 第三方店铺列表
	 */
	public static OtherStoreDetailEntity getOtherStoreDetail(String data) {
		OtherStoreDetailEntity otherStoreDetailEntity = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			otherStoreDetailEntity = gson.fromJson(data, new TypeToken<OtherStoreDetailEntity>(){}.getType());
			if (otherStoreDetailEntity != null) {
				List<ProductEntity> productEntities = gson.fromJson(jsonObject.optString("goods_list"), new TypeToken<List<ProductEntity>>(){}.getType());
				otherStoreDetailEntity.setDatas(productEntities);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return otherStoreDetailEntity;
	}

	/**
	 * @return 版本更新
	 */
	public static UpdateEntity getUpdate(String data) {
		return gson.fromJson(data, new TypeToken<UpdateEntity>(){}.getType());
	}

	/**
	 * @return 定制头像上传成功后的图片地址
	 */
	public static String getAvatarUrl(String data) {
		String avatarUrl = "";
		try {
			avatarUrl = new JSONObject(data).optString("head_portrait");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return avatarUrl;
	}

	/**
	个人中心订单数量 收藏 优惠券数量
	* */
	public static PersonEntity getPersonOrderNum(String data){
		return gson.fromJson(data,new TypeToken<PersonEntity>(){}.getType());
	}

	/**
	 * 我的收藏
	 * */
	public static PagesEntity<RecommendItemEntity> getPageCollection(String data) {
		PagesEntity<RecommendItemEntity> recommendEntities = new PagesEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<RecommendItemEntity> collectionList = gson.fromJson(jsonObject.optString("goods_list"), new TypeToken<List<RecommendItemEntity>>(){}.getType());
			recommendEntities.setTotalPage(jsonObject.optInt("total_page"));
			recommendEntities.setResultCode(jsonObject.optString("result_code"));
			recommendEntities.setResultMsg(jsonObject.optString("result_desc"));
			recommendEntities.setDatas(collectionList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return recommendEntities;
	}

	/**
	 * 我的优惠券
	 * */
	public static CouponPagesEntity<CouponEntity> getPageCoupon(String data) {
		CouponPagesEntity<CouponEntity> couponEntities = new CouponPagesEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<CouponEntity> couponList = gson.fromJson(jsonObject.optString("coupon_list"), new TypeToken<List<CouponEntity>>(){}.getType());
			couponEntities.setTotalPage(jsonObject.optInt("total_page"));
			couponEntities.setResultCode(jsonObject.optString("result_code"));
			couponEntities.setResultMsg(jsonObject.optString("result_desc"));
			couponEntities.setUnUsedNum(jsonObject.optString("no_user_count"));
			couponEntities.setUsedNum(jsonObject.optString("user_count"));
			couponEntities.setOverDueNum(jsonObject.optString("overdue_count"));
			couponEntities.setDatas(couponList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return couponEntities;
	}

	/**
	 * 我的订单列表
	 * */
	public static PagesEntity<OrderItemEntity> getPageOrder(String data) {
		PagesEntity<OrderItemEntity> orderEntities = new PagesEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<OrderItemEntity> orderList = gson.fromJson(jsonObject.optString("order_list"), new TypeToken<List<OrderItemEntity>>(){}.getType());
			orderEntities.setTotalPage(jsonObject.optInt("total_page"));
			orderEntities.setResultCode(jsonObject.optString("result_code"));
			orderEntities.setResultMsg(jsonObject.optString("result_desc"));
			orderEntities.setDatas(orderList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return orderEntities;
	}

	/**
	 商品详情
	 * */
	public static ProductDetailEntity getProductDetail(String data){
		return gson.fromJson(data,new TypeToken<ProductDetailEntity>(){}.getType());
	}

	/**
	 * @return 获取分享信息
	 */
	public static ShareEntity getShare(String data) {
		return gson.fromJson(data, ShareEntity.class);
	}

	/**
	 * @return 消息分页数据
	 */
	public static PagesEntity<MessageEntity> getMessage(String data) {
		PagesEntity<MessageEntity> messageEntityPagesEntity = new PagesEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<MessageEntity> messageEntities = gson.fromJson(jsonObject.optString("message_list"), new TypeToken<List<MessageEntity>>(){}.getType());
			messageEntityPagesEntity.setDatas(messageEntities);
			messageEntityPagesEntity.setTotalPage(jsonObject.optInt("total_page"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return messageEntityPagesEntity;
	}

	/**
	 个人中心订单数量 收藏 优惠券数量
	 * */
	public static ShopCarNumEntity getShopCarNum(String data){
		return gson.fromJson(data,new TypeToken<ShopCarNumEntity>(){}.getType());
	}

	/**
	 * 商品尺寸颜色
	 * */
	public static PagesEntity<FixSizeAndColorEntity> getSizeOrColor(String data) {
		PagesEntity<FixSizeAndColorEntity> sizeAndColorEntityPagesEntity = new PagesEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<FixSizeAndColorEntity> sizeAndColorEntities = gson.fromJson(jsonObject.optString("sku_list"), new TypeToken<List<FixSizeAndColorEntity>>(){}.getType());
			sizeAndColorEntityPagesEntity.setResultCode(jsonObject.optString("result_code"));
			sizeAndColorEntityPagesEntity.setResultMsg(jsonObject.optString("result_desc"));
			sizeAndColorEntityPagesEntity.setDatas(sizeAndColorEntities);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sizeAndColorEntityPagesEntity;
	}

	/**
	 * 我的购物车
	 * */
	public static PagesEntity<ShopCarStoreEntity> getShopCarStore(String data) {
		PagesEntity<ShopCarStoreEntity> shopCarStoreEntityPagesEntity = new PagesEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<ShopCarStoreEntity> shopCarStoreEntities = gson.fromJson(jsonObject.optString("store_list"), new TypeToken<List<ShopCarStoreEntity>>(){}.getType());
			shopCarStoreEntityPagesEntity.setResultCode(jsonObject.optString("result_code"));
			shopCarStoreEntityPagesEntity.setResultMsg(jsonObject.optString("result_desc"));
			shopCarStoreEntityPagesEntity.setDatas(shopCarStoreEntities);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return shopCarStoreEntityPagesEntity;
	}

	/**
	购物车修改颜色尺寸
	 * */
	public static ShopCarFixEntity getShopCarFix(String data){
		return gson.fromJson(data,new TypeToken<ShopCarFixEntity>(){}.getType());
	}

	/**
	 * 确认订单页
	 * */
	public static ConfirmOrderEntity<ShopCarStoreEntity> getConfirmOrder(String data) {
		ConfirmOrderEntity<ShopCarStoreEntity> confirmOrderEntity = new ConfirmOrderEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<ShopCarStoreEntity> shopCarStoreEntities = gson.fromJson(jsonObject.optString("store_list"), new TypeToken<List<ShopCarStoreEntity>>(){}.getType());
			confirmOrderEntity.setResultCode(jsonObject.optString("result_code"));
			confirmOrderEntity.setResultMsg(jsonObject.optString("result_desc"));
			confirmOrderEntity.setReceiveId(jsonObject.optString("receiving_id"));
			confirmOrderEntity.setUserAddress(jsonObject.optString("address"));
			confirmOrderEntity.setUserName(jsonObject.optString("consignee"));
			confirmOrderEntity.setUserPhone(jsonObject.optString("contacts"));
			confirmOrderEntity.setStoreEntities(shopCarStoreEntities);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return confirmOrderEntity;
	}

	/**
	 提交订单成功
	 * */
	public static CommitOrderEntity getCommitSuccess(String data){
		return gson.fromJson(data,new TypeToken<CommitOrderEntity>(){}.getType());
	}

	/**
	 * @return 微信或支付宝的支付凭证
	 */
	public static String getWXPrepayId(String data) {
		String prepayId = "";
		try {
			prepayId = new JSONObject(data).optString("prepayid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return prepayId;
	}

	/**
	 * 地址管理
	 * */
	public static PagesEntity<AddressEntity> getAddress(String data) {
		PagesEntity<AddressEntity> addressEntities = new PagesEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<AddressEntity> addressList = gson.fromJson(jsonObject.optString("receiving_list"), new TypeToken<List<AddressEntity>>(){}.getType());
			addressEntities.setTotalPage(jsonObject.optInt("total_page"));
			addressEntities.setResultCode(jsonObject.optString("result_code"));
			addressEntities.setResultMsg(jsonObject.optString("result_desc"));
			addressEntities.setDatas(addressList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return addressEntities;
	}

	/**
	 * 订单详情
	 **/

	public static OrderDetailPageEntity<OrderStoreEntity> getOrderDetail (String data) {
		OrderDetailPageEntity<OrderStoreEntity> orderDetailPageEntity = new OrderDetailPageEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<OrderStoreEntity> storeEntities = gson.fromJson(jsonObject.optString("order_store_list"),new TypeToken<List<OrderStoreEntity>>(){}.getType());
			orderDetailPageEntity.setResultCode(jsonObject.optString("result_code"));
			orderDetailPageEntity.setResultMsg(jsonObject.optString("result_desc"));
			orderDetailPageEntity.setOrderId(jsonObject.optString("order_id"));
			orderDetailPageEntity.setStatus(jsonObject.optString("status"));
			orderDetailPageEntity.setUserName(jsonObject.optString("consignee"));
			orderDetailPageEntity.setUserPhone(jsonObject.optString("contacts"));
			orderDetailPageEntity.setUserAddress(jsonObject.optString("address"));
			orderDetailPageEntity.setGoodsPrice(jsonObject.optString("order_price"));
			orderDetailPageEntity.setCouponPrice(jsonObject.optString("amount"));
			orderDetailPageEntity.setRevelingPrice(jsonObject.optString("reveling_price"));
			orderDetailPageEntity.setOrderPrice(jsonObject.optString("order_price"));
			orderDetailPageEntity.setCreatTime(jsonObject.optString("create_date"));
			orderDetailPageEntity.setOrderTotal(jsonObject.optString("order_total"));
			orderDetailPageEntity.setRemainTime(jsonObject.optString("order_out_itme"));
			orderDetailPageEntity.setOrderStoreEntities(storeEntities);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return orderDetailPageEntity;
	}

	/**
	 * 物流詳情
	 * */

	public static LogisticPageEntity<LogisticEntity> getLogistics (String data) {
		LogisticPageEntity<LogisticEntity> logisticPageEntity = new LogisticPageEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<LogisticEntity> logisticEntities = gson.fromJson(jsonObject.optString("logistics_list"),new TypeToken<List<LogisticEntity>>(){}.getType());
			logisticPageEntity.setResultCode(jsonObject.optString("result_code"));
			logisticPageEntity.setResultMsg(jsonObject.optString("result_desc"));
			logisticPageEntity.setLogisticNumber(jsonObject.optString("logistics_no"));
			logisticPageEntity.setLogisticName(jsonObject.optString("logistics_name"));
			logisticPageEntity.setLogisticPhone(jsonObject.optString("logistics_phone"));
			logisticPageEntity.setLogisticEntities(logisticEntities);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return logisticPageEntity;
	}

	/**
	 * 退货进度
	 * */
	public static RefundDetailPageEntity<RefundDetailEntity> getRefundDetail(String data) {
		RefundDetailPageEntity<RefundDetailEntity> refundDetailPagesEntity = new RefundDetailPageEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<RefundDetailEntity> refundDetailEntities = gson.fromJson(jsonObject.optString("order_path_list"), new TypeToken<List<RefundDetailEntity>>(){}.getType());
			refundDetailPagesEntity.setResultCode(jsonObject.optString("result_code"));
			refundDetailPagesEntity.setResultMsg(jsonObject.optString("result_desc"));
			refundDetailPagesEntity.setStatus(jsonObject.optString("status"));
			refundDetailPagesEntity.setDatas(refundDetailEntities);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return refundDetailPagesEntity;
	}

	/**
	 * 退货进度
	 * */
	public static ListDialogPageEntity<ListDialogEntity> getLogisticUnit(String data) {
		ListDialogPageEntity<ListDialogEntity> dialogEntityPagesEntity = new ListDialogPageEntity<>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			List<ListDialogEntity> logisticUnits = gson.fromJson(jsonObject.optString("logistics_list"), new TypeToken<List<ListDialogEntity>>(){}.getType());
			dialogEntityPagesEntity.setResultCode(jsonObject.optString("result_code"));
			dialogEntityPagesEntity.setResultMsg(jsonObject.optString("result_desc"));
			dialogEntityPagesEntity.setEntities(logisticUnits);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return dialogEntityPagesEntity;
	}

	/*
	* 店铺装修
	* */
	public static StoreRenovationEntity storeRenovationEntity (String data) {
		return gson.fromJson(data,new TypeToken<StoreRenovationEntity>(){}.getType());
	}
}