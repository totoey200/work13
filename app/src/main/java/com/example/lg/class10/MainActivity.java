package com.example.lg.class10;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    String SERVER_IP = "172.17.65.228";
    String SERVER_PORT= "200";
    String msg = "";
    EditText e1;

    public void onClick(View v){
        msg = e1.getText().toString();
        myThread.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = (EditText)findViewById(R.id.etmsg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"실습2");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    Handler mHandler = new Handler();

    Thread myThread = new Thread(){
        @Override
        public void run() {
            try {
                Socket aSocket = new Socket(SERVER_IP, Integer.parseInt(SERVER_PORT));
                System.out.println("Client] 서버 접속");

                ObjectOutputStream outstream = new ObjectOutputStream(aSocket.getOutputStream());
                outstream.writeObject(msg);
                outstream.flush();

                ObjectInputStream instream = new ObjectInputStream(aSocket.getInputStream());
                final Object obj = instream.readObject();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),(String)obj,Toast.LENGTH_SHORT).show();
                    }
                });

                aSocket.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

}
