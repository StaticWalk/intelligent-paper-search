package com.xxy.intelligentpapersearch.enums;

/**
 * Created by xiongxiaoyu
 * Data:2018/11/2
 * Time:15:39
 *
 * 信息类词汇枚举
 */

public enum AbstractWordEnum {

	/**
	 * 人名
	 */
	PERSONNAME("nm"),

	/**
	 * 关键词
	 */
	KEYWORD("kw"),

	/**
	 * 论文类别
	 */
	GENRE("gr");

	private String nature;

	AbstractWordEnum(String nature) {
		this.nature = nature;
	}

	public String getNature() {
		return nature;
	}

}
