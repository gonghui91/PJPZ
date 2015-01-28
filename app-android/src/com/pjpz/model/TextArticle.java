package com.pjpz.model;

import java.util.List;

/**
 * 文字版内容
 * 
 * @author gh
 *
 */
public class TextArticle {
	public String articleName;
	public int praise;
	public int reply;
	public String shareUrl;
	public List<TextItem> body;

	public static class TextArticleRequestData extends BaseModel {
		public String commandName;
		public String commandType;
		public String commandStatus;
		public String commandDevice;
		public List<RequestItem> commandParam;
		public TextArticle commandData;

	}

	public static class RequestItem {
		public String articleId;
	}

	public static class TextItem {
		public List<String> imageurl;
		public String text;
	}
}
