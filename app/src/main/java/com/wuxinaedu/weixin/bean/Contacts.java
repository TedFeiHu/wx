package com.wuxinaedu.weixin.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Contacts implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3499661453217116714L;
	//用户名，头像url，地区，签名，最后聊天记录
	private String name,head,area,autograph,lastStr;
	//微信号  未读消息条数
	private int weCode,newsNum;
	//个人相册 集合
	private List<String> images;
	
	// 姓名拼音第一个字母，姓名拼音全拼，姓名每个字拼音的首字母
	private String nameFirst,namePinyin,nameFirstPinyin;
	
	//是否已被添加，主要用于添加好友界面 ，其他界面返回true
	private boolean isAdd;
	
	//是否被加入群聊   非json返回值，群聊界面写入
	private boolean isAddGroup;
	
	//最后聊天时间  联系人界面可不解析
	private Date lastTime;

	//图片的透明度  0 -255
	private int alpha = 255;
	
	
	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public boolean isAddGroup() {
		return isAddGroup;
	}

	public void setAddGroup(boolean isAddGroup) {
		this.isAddGroup = isAddGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAutograph() {
		return autograph;
	}

	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}

	public String getLastStr() {
		return lastStr;
	}

	public void setLastStr(String lastStr) {
		this.lastStr = lastStr;
	}

	public int getWeCode() {
		return weCode;
	}

	public void setWeCode(int weCode) {
		this.weCode = weCode;
	}

	public int getNewsNum() {
		return newsNum;
	}

	public void setNewsNum(int newsNum) {
		this.newsNum = newsNum;
	}

	public boolean isAdd() {
		return isAdd;
	}

	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getNameFirst() {
		return nameFirst;
	}

	public void setNameFirst(String nameFirst) {
		this.nameFirst = nameFirst;
	}

	public String getNamePinyin() {
		return namePinyin;
	}

	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}

	public String getNameFirstPinyin() {
		return nameFirstPinyin;
	}

	public void setNameFirstPinyin(String nameFirstPinyin) {
		this.nameFirstPinyin = nameFirstPinyin;
	}
}
