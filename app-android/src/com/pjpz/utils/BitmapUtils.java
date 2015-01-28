package com.pjpz.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by storm on 14-6-17.
 */
public class BitmapUtils {
	public static Bitmap drawViewToBitmap(View view, int width, int height,
			int downSampling) {
		return drawViewToBitmap(view, width, height, 0f, 0f, downSampling);
	}

	public static Bitmap drawViewToBitmap(View view, int width, int height,
			float translateX, float translateY, int downSampling) {
		float scale = 1f / downSampling;
		int bmpWidth = (int) (width * scale - translateX / downSampling);
		int bmpHeight = (int) (height * scale - translateY / downSampling);
		Bitmap dest = Bitmap.createBitmap(bmpWidth, bmpHeight,
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(dest);
		c.translate(-translateX / downSampling, -translateY / downSampling);
		if (downSampling > 1) {
			c.scale(scale, scale);
		}
		view.draw(c);
		return dest;
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}
}
