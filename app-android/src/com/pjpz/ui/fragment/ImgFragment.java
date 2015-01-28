package com.pjpz.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.pjpz.R;
import com.pjpz.data.Constants;
import com.pjpz.data.GsonRequest;
import com.pjpz.model.Periodical;
import com.pjpz.ui.adapter.ImgListAdapter;
import com.pjpz.utils.TaskUtils;

public class ImgFragment extends BaseFragment {
	private View imgLayout;
	private ListView listView;
	private List<Periodical> periodicals;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		imgLayout = inflater.inflate(R.layout.layout_showimg, container, false);
		listView = (ListView) imgLayout.findViewById(R.id.imgList);
		getPeridicals();
		listView.setAdapter(new ImgListAdapter(getActivity(), periodicals));
		return imgLayout;
	}

	private void getPeridicals() {
		periodicals = new ArrayList<Periodical>();
		Periodical periodical = null;
		for (int i = 0; i < 5; i++) {
			periodical = new Periodical();
			periodical = new Periodical();
			periodical.name = "2012 壬辰年 秋季版";
			periodical.summary="朱清时：中国科学院士、南方科技大学校长";
			periodical.syncTime= "更新日期 ： 2014-12-10";
			periodicals.add(periodical);
		}
	}
	private void loadData() {
		GsonRequest<Periodical.PeriodicalRequestData> gsonRequest = new GsonRequest<Periodical.PeriodicalRequestData>(
				Constants.url, Periodical.PeriodicalRequestData.class,
				responseListener(), errorListener()) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Periodical.PeriodicalRequestData requestData = new Periodical.PeriodicalRequestData();
				requestData.commandName = "periodical";
				requestData.commandType = Constants.IMAGE;
				requestData.commandDevice = Constants.ANDROID;
				Periodical.RequestItem requestItem = new Periodical.RequestItem();
				requestItem.page = "1";
				requestItem.pageSize = "10";
				ArrayList<Periodical.RequestItem> requestItems = new ArrayList<Periodical.RequestItem>();
				requestItems.add(requestItem);
				requestData.commandParam = requestItems;
				Map<String, String> map = new HashMap<String, String>();
//				map.put(Constant.key, requestData.toJson());
				return super.getParams();
			}
		};
		executeRequest(gsonRequest);
	}

	private Response.Listener<Periodical.PeriodicalRequestData> responseListener() {
		return new Response.Listener<Periodical.PeriodicalRequestData>() {
			@Override
			public void onResponse(
					final Periodical.PeriodicalRequestData response) {
				TaskUtils
						.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
							@Override
							protected Object doInBackground(Object... params) {
								List<Periodical> periodicals = response.commandData;
								return periodicals;
							}

							@Override
							protected void onPostExecute(Object o) {
								super.onPostExecute(o);
							}
						});
			}
		};
	}
}
