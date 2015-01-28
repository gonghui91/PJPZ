package com.pjpz.utils;

import android.app.Activity;
import android.content.Context;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.pjpz.R;

public class DialogUtil {
	public static void showLoadFailDialog(final Context context){
		new SweetAlertDialog(context,
				SweetAlertDialog.ERROR_TYPE)
				.setTitleText(context.getString(R.string.error))
				.setContentText(context.getString(R.string.load_fail))
				.setConfirmText(context.getString(R.string.confirm))
				.setConfirmClickListener(new OnSweetClickListener() {
					@Override
					public void onClick(
							SweetAlertDialog sweetAlertDialog) {
						sweetAlertDialog.cancel();
						((Activity) context).finish();
					}
				}).show();
	}
}
