package com.pjpz.model;

import java.util.HashMap;
import java.util.List;

import android.database.Cursor;

import com.google.gson.Gson;
import com.pjpz.dao.PeriodicalsDataHelper;

/**
 * 期刊
 * 
 * @author gh
 *
 */
public class Periodical extends BaseModel {
	private static final HashMap<String, Periodical> CACHE = new HashMap<String, Periodical>();
	public String periodicalId;
	public String logoUrl;
	public String name;
	public String summary;
	public String syncTime;

	private static void addToCache(Category category,Periodical periodical) {
		CACHE.put(category.name()+periodical.periodicalId, periodical);
	}

	private static Periodical getFromCache(String id) {
		return CACHE.get(id);
	}

	public static Periodical fromJson(String json) {
		return new Gson().fromJson(json, Periodical.class);
	}

	public static Periodical fromCursor(Category category,Cursor cursor) {
		String id = cursor.getString(cursor
				.getColumnIndex(PeriodicalsDataHelper.PeriodicalsDBInfo.ID));
		Periodical periodical = getFromCache(id);
		if (periodical != null) {
			return periodical;
		}
		periodical = new Gson().fromJson(cursor.getString(cursor
				.getColumnIndex(PeriodicalsDataHelper.PeriodicalsDBInfo.JSON)),
				Periodical.class);
		addToCache(category,periodical);
		return periodical;
	}

	@Override
	public String toString() {
		return "Periodical [periodicalId=" + periodicalId + ", logoUrl="
				+ logoUrl + ", name=" + name + ", summary=" + summary
				+ ", syncTime=" + syncTime + "]";
	}

	public static class PeriodicalRequestData extends BaseModel {
		public String commandName;
		public String commandType;
		public String commandStatus;
		public String commandDevice;
		public List<RequestItem> commandParam;
		public List<Periodical> commandData;

		@Override
		public String toString() {
			return "PeriodicalRequestData [commandName=" + commandName
					+ ", commandType=" + commandType + ", commandStatus="
					+ commandStatus + ", commandDevice=" + commandDevice
					+ ", commandParam=" + commandParam + ", commandData="
					+ commandData + "]";
		}

	}

	public static class RequestItem {
		public String page;
		public String pageSize;
	}

}
