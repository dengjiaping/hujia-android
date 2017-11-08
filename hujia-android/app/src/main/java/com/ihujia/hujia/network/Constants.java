package com.ihujia.hujia.network;

/**
 * Created by jacktian on 15/8/19.
 * 接口地址
 */
public class Constants {

	public static final String USERID = "user_id";
	public static final String PAGENUM = "current_page";
	public static final String PAGESIZE = "page_size";
	public static final String PAGE_SIZE_10 = "10";
	public static final String PAGE_SIZE_20 = "20";

	public static class Urls {
		// 测试环境域名
//		public static String URL_BASE_DOMAIN = "http://192.168.2.159:8080";
//		public static String URL_BASE_DOMAIN = "http://192.168.1.90:8081";
//		public static String URL_BASE_DOMAIN = "http://192.168.1.162:8080";
		// 正式环境域名
		public static String URL_BASE_DOMAIN = "http://api.ihujia.com";

		//获取短信验证码
		public static String URL_POST_IDENTIFY_CODE = URL_BASE_DOMAIN + "/identifyCode";
		//登录
		public static String URL_POST_LOGIN = URL_BASE_DOMAIN + "/login";
		//首页上半部分
		public static String URL_POST_HOME_TOP = URL_BASE_DOMAIN + "/indexInfo";
		//首页下半部分
		public static String URL_POST_HOME_BOTTOM = URL_BASE_DOMAIN + "/indexFloor";
		//推荐更多
		public static String URL_POST_RECOMMEND_MORE = URL_BASE_DOMAIN + "/indexFloorDetail";
		//品牌列表
		public static String URL_POST_BRAND_LIST = URL_BASE_DOMAIN + "/brandList";
		//品牌详情
		public static String URL_POST_BRAND_DETAIL = URL_BASE_DOMAIN + "/brandDetails";
		//第三方店铺
		public static String URL_POST_OTHER_STORE_LIST = URL_BASE_DOMAIN + "/storeList";
		//第三方店铺详情
		public static String URL_POST_OTHER_STORE_DETAIL = URL_BASE_DOMAIN + "/storeInfo";
		//分类列表
		public static String URL_POST_CATEGORY_LIST = URL_BASE_DOMAIN + "/categrayList";
		//商品筛选分类
		public static String URL_POST_CONDITION_LIST = URL_BASE_DOMAIN + "/goodsListCondition";
		//商品列表、搜索商品
		public static String URL_POST_PRODUCT_LIST = URL_BASE_DOMAIN + "/goodsList";
		//热门搜索
		public static String URL_POST_HOT_SEARCH = URL_BASE_DOMAIN + "/hotSearch";
		//个人中心
		public static String URL_POST_PERSON = URL_BASE_DOMAIN + "/queryOrderAndNewsNum";
		//个人信息
		public static String URL_POST_PERSON_INFO = URL_BASE_DOMAIN + "/completeUserInfo";
		//检查更新
		public static String URL_POST_CHECK_UPDATE = URL_BASE_DOMAIN + "/versionUpdate";
		//意见反馈
		public static String URL_POST_FEED_BACK = URL_BASE_DOMAIN + "/addOpinion";
		//消息列表
		public static String URL_POST_MESSAGE_LIST = URL_BASE_DOMAIN + "/messageList";
		//我的收藏
		public static String URL_POST_COLLECTION = URL_BASE_DOMAIN + "/collectionList";
		//删除收藏
		public static String URL_POST_COLLECTION_DELETE = URL_BASE_DOMAIN + "/deleteCollection";
		//我的优惠券
		public static String URL_POST_COUPON = URL_BASE_DOMAIN + "/queryUserCouponList";
		//添加优惠券
		public static String URL_POST_ADD_COUPON = URL_BASE_DOMAIN + "/exchangeCoupon";
		//收货地址列表
		public static String URL_POST_ADDRESS_LIST = URL_BASE_DOMAIN + "/receivingList";
		//添加/修改收货地址
		public static String URL_POST_UPDATE_ADDRESS = URL_BASE_DOMAIN + "/addReceiving";
		//删除收货地址
		public static String URL_POST_DELETE_ADDRESS = URL_BASE_DOMAIN + "/deleteReceiving";
		//订单
		public static String URL_POST_ORDER = URL_BASE_DOMAIN + "/orderList";
		//订单获取支付方式
		public static String URL_POST_GET_PAYTYPE = URL_BASE_DOMAIN + "/payType";
		//退货列表
		public static String URL_POST_REFUND_LIST = URL_BASE_DOMAIN + "/queryRefundOrderList";
		//订单详情
		public static String URL_POST_ORDER_DETAIL = URL_BASE_DOMAIN + "/queryOrderInfo";
		//取消订单
		public static String URL_POST_CANCEL_ORDER = URL_BASE_DOMAIN + "/cancelOrder";
		//删除订单
		public static String URL_POST_DELETE_ORDER = URL_BASE_DOMAIN + "/delOrder";
		//确认收货
		public static String URL_POST_QUERY_ORDER = URL_BASE_DOMAIN + "/confirmReceipt";
		//退款退货
		public static String URL_POST_REFUND = URL_BASE_DOMAIN + "/refundOrder";
		//仅退款 提交退款申请
		public static String URL_POST_REFUND_MONEY = URL_BASE_DOMAIN + "/onlyRefundOrder";
		//退货进度查询
		public static String URL_POST_REFUND_PROGRESS = URL_BASE_DOMAIN + "/queryOrderPathList";
		//查询物流公司
		public static String URL_POST_LOGISTIC_COMPANY = URL_BASE_DOMAIN + "/queryLogisticsList";
		//提交物流信息
		public static String URL_POST_COMMIT_LOGISTIC = URL_BASE_DOMAIN + "/addInvoice";
		//取消退款
		public static String URL_POST_CANCEL_REFUND = URL_BASE_DOMAIN + "/cancelRefundOrder";
		//物流详情
		public static String URL_POST_LOGISTICS_DETAIL = URL_BASE_DOMAIN + "/querylogistics";
		//商品详情
		public static String URL_POST_PRODUCT_DETAIL = URL_BASE_DOMAIN + "/goodsDetails";
		//通用分享
		public static String URL_POST_SHARE_INFO = URL_BASE_DOMAIN + "/queryShare";
		//添加收藏
		public static String URL_POST_PRODUCT_ADD_COLLECT = URL_BASE_DOMAIN + "/addCollection";
		//我的购物车
		public static String URL_POST_MY_SHOPCAR = URL_BASE_DOMAIN + "/myShoppingCart";
		//添加到购物车
		public static String URL_POST_PRODUCT_ADD_SHOPCAR = URL_BASE_DOMAIN + "/addShoppingCart";
		//删除购物车
		public static String URL_POST_PRODUCT_DELETE_SHOPCAR = URL_BASE_DOMAIN + "/deleteShoppingCart";
		//修改购物车的尺寸颜色
		public static String URL_POST_PRODUCT_FIX_SHOPCAR = URL_BASE_DOMAIN + "/cartAttribute";
		//购物车修改完成
		public static String URL_POST_PRODUCT_FINISH_SHOPCAR = URL_BASE_DOMAIN + "/editShoppingCart";
		//获取购物车数量
		public static String URL_POST_GET_SHOPCAR_NUM = URL_BASE_DOMAIN + "/myShoppingCartCount";
		//修改尺寸颜色等属性获取skuid
		public static String URL_POST_FIX_ATTR = URL_BASE_DOMAIN + "/goodsSkuInfo";
		//确认订单
		public static String URL_POST_CREAT_ORDER = URL_BASE_DOMAIN + "/confirmOrderInfo";
		//提交订单
		public static String URL_POST_COMMIT_ORDER = URL_BASE_DOMAIN + "/createOrder";
		//支付
		public static String URL_POST_PAY = URL_BASE_DOMAIN + "/payment";
		//立即购买
		public static String URL_POST_BUY_NOW = URL_BASE_DOMAIN + "/payNow";

		//定制-上传头像
		public static String URL_POST_CUSTOM_UPDATE_AVATAR = URL_BASE_DOMAIN + "/pictureUpload";
	}
}
