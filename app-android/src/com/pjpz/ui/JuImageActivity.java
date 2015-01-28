package com.pjpz.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoView;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pjpz.R;
import com.pjpz.data.Constants;
import com.pjpz.data.GsonRequest;
import com.pjpz.model.Catalog;
import com.pjpz.model.Catalog.CatalogMenu;
import com.pjpz.model.Category;
import com.pjpz.ui.fragment.PeriodicalFragment;
import com.pjpz.utils.DialogUtil;
import com.pjpz.utils.IntentUtils;
import com.pjpz.view.CustomProgressDialog;
import com.pjpz.view.HackyViewPager;
import com.pjpz.view.ProgressWheel;

public class JuImageActivity extends BaseActivity {
	private TextView tv_count;
	private RelativeLayout layout_img;
	private HackyViewPager viewPager;
	private CustomProgressDialog progressDialog;
	private Category category;
	private ImageView btn_catalog;
	private CatalogMenu catalogMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ju_image);
		initView();
		loadData();
	}

	private void initView() {
		actionBar.setTitle("卷首");
		tv_count = (TextView) findViewById(R.id.tv_count);
		layout_img = (RelativeLayout) findViewById(R.id.layout_img);
		viewPager = new HackyViewPager(this);
		layout_img.addView(viewPager);
		progressDialog = new CustomProgressDialog(this);
		btn_catalog = (ImageView) findViewById(R.id.btn_catalog);
		btn_catalog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showCatalog();
			}
		});
	}

	private void loadData() {
		Bundle bundle = getIntent().getExtras();
		String periodicalId = bundle.getString("periodicalId");
		category = Category.valueOf(bundle
				.getString(PeriodicalFragment.EXTRA_CATEGORY));
		Catalog.CatalogRequestData requestData = new Catalog.CatalogRequestData();
		requestData.commandName = "catalog";
		requestData.commandType = category.name();
		requestData.commandDevice = Constants.ANDROID;
		Catalog.RequestItem requestItem = new Catalog.RequestItem();
		requestItem.periodicalId = periodicalId;
		ArrayList<Catalog.RequestItem> requestItems = new ArrayList<Catalog.RequestItem>();
		requestItems.add(requestItem);
		requestData.commandParam = requestItems;
		GsonRequest<Catalog.CatalogRequestData> gsonRequest = new GsonRequest<Catalog.CatalogRequestData>(
				requestData.toJson(), Catalog.CatalogRequestData.class,
				responseListener(), errorListener());
		progressDialog.show();
		executeRequest(gsonRequest);
	}

	private Response.Listener<Catalog.CatalogRequestData> responseListener() {
		return new Response.Listener<Catalog.CatalogRequestData>() {
			@Override
			public void onResponse(final Catalog.CatalogRequestData response) {
				catalogMenu = response.commandData;
				System.out.println(catalogMenu);
				progressDialog.dismiss();
				if (response.commandStatus.equals(Constants.ERROR)) {
					DialogUtil.showLoadFailDialog(JuImageActivity.this);
				}
				if (catalogMenu != null) {
					viewPager.setOnPageChangeListener(onPageChangeListener);
					List<Map<String, String>> frontcover = catalogMenu.frontcover;
					if (frontcover != null && frontcover.size() > 0) {
						// 初始化图片异步加载类
						viewPager.setAdapter(new mPagerAdapter(
								catalogMenu.frontcover));
						tv_count.setText("1/"
								+ frontcover.size());
						if (frontcover.size() == 1) {
							btn_catalog.setVisibility(View.VISIBLE);
						}
					} else {
						showCatalog();
					}
				}
			}

		};
	}

	private void showCatalog() {
		Bundle bundle = new Bundle();
		bundle.putString("catalog", catalogMenu.toJson());
		bundle.putString("category", category.name());
		IntentUtils.startIntent(JuImageActivity.this, CatalogActivity.class,
				bundle);
		JuImageActivity.this.finish();
	}

	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				progressDialog.cancel();
				DialogUtil.showLoadFailDialog(JuImageActivity.this);
			}
		};
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
			tv_count.setText((position + 1) + "/"
					+ viewPager.getAdapter().getCount());
			int count = viewPager.getAdapter().getCount();
			if (position == count - 1) {
				btn_catalog.setVisibility(View.VISIBLE);
			} else {
				btn_catalog.setVisibility(View.GONE);
			}
		}

	};

	class mPagerAdapter extends PagerAdapter {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).considerExifParams(true).build();
		private List<Map<String, String>> frontcover;
		private Resources mResource;
		private Drawable mDefaultImageDrawable;
		private LayoutInflater mInflater;

		public mPagerAdapter(List<Map<String, String>> frontcover) {
			this.frontcover = frontcover;
			mResource = JuImageActivity.this.getResources();
			mInflater = LayoutInflater.from(JuImageActivity.this);
		}

		@Override
		public int getCount() {
			return frontcover.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View view = mInflater.inflate(R.layout.layout_imageview, null);
			PhotoView photoView = (PhotoView) view.findViewById(R.id.photoView);
			mDefaultImageDrawable = new ColorDrawable(
					mResource.getColor(Constants.COLORS[position
							% Constants.COLORS.length]));
			photoView.setImageDrawable(mDefaultImageDrawable);
			final ProgressWheel progressWheel = (ProgressWheel) view
					.findViewById(R.id.progressWheel);
			String url = "";
			if (frontcover.get(position).containsKey("cover")) {
				url = frontcover.get(position).get("cover");
			} else if (frontcover.get(position).containsKey("imageurl")) {
				url = frontcover.get(position).get("imageurl");
			}
			ImageLoader.getInstance().displayImage(url, photoView, options,
					new SimpleImageLoadingListener() {
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			JuImageActivity.this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
