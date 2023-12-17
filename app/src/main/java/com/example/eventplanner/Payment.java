package com.example.eventplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.UUID;

public class Payment extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment);
    WebView webView=findViewById(R.id.paymentWebView);
    webView.getSettings().setJavaScriptEnabled(true);
    ProgressBar progressBar=findViewById(R.id.spin_kit);
  //  progressBar.setVisibility(View.VISIBLE);
    webView.loadUrl("https://chapa-pay.onrender.com");
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.equals("https://chapa-pay.onrender.com/pay")) {
          webView.loadUrl(url);


          return true;
        }
        else {
          webView.loadUrl(url);
        }
        Log.d("url", "shouldOverrideUrlLoading: "+url);
        return super.shouldOverrideUrlLoading(view, url);
      }
      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        progressBar.setVisibility(View.VISIBLE);
      }
      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
      //  totalPrice =(Double.valueOf(exactEventSize.getText().toString())*totalPriceOfChoices())+totalPriceOfChoicesFixed();
        String token = UUID.randomUUID().toString().replace("-", "");
        String javascript3 = "document.getElementById('inp_amount').value = '" + "70000"+ "';";
        String javascript2 = "document.getElementById('tex_ref').value = '" + token + "';";
        webView.evaluateJavascript(javascript2, null);
        webView.evaluateJavascript(javascript3, null);
        progressBar.setVisibility(View.GONE);
      }
    });
  }
}
