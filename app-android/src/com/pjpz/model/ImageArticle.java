package com.pjpz.model;

import java.util.List;

/**
 * 文字版内容
 * 
 * @author gh
 *
 */
public class ImageArticle {
	public String articleName;
	public int praise;
	public int reply;
	public String shareUrl;
	public ImageItem body;

	public static class ImageArticleRequestData extends BaseModel{
		public String commandName;
		public String commandType;
		public String commandStatus;
		public String commandDevice;
		public List<RequestItem> commandParam;
		public ImageArticle commandData;

	}

	public static class ImageItem {
		public List<String> imageurl;
	}

	public static class RequestItem {
		public String articleId;
	}
}
