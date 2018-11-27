package com.example.leina.example2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebViewInterface mWebViewInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        WebView mWebView = (WebView) findViewById(R.id.WebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        mWebViewInterface = new WebViewInterface(MainActivity.this, mWebView); //JavascriptInterface obj
        mWebView.addJavascriptInterface(mWebViewInterface, "Android"); //웹뷰에 JavascriptInterface를 connect

        //mWebView.loadUrl("file:///android_asset/www/ardublockly/index.html#"); // URL
        mWebView.loadUrl("http://175.195.42.157:8000"); // URL
        //mWebView.loadUrl("http://121.168.23.64:8000"); // URL
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClientClass());

        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public  boolean onTouch(View view, MotionEvent motionEvent) {
                WebView.HitTestResult hr = ((WebView)view).getHitTestResult();

                //Toast.makeText(getApplicationContext(),str, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public class WebViewInterface {

        private WebView mAppView;
        private Activity mContext;

        public WebViewInterface(Activity activity, WebView view) {
            mAppView = view;
            mContext = activity;
        }

        @JavascriptInterface
        public void getclick () {
            Toast.makeText(mContext, "clicked!", Toast.LENGTH_LONG).show();
        }

    }



}