package com.example.nayerishtey;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class homeBrowser extends AppCompatActivity {

    public WebView mywebview;
    private ProgressBar mProgressBar;
    SwipeRefreshLayout swipe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_browser);

        checkConnection();

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mywebview = (WebView) findViewById(R.id.webview);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(100);

        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mywebview.loadUrl("https://www.nayerishtey.in/matchboard");
        mywebview.setHorizontalScrollBarEnabled(false);
        mywebview.setVerticalScrollBarEnabled(false);
        mywebview.getSettings().setLoadWithOverviewMode(true);
        mywebview.getSettings().setAllowFileAccess(true);

        mywebview.setWebViewClient(new MyWebViewClient());

//        swipe to refresh
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mywebview.reload();
                swipe.setRefreshing(false   );
            }
        });

        //code for download in the app
        mywebview.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }




    private class MyWebViewClient extends WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgressBar.setVisibility(View.VISIBLE);
            setTitle("Loading....");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
            setTitle(view.getTitle());
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError (WebView view, WebResourceRequest request, WebResourceError error)
        {
//                mywebview.loadUrl("file:///android_asset/error.html");
            startActivity(new Intent(homeBrowser.this,NetworkFailed.class));
        }
    }





//    network error

    public void checkConnection(){

        ConnectivityManager manager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

         NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null!=activeNetwork){

//            if (activeNetwork.getType()== ConnectivityManager.TYPE_WIFI){
//                Toast.makeText(this, "Wifi Enableed", Toast.LENGTH_SHORT).show();
//                //startActivity(new Intent(homeBrowser.this,NetworkFailed.class));
//            }
//            else if (activeNetwork.getType()== ConnectivityManager.TYPE_MOBILE){
//                Toast.makeText(this, "Network Enableed", Toast.LENGTH_SHORT).show();
//            }
        }
        else{

            startActivity(new Intent(homeBrowser.this,NetworkFailed.class));

//            Toast.makeText(this, "No Connection", Toast.LENGTH_SHORT).show();
        }
    }



    //code for back press button

    @Override
    public void onBackPressed() {
        if (mywebview.canGoBack())
        {
            mywebview.goBack();
        }
        else
        {
//            super.onBackPressed();

            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                    .setMessage("Are you sure want to exit ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("No", null).show();
        }
    }
}
