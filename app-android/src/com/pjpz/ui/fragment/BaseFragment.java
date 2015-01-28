package com.pjpz.ui.fragment;

import android.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pjpz.data.RequestManager;
import com.pjpz.utils.ToastUtils;

public class BaseFragment extends Fragment{
	 @Override
	    public void onDestroy() {
	        super.onDestroy();
	        RequestManager.cancelAll(this);
	    }

	    protected void executeRequest(Request request) {
	        RequestManager.addRequest(request, this);
	    }

	    protected Response.ErrorListener errorListener() {
	        return new Response.ErrorListener() {
	            @Override
	            public void onErrorResponse(VolleyError error) {
	                ToastUtils.showLong("加载失败");
	            }
	        };
	    }
}
