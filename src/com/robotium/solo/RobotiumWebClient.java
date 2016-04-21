package com.robotium.solo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;





import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

/**
 * WebChromeClient used to get information on web elements by injections of JavaScript. 
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class RobotiumWebClient extends WebChromeClient{
	WebElementCreator webElementCreator;
	private Instrumentation inst;
	private WebChromeClient robotiumWebClient;
	private WebChromeClient originalWebChromeClient = null;


	/**
	 * Constructs this object.
	 *
	 * @param instrumentation the {@code Instrumentation} instance
	 * @param webElementCreator the {@code WebElementCreator} instance
	 */

	public RobotiumWebClient(Instrumentation inst, WebElementCreator webElementCreator){
		this.inst = inst;
		this.webElementCreator = webElementCreator;
		robotiumWebClient = this;
	}

	/**
	 * Enables JavaScript in the given {@code WebViews} objects.
	 * 
	 * @param webViews the {@code WebView} objects to enable JavaScript in
	 */
	public void enableJavascriptAndSetRobotiumWebClient(List<WebView> webViews, WebChromeClient originalWebChromeClient){
		//this.originalWebChromeClient = originalWebChromeClient;
		
		//cordova相关修正
			set_originalWebChromeClient(webViews,originalWebChromeClient);
		
		
		//Log.i("123", "WebViews:"+webViews.size());
		for(final WebView webView : webViews){
			//LOG.i("123", "url:"+webView.getUrl());
			//LOG.i("123", "url:"+webView.getOriginalUrl());
			if(webView != null){ 
				inst.runOnMainSync(new Runnable() {
					public void run() {
						
						webView.getSettings().setJavaScriptEnabled(true);
							Method swcc=null;
							try {
								swcc = webView.getClass().getMethod("setWebChromeClient", new Class[]{WebChromeClient.class});
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//Log.i("robotium", "webview22:"+webView.getClass());
							//Log.i("robotium", "swcc:"+swcc.getDeclaringClass());
							if(!swcc.getDeclaringClass().getName().contains("CordovaWebView"))
							{
								webView.setWebChromeClient(robotiumWebClient);
							}else
							{
								//Log.i("robotium", "非webview");
								try {
									
									Method[] mts = webView.getClass().getSuperclass().getMethods();
									for(Method m:mts)
									{
										
										if(m.getName().equals("checkThread"))
										{
											//Log.i("123", m.getName());
											m.setAccessible(true);
											m.invoke(webView);
											break;
										}
									}
									
									
									Field mP = webView.getClass().getSuperclass().getDeclaredField("mProvider");
									mP.setAccessible(true);
									Object obj = mP.get(webView);
									//Log.i("123", "obj:"+obj.getClass().getName());
									Method mt = obj.getClass().getMethod("setWebChromeClient", new Class[]{WebChromeClient.class});
									mt.invoke(obj, new Object[]{robotiumWebClient});
										
									//Method mt = webView.getClass().getMethod("superSetWebChromeClient",new Class[]{WebChromeClient.class});
									//mt.invoke(webView, new Object[]{robotiumWebClient});
									
									
								} catch(Exception e)
								{
									Log.i("123", "error:注入失败"+e.getMessage());
									throw new IllegalArgumentException("robotium error"); 
//									Assert.fail("sunjianping error");
								}
								
								
							}
							
					}
				});
			}
		}
	}
	//孙建平新增
	//保存原有的WebChromeClient
	//cordova相关
	private void set_originalWebChromeClient(List<WebView> webViews,WebChromeClient originalWebChromeClient) 
	{
		this.originalWebChromeClient = originalWebChromeClient;
		if(webViews.size()>0)
		{
			WebView wv = webViews.get(0);
			if(wv.getClass().getName().contains("CordovaWebView"))
			{
				try {
					Method mt = wv.getClass().getMethod("getWebChromeClient");
					this.originalWebChromeClient = (WebChromeClient)mt.invoke(wv);
					//Log.i("robotium", "set_originalWebChromeClient:OK");
				}  catch (Exception e) {
					// TODO Auto-generated catch block
					throw new IllegalArgumentException("robotium error"); 
//					Assert.fail("sunjianping error");
				} 
			}
			//Log.i("robotium", "cancelRobotiumWebClient_OK");
		}
	}

	/**
	 * Overrides onJsPrompt in order to create {@code WebElement} objects based on the web elements attributes prompted by the injections of JavaScript
	 */

	@Override
	public boolean onJsPrompt(WebView view, String url, String message,	String defaultValue, JsPromptResult r) {

		if(message != null && (message.contains(";,") || message.contains("robotium-finished"))){

			if(message.equals("robotium-finished")){
				webElementCreator.setFinished(true);
			}
			else{
				webElementCreator.createWebElementAndAddInList(message, view);
			}
			r.confirm();
			return true;
		}
		else {
			if(originalWebChromeClient != null) {
				return originalWebChromeClient.onJsPrompt(view, url, message, defaultValue, r); 
			}
			return true;
		}

	}

	@Override
	public Bitmap getDefaultVideoPoster() {
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.getDefaultVideoPoster();
		} 
		return null;
	}

	@Override
	public View getVideoLoadingProgressView() {
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.getVideoLoadingProgressView();
		} 
		return null;
	}

	@Override
	public void getVisitedHistory(ValueCallback<String[]> callback) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.getVisitedHistory(callback);
		} 
	}

	@Override
	public void onCloseWindow(WebView window) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onCloseWindow(window);
		} 
	}

	@Override
	public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onConsoleMessage(message, lineNumber, sourceID);
		}
	}

	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage) {       
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.onConsoleMessage(consoleMessage);
		} 
		return true;
	}

	@Override
	public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
		} 
		return true;
	}

	@Override
	public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota,
			long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
		} 
	}

	@Override
	public void onGeolocationPermissionsHidePrompt() {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onGeolocationPermissionsHidePrompt();
		} 
	}

	@Override
	public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onGeolocationPermissionsShowPrompt(origin, callback);
		} 
	}

	@Override
	public void onHideCustomView() {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onHideCustomView();
		} 
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.onJsAlert(view, url, message, result);
		} 
		return true;
	}

	@Override
	public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
		if (originalWebChromeClient.onJsBeforeUnload(view, url, message, result)) {
			return originalWebChromeClient.onJsBeforeUnload(view, url, message, result);
		}
		return true;
	}

	@Override
	public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.onJsConfirm(view, url, message, result);
		} 
		return true;
	}

	@Override
	public boolean onJsTimeout() {
		if (originalWebChromeClient != null) {
			return originalWebChromeClient.onJsTimeout();
		} 
		return true;
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		if (originalWebChromeClient != null) {            
			originalWebChromeClient.onProgressChanged(view, newProgress);
		} 
	}

	@Override
	public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
		} 
	}

	@Override
	public void onReceivedIcon(WebView view, Bitmap icon) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onReceivedIcon(view, icon);
		} 
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onReceivedTitle(view, title);
		} 
	}

	@Override
	public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onReceivedTouchIconUrl(view, url, precomposed);
		} 
	}

	@Override
	public void onRequestFocus(WebView view) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onRequestFocus(view);
		}
	}

	@Override
	public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
		if (originalWebChromeClient != null) {
			originalWebChromeClient.onShowCustomView(view, callback);
		} 
	}
}
