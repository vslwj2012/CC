package com.cc.client.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * 封装一个消息类型
 * 因为本次聊天软件中的任何业务请求都是通过发送标准的消息对象
 * （类似快递必须封装成标准的快递包裹）
 */


public class ChatMessage implements Serializable {
    public CCUser getFrom() {
		return from;
	}
	public void setFrom(CCUser from) {
		this.from = from;
	}
	public CCUser getTo() {
		return to;
	}
	public void setTo(CCUser to) {
		this.to = to;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	private CCUser from;
    private CCUser to;
    private String content;
    private String time;
    private MessageType type;
	private RespoundType restype;
	private String friendusername;
	private String friendtype;
	private int state;
	private String fromusername;//判断消息是哪个好友发来的
	private Files files;

	public Files getFiles() {
		return files;
	}

	public void setFiles(Files files) {
		this.files = files;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChatMessage that = (ChatMessage) o;
		return Objects.equals(fromusername, that.fromusername);
	}

	@Override
	public String toString() {
		return "ChatMessage{" +
				"from=" + from +
				", to=" + to +
				", content='" + content + '\'' +
				", time='" + time + '\'' +
				", type=" + type +
				", restype=" + restype +
				", friendusername='" + friendusername + '\'' +
				", friendtype='" + friendtype + '\'' +
				", state=" + state +
				", fromusername='" + fromusername + '\'' +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(fromusername);
	}

	public String getFromusername() {
		return fromusername;
	}

	public void setFromusername(String fromusername) {
		this.fromusername = fromusername;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getFriendusername() {
		return friendusername;
	}

	public void setFriendusername(String friendusername) {
		this.friendusername = friendusername;
	}

	public String getFriendtype() {
		return friendtype;
	}

	public void setFriendtype(String friendtype) {
		this.friendtype = friendtype;
	}

	public void setRestype(RespoundType restype) {
		this.restype = restype;
	}

	public RespoundType getRestype() {
		return restype;
	}


}
