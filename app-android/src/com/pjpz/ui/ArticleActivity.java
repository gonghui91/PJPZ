package com.pjpz.ui;

import java.util.List;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pjpz.R;
import com.pjpz.data.Constants;
import com.pjpz.model.Catalog;
import com.pjpz.ui.fragment.ArticleFragment;
import com.pjpz.ui.fragment.CommentFragment;
import com.pjpz.utils.BitmapUtils;
import com.pjpz.utils.IntentUtils;
import com.pjpz.utils.OnSelectListener;
import com.pjpz.utils.ToastUtils;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class ArticleActivity extends BaseActivity implements OnSelectListener,
		IWeiboHandler.Response {
	private ArticleFragment articleFragment;
	private CommentFragment commentFragment;
	private FragmentManager fragmentManager;
	private Bundle bundle;
	private List<Catalog> catalogs;
	private Catalog catalog;
	private int position;
	private IWXAPI iwxapi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		initView();
		fragmentManager = getFragmentManager();
		setTabSelection(0);
		reg(savedInstanceState);
	}

	private void reg(Bundle savedInstanceState) {
		iwxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);
		iwxapi.registerApp(Constants.WX_APP_ID);
	}
	private void initView() {
		bundle = getIntent().getExtras();
		String catalogStr = bundle.getString("catalog");
		catalogs = new Gson().fromJson(catalogStr,
				new TypeToken<List<Catalog>>() {
				}.getType());
		position = bundle.getInt("position");
		catalog = catalogs.get(position);
		actionBar.setTitle(catalog.articleName);
	}

	@Override
	public void onBackPressed() {
		if (articleFragment.isVisible()) {
			// 文章界面是退出文章
			ArticleActivity.this.finish();
		} else if (commentFragment.isVisible()) {
			// 退出评论到文章
			setTabSelection(0);
		}
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 *            每个tab页对应的下标。
	 */
	private void setTabSelection(int index) {
		// 每次选中之前先清楚掉上次的选中状态
		clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:
			// 文章内容
			if (articleFragment == null) {
				articleFragment = new ArticleFragment();
				articleFragment.setArguments(bundle);
				transaction.add(R.id.content, articleFragment).setTransition(
						FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			} else {
				transaction.show(articleFragment).setTransition(
						FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			}
			break;

		case 1:
			// 评论
			// transaction.setCustomAnimations(R.anim.slide_in_bottom,
			// R.anim.slide_in_top);
			if (commentFragment == null) {
				commentFragment = new CommentFragment();
				commentFragment.setArguments(bundle);
				transaction.add(R.id.content, commentFragment).setTransition(
						FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			} else {
				transaction.show(commentFragment).setTransition(
						FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (articleFragment != null) {
			transaction.hide(articleFragment);
		}
		if (commentFragment != null) {
			transaction.hide(commentFragment);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.article, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (articleFragment.isVisible()) {
				// 文章界面是退出文章
				ArticleActivity.this.finish();
			} else if (commentFragment.isVisible()) {
				// 退出评论到文章
				setTabSelection(0);
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onShare(boolean isShareOpen) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		if (isShareOpen) {
			lp.alpha = 0.3f;
			getWindow().setAttributes(lp);
		} else {
			lp.alpha = 1f;
			getWindow().setAttributes(lp);
		}
	}

	@Override
	public void onComment() {
		setTabSelection(1);
	}

	@Override
	public void setTitle(String title) {
		actionBar.setTitle(title);
	}

	@Override
	public void share(int platform, String title, String url) {
		switch (platform) {
		case Constants.WECHAT:
			sendWxReq(url, title, SendMessageToWX.Req.WXSceneSession);
			break;
		case Constants.WECHAT_MOMENT:
			sendWxReq(url, title, SendMessageToWX.Req.WXSceneTimeline);
			break;
		case Constants.WEIBO:
			Bundle bundle = new  Bundle();
			bundle.putString("title", title);
			bundle.putString("url", url);
			IntentUtils.startIntent(ArticleActivity.this, WBShareActivity.class, bundle);
			break;
		case Constants.TENCENT:

			break;
		}
	}

	private void sendWxReq(String url, String title, int which) {
		if (!iwxapi.isWXAppInstalled()) {
			ToastUtils.showShort("未安装微信客户端！");
			return;
		}
		if (!iwxapi.isWXAppSupportAPI()) {
			ToastUtils.showShort("微信版本问题");
			return;
		}
		WXWebpageObject webpageObject = new WXWebpageObject();
		webpageObject.webpageUrl = url;
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = webpageObject;
		switch (which) {
		case SendMessageToWX.Req.WXSceneSession:
			msg.description = title;
			break;

		case SendMessageToWX.Req.WXSceneTimeline:
			msg.title = title;
			break;
		}
		msg.thumbData = BitmapUtils.Bitmap2Bytes(BitmapUtils
				.drawableToBitmap(getResources().getDrawable(
						R.drawable.ic_launcher)));
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.scene = which;
		req.message = msg;
		iwxapi.sendReq(req);
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			Toast.makeText(this, "R.string.weibosdk_demo_toast_share_success",
					Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			Toast.makeText(this, "R.string.weibosdk_demo_toast_share_canceled",
					Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Toast.makeText(
					this,
					"R.string.weibosdk_demo_toast_share_failed"
							+ "Error Message: " + baseResp.errMsg,
					Toast.LENGTH_LONG).show();
			break;
		}
	}
}
