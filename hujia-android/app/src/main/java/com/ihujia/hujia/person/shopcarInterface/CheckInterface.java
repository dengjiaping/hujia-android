package com.ihujia.hujia.person.shopcarInterface;

/**
 * Created by zhaoweiwei on 2017/2/10.
 */

public interface CheckInterface {
	/**
	 * 组选框状态改变触发的事件
	 *
	 * @param groupPosition 组元素位置
	 * @param isChecked     组元素选中与否
	 */
	void checkGroup(int groupPosition, boolean isChecked);

	/**
	 * 子选框状态改变时触发的事件
	 *
	 * @param groupPosition 组元素位置
	 * @param childPosition 子元素位置
	 * @param isChecked     子元素选中与否
	 */
	void checkChild(int groupPosition, int childPosition, boolean isChecked);
}
