package com.example.cover;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MakePaymentActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        mWebView = (WebView) findViewById(R.id.mWebView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new WebViewClient()

                                  {
                                      @Override
                                      public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                          super.onPageStarted(view, url, favicon);
                                          mWebView.setVisibility(View.GONE);
                                          mProgressBar.setVisibility(View.VISIBLE);
                                          if (url.equals("http://dev.covertocover.cf")){
                                              Toast.makeText(MakePaymentActivity.this, "payment is Cancelled!!", Toast.LENGTH_SHORT).show();
                                          } else if (url.equals("http://dev.covertocover.cf/done")){
                                              Toast.makeText(MakePaymentActivity.this, "Payment is Success!", Toast.LENGTH_SHORT).show();
                                          }
                                          
                                      }

                                      @Override
                                      public void onPageFinished(WebView view, String url) {
                                          super.onPageFinished(view, url);
                                          mWebView.setVisibility(View.VISIBLE);
                                          mProgressBar.setVisibility(View.GONE);
                                      }
                                  }
        );
        mWebView.loadUrl("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=SYPPWMKLVRRKA");

    }
}
