package kr.co.ezinfotech.fwjournal;

import android.Manifest;
import android.content.Context;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

// public class MainActivity extends AppCompatActivity implements View.OnClickListener {
public class MainActivity extends AppCompatActivity {

    // https://www.journaldev.com/9333/android-webview-example-tutorial
    /**
     * WebViewClient subclass loads all hyperlinks in the existing WebView
     */
    public class GeoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // When user clicks a hyperlink, load in the existing WebView
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }
    }

    /**
     * WebChromeClient subclass handles UI-related calls
     * Note: think chrome as in decoration, not the Chrome browser
     */
    public class GeoWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            // Always grant permission since the app itself requires location
            // permission and the user has therefore already granted it
            callback.invoke(origin, true, false);
        }
    }

    WebView mWebView = null;
    ImageView IvHome = null;
    ImageView IvSearch = null;
    ImageView IvPredict = null;
    ImageView IvStat = null;
    ImageView IvSet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        InitializeBottomImageView();
        runPermissionListener(this);
    }

    private void InitializeBottomImageView() {
        IvHome = findViewById(R.id.IvHome);
        IvHome.setTag("home_n");
        IvHome.setOnClickListener(new IvHomeListener());

        IvSearch = findViewById(R.id.IvSearch);
        IvSearch.setTag("search_n");
        IvSearch.setOnClickListener(new IvSearchListener());

        IvPredict = findViewById(R.id.IvPredict);
        IvPredict.setTag("predict_n");
        IvPredict.setOnClickListener(new IvPredictListener());

        IvStat = findViewById(R.id.IvStat);
        IvStat.setTag("stat_n");
        IvStat.setOnClickListener(new IvStatListener());

        IvSet = findViewById(R.id.IvSet);
        IvSet.setTag("set_n");
        IvSet.setOnClickListener(new IvSetListener());
    }

    class IvSetListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Toast.makeText(MainActivity.this, "Set Clicked!", Toast.LENGTH_SHORT).show();
            IvHome.setImageResource(R.drawable.homeicon_n);
            IvHome.setTag("home_n");
            IvSearch.setImageResource(R.drawable.searchicon_n);
            IvSearch.setTag("search_n");
            IvPredict.setImageResource(R.drawable.prediction_n);
            IvPredict.setTag("predict_n");
            IvStat.setImageResource(R.drawable.statisticsicon_n);
            IvStat.setTag("stat_n");
            ImageView tmpIv = (ImageView)v;
            if("set_n".equals(v.getTag().toString())) {
                tmpIv.setImageResource(R.drawable.seticon_p);
                v.setTag("set_p");
            }
            mWebView.loadUrl("http://192.168.0.73:8082/config");
        }
    }

    class IvStatListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Toast.makeText(MainActivity.this, "Stat Clicked!", Toast.LENGTH_SHORT).show();
            IvHome.setImageResource(R.drawable.homeicon_n);
            IvHome.setTag("home_n");
            IvSearch.setImageResource(R.drawable.searchicon_n);
            IvSearch.setTag("search_n");
            IvPredict.setImageResource(R.drawable.prediction_n);
            IvPredict.setTag("predict_n");
            IvSet.setImageResource(R.drawable.seticon_n);
            IvSet.setTag("set_n");
            ImageView tmpIv = (ImageView)v;
            if("stat_n".equals(v.getTag().toString())) {
                tmpIv.setImageResource(R.drawable.statisticsicon_p);
                v.setTag("stat_p");
            }
            // mWebView.loadUrl("http://192.168.0.73:8082/stats");

            // http://bitsoul.tistory.com/20
            PopupMenu p = new PopupMenu(
                    getApplicationContext(), // 현재 화면의 제어권자
                    v); // anchor : 팝업을 띄울 기준될 위젯
            getMenuInflater().inflate(R.layout.stat_sub_menu, p.getMenu());
            // 이벤트 처리
            p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // Toast.makeText(getApplicationContext(), "팝업메뉴 이벤트 처리 - " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    switch(item.getTitle().toString()) {
                        case "작업시간" :
                            mWebView.loadUrl("http://192.168.0.73:8082/workTime");
                            break;
                        case "환경모니터링" :
                            mWebView.loadUrl("http://192.168.0.73:8082/environment");
                            break;
                    }
                    return false;
                }
            });
            p.show(); // 메뉴를 띄우기
        }
    }

    class IvPredictListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Toast.makeText(MainActivity.this, "Predict Clicked!", Toast.LENGTH_SHORT).show();
            IvHome.setImageResource(R.drawable.homeicon_n);
            IvHome.setTag("home_n");
            IvSearch.setImageResource(R.drawable.searchicon_n);
            IvSearch.setTag("search_n");
            IvStat.setImageResource(R.drawable.statisticsicon_n);
            IvStat.setTag("stat_n");
            IvSet.setImageResource(R.drawable.seticon_n);
            IvSet.setTag("set_n");
            ImageView tmpIv = (ImageView)v;
            if("predict_n".equals(v.getTag().toString())) {
                tmpIv.setImageResource(R.drawable.prediction_p);
                v.setTag("predict_p");
            }
            mWebView.loadUrl("http://192.168.0.73:8082/predict");
        }
    }

    class IvSearchListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Toast.makeText(MainActivity.this, "Search Clicked!", Toast.LENGTH_SHORT).show();
            IvHome.setImageResource(R.drawable.homeicon_n);
            IvHome.setTag("home_n");
            IvPredict.setImageResource(R.drawable.prediction_n);
            IvPredict.setTag("predict_n");
            IvStat.setImageResource(R.drawable.statisticsicon_n);
            IvStat.setTag("stat_n");
            IvSet.setImageResource(R.drawable.seticon_n);
            IvSet.setTag("set_n");
            ImageView tmpIv = (ImageView)v;
            if("search_n".equals(v.getTag().toString())) {
                tmpIv.setImageResource(R.drawable.searchicon_p);
                v.setTag("search_p");
            }
            mWebView.loadUrl("http://192.168.0.73:8082/search");
        }
    }

    class IvHomeListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Toast.makeText(MainActivity.this, "Home Clicked!", Toast.LENGTH_SHORT).show();
            IvSearch.setImageResource(R.drawable.searchicon_n);
            IvSearch.setTag("search_n");
            IvPredict.setImageResource(R.drawable.prediction_n);
            IvPredict.setTag("predict_n");
            IvStat.setImageResource(R.drawable.statisticsicon_n);
            IvStat.setTag("stat_n");
            IvSet.setImageResource(R.drawable.seticon_n);
            IvSet.setTag("set_n");
            ImageView tmpIv = (ImageView)v;
            if("home_n".equals(v.getTag().toString())) {
                tmpIv.setImageResource(R.drawable.homeicon_p);
                v.setTag("home_p");
            }
            mWebView.loadUrl("http://192.168.0.73:8082");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.mWebView.canGoBack()) {
            this.mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void setWebView() {
        // https://turbomanage.wordpress.com/2012/04/23/how-to-enable-geolocation-in-a-webview-android/
        mWebView = (WebView) findViewById(R.id.mainWebView);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new GeoWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setGeolocationEnabled(true);
        mWebView.setWebChromeClient(new GeoWebChromeClient());
        mWebView.loadUrl("http://192.168.0.73:8082");
    }

    // Using TedPermission library - https://github.com/ParkSangGwon/TedPermission
    private void runPermissionListener(Context ctx) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                setWebView();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(ctx)
                .setPermissionListener(permissionlistener)
                .setRationaleTitle("Rational Title")
                .setRationaleTitle(R.string.rationale_title)
                .setRationaleMessage(R.string.rationale_message)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("bla bla")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }

}
