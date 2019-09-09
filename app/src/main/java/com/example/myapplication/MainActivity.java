package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //login button
        final Button login = findViewById(R.id.button);


        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                String username = "";
//                EditText editText1 = (EditText)findViewById(R.id.editText);
//                username = editText1.getText().toString();
//                String password = "";
//                EditText editText2 = (EditText)findViewById(R.id.editText2);
//                password = editText2.getText().toString();
//                if (username.equals(user) & password.equals(pass)) {
                    String code = doGetTestOne();

                    if(!"".equals(code)){
                       ComponentName componetName = new ComponentName(
                            //这个是另外一个应用程序的包名
                            "com.example.customerdemo",
                            //这个参数是要启动的Activity
                            "com.example.customerdemo.MainActivity");
                    Intent intent= new Intent("android.intent.action.VIEW");
                    //我们给他添加一个参数表示从apk1传过去的
                    Bundle bundle = new Bundle();
                    bundle.putString("code", code);
                    intent.putExtras(bundle);
                    intent.setComponent(componetName);
                    startActivity(intent);
                    overridePendingTransition(R.animator.zoomout, R.animator.zoomin);
                    }
                }
//                else {
//                    new AlertDialog.Builder(MainActivity.this).setTitle("Error!").setMessage("Wrong username or password.")
//                            .setNegativeButton("OK",null)
//                            .show();
//                }
//            }
        });
        //register button
        final Button register = (Button) findViewById(R.id.button2);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //提示框确定是否跳转
                new AlertDialog.Builder(MainActivity.this).setTitle("Jump").setMessage("Ready to jump?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, RegisterActity.class);
                                startActivity(intent);
                            }})
                        .setNegativeButton("No",null)
                        .show();
            }
        });
    }

    public static String doGetTestOne(){
        String code = "888888";
        StringBuilder builder=new StringBuilder();
     try{
        URL restServiceURL = new URL("http://core.piam.cn1-dev.mindsphere-in.cn/uaa/passcode?login_hint=core");
        HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
        httpConnection.setConnectTimeout(10000);
        httpConnection.setReadTimeout(10000);
        httpConnection.setRequestMethod("GET");
        httpConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        httpConnection.setRequestProperty("Cookie","X-Uaa-Csrf=zQ4JRa4CLfoy1wtT9VLQPS; JSESSIONID=4B80CCCE2657F3A27716CFD0FC852C3B; Current-User=%7B%22userId%22%3A%2258d14dde-da9f-47e4-9d8b-e13dced09217%22%7D; SERVERID=327eb6a5c0ecc7c7d52f0f44cfcaacac|1567933855|1567933821");
         httpConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");

        if (httpConnection.getResponseCode() != 200) {
            throw new RuntimeException("HTTP GET Request Failed with Error code : "
                    + httpConnection.getResponseCode());
        }
        InputStream inStrm = httpConnection.getInputStream();
        byte []b=new byte[1024];
        int length=-1;
        while((length=inStrm.read(b))!=-1){
            builder.append(new String(b,0,length));
        }
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
            String html = builder.toString();
            Document doc = Jsoup.parse(html);
            Elements h2Elements = doc.select("h2");
            code = h2Elements.get(0).text();

        return code;
    }
}
