package com.pjpz.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;

import com.pjpz.database.Column;
import com.pjpz.database.SQLiteTable;
import com.pjpz.model.DownLoad;

/**
 * Created by storm on 14-4-8.
 */
public class DownloadsDataHelper extends BaseDataHelper {

	public DownloadsDataHelper(Context context) {
		super(context);
	}

	@Override
	protected Uri getContentUri() {
		return DataProvider.DOWNLOADS_CONTENT_URI;
	}

	private ContentValues getContentValues(DownLoad downLoad) {
		ContentValues values = new ContentValues();
		values.put(DownloadsDBInfo.ID, downLoad.id);
		values.put(DownloadsDBInfo.CATEGORY, downLoad.category);
		values.put(DownloadsDBInfo.CONTENT, downLoad.content);
		values.put(DownloadsDBInfo.ARTICLENAME, downLoad.articleName);
		return values;
	}

	public DownLoad query(long id) {
		DownLoad downLoad = null;
		Cursor cursor = query(null, DownloadsDBInfo.ID + "= ?",
				new String[] { String.valueOf(id) }, null);
		if (cursor.moveToFirst()) {
			downLoad = DownLoad.fromCursor(cursor);
		}
		cursor.close();
		return downLoad;
	}

	public int update(DownLoad downLoad) {
		return update(getContentValues(downLoad), DownloadsDBInfo.ID
				+ "= ? AND " + DownloadsDBInfo.CATEGORY + "=?", new String[] {
				downLoad.id, downLoad.category });

	}

	public DownLoad query(String id, String category) {
		DownLoad downLoad = null;
		Cursor cursor = query(null, DownloadsDBInfo.ID + "= ? AND "
				+ DownloadsDBInfo.CATEGORY + "=?",
				new String[] { id, category }, null);
		if (cursor.moveToFirst()) {
			downLoad = DownLoad.fromCursor(cursor);
		}
		cursor.close();
		return downLoad;
	}

	public ArrayList<DownLoad> list() {
		Cursor cursor = query(null, null, null, DownloadsDBInfo._ID + " ASC");
		ArrayList<DownLoad> downLoads = new ArrayList<DownLoad>();
		DownLoad downLoad = null;
		while (cursor.moveToNext()) {
			downLoad = DownLoad.fromCursor(cursor);
			downLoads.add(downLoad);
		}
		cursor.close();
		return downLoads;
	}

	public void bulkInsert(List<DownLoad> downLoads) {
		ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
		for (DownLoad downLoad : downLoads) {
			ContentValues values = getContentValues(downLoad);
			contentValues.add(values);
		}
		ContentValues[] valueArray = new ContentValues[contentValues.size()];
		bulkInsert(contentValues.toArray(valueArray));
	}

	public void Insert(DownLoad downLoad) {
		ContentValues values = getContentValues(downLoad);
		insert(values);
	}

	public int deleteAll() {
		synchronized (DataProvider.DBLock) {
			DBHelper mDBHelper = DataProvider.getDBHelper();
			SQLiteDatabase db = mDBHelper.getWritableDatabase();
			int row = db.delete(DownloadsDBInfo.TABLE_NAME, null, null);
			return row;
		}
	}

	public int delete(DownLoad downLoad) {
		synchronized (DataProvider.DBLock) {
			DBHelper mDBHelper = DataProvider.getDBHelper();
			SQLiteDatabase db = mDBHelper.getWritableDatabase();
			int row = db.delete(DownloadsDBInfo.TABLE_NAME, DownloadsDBInfo.ID
					+ "= ? AND " + DownloadsDBInfo.CATEGORY + "=?",
					new String[] { downLoad.id, downLoad.category });
			return row;
		}
	}

	public CursorLoader getCursorLoader() {
		return new CursorLoader(getContext(), getContentUri(), null, null,
				null, DownloadsDBInfo._ID + " ASC");
	}

	public static final class DownloadsDBInfo implements BaseColumns {
		private DownloadsDBInfo() {
		}

		public static final String TABLE_NAME = "downloads";

		public static final String ID = "id";

		public static final String ARTICLENAME = "articlename";

		public static final String CATEGORY = "category";

		public static final String CONTENT = "content";

		public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
				.addColumn(ID, Column.DataType.INTEGER)
				.addColumn(ARTICLENAME, Column.DataType.TEXT)
				.addColumn(CATEGORY, Column.DataType.INTEGER)
				.addColumn(CONTENT, Column.DataType.TEXT);
	}
}
