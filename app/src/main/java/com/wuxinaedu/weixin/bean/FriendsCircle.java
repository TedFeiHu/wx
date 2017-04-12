package com.wuxinaedu.weixin.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 朋友圈javaBean 
 *
 */
public class FriendsCircle implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4705162924551377162L;
	// 头像url 用户名  内容  时间  背景url
	private String head,name,content;
	// 图片集合url
	private List<String> images;
	// 时间
	private Date time;
	
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}
