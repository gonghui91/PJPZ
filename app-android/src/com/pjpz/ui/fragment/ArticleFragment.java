package com.pjpz.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pjpz.R;
import com.pjpz.data.Constants;
import com.pjpz.data.GsonRequest;
import com.pjpz.model.Catalog;
import com.pjpz.model.Category;
import com.pjpz.model.OptRequestData;
import com.pjpz.model.TextArticle;
import com.pjpz.utils.OnSelectListener;
import com.pjpz.utils.ToastUtils;
import com.pjpz.view.CustomProgressDialog;
import com.pjpz.view.ProgressWebView;
import com.pjpz.widget.SharePopupWindow;

public class ArticleFragment extends BaseFragment {
	private ImageView btn_forward, btn_back;
	private ImageView btn_share, btn_comment, btn_collect, btn_praise;
	private Context context;
	private View view;
	private OnSelectListener onSelectListener;
	private Category category;
	private CustomProgressDialog progressDialog;
	private Animation rotate_big, rotate_small;
	private String articleName;
	private String shareUrl;
	private List<Catalog> catalogs;
	private Catalog catalog;
	private int position;
	/** 微博分享的接口实例 */
	 // 创建微博分享接口实例
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		view = inflater.inflate(R.layout.layout_article, container, false);
		btn_forward = (ImageView) view.findViewById(R.id.btn_forward);
		btn_back = (ImageView) view.findViewById(R.id.btn_back);
		btn_praise = (ImageView) view.findViewById(R.id.btn_praise);
		btn_share = (ImageView) view.findViewById(R.id.btn_share);
		btn_comment = (ImageView) view.findViewById(R.id.btn_comment);
		btn_collect = (ImageView) view.findViewById(R.id.btn_collect);
		btn_forward.setOnClickListener(clickListener);
		btn_back.setOnClickListener(clickListener);
		btn_praise.setOnClickListener(clickListener);
		btn_share.setOnClickListener(clickListener);
		btn_comment.setOnClickListener(clickListener);
		btn_collect.setOnClickListener(clickListener);
		Bundle bundle = getArguments();
		String catalogStr = bundle.getString("catalog");
		catalogs = new Gson().fromJson(catalogStr,
				new TypeToken<List<Catalog>>() {
				}.getType());
		position = bundle.getInt("position");
		catalog = catalogs.get(position);
		changeBtnStatus();
		category = Category.valueOf(bundle.getString("category"));
		progressDialog = new CustomProgressDialog(context);
		// 初始化动画资源
		rotate_big = AnimationUtils.loadAnimation(context,
				R.anim.progress_refresh);
		rotate_small = AnimationUtils.loadAnimation(context,
				R.anim.progress_refresh_small);
		LinearInterpolator polator = new LinearInterpolator();// 匀速旋转
		rotate_big.setInterpolator(polator);
		rotate_small.setInterpolator(polator);
		// ~~~ 绑定控件
		webview = (ProgressWebView) view.findViewById(R.id.webView1);
		loadData();
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			onSelectListener = (OnSelectListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnArticleSelectedListener");
		}
	}

	


	private OnClickListener clickListener = new OnClickListener() {

		private SharePopupWindow sharePopup;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_praise:
				opt("praise");
				break;
			case R.id.btn_back:
				catalog = catalogs.get(--position);
				onSelectListener.setTitle(catalog.articleName);
				changeBtnStatus();
				loadData();
				break;
			case R.id.btn_forward:
				catalog = catalogs.get(++position);
				onSelectListener.setTitle(catalog.articleName);
				changeBtnStatus();
				loadData();
				break;
			case R.id.btn_share:
				opt("share");
				onSelectListener.onShare(true);
				sharePopup = new SharePopupWindow(context, clickListener);
				sharePopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
				sharePopup.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss() {
						onSelectListener.onShare(false);
					}
				});
				break;
			case R.id.btn_comment:
				onSelectListener.onComment();
				break;
			case R.id.btn_collect:
				opt("collect");
				break;
			case R.id.iv_share_sina:
				onSelectListener.share(Constants.WEIBO, articleName, shareUrl);
				if (sharePopup != null) {
					sharePopup.dismiss();
				}
				break;
			case R.id.iv_share_wechat:
				onSelectListener.share(Constants.WECHAT, articleName, shareUrl);
				if (sharePopup != null) {
					sharePopup.dismiss();
				}
				break;
			case R.id.iv_share_wechatm:
				onSelectListener.share(Constants.WECHAT_MOMENT, articleName, shareUrl);
				if (sharePopup != null) {
					sharePopup.dismiss();
				}
				break;
			}
		}

	};

	private ProgressWebView webview;

	private void changeBtnStatus() {
		if (catalogs != null && catalogs.size() > 0) {
			if (position == 0) {
				btn_back.setClickable(false);
			}
			if (position > 0) {
				btn_back.setClickable(true);
			}
			if (position == (catalogs.size() - 1)) {
				btn_forward.setClickable(false);
			}
			if (position < (catalogs.size() - 1)) {
				btn_forward.setClickable(true);
			}
		} else {
			btn_back.setClickable(false);
			btn_forward.setClickable(false);
		}
		btn_praise.setClickable(true);
		btn_praise.setImageDrawable(context.getResources().getDrawable(R.drawable.article_like_normal));
		btn_collect.setClickable(true);
		btn_collect.setImageDrawable(context.getResources().getDrawable(R.drawable.article_favorite_normal));
	
	}

	private void opt(final String opt) {
		OptRequestData requestData = new OptRequestData();
		requestData.commandName = "articleopt";
		requestData.commandType = opt;
		requestData.commandDevice = "0";
		OptRequestData.RequestItem requestItem = new OptRequestData.RequestItem();
		requestItem.articleId = catalog.articleId;
		requestItem.articleType = category.name();
		ArrayList<OptRequestData.RequestItem> requestItems = new ArrayList<OptRequestData.RequestItem>();
		requestItems.add(requestItem);
		requestData.commandParam = requestItems;
		GsonRequest<OptRequestData> gsonRequest = new GsonRequest<OptRequestData>(
				requestData.toJson(), OptRequestData.class,
				new Response.Listener<OptRequestData>() {
					@Override
					public void onResponse(OptRequestData response) {
						if (response.commandStatus.equals(Constants.TRUE)) {
							if (opt.equals("praise")) {
								btn_praise.setClickable(false);
								btn_praise.setImageDrawable(context.getResources().getDrawable(R.drawable.article_like_pressed));
								ToastUtils.showShort("已赞");
							}
							if (opt.equals("collect")) {
								btn_collect.setClickable(false);
								btn_collect.setImageDrawable(context.getResources().getDrawable(R.drawable.article_favorite_pressed));
								ToastUtils.showShort("已收藏");
							}
						}
					}
				}, errorListener());
		executeRequest(gsonRequest);

	}

	private void loadData() {
		TextArticle.TextArticleRequestData requestData = new TextArticle.TextArticleRequestData();
		requestData.commandName = "article";
		requestData.commandType = category.name();
		requestData.commandDevice = Constants.ANDROID;
		TextArticle.RequestItem requestItem = new TextArticle.RequestItem();
		requestItem.articleId = catalog.articleId;
		ArrayList<TextArticle.RequestItem> requestItems = new ArrayList<TextArticle.RequestItem>();
		requestItems.add(requestItem);
		requestData.commandParam = requestItems;
		GsonRequest<TextArticle.TextArticleRequestData> gsonRequest = new GsonRequest<TextArticle.TextArticleRequestData>(
				requestData.toJson(), TextArticle.TextArticleRequestData.class,
				responseListener(), errorListener());
		progressDialog.show();
		executeRequest(gsonRequest);
	}

	private Response.Listener<TextArticle.TextArticleRequestData> responseListener() {
		return new Response.Listener<TextArticle.TextArticleRequestData>() {

			@Override
			public void onResponse(
					final TextArticle.TextArticleRequestData response) {
				progressDialog.dismiss();
				TextArticle articles = response.commandData;
				articleName = articles.articleName;
				shareUrl = articles.shareUrl;
				Web_link(articles.shareUrl);
			}
		};
	}

	private void Web_link(String url) {

		webview = (ProgressWebView) view.findViewById(R.id.webView1);
		webview.setDownloadListener(new DownloadListener() {

			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				if (url != null && url.startsWith("http://"))
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			}
		});
		webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

		// 设置支持javascript脚本
		WebSettings webSettings = webview.getSettings();
		// // 设置可以访问的文件
		// webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webview.loadUrl(url);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setSupportZoom(false);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setDomStorageEnabled(true);
		// 设置webviewclient
		MyWebViewClient mWebViewClient = new MyWebViewClient();
		webview.setWebViewClient(mWebViewClient);
	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);

		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

	}

}
