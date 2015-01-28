package com.pjpz.utils;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class IntentUtils {
	public static void startIntent(Context context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
	}

	public static void startIntent(Context context, Class<?> cls,
			BasicNameValuePair... name) {
		Intent intent = new Intent(context, cls);
		for (int i = 0; i < name.length; i++) {
			intent.putExtra(name[i].getName(), name[i].getValue());
		}
		context.startActivity(intent);
//        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	public static void startIntent(Context context, Class<?> cls,
			Bundle bundle) {
		Intent intent = new Intent(context, cls);
		intent.putExtras(bundle);
		context.startActivity(intent);
//        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
}
