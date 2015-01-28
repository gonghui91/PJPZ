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
import com.pjpz.model.Category;
import com.pjpz.model.Periodical;

/**
 * Created by storm on 14-4-8.
 */
public class PeriodicalsDataHelper extends BaseDataHelper {
	private Category mCategory;

	public PeriodicalsDataHelper(Context context, Category category) {
		super(context);
		mCategory = category;
	}

	@Override
	protected Uri getContentUri() {
		return DataProvider.PERIODICALS_CONTENT_URI;
	}

	private ContentValues getContentValues(Periodical periodical) {
		ContentValues values = new ContentValues();
		values.put(PeriodicalsDBInfo.ID, periodical.periodicalId);
		values.put(PeriodicalsDBInfo.CATEGORY, mCategory.ordinal());
		values.put(PeriodicalsDBInfo.JSON, periodical.toJson());
		return values;
	}

	public Periodical query(long id) {
		Periodical periodical = null;
		Cursor cursor = query(
				null,
				PeriodicalsDBInfo.CATEGORY + "=?" + " AND "
						+ PeriodicalsDBInfo.ID + "= ?",
				new String[] { String.valueOf(mCategory.ordinal()),
						String.valueOf(id) }, null);
		if (cursor.moveToFirst()) {
			periodical = Periodical.fromCursor(mCategory,cursor);
		}
		cursor.close();
		return periodical;
	}

	public ArrayList<Periodical> list() {
		Cursor cursor = query(null, PeriodicalsDBInfo.CATEGORY + "=?",
				new String[] { String.valueOf(mCategory.ordinal()) }, null);
		ArrayList<Periodical> periodicals = new ArrayList<Periodical>();
		Periodical periodical = null;
		while (cursor.moveToNext()) {
			periodical = Periodical.fromCursor(mCategory,cursor);
			periodicals.add(periodical);
		}
		cursor.close();
		return periodicals;
	}

	public void bulkInsert(List<Periodical> periodicals) {
		ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
		for (Periodical periodical : periodicals) {
			ContentValues values = getContentValues(periodical);
			contentValues.add(values);
		}
		ContentValues[] valueArray = new ContentValues[contentValues.size()];
		bulkInsert(contentValues.toArray(valueArray));
	}

	public int deleteAll() {
		synchronized (DataProvider.DBLock) {
			DBHelper mDBHelper = DataProvider.getDBHelper();
			SQLiteDatabase db = mDBHelper.getWritableDatabase();
			int row = db.delete(PeriodicalsDBInfo.TABLE_NAME,
					PeriodicalsDBInfo.CATEGORY + "=?",
					new String[] { String.valueOf(mCategory.ordinal()) });
			return row;
		}
	}

	public CursorLoader getCursorLoader() {
		return new CursorLoader(getContext(), getContentUri(), null,
				PeriodicalsDBInfo.CATEGORY + "=?",
				new String[] { String.valueOf(mCategory.ordinal()) },
				PeriodicalsDBInfo._ID + " ASC");
	}

	public static final class PeriodicalsDBInfo implements BaseColumns {
		private PeriodicalsDBInfo() {
		}

		public static final String TABLE_NAME = "periodicals";

		public static final String ID = "id";

		public static final String CATEGORY = "category";

		public static final String JSON = "json";

		public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
				.addColumn(ID, Column.DataType.INTEGER)
				.addColumn(CATEGORY, Column.DataType.INTEGER)
				.addColumn(JSON, Column.DataType.TEXT);
	}
}
