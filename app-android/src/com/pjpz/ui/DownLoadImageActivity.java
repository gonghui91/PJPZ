package com.pjpz.ui;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pjpz.R;
import com.pjpz.data.Constants;
import com.pjpz.model.DownLoad;
import com.pjpz.utils.ToastUtils;
import com.pjpz.view.HackyViewPager;
import com.pjpz.view.ProgressWheel;

public class DownLoadImageActivity extends BaseActivity {
	private TextView tv_count;
	private RelativeLayout layout_img;
	private HackyViewPager viewPager;
	private List<String> imageUrls;
	private int STATUS = 1;
	private String imageDir;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down_load_image);
		initView();
	}

	private void initView() {
		DownLoad downLoad = (DownLoad) getIntent().getSerializableExtra(
				"downLoad");
		imageDir = Constants.DEFAULT_IMAGE_URL + downLoad.id + "/";
		if (getImageFromLocal()) {
			STATUS = 0;
		} else {
			ToastUtils.showShort("未找到本地图片，将加载网络图片！");
			imageUrls = new Gson().fromJson(downLoad.content,
					new TypeToken<List<String>>() {
					}.getType());
			STATUS = 1;
		}
		actionBar.setTitle(downLoad.articleName);
		tv_count = (TextView) findViewById(R.id.tv_count);
		layout_img = (RelativeLayout) findViewById(R.id.layout_img);
		viewPager = new HackyViewPager(this);
		layout_img.addView(viewPager);
		tv_count.setText("1/" + imageUrls.size());
		viewPager.setAdapter(new mPagerAdapter(imageUrls));
		viewPager.setOnPageChangeListener(onPageChangeListener);
	}

	private boolean getImageFromLocal() {
		File file = new File(imageDir);
		if (file.exists()) {
			String[] list = file.list();
			if (list.length > 0) {
				imageUrls = Arrays.asList(list);
				Collections.sort(imageUrls);
				return true;
			}
		}
		return false;
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
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.down_load_image, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			DownLoadImageActivity.this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class mPagerAdapter extends PagerAdapter {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).considerExifParams(true).build();
		private LayoutInflater inflater;
		private List<String> imageUrls;
		private Resources mResource;
		private Drawable mDefaultImageDrawable;

		public mPagerAdapter(List<String> imageUrls) {
			inflater = LayoutInflater.from(DownLoadImageActivity.this);
			this.imageUrls = imageUrls;
			mResource = DownLoadImageActivity.this.getResources();
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
			String imageUrl = "";
			switch (STATUS) {
			case 0:
				imageUrl = "file://" + imageDir + imageUrls.get(position);
				break;
			case 1:
				imageUrl = imageUrls.get(position);
				break;
			}
			ImageLoader.getInstance().displayImage(imageUrl, photoView,
					options, new SimpleImageLoadingListener() {
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
}
