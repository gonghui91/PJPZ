package com.pjpz.model;

public class Comment {
	private String userName;
	private String content;
	private String reply;

	public Comment() {
		// TODO Auto-generated constructor stub
	}

	public Comment(String userName, String content, String reply) {
		super();
		this.userName = userName;
		this.content = content;
		this.reply = reply;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	@Override
	public String toString() {
		return "Comment [userName=" + userName + ", content=" + content
				+ ", reply=" + reply + "]";
	}

}
