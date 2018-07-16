package kr.co.ezinfotech.fwjournal;

import android.Manifest;
import android.content.Context;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
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

    /*
    private FloatingActionButton fab = null;
    private FloatingActionButton fab2 = null;
    private FloatingActionButton fab3 = null;
    private FloatingActionButton fab4 = null;
    private FloatingActionButton fab5 = null;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // InitializeFAB();
        InitializeBottomNavigation();

        runPermissionListener(this);
    }

    private void InitializeBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_one:
                        mWebView.loadUrl("http://192.168.0.73:8082");
                        return true;
                    case R.id.action_two:
                        mWebView.loadUrl("http://192.168.0.73:8082/search");
                        return true;
                    case R.id.action_three:
                        mWebView.loadUrl("http://192.168.0.73:8082/predict");
                        return true;
                    case R.id.action_four:
                        mWebView.loadUrl("http://192.168.0.73:8082/stats");
                        return true;
                    case R.id.action_five:
                        mWebView.loadUrl("http://192.168.0.73:8082/config");
                        return true;
                }
                return false;
            }
        });
    }

    // http://codeman77.tistory.com/27
    // https://medium.com/wasd/android-floating-action-button-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0-6ca52aba7a1f
    /*
    private void InitializeFAB() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(this);

        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(this);

        fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        fab4.setOnClickListener(this);

        fab5 = (FloatingActionButton) findViewById(R.id.fab5);
        fab5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                Toast.makeText(this, "영농일지", Toast.LENGTH_SHORT).show();
                mWebView.loadUrl("http://192.168.0.73:8082");
                break;
            case R.id.fab2:
                Toast.makeText(this, "일지검색", Toast.LENGTH_SHORT).show();
                mWebView.loadUrl("http://192.168.0.73:8082/search");
                break;
            case R.id.fab3:
                Toast.makeText(this, "작업예측", Toast.LENGTH_SHORT).show();
                mWebView.loadUrl("http://192.168.0.73:8082/predict");
                break;
            case R.id.fab4:
                Toast.makeText(this, "통계", Toast.LENGTH_SHORT).show();
                mWebView.loadUrl("http://192.168.0.73:8082/stats");
                break;
            case R.id.fab5:
                Toast.makeText(this, "설정", Toast.LENGTH_SHORT).show();
                mWebView.loadUrl("http://192.168.0.73:8082/config");
                break;
        }
    }
    */

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
