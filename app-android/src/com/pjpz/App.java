package com.pjpz;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.pjpz.data.Constants;
import com.pjpz.utils.SPUtils;

/**
 * 
 * Create by gonghui on 14-12-15s
 */
public class App extends Application {
	private static Context mContext;
	public static int STATUS = 0;
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		initImageLoader(getApplicationContext());
//		Constant.url = SPUtils.get(mContext, "url", Constant.url).toString();
	}

	public static Context getContext() {
		return mContext;
	}

	// 初始化ImageLoader
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.discCacheSize(10 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
	}
}
