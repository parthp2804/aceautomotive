package in.co.aceautomotive;
//import in.co.aceautomotive.InternetConnection1;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.content.Intent;
import android.widget.Toast;

import static android.app.PendingIntent.getActivity;

//import static in.co.aceautomotive.InternetConnection1.isConnectingToInternet;

public class MainActivity extends AppCompatActivity {
    private WebView mywebView;
    SwipeRefreshLayout swipe;
    private Context context = this;
    public String lastUrl="\"http://aceautomotive.co.in/index.php/en/\"";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mywebView = (WebView) findViewById(R.id.webview);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                       @Override
                                       public void onRefresh() {
                                        if(mywebView.getUrl().equals("file:///android_asset/error.html") && isNetworkAvailable())
                                            LoadWeb(lastUrl);
                                        else if(mywebView.getUrl().equals("file:///android_asset/error.html") && !isNetworkAvailable())
                                            mywebView.loadUrl("file:///android_asset/error.html");
                                        else
                                               LoadWeb(mywebView.getUrl());
                                       }
                                   });
        LoadWeb("<Your Website Link>");

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void LoadWeb(String url){

        WebSettings webSettings = mywebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mywebView.getSettings().setAppCacheEnabled(true);
        mywebView.loadUrl(url);
        swipe.setRefreshing(true);

        mywebView.setWebViewClient(new WebViewClient(){
            @Override
           /*public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }*/

            public void onPageFinished(WebView view, String url){
              //Hide the SwipeRefreshLayout
              swipe.setRefreshing(false);



            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                lastUrl = failingUrl;
                mywebView.loadUrl("file:///android_asset/error.html");

            }

        });
    }
    /*public class myWebClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            if(isConnectingToInternet(context)){
                super.onPageStarted(view, url, favicon);
            }else{

                InternetConnection1.showAlert("Internet is not working, Tap Refresh to retry",context);
            }
        }
    }*/

    @Override
    public void onBackPressed(){
        if (mywebView.canGoBack()){
            mywebView.goBack();
        }else {
            super.onBackPressed();
        }
    }



}
