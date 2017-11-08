package com.ihujia.hujia.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/2/13.
 * 用户实体
 */
public class UserEntity extends Entity {

	@SerializedName("user_id")
	private String id;
	@SerializedName("user_name")
	private String userName;
	@SerializedName("head_portrait")
	private String avatar;//头像
	@SerializedName("email")
	private String email;
	@SerializedName("login_name")
	private String loginName;
	@SerializedName("nick_name")
	private String nickName;
	@SerializedName("sex")
	private String sex;
	@SerializedName("cert_code")
	private String idNo;//身份证号
	@SerializedName("status")
	private int status;//状态，4:认证成功
	@SerializedName("province")
	private String province;
	@SerializedName("city")
	private String city;
	@SerializedName("district")
	private String district;
	@SerializedName("birthday")
	private String birthday;

	public UserEntity(String nickName, String sex, String city, String district, String birthday, String province, String avatar) {
		this.nickName = nickName;
		this.sex = sex;
		this.city = city;
		this.district = district;
		this.birthday = birthday;
		this.province = province;
		this.avatar = avatar;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
}
