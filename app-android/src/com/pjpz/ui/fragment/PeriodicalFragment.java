package com.pjpz.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.pjpz.R;
import com.pjpz.dao.DownloadsDataHelper;
import com.pjpz.dao.PeriodicalsDataHelper;
import com.pjpz.data.Constants;
import com.pjpz.data.GsonRequest;
import com.pjpz.model.Category;
import com.pjpz.model.DownLoad;
import com.pjpz.model.Periodical;
import com.pjpz.ui.JuImageActivity;
import com.pjpz.ui.adapter.PeriodicalAdapter;
import com.pjpz.utils.IntentUtils;
import com.pjpz.utils.TaskUtils;
import com.pjpz.utils.ToastUtils;
import com.pjpz.view.CustomProgressDialog;

public class PeriodicalFragment extends BaseFragmentV4 {
	public static final String EXTRA_CATEGORY = "extra_category";
	private View txtLayout;
	private Context context;
	private PeriodicalAdapter mAdapter;
	private ListView listView;
	private Category mCategory;
	private PeriodicalsDataHelper mDataHelper;
	private DownloadsDataHelper mDownloadHelper;
	private String mPage = "0";
	private ArrayList<Periodical> periodicals;
	private CustomProgressDialog progressDialog;

	public static PeriodicalFragment newInstance(Category category) {
		PeriodicalFragment fragment = new PeriodicalFragment();
		Bundle bundle = new Bundle();
		bundle.putString(EXTRA_CATEGORY, category.name());
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		txtLayout = inflater.inflate(R.layout.layout_showtxt, container, false);
		context = getActivity();
		// getPeridicals();
		parseArgument();
		progressDialog = new CustomProgressDialog(context);
		mDataHelper = new PeriodicalsDataHelper(context, mCategory);
		mDownloadHelper = new DownloadsDataHelper(context);
		// mAdapter = new PeriodicalAdapter(context, inflater);
		periodicals = mDataHelper.list();
		mAdapter = new PeriodicalAdapter(context, periodicals, inflater);
		listView = (ListView) txtLayout.findViewById(R.id.txtList);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Periodical periodical = (Periodical) listView
						.getItemAtPosition(position);
				Bundle bundle = new Bundle();
				bundle.putString(EXTRA_CATEGORY, mCategory.name());
				bundle.putString("periodicalId", periodical.periodicalId);
				IntentUtils.startIntent(context, JuImageActivity.class, bundle);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mCategory.ordinal() == 1) {
					final Periodical periodical = (Periodical) listView
							.getItemAtPosition(position);
					new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
							.setTitleText("确定下载本期刊？")
							.setCancelText("取消")
							.setConfirmText("下载")
							.showCancelButton(true)
							.setCancelClickListener(
									new SweetAlertDialog.OnSweetClickListener() {
										@Override
										public void onClick(
												SweetAlertDialog sDialog) {
											sDialog.cancel();
										}
									})
							.setConfirmClickListener(
									new SweetAlertDialog.OnSweetClickListener() {

										@Override
										public void onClick(
												SweetAlertDialog sDialog) {
											DownLoad.DownLoadRequestData requestData = new DownLoad.DownLoadRequestData();
											requestData.commandName = "download";
											requestData.commandType = mCategory
													.name();
											requestData.commandDevice = Constants.ANDROID;
											Map<String, String> map = new HashMap<String, String>();
											map.put("periodicalId",
													periodical.periodicalId);
											requestData.commandParam = map;
											GsonRequest<DownLoad.DownLoadRequestData> gsonRequest = new GsonRequest<DownLoad.DownLoadRequestData>(
													requestData.toJson(),
													DownLoad.DownLoadRequestData.class,
													dresponseListener(periodical),
													errorListener());
											progressDialog.show();
											executeRequest(gsonRequest);
											sDialog.cancel();
										}
									}).show();
				}
				return true;
			}
		});
		loadFirst();
		return txtLayout;
	}

	private void loadFirst() {
		mPage = "0";
		loadData(mPage);
		// loadData();
	}

	private void loadNext() {
		loadData(mPage);
		// loadData();
	}

	public void setCategory(Category category) {
		if (mCategory == category) {
			return;
		}
		mCategory = category;
	}

	private void parseArgument() {
		Bundle bundle = getArguments();
		mCategory = Category.valueOf(bundle.getString(EXTRA_CATEGORY));
	}

	private ArrayList<Periodical> getPeridicals() {
		ArrayList<Periodical> periodicals = new ArrayList<Periodical>();
		Periodical periodical = null;
		for (int i = 0; i < 5; i++) {
			periodical = new Periodical();
			periodical.periodicalId = "" + i;
			periodical.name = "2012 壬辰年 秋季版 " + (i + 1) + "-"
					+ mCategory.getDisplayName();
			periodical.summary = "朱清时：中国科学院士、南方科技大学校长";
			periodical.syncTime = "更新日期 ： 2014-12-10";
			periodical.logoUrl = Constants.imageUrls[i];
			periodicals.add(periodical);
		}
		return periodicals;
	}

	private void loadData(String next) {
		Periodical.PeriodicalRequestData requestData = new Periodical.PeriodicalRequestData();
		requestData.commandName = "periodical";
		requestData.commandType = mCategory.name();
		requestData.commandDevice = Constants.ANDROID;
		Periodical.RequestItem requestItem = new Periodical.RequestItem();
		requestItem.page = "0";
		requestItem.pageSize = "10";
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
								List<Periodical> periodicals = response.commandData;
								if (periodicals != null) {
									mDataHelper.bulkInsert(periodicals);
								}
								return null;
							}

							@Override
							protected void onPostExecute(Object o) {
								periodicals = mDataHelper.list();
								mAdapter.set(periodicals);
							}
						});
			}
		};
	}

	private Response.Listener<DownLoad.DownLoadRequestData> dresponseListener(
			final Periodical periodical) {
		return new Response.Listener<DownLoad.DownLoadRequestData>() {
			@Override
			public void onResponse(final DownLoad.DownLoadRequestData response) {
				TaskUtils
						.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
							@Override
							protected Object doInBackground(Object... params) {
								if (response.commandStatus
										.equals(Constants.TRUE)
										&& response.commandData != null) {
//									ArrayList<String> imageUrls = new ArrayList<String>();
//									if (response.commandData.cover != null) {
//										imageUrls
//												.add(response.commandData.cover);
//									}
//									if (response.commandData.juanshoutu != null) {
//										for (String string : response.commandData.juanshoutu) {
//											imageUrls.add(string);
//										}
//									}
//									if (response.commandData.images != null) {
//										for (String string : response.commandData.images) {
//											imageUrls.add(string);
//										}
//									}
									if (response.commandData.images != null) {
//										for (String string : response.commandData.images) {
//											imageUrls.add(string);
//										}
//									}
//									if (imageUrls.size() > 0) {

										DownLoad mdownLoad = mDownloadHelper
												.query(periodical.periodicalId,
														mCategory.name());
										if (mdownLoad != null) {
											mDownloadHelper.update(mdownLoad);
										} else {
											DownLoad downLoad = new DownLoad();
											downLoad.id = periodical.periodicalId;
											downLoad.articleName = periodical.name;
											downLoad.category = mCategory
													.name();
											downLoad.content = new Gson()
													.toJson(response.commandData.images);
											mDownloadHelper.Insert(downLoad);
										}
										TaskUtils.getPhotoFromServer(
												periodical.periodicalId,
												response.commandData.images);
										return true;
									}
								}

								return null;
							}

							@Override
							protected void onPostExecute(Object o) {
								if (o != null) {
									progressDialog.dismiss();
									ToastUtils.showShort("期刊已保存");
								}
							}
						});
			}
		};
	}

}
