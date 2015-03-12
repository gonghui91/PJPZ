package com.pjpz.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * 目录
 * 
 * @author gh
 *
 */
public class Catalog {
	public String catalogId;
	public String first;
	public String second;
	public String articleName;
	public String articlePage;
	public String articleId;

	@Override
	public String toString() {
		return "Catalog [catalogId=" + catalogId + ", catalogName="
				+ articleName + ", articleId=" + articleId + "]";
	}

	public static class CatalogRequestData extends BaseModel {
		public String commandName;
		public String commandType;
		public String commandStatus;
		public String commandDevice;
		public List<RequestItem> commandParam;
		public CatalogMenu commandData;

		@Override
		public String toString() {
			return "CatalogRequestData [commandName=" + commandName
					+ ", commandType=" + commandType + ", commandStatus="
					+ commandStatus + ", commandDevice=" + commandDevice
					+ ", commandParam=" + commandParam + ", commandData="
					+ commandData + "]";
		}

	}

	public static class CatalogMenu extends BaseModel {
		public List<Map<String, String>> frontcover;
		public List<Catalog> catalog;

		@Override
		public String toString() {
			return "CatalogMenu [frontcover=" + frontcover + ", catalog="
					+ catalog + "]";
		}
	}

	public static class ImageItem {
		public String imageurl;
		public String cover;

		@Override
		public String toString() {
			return "ImageItem [imageurl=" + imageurl + ", cover=" + cover + "]";
		}

	}

	public static class RequestItem {
		public String periodicalId;

		@Override
		public String toString() {
			return "RequestItem [periodicalId=" + periodicalId + "]";
		}
	}
}
