package com.pjpz.model;

import java.util.List;

/**
 * 评论
 * 
 * @author gh
 *
 */
public class Comment {
	public String commentId;
	public String userName;
	public String content;
	public String reply;

	public static class ListCommentRequestData extends BaseModel{
		public String commandName;
		public String commandType;
		public String commandStatus;
		public String commandDevice;
		public RequestItem commandParam;
		public List<Comment> commandData;

	}
	public static class AddCommentRequestData extends BaseModel{
		public String commandName;
		public String commandType;
		public String commandStatus;
		public String commandDevice;
		public AddItem commandParam;
		public List<Comment> commandData;

	}
	public static class RequestItem {
		public String articleId;
	}
	public static class AddItem {
		public String articleId;
		public String content;
	}
	public static class CommentItem {
		public String commentId;
		public String userName;
		public String content;
		public String reply;
	}
}
