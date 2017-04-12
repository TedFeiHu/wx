package com.wuxinaedu.weixin.bean;

import java.io.Serializable;

public class UserInfor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4399510609450672981L;

	private String userName,userPhoneNum,head,backGround;
	
	private String id;
	

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

	public String getUserPhoneNum() {
		return userPhoneNum;
	}

	public void setUserPhoneNum(String userPhoneNum) {
		this.userPhoneNum = userPhoneNum;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getBackGround() {
		return backGround;
	}

	public void setBackGround(String backGround) {
		this.backGround = backGround;
	}
	
	
}
