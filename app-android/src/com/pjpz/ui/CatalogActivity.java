package com.pjpz.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoView;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pjpz.R;
import com.pjpz.dao.DownloadsDataHelper;
import com.pjpz.data.Constants;
import com.pjpz.data.GsonRequest;
import com.pjpz.model.Catalog;
import com.pjpz.model.Catalog.CatalogMenu;
import com.pjpz.model.Category;
import com.pjpz.model.DownLoad;
import com.pjpz.model.TextArticle;
import com.pjpz.model.TextArticle.TextArticleRequestData;
import com.pjpz.ui.adapter.CataLogAdapter;
import com.pjpz.ui.fragment.PeriodicalFragment;
import com.pjpz.utils.DialogUtil;
import com.pjpz.utils.IntentUtils;
import com.pjpz.utils.ToastUtils;
import com.pjpz.view.CustomProgressDialog;
import com.pjpz.view.HackyViewPager;
import com.pjpz.view.ProgressWheel;

public class CatalogActivity extends BaseActivity {
	private ListView cataList;
	private Category category;
	private DownloadsDataHelper mDataHelper;
	private CustomProgressDialog progressDialog;
	private List<Catalog> catalogs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catalog);
		initView();
	}

	private void initView() {
		actionBar.setTitle(R.string.catalog);
		mDataHelper = new DownloadsDataHelper(CatalogActivity.this);
		progressDialog = new CustomProgressDialog(this);
		cataList = (ListView) findViewById(R.id.catalogList);
		Bundle bundle = getIntent().getExtras();
		CatalogMenu catalogMenu = new Gson().fromJson(bundle.get("catalog").toString(), CatalogMenu.class);
		category = Category.valueOf(bundle.getString("category"));
		catalogs = catalogMenu.catalog;
		cataList.setAdapter(new CataLogAdapter(CatalogActivity.this, catalogs, category));
		cataList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("catalog", new Gson().toJson(catalogs));
				bundle.putString("category", category.name());
				bundle.putString("articleName",
						catalogs.get(position).articleName);
				bundle.putString("articleId", catalogs.get(position).articleId);
				bundle.putInt("position", position);
				switch (category.ordinal()) {
				case Constants.Text:
					IntentUtils.startIntent(CatalogActivity.this,
							ArticleActivity.class, bundle);
					break;
				case Constants.Image:
					IntentUtils.startIntent(CatalogActivity.this,
							ImgArticleActivity.class, bundle);
					break;
				}
			}
		});
		cataList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (category.ordinal() == 0) {
					final String articleId = (String) cataList
							.getItemAtPosition(position);
					new SweetAlertDialog(CatalogActivity.this,
							SweetAlertDialog.NORMAL_TYPE)
							.setTitleText("确定下载文章？")
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
											DownLoad downLoad = mDataHelper
													.query(articleId,
															category.name());
											if (downLoad != null) {
												ToastUtils
														.showShort("已经下载过该篇文章");
											} else {
												TextArticle.TextArticleRequestData requestData = new TextArticle.TextArticleRequestData();
												requestData.commandName = "article";
												requestData.commandType = category
														.name();
												requestData.commandDevice = Constants.ANDROID;
												TextArticle.RequestItem requestItem = new TextArticle.RequestItem();
												requestItem.articleId = articleId;
												ArrayList<TextArticle.RequestItem> requestItems = new ArrayList<TextArticle.RequestItem>();
												requestItems.add(requestItem);
												requestData.commandParam = requestItems;
												GsonRequest<TextArticle.TextArticleRequestData> gsonRequest = new GsonRequest<TextArticle.TextArticleRequestData>(
														requestData.toJson(),
														TextArticle.TextArticleRequestData.class,
														new Response.Listener<TextArticle.TextArticleRequestData>() {
															@Override
															public void onResponse(
																	TextArticleRequestData response) {
																if (response.commandStatus
																		.equals("true")) {
																	DownLoad downLoad = new DownLoad();
																	downLoad.id = articleId;
																	downLoad.articleName = response.commandData.articleName;
																	downLoad.category = category
																			.name();
																	downLoad.content = response.commandData.body
																			.get(0).text;
																	mDataHelper
																			.Insert(downLoad);
																	ToastUtils
																			.showShort("文章下载成功");
																}
																progressDialog
																		.dismiss();
															}
														},
														new Response.ErrorListener() {

															@Override
															public void onErrorResponse(
																	VolleyError arg0) {

															}
														});
												progressDialog.show();
												executeRequest(gsonRequest);

											}
											sDialog.cancel();
										}
									}).show();
				}
				return true;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			CatalogActivity.this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
