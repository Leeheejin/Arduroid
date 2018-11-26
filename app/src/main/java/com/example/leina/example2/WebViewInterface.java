package com.example.leina.example2;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class WebViewInterface extends Activity{

    private WebView mAppView;
    private Activity mContext;
    private String filename = "arduoidsketch";

    public WebViewInterface(Activity activity, WebView view) {
        mAppView = view;
        mContext = activity;
    }

    @JavascriptInterface
    public void getcode (String code) { // Show toast for a short time
        //Toast.makeText(mContext, code, Toast.LENGTH_SHORT).show();
        stringToHex(code);
    }

    @JavascriptInterface
    public void getfilename (String getname) { // Show toast for a short time
        //Toast.makeText(mContext, code, Toast.LENGTH_SHORT).show();
        filename = getname;
    }


    public String stringToHex(String s) {
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            result += String.format("%02X", (int) s.charAt(i));
        }
        hexdataSave(s,filename);
        return result;
    }

    public void showtoast (String s) { // Show toast for a short time
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    public void hexdataSave(String result, String filename) {

        try {
            FileOutputStream fileout = openFileOutput(filename + ".ino.hex", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(result);
            outputWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}