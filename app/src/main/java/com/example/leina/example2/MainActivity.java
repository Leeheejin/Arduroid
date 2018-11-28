package com.example.leina.example2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import android.view.View;
import java.io.UnsupportedEncodingException;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.widget.TextView;

import com.physicaloid.lib.Boards;
import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.Physicaloid.UploadCallBack;
import com.physicaloid.lib.programmer.avr.UploadErrors;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;
import com.physicaloid.lib.usb.driver.uart.UartConfig;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private WebView mWebView;
    private WebViewInterface mWebViewInterface;
    private File outputFile;
    private File path;

    String[] PERMISSIONS = {"android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION",
            "android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE",
            "android.hardware.usb.host","com.google.android.things.permission.USE_PERIPHERAL_IO",
            "android.hardware.usb.action.USB_DEVICE_ATTACHED"};
    static final int PERMISSION_REQUEST_CODE = 1;

    //****************************************************************
    //TODO:file opne, save

    TextView tvRead;
    Physicaloid mPhysicaloid;

    //****************************************************************

    private boolean hasPermissions(String[] permissions) {
        int res = 0;
        // Check state of permission of string array
        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                // case : denied permission
                return false;
            }

        }
        // case : accepted permission
        return true;
    }

    private void requestNecessaryPermissions(String[] permissions) {
        // API 23 of higher version need check Runtime Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(MainActivity.this,IntroActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });
        // Start the thread
        t.start();

        WebView mWebView = (WebView) findViewById(R.id.WebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setCacheMode(mWebView.getSettings().LOAD_NO_CACHE);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setSupportMultipleWindows(true);

        mWebViewInterface = new WebViewInterface(MainActivity.this, mWebView); //JavascriptInterface obj
        mWebView.addJavascriptInterface(mWebViewInterface, "Android"); // JavascriptInterface를 connect

        //mWebView.loadUrl("file:///android_asset/www/ardublockly/index.html#"); // URL
        mWebView.loadUrl("http://175.195.42.157:8000"); // URL
        //mWebView.loadUrl("http://121.168.23.64:8000"); // URL
        mWebView.setWebViewClient(new WebViewClientClass());
        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.addJavascriptInterface(new Object()
        {
            @JavascriptInterface
            public void save_click()
            {
                Toast.makeText(getApplicationContext(), "저장클릭", Toast.LENGTH_LONG).show();
            }
        }, "one");

        mWebView.addJavascriptInterface(new Object()
        {
            @JavascriptInterface
            public void open_click()
            {
                Toast.makeText(getApplicationContext(), "오픈클릭", Toast.LENGTH_LONG).show();
            }
        }, "two");



        mWebView.addJavascriptInterface(new Object()
        {
            @JavascriptInterface
            public void help_click()
            {
                //Toast.makeText(getApplicationContext(), "헬프클릭", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(
                        getApplicationContext(), IntroActivity.class);
                startActivity(intent);
            }
        }, "two");


        tvRead  = (TextView) findViewById(R.id.tvRead);
        tvRead.setVisibility(View.VISIBLE);
        tvRead.setEnabled(false);
        mPhysicaloid = new Physicaloid(this);

        //****************************************************************
        //This part is Physicaloid Library


        //****************************************************************
        // TODO : register intent filtered actions for device being attached or detached
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbReceiver, filter);
        //****************************************************************

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
            //Toast.makeText(mContext, "clicked!", Toast.LENGTH_LONG).show();

            final String fileURL = "http://175.195.42.157:8000/ardublockly/data/ArduroidSketch.ino.hex";

            path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            outputFile= new File(path, "ArduroidSketch.ino.hex"); //

            if (outputFile.exists()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("파일 다운로드");
                builder.setMessage("이미 SD 카드에 존재합니다. 다시 다운로드 받을까요?");
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Toast.makeText(getApplicationContext(),"기존 파일을 플레이합니다.",Toast.LENGTH_LONG).show();
                            }
                        });
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                outputFile.delete(); //파일 삭제

                                final DownloadFilesTask downloadTask = new DownloadFilesTask(MainActivity.this);
                                downloadTask.execute(fileURL);

                            }
                        });
                builder.show();

            } else { // new download
                final DownloadFilesTask downloadTask = new DownloadFilesTask(MainActivity.this);
                downloadTask.execute(fileURL);
            }

            //****************************************************************
            //This part is Physicaloid Library
            openDevice();
            Toast.makeText(getApplicationContext(), "아두이노에 연결합니다.", Toast.LENGTH_LONG).show();
            Log.v("ArduroidNew", "This part button click");
            onClickUpload();

        }
        /*
        @JavascriptInterface
        public void savexmlclick (String blob,String fileName) {
            Toast.makeText(getApplicationContext(), "저장클릭", Toast.LENGTH_LONG).show();

            String filename = fileName;
            String fileContents = blob;
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename+".xml", Context.MODE_PRIVATE);
                outputStream.write(fileContents.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */
    }
    private class DownloadFilesTask extends AsyncTask<String, String, Long> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadFilesTask(Context context) {
            this.context = context;
        }


        @Override
        protected void onPreExecute() { //2
            super.onPreExecute();

            // during download, running cpu background
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();

        }


        // fil downloading....
        @Override
        protected Long doInBackground(String... string_url) { //3
            int count;
            long FileSize = -1;
            InputStream input = null;
            OutputStream output = null;
            URLConnection connection = null;

            try {
                URL url = new URL(string_url[0]);
                connection = url.openConnection();
                connection.connect();


                // file size calc
                FileSize = connection.getContentLength();

                // input stream for url
                input = new BufferedInputStream(url.openStream(), 8192);

                path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                outputFile= new File(path, "ArduroidSketch.ino.hex"); //File obj create

                // Output stream for SDcard
                output = new FileOutputStream(outputFile);


                byte data[] = new byte[1024];
                long downloadedSize = 0;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return Long.valueOf(-1);
                    }

                    downloadedSize += count;

                    if (FileSize > 0) {
                        float per = ((float)downloadedSize/FileSize) * 100;
                        String str = "Downloaded " + downloadedSize + "KB / " + FileSize + "KB (" + (int)per + "%)";
                        publishProgress("" + (int) ((downloadedSize * 100) / FileSize), str);

                    }

                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();

                // Close streams
                output.close();
                input.close();


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                mWakeLock.release();

            }
            return FileSize;
        }


        // when file download done
        @Override
        protected void onPostExecute(Long size) { //5
            super.onPostExecute(size);

            if ( size > 0) {
                Toast.makeText(getApplicationContext(), "다운로드 완료되었습니다. 파일 크기=" + size.toString(), Toast.LENGTH_LONG).show();

                Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(outputFile));
                sendBroadcast(mediaScanIntent);


            }
            else
                Toast.makeText(getApplicationContext(), "다운로드 에러", Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if ( !readAccepted || !writeAccepted  )
                        {
                            showDialogforPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void showDialogforPermission(String msg) {

        final AlertDialog.Builder myDialog = new AlertDialog.Builder(  MainActivity.this);
        myDialog.setTitle("알림");
        myDialog.setMessage(msg);
        myDialog.setCancelable(false);
        myDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
                }

            }
        });
        myDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        myDialog.show();
    }


    //****************************************************************
    //TODO : This part is Hex file upload method
    //****************************************************************

    @Override
    public void onDestroy() {
        super.onDestroy();
        //****************************************************************
        // TODO : unregister the intent filtered actions
        unregisterReceiver(mUsbReceiver);
        //****************************************************************
    }


    private void openDevice() {
        if (!mPhysicaloid.isOpened()) {
            Log.v("ArduroidNEw", "This part 001");
            if (mPhysicaloid.open()) { // default 9600bps
                Log.v("ArduroidNEw", "This part 002");


                mPhysicaloid.addReadListener(new ReadLisener() {
                    String readStr;
                    // callback when reading one or more size buffer
                    @Override
                    public void onRead(int size) {
                        byte[] buf = new byte[size];

                        mPhysicaloid.read(buf, size);
                        try {
                            readStr = new String(buf, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            Log.e(TAG, e.toString());
                            return;
                        }

                        // UI thread
                        tvAppend(tvRead, readStr);
                    }
                });
            }
        }
    }

    public void onClickClose(View v) {
        closeDevice();
    }

    private void closeDevice() {
        if(mPhysicaloid.close()) {
            mPhysicaloid.clearReadListener();
        }
    }


    private UploadCallBack mUploadCallback = new UploadCallBack() {
        @Override
        public void onPreUpload() {
            tvAppend(tvRead, "Upload : Start\n");
            Log.v("arduroid", "Upload : Start");
        }

        @Override
        public void onUploading(int value) {
            tvAppend(tvRead, "Upload : "+value+" %\n");
            Log.v("arduroid", "Upload : onUploading");
        }

        @Override
        public void onPostUpload(boolean success) {
            if(success) {
                tvAppend(tvRead, "Upload : Successful\n");
                Log.v("arduroid", "Upload : onPostUpload");
            } else {
                tvAppend(tvRead, "Upload fail\n");
                Log.v("arduroid", "Upload : onPostUpload fail");
            }
        }

        @Override
        public void onCancel() {
            tvAppend(tvRead, "Cancel uploading\n");
            Log.v("arduroid", "Upload : onPostUpload onCancel");
        }

        @Override
        public void onError(UploadErrors err) {
            tvAppend(tvRead, "Error  : "+err.toString()+"\n");
            Log.v("arduroid", "Upload : Error" + err.toString());
        }
    };

    public void onClickUpload() {
        Log.v("arduroid", "Click Upload");
        try {


            String fileName = "ArduroidSketch.ino.hex";
            String path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/"+fileName;
            File file = new File(path1);
            FileInputStream fileInputStream = new FileInputStream(file);
            mPhysicaloid.upload(Boards.ARDUINO_UNO, fileInputStream, mUploadCallback);


            Log.v("arduroid", "Try what?");
        } catch (RuntimeException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

    }

    Handler mHandler = new Handler();
    private void tvAppend(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ftv.append(ftext);
            }
        });
    }


    //****************************************************************
    // TODO : get intent when a USB device attached
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();

        if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
            openDevice();
        }
    };
    //****************************************************************

    //****************************************************************
    // TODO : get intent when a USB device detached
    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                closeDevice();
            }
        }
    };

}