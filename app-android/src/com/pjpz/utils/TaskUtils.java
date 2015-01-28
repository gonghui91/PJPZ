package com.pjpz.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import android.os.AsyncTask;
import android.os.Build;

import com.pjpz.data.Constants;

/**
 * Created by storm on 14-4-11.
 */
public class TaskUtils {
	public static <Params, Progress, Result> void executeAsyncTask(
			AsyncTask<Params, Progress, Result> task, Params... params) {
		if (Build.VERSION.SDK_INT >= 11) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			task.execute(params);
		}
	}

	// 从服务器下载图片到本地
	public static void getPhotoFromServer(String periodicalid,
			List<String> imageUrls) {
		String dirName = Constants.DEFAULT_IMAGE_URL + periodicalid + "/";
		File f = new File(dirName);
		if (!f.exists()) {
			f.mkdirs();
		}
		DecimalFormat df = new DecimalFormat("000");
		// 准备拼接新的文件名（保存在存储卡后的文件名）
		for (int i = 0; i < imageUrls.size(); i++) {
			String imageUrl = imageUrls.get(i);
			// 获取广告图片的名字，其实使用advert.getUrl()也可
			String newFilename = "pjpz_"+df.format(i)+"_" + UUID.fromString(imageUrl);
			// 本地播放广告图片的完整路径
			newFilename = dirName + newFilename;
			File file = new File(newFilename);
			// 如果目标文件已经存在，则删除，产生覆盖旧文件的效果（此处以后可以扩展为已经存在图片不再重新下载功能）
			if (!file.exists()) {
				try {
					file.createNewFile();
					// 构造URL
					URL url = new URL(imageUrl);
					// 打开连接
					URLConnection con = url.openConnection();
					// 获得文件的长度
					// int contentLength = con.getContentLength();
					// System.out.println("长度 :"+contentLength);
					// 输入流
					InputStream is = con.getInputStream();
					// 1K的数据缓冲
					byte[] bs = new byte[1024];
					// 读取到的数据长度
					int len;
					// 输出的文件流
					OutputStream os = new FileOutputStream(newFilename);
					// 开始读取
					while ((len = is.read(bs)) != -1) {
						os.write(bs, 0, len);
					}
					// 完毕，关闭所有链接
					os.close();
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
