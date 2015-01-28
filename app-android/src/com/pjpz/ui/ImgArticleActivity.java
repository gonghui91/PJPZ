package com.pjpz.ui;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pjpz.R;
import com.pjpz.data.Constants;
import com.pjpz.data.GsonRequest;
import com.pjpz.model.Catalog;
import com.pjpz.model.Category;
import com.pjpz.model.ImageArticle;
import com.pjpz.model.OptRequestData;
import com.pjpz.utils.BitmapUtils;
import com.pjpz.utils.DialogUtil;
import com.pjpz.utils.IntentUtils;
import com.pjpz.view.CustomProgressDialog;
import com.pjpz.view.HackyViewPager;
import com.pjpz.view.ProgressWheel;
import com.pjpz.widget.SharePopupWindow;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class ImgArticleActivity extends BaseActivity {
	private ViewPager viewPager;
	private Context context;
	private ImageView btn_share, btn_comment, btn_collect, btn_praise;
	private TextView tvCount;
	private int position1;
	private View view;
	private Category category;
	private CustomProgressDialog progressDialog;
	private IWXAPI iwxapi;
	private String articleName;
	private String shareUrl;
	private List<Catalog> catalogs;
	private Catalog catalog;
	private int position;
	private ImageView btn_forward, btn_back;
	private Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = ImgArticleActivity.this;
		view = LayoutInflater.from(context).inflate(R.layout.layout_imgarticle,
				null);
		setContentView(view);
		initView();
		loadData();
	}

	private void initView() {
		bundle = getIntent().getExtras();
		articleName = bundle.getString("articleName");
		actionBar.setTitle(articleName);
		tvCount = (TextView) view.findViewById(R.id.tv_count);
		context = ImgArticleActivity.this;
		regToWx();
		viewPager = new HackyViewPager(context);
		RelativeLayout layout_img = (RelativeLayout) view
				.findViewById(R.id.layout_img);
		layout_img.addView(viewPager);
		btn_praise = (ImageView) view.findViewById(R.id.btn_praise);
		btn_share = (ImageView) view.findViewById(R.id.btn_share);
		btn_comment = (ImageView) view.findViewById(R.id.btn_comment);
		btn_collect = (ImageView) view.findViewById(R.id.btn_collect);
		btn_forward = (ImageView) view.findViewById(R.id.btn_forward);
		btn_back = (ImageView) view.findViewById(R.id.btn_back);
		btn_praise.setOnClickListener(clickListener);
		btn_share.setOnClickListener(clickListener);
		btn_comment.setOnClickListener(clickListener);
		btn_collect.setOnClickListener(clickListener);
		btn_forward.setOnClickListener(clickListener);
		btn_back.setOnClickListener(clickListener);
		String catalogStr = bundle.getString("catalog");
		catalogs = new Gson().fromJson(catalogStr,
				new TypeToken<List<Catalog>>() {
				}.getType());
		position = bundle.getInt("position");
		catalog = catalogs.get(position);
		changeBtnStatus();
		category = Category.valueOf(bundle.getString("category"));
		progressDialog = new CustomProgressDialog(context);
	}

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
	}

	private void regToWx() {
		iwxapi = WXAPIFactory.createWXAPI(context, Constants.WX_APP_ID, true);
		iwxapi.registerApp(Constants.WX_APP_ID);
	}

	private void sendWxReq(String url, String title, int which) {
		WXWebpageObject webpageObject = new WXWebpageObject();
		webpageObject.webpageUrl = url;
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = webpageObject;
		switch (which) {
		case SendMessageToWX.Req.WXSceneSession:
			msg.description = title;
			break;

		case SendMessageToWX.Req.WXSceneTimeline:
			msg.title = title;
			break;
		}
		msg.thumbData = BitmapUtils.Bitmap2Bytes(BitmapUtils
				.drawableToBitmap(getResources().getDrawable(
						R.drawable.ic_launcher)));
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.scene = which;
		req.message = msg;
		iwxapi.sendReq(req);
	}

	private OnClickListener clickListener = new OnClickListener() {

		private SharePopupWindow sharePopup;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				catalog = catalogs.get(--position);
				actionBar.setTitle(catalog.articleName);
				changeBtnStatus();
				loadData();
				break;
			case R.id.btn_forward:
				catalog = catalogs.get(++position);
				actionBar.setTitle(catalog.articleName);
				changeBtnStatus();
				loadData();
				break;
			case R.id.btn_praise:
				opt("praise");
				break;
			case R.id.btn_share:
				onShare(true);
				sharePopup = new SharePopupWindow(context, clickListener);
				sharePopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
				sharePopup.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss() {
						onShare(false);
					}
				});
				break;
			case R.id.btn_comment:
				Bundle bundle = new Bundle();
				bundle.putString("articleId", catalog.articleId);
				bundle.putString("category", category.name());
				IntentUtils.startIntent(context, CommentActivity.class, bundle);
				break;
			case R.id.btn_collect:
				opt("collect");
				break;
			case R.id.iv_share_wechat:
				sendWxReq(shareUrl, articleName,
						SendMessageToWX.Req.WXSceneSession);
//				opt("share");
				if (sharePopup != null) {
					sharePopup.dismiss();
				}
				break;
			case R.id.iv_share_wechatm:
				sendWxReq(shareUrl, articleName,
						SendMessageToWX.Req.WXSceneTimeline);
//				opt("share");
				if (sharePopup != null) {
					sharePopup.dismiss();
				}
				break;
			}
		}
	};

	public void onShare(boolean isShareOpen) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		if (isShareOpen) {
			lp.alpha = 0.3f;
			getWindow().setAttributes(lp);
		} else {
			lp.alpha = 1f;
			getWindow().setAttributes(lp);
		}
	}

	private void opt(final String opt) {

		OptRequestData requestData = new OptRequestData();
		requestData.commandName = "articleopt";
		requestData.commandType = opt;
		requestData.commandDevice = Constants.ANDROID;
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
							}
							if (opt.equals("collect")) {
								btn_collect.setClickable(false);
								btn_collect.setImageDrawable(context.getResources().getDrawable(R.drawable.article_favorite_pressed));
							}
						}
					}
				}, errorListener());
		executeRequest(gsonRequest);

	}

	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			tvCount.setText((position + 1) + "/"
					+ viewPager.getAdapter().getCount());
		}

	};

	class mPagerAdapter extends PagerAdapter {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).considerExifParams(true).build();
		private LayoutInflater inflater;
		private List<String> imageUrls;
		private Resources mResource;
		private Drawable mDefaultImageDrawable;

		public mPagerAdapter(List<String> imageUrls) {
			inflater = LayoutInflater.from(context);
			this.imageUrls = imageUrls;
			mResource = context.getResources();
		}

		@Override
		public int getCount() {
			return imageUrls.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View view = inflater.inflate(R.layout.layout_imageview, null);
			PhotoView photoView = (PhotoView) view.findViewById(R.id.photoView);
			mDefaultImageDrawable = new ColorDrawable(
					mResource.getColor(Constants.COLORS[position
							% Constants.COLORS.length]));
			photoView.setImageDrawable(mDefaultImageDrawable);
			final ProgressWheel progressWheel = (ProgressWheel) view
					.findViewById(R.id.progressWheel);
			ImageLoader.getInstance().displayImage(imageUrls.get(position),
					photoView, options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							progressWheel.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							progressWheel.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							progressWheel.setProgress(360 * current / total);
						}
					});
			container.addView(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			((ViewPager) container).removeView(view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	private void loadData() {
		ImageArticle.ImageArticleRequestData requestData = new ImageArticle.ImageArticleRequestData();
		requestData.commandName = "article";
		requestData.commandType = category.name();
		requestData.commandDevice = Constants.ANDROID;
		ImageArticle.RequestItem requestItem = new ImageArticle.RequestItem();
		requestItem.articleId = catalog.articleId;
		ArrayList<ImageArticle.RequestItem> requestItems = new ArrayList<ImageArticle.RequestItem>();
		requestItems.add(requestItem);
		requestData.commandParam = requestItems;
		GsonRequest<ImageArticle.ImageArticleRequestData> gsonRequest = new GsonRequest<ImageArticle.ImageArticleRequestData>(
				requestData.toJson(),
				ImageArticle.ImageArticleRequestData.class, responseListener(),
				errorListener());
		progressDialog.show();
		executeRequest(gsonRequest);
	}

	private Response.Listener<ImageArticle.ImageArticleRequestData> responseListener() {
		return new Response.Listener<ImageArticle.ImageArticleRequestData>() {
			@Override
			public void onResponse(
					final ImageArticle.ImageArticleRequestData response) {
				progressDialog.dismiss();
				if (response.commandStatus.equals(Constants.TRUE)) {
					ImageArticle article = response.commandData;
					articleName = article.articleName;
					shareUrl = article.shareUrl;
					List<String> imageurls = article.body.imageurl;
					if (imageurls.size() > 0) {
						tvCount.setText((position1 + 1) + "/"
								+ article.body.imageurl.size());
						viewPager.setOnPageChangeListener(onPageChangeListener);
						viewPager.setAdapter(new mPagerAdapter(
								article.body.imageurl));
						viewPager.setCurrentItem(position1);
					} else {
						tvCount.setVisibility(View.GONE);
					}
				} else {
					DialogUtil.showLoadFailDialog(context);
				}
			}
		};
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			ImgArticleActivity.this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
