package com.example.leina.example2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebViewInterface mWebViewInterface;
    private File outputFile;
    private File path;
    String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"};

    static final int PERMISSION_REQUEST_CODE = 1;

    private boolean hasPermissions(String[] permissions) {
        int res = 0;
        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                //퍼미션 허가 안된 경우
                return false;
            }

        }
        //퍼미션이 허가된 경우
        return true;
    }


    private void requestNecessaryPermissions(String[] permissions) {
        //마시멜로( API 23 )이상에서 런타임 퍼미션(Runtime Permission) 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (!hasPermissions(PERMISSIONS)) { //퍼미션 허가를 했었는지 여부를 확인
            requestNecessaryPermissions(PERMISSIONS);//퍼미션 허가안되어 있다면 사용자에게 요청
        } else {
            //이미 사용자에게 퍼미션 허가를 받음.
        }

        WebView mWebView = (WebView) findViewById(R.id.WebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        mWebViewInterface = new WebViewInterface(MainActivity.this, mWebView); //JavascriptInterface obj
        mWebView.addJavascriptInterface(mWebViewInterface, "Android"); // JavascriptInterface를 connect

        //mWebView.loadUrl("file:///android_asset/www/ardublockly/index.html#"); // URL
        mWebView.loadUrl("http://175.195.42.157:8000"); // URL
        //mWebView.loadUrl("http://121.168.23.64:8000"); // URL
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClientClass());
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

            final String fileURL = "http://175.195.42.157:8000/ardublockly/ArdublocklySketch.ino.hex";

            path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            outputFile= new File(path, "ArdublocklySketch.ino.hex"); //

            if (outputFile.exists()) { //이미 다운로드 되어 있는 경우

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

            } else { //새로 다운로드 받는 경우
                final DownloadFilesTask downloadTask = new DownloadFilesTask(MainActivity.this);
                downloadTask.execute(fileURL);
            }
        }
    }
    private class DownloadFilesTask extends AsyncTask<String, String, Long> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadFilesTask(Context context) {
            this.context = context;
        }


        //파일 다운로드를 시작하기 전에 프로그레스바를 화면에 보여줍니다.
        @Override
        protected void onPreExecute() { //2
            super.onPreExecute();

            //사용자가 다운로드 중 파워 버튼을 누르더라도 CPU가 잠들지 않도록 해서
            //다시 파워버튼 누르면 그동안 다운로드가 진행되고 있게 됩니다.
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();

        }


        //파일 다운로드를 진행합니다.
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


                //파일 크기를 가져옴
                FileSize = connection.getContentLength();

                //URL 주소로부터 파일다운로드하기 위한 input stream
                input = new BufferedInputStream(url.openStream(), 8192);

                path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                outputFile= new File(path, "ArdublocklySketch.ino.hex"); //파일명까지 포함함 경로의 File 객체 생성

                // SD카드에 저장하기 위한 Output stream
                output = new FileOutputStream(outputFile);


                byte data[] = new byte[1024];
                long downloadedSize = 0;
                while ((count = input.read(data)) != -1) {
                    //사용자가 BACK 버튼 누르면 취소가능
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

                    //파일에 데이터를 기록합니다.
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


        //파일 다운로드 완료 후
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

}