package com.pjpz.data;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by storm on 14-3-25.
 */
public class GsonRequest<T> extends JsonRequest<T> {
	private final Gson mGson = new Gson();
	private final Class<T> mClazz;
	private final Listener<T> mListener;
	private final Map<String, String> mHeaders;

	public GsonRequest(String requestBody, Class<T> clazz,
			Listener<T> listener, ErrorListener errorListener) {
		this(Method.POST, requestBody, clazz, null, listener, errorListener);
	}

	public GsonRequest(int method, String requestBody, Class<T> clazz,
			Map<String, String> mHeaders, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, Constants.url, requestBody, listener, errorListener);
//		System.out.println("requestBody---"+requestBody);
		this.mClazz = clazz;
		this.mHeaders = mHeaders;
		this.mListener = listener;
	}
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders != null ? mHeaders : super.getHeaders();
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			System.out.println("response---"+json);
			return Response.success(mGson.fromJson(json, mClazz),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}
