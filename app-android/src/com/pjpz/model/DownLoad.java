package com.pjpz.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;

import com.google.gson.Gson;
import com.pjpz.dao.DownloadsDataHelper;
import com.pjpz.model.Periodical.RequestItem;

/**
 * 期刊
 * 
 * @author gh
 *
 */
public class DownLoad extends BaseModel implements Serializable{
	private static final long serialVersionUID = 6428888647701646594L;
	private static final HashMap<String, DownLoad> CACHE = new HashMap<String, DownLoad>();
	public String id;
	public String articleName;
	public String category;
	public String content;

	private static void addToCache(DownLoad downLoad) {
		CACHE.put(downLoad.category+downLoad.id, downLoad);
	}

	private static DownLoad getFromCache(String id) {
		return CACHE.get(id);
	}

	public static DownLoad fromJson(String json) {
		return new Gson().fromJson(json, DownLoad.class);
	}

	public static DownLoad fromCursor(Cursor cursor) {
		String id = cursor.getString(cursor
				.getColumnIndex(DownloadsDataHelper.DownloadsDBInfo.ID));
		DownLoad downLoad = getFromCache(id);
		if (downLoad != null) {
			return downLoad;
		}
		downLoad = new DownLoad();
		downLoad.id = id;
		downLoad.articleName = cursor
				.getString(cursor
						.getColumnIndex(DownloadsDataHelper.DownloadsDBInfo.ARTICLENAME));
		downLoad.category = cursor.getString(cursor
				.getColumnIndex(DownloadsDataHelper.DownloadsDBInfo.CATEGORY));
		downLoad.content = cursor.getString(cursor
				.getColumnIndex(DownloadsDataHelper.DownloadsDBInfo.CONTENT));
		addToCache(downLoad);
		return downLoad;
	}
	public static class DownLoadRequestData extends BaseModel {
		public String commandName;
		public String commandType;
		public String commandStatus;
		public String commandDevice;
		public Map<String, String> commandParam;
		public ResponseData commandData;

		@Override
		public String toString() {
			return "PeriodicalRequestData [commandName=" + commandName
					+ ", commandType=" + commandType + ", commandStatus="
					+ commandStatus + ", commandDevice=" + commandDevice
					+ ", commandParam=" + commandParam + ", commandData="
					+ commandData + "]";
		}

	}
	public class ResponseData{
		public String cover;
		public List<String> juanshoutu;
		public List<String> images;
	}
}
