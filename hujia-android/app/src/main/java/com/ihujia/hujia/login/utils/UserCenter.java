package com.ihujia.hujia.login.utils;

import android.content.Context;

import com.common.utils.PreferencesUtils;
import com.common.utils.StringUtil;
import com.ihujia.hujia.network.entities.UserEntity;

/**
 * Created by liuzhichao on 2016/12/19.
 * 个人信息
 */
public final class UserCenter {

	private static final String USER_ID = "user_id";
	private static final String USER_LOGIN_NAME = "user_login_name";
	private static final String USER_NICK_NAME = "user_nick_name";
	private static final String USER_REAL_NAME = "user_real_name";
	private static final String USER_AVATAR = "user_avatar";
	private static final String USER_EMAIL = "user_email";
	private static final String USER_SEX = "user_sex";
	private static final String USER_STATUS = "user_status";
	private static final String USER_ADDRESS_PROVINCE = "user_address_province";
	private static final String USER_ADDRESS_CITY = "user_address_city";
	private static final String USER_ADDRESS_DISTRICT = "user_address_district";
	private static final String USER_BIRTHDAY = "user_birthday";

	public static void saveUserInfo(Context context, UserEntity userEntity) {
		PreferencesUtils.putString(context, USER_ID, userEntity.getId());
		PreferencesUtils.putString(context, USER_LOGIN_NAME, userEntity.getLoginName());
		if (StringUtil.isEmpty(userEntity.getNickName())) {
			PreferencesUtils.putString(context, USER_NICK_NAME, userEntity.getLoginName());
		} else {
			PreferencesUtils.putString(context, USER_NICK_NAME, userEntity.getNickName());
		}
		PreferencesUtils.putString(context, USER_REAL_NAME, userEntity.getUserName());
		PreferencesUtils.putString(context, USER_AVATAR, userEntity.getAvatar());
		PreferencesUtils.putString(context, USER_EMAIL, userEntity.getEmail());
		PreferencesUtils.putString(context, USER_SEX, userEntity.getSex());
		PreferencesUtils.putInt(context, USER_STATUS, userEntity.getStatus());
		PreferencesUtils.putString(context, USER_ADDRESS_PROVINCE, userEntity.getProvince());
		PreferencesUtils.putString(context, USER_ADDRESS_CITY, userEntity.getCity());
		PreferencesUtils.putString(context, USER_ADDRESS_DISTRICT, userEntity.getDistrict());
		PreferencesUtils.putString(context, USER_BIRTHDAY, userEntity.getBirthday());
	}

	public static void cleanUserInfo(Context context) {
		PreferencesUtils.removeSharedPreferenceByKey(context, USER_ID);
		PreferencesUtils.removeSharedPreferenceByKey(context, USER_LOGIN_NAME);
		PreferencesUtils.removeSharedPreferenceByKey(context, USER_NICK_NAME);
		PreferencesUtils.removeSharedPreferenceByKey(context, USER_REAL_NAME);
		PreferencesUtils.removeSharedPreferenceByKey(context, USER_AVATAR);
		PreferencesUtils.removeSharedPreferenceByKey(context, USER_EMAIL);
		PreferencesUtils.removeSharedPreferenceByKey(context, USER_SEX);
		PreferencesUtils.removeSharedPreferenceByKey(context, USER_STATUS);
	}

	public static String getUserId(Context context) {
		return PreferencesUtils.getString(context, USER_ID);
	}

	public static String getUserLoginName(Context context) {
		return PreferencesUtils.getString(context, USER_LOGIN_NAME);
	}

	public static String getNickName(Context context) {
		return PreferencesUtils.getString(context, USER_NICK_NAME);
	}

	public static String getUserAvatar(Context context) {
		return PreferencesUtils.getString(context, USER_AVATAR);
	}

	public static boolean isLogin(Context context) {
		String token = PreferencesUtils.getString(context, USER_ID);
		return !StringUtil.isEmpty(token);
	}

	public static UserEntity getUserInfo(Context context){
		String avatar = PreferencesUtils.getString(context,USER_AVATAR);
		String nickName = PreferencesUtils.getString(context,USER_NICK_NAME);
		String sex = PreferencesUtils.getString(context,USER_SEX);
		String birthday = PreferencesUtils.getString(context,USER_BIRTHDAY);
		String province = PreferencesUtils.getString(context,USER_ADDRESS_PROVINCE);
		String city = PreferencesUtils.getString(context,USER_ADDRESS_CITY);
		String district = PreferencesUtils.getString(context,USER_ADDRESS_DISTRICT);
		return new UserEntity(nickName,sex,city,district,birthday,province,avatar);
	}
}
