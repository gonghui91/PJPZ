package com.pjpz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.pjpz.data.Constants;
import com.pjpz.utils.AccessTokenKeeper;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;

public class WBShareActivity extends BaseActivity implements
		IWeiboHandler.Response {

	private IWeiboShareAPI mWeiboShareAPI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 创建微博分享接口实例
		mWeiboShareAPI = WeiboShareSDK
				.createWeiboAPI(this, Constants.WB_APP_ID);
		// 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
		// 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
		// NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
		mWeiboShareAPI.registerApp();
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
		Bundle bundle = getIntent().getExtras();
		String title = bundle.getString("title");
		String url = bundle.getString("url");
		shareWBweb(title, url);
	}

	private void shareWBweb(String title, String url) {
		TextObject textObject = new TextObject();
		String format = "【%1$s】（来自《 品鉴彭州》）%2$s";
		textObject.text = String.format(format, title, url);
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		weiboMessage.mediaObject = textObject;
		SendMultiMessageToWeiboRequest req = new SendMultiMessageToWeiboRequest();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.multiMessage = weiboMessage;
		AuthInfo authInfo = new AuthInfo(this, Constants.WB_APP_ID,
				Constants.REDIRECT_URL, Constants.SCOPE);
		Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(this);
		String token = "";
		if (accessToken != null) {
			token = accessToken.getToken();
		}
		mWeiboShareAPI.sendRequest(this, req, authInfo, token,
				new WeiboAuthListener() {

					@Override
					public void onWeiboException(WeiboException arg0) {
						System.out.println("onWeiboException");
					}

					@Override
					public void onComplete(Bundle bundle) {
						System.out.println("onComplete");
						Oauth2AccessToken newToken = Oauth2AccessToken
								.parseAccessToken(bundle);
						AccessTokenKeeper.writeAccessToken(
								getApplicationContext(), newToken);
					}

					@Override
					public void onCancel() {

					}
				});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
		// 来接收微博客户端返回的数据；执行成功，返回 true，并调用
		// {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
		mWeiboShareAPI.handleWeiboResponse(intent, this);
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			Toast.makeText(this, "取消分享", Toast.LENGTH_SHORT).show();
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Toast.makeText(this,
					"分享失败  " + "Error Message: " + baseResp.errMsg,
					Toast.LENGTH_SHORT).show();
			break;
		}
		WBShareActivity.this.finish();
	}

}
