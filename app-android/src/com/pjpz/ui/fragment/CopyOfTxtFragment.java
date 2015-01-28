package com.pjpz.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.pjpz.R;
import com.pjpz.dao.PeriodicalsDataHelper;
import com.pjpz.data.Constants;
import com.pjpz.data.GsonRequest;
import com.pjpz.model.Category;
import com.pjpz.model.Periodical;
import com.pjpz.ui.adapter.PeriodicalAdapter;
import com.pjpz.utils.TaskUtils;

public class CopyOfTxtFragment extends BaseFragment {
	public static final String EXTRA_CATEGORY = "extra_category";
	private View txtLayout;
	private Context context;
	private PeriodicalAdapter mAdapter;
	private ListView listView;
	private Category mCategory;
	private PeriodicalsDataHelper mDataHelper;
	private String mPage = "0";
	private ArrayList<Periodical> periodicals;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		txtLayout = inflater.inflate(R.layout.layout_showtxt, container, false);
		context = getActivity();
		// getPeridicals();
		setCategory(Category.text);
		parseArgument();
		mDataHelper = new PeriodicalsDataHelper(context, mCategory);
		// mAdapter = new PeriodicalAdapter(context, inflater);
		periodicals = mDataHelper.list();
		mAdapter = new PeriodicalAdapter(context, periodicals, inflater);
		listView = (ListView) txtLayout.findViewById(R.id.txtList);
		listView.setAdapter(mAdapter);
		// loadData();
		loadFirst();
		return txtLayout;
	}

	private void loadFirst() {
		mPage = "0";
		loadData(mPage);
	}

	private void loadNext() {
		loadData(mPage);
	}

	public void setCategory(Category category) {
		if (mCategory == category) {
			return;
		}
		mCategory = category;
	}

	private void parseArgument() {
		// Bundle bundle = getArguments();
		// mCategory = Category.valueOf(bundle.getString(EXTRA_CATEGORY));
		mCategory = Category.valueOf(mCategory.name());
	}

	private ArrayList<Periodical> getPeridicals() {
		ArrayList<Periodical> periodicals = new ArrayList<Periodical>();
		Periodical periodical = null;
		for (int i = 0; i < 5; i++) {
			periodical = new Periodical();
			periodical.name = "2012 壬辰年 秋季版";
			periodical.summary = "朱清时：中国科学院士、南方科技大学校长";
			periodical.syncTime = "更新日期 ： 2014-12-10";
			periodical.logoUrl = "http://img.my.csdn.net/uploads/201309/01/1378037178_9374.jpg";
			periodicals.add(periodical);
		}
		return periodicals;
	}

	private void loadData(String next) {
		Periodical.PeriodicalRequestData requestData = new Periodical.PeriodicalRequestData();
		requestData.commandName = "periodical";
		requestData.commandType = Constants.TEXT;
		requestData.commandDevice = Constants.ANDROID;
		Periodical.RequestItem requestItem = new Periodical.RequestItem();
//		requestItem.begin = "0";
//		requestItem.end = "20";
		ArrayList<Periodical.RequestItem> requestItems = new ArrayList<Periodical.RequestItem>();
		requestItems.add(requestItem);
		requestData.commandParam = requestItems;
		GsonRequest<Periodical.PeriodicalRequestData> gsonRequest = new GsonRequest<Periodical.PeriodicalRequestData>(
				requestData.toJson(), Periodical.PeriodicalRequestData.class,
				responseListener(), errorListener());
		executeRequest(gsonRequest);
	}

	private Response.Listener<Periodical.PeriodicalRequestData> responseListener() {
		final boolean isRefreshFromTop = ("0".equals(mPage));
		return new Response.Listener<Periodical.PeriodicalRequestData>() {
			@Override
			public void onResponse(
					final Periodical.PeriodicalRequestData response) {
				TaskUtils
						.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
							@Override
							protected Object doInBackground(Object... params) {
								if (isRefreshFromTop) {
									mDataHelper.deleteAll();
								}
								// List<Periodical> periodicals =
								// response.commandData;
								List<Periodical> periodicals = getPeridicals();
								mDataHelper.bulkInsert(periodicals);
								return null;
							}

							@Override
							protected void onPostExecute(Object o) {
								super.onPostExecute(o);
							}
						});
			}
		};
	}
	// private Response.Listener<Song.SongRequestData> responseListener() {
	// return new Response.Listener<Song.SongRequestData>() {
	// @Override
	// public void onResponse(final Song.SongRequestData response) {
	// TaskUtils
	// .executeAsyncTask(new AsyncTask<Object, Object, Object>() {
	// @Override
	// protected Object doInBackground(Object... params) {
	// System.out.println(response);
	// ArrayList<Song> feeds = response.song;
	// System.out.println("feeds.size()="
	// + feeds.size());
	// for (Song song : feeds) {
	// System.out.println(song);
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Object o) {
	// super.onPostExecute(o);
	// }
	// });
	// }
	// };
	// }
}
