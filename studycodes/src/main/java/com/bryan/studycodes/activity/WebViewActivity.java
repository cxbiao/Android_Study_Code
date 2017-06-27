package com.bryan.studycodes.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bryan.studycodes.R;


public class WebViewActivity extends AppCompatActivity implements OnClickListener {

	private static final String TAG = "WebViewActivity";

	private TextView webviewTitle;
	private ProgressBar progressbar;
	private TextView closeButton;
	private WebView webView;

	private Activity context;

   private String url;

	private static final String initalUrl="file:///android_asset/location.html";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		}
		setContentView(R.layout.activity_webview);
		context = this;
		webviewTitle = (TextView) findViewById(R.id.tv_webview_title);
		closeButton = (TextView) findViewById(R.id.btn_webview_close);
		progressbar = (ProgressBar) findViewById(R.id.pb_webview);

		webView = (WebView) findViewById(R.id.webview);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setDatabaseEnabled(true);
		String cacheDirPath = getFilesDir().getAbsolutePath();
		//设置  Application Caches 缓存目录
		settings.setAppCachePath(cacheDirPath);
		//开启 Application Caches 功能
		settings.setAppCacheEnabled(true);

		webView.setWebViewClient(new CustomWebViewClient());
		webView.setWebChromeClient(new CustomWebChromeClient());

		webView.loadUrl(initalUrl);
		closeButton.setOnClickListener(this);



	}

	//屏蔽系统自带浏览器的弹出
	private class CustomWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
			return super.shouldOverrideUrlLoading(view, request);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		@Override
		public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
			super.onReceivedError(view, request, error);
			//	view.loadUrl("about:blank");
			//用javascript隐藏系统定义的404页面信息
            //	String data = "Page NO FOUND！";
           //view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
		}



	}

	private  ValueCallback<Uri[]> mfilePathCallback;

	private class CustomWebChromeClient extends WebChromeClient {

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			Log.d(TAG, "chromeView : " + title);
			// 获取到Title
			webviewTitle.setText(title);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			Log.d(TAG, "chromeView : " + newProgress);
			if (newProgress == 100) {
				progressbar.setVisibility(View.GONE);
			} else {
				if (progressbar.getVisibility() == View.GONE)
					progressbar.setVisibility(View.VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

		//允许获得地理位置
		//h5原生geolocation api无效，改用百度js api
		@Override
		public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, true);
		}



		@Override
		public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

		//	Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			//i.addCategory(Intent.CATEGORY_OPENABLE);
			//i.setType("image/*");
			//startActivityForResult( Intent.createChooser( i, "File Chooser" ),222 );

			if(mfilePathCallback!=null) return true;
			mfilePathCallback=filePathCallback;

			Intent intent = new Intent(Intent.ACTION_PICK).setType("image/*");
			startActivityForResult( intent,222 );
			return true;
		}



		//		/*
//		 * 此处覆盖的是javascript中的alert方法。 当网页需要弹出alert窗口时，会执行onJsAlert中的方法 网页自身的alert方法不会被调用。
//		 */
//		@Override
//		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//			Log.d("onJsAlert", "弹出了提示框" + ";message = " + message + ";url = " + url + ";");
//			/*
//			 * 此处代码非常重要，若没有，android就不能与js继续进行交互了， 且第一次交互后，webview不再展示出来。 result：A JsResult to confirm that the user hit enter. 我的理解是，confirm代表着此次交互执行完毕。只有执行完毕了，才可以进行下一次交互。
//			 */
//			result.confirm();
//			return true;
//		}
//
//		/*
//		 * 此处覆盖的是javascript中的confirm方法。 当网页需要弹出confirm窗口时，会执行onJsConfirm中的方法 网页自身的confirm方法不会被调用。
//		 */
//		@Override
//		public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
//			Log.d("onJsConfirm", "弹出了确认框");
//			result.confirm();
//			return true;
//		}
//
//		/*
//		 * 此处覆盖的是javascript中的confirm方法。 当网页需要弹出confirm窗口时，会执行onJsConfirm中的方法 网页自身的confirm方法不会被调用。
//		 */
//		@Override
//		public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
//			Log.d("onJsPrompt", "弹出了输入框");
//			result.confirm();
//			return true;
//		}
//
//		/*
//		 * 如果页面被强制关闭,弹窗提示：是否确定离开？ 点击确定 保存数据离开，点击取消，停留在当前页面
//		 */
//		@Override
//		public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
//			Log.d("WebView", "弹出了离开确认框");
//			result.confirm();
//			return true;
//		}
	}




	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_webview_close:
				finish();
				break;

			default:
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		webView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		webView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.removeAllViews();
		webView.destroy();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==222 && resultCode==RESULT_OK){
			//很重要，给h5的input file设置返回值
			mfilePathCallback.onReceiveValue(new Uri[]{data.getData()});
		}
	}
}
