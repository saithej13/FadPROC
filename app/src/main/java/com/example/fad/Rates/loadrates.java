package com.example.fad.Rates;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class loadrates extends WebViewClient {
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
