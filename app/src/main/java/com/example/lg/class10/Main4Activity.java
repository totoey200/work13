package com.example.lg.class10;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class Main4Activity extends AppCompatActivity {

    EditText id,pwd;
    TextView tv;
    String userid,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        id = (EditText)findViewById(R.id.id);
        pwd = (EditText)findViewById(R.id.pwd);
        tv = (TextView)findViewById(R.id.tv);
    }
    Handler handler = new Handler();
    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                URL url = new URL("http://jerry1004.dothome.co.kr/info/login.php");
                HttpURLConnection httpURLConnection =
                        (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                String postData = "userid=" + URLEncoder.encode(userid)
                        + "&password=" + URLEncoder.encode(password);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postData.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                InputStream inputStream;
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    inputStream = httpURLConnection.getInputStream();
                else
                    inputStream = httpURLConnection.getErrorStream();
                final String result = loginResult(inputStream);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("FAIL"))
                            tv.setText("로그인이 실패했습니다.");
                        else
                            tv.setText(result + "님 로그인 성공");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    String loginResult(InputStream inputStream){
        String data = "";
        Scanner s = new Scanner(inputStream);
        while(s.hasNext()) data += s.nextLine();
        s.close();
        return data;
    }

    public void onClick4(View v){
        userid = id.getText().toString();
        password = pwd.getText().toString();
        thread.start();
    }
}
