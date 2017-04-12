package com.wuxinaedu.weixin.bean;

import java.io.Serializable;

/**
 * 聊天内容bean
 */
public class Chat implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4701165722139060323L;
	public static final int TEXT=0,IMAGE=1,VOICE=2,ME=0,OTHERS =1;	
	private int id;
	private int type;
	private String time;
	private String content;
	private String myHead,head;
	private String imageUrl;
	private String AudioUrl;
	
	
	public String getAudioUrl() {
		return AudioUrl;
	}
	public void setAudioUrl(String audioUrl) {
		AudioUrl = audioUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	/**
	 *消息类型 0 文本 1 图片 2 声音 
	 */
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * 辨识是对方消息还是自己发送的消息 0是我1是对方
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 辨识是对方消息还是自己发送的消息0是我1是对方
	 */
	public void setId(int id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMyHead() {
		return myHead;
	}
	public void setMyHead(String myHead) {
		this.myHead = myHead;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
}

