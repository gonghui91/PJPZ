package com.pjpz.model;

import java.util.List;

/**
 * 文字版内容
 * 
 * @author gh
 *
 */
public class OptRequestData extends BaseModel{
		public String commandName;
		public String commandType;
		public String commandStatus;
		public String commandDevice;
		public List<RequestItem> commandParam;
		public List<String> commandData;


	public static class RequestItem {
		public String articleId;
		public String articleType;
	}

}
