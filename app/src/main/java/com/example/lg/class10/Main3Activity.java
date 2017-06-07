package com.example.lg.class10;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Main3Activity extends AppCompatActivity {

    ListView list;
    ArrayList<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        list = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                data);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"실습4");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Main3Activity.this, Main4Activity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    Handler handler = new Handler();
    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                URL url = new
                        URL("https://news.google.com/news?cf=all&hl=ko&pz=1&ned=kr&topic=m&output=rss");
                HttpURLConnection urlConnection =
                        (HttpURLConnection) url.openConnection();//rss연결
                if (urlConnection.getResponseCode() ==
                        HttpURLConnection.HTTP_OK) {//연결되면
                    readData(urlConnection.getInputStream());//데이터 읽기
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged(); //리스트뷰에 업데이트
                        }
                    });
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    int readData(InputStream is) {
        DocumentBuilderFactory builderFactory =
                DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(is);
            int datacount = parseDocument(document);
            return datacount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int parseDocument(Document doc) {
        Element docEle = doc.getDocumentElement();
        NodeList nodelist = docEle.getElementsByTagName("item");
        int count = 0;
        if ((nodelist != null) && (nodelist.getLength() > 0)) {
            for (int i = 0; i < nodelist.getLength(); i++) {
                String newsItem = getTagData(nodelist, i);
                if (newsItem != null) {
                    data.add(newsItem);//어레이에 아이템추가
                    count++;
                }
            }
        }
        return count;
    }

    private String getTagData(NodeList nodelist, int index) {
        String newsItem = null;
        try {
            Element entry = (Element) nodelist.item(index); //내용가져오기
            Element title = (Element) entry.getElementsByTagName("title").item(0); // 제목가져오기
            Element pubDate = (Element) entry.getElementsByTagName("pubDate").item(0); // 날짜 가져오기
            String titleValue = null;
            if (title != null) {
                Node firstChild = title.getFirstChild();
                if (firstChild != null) titleValue = firstChild.getNodeValue();
            }
            String pubDateValue = null;
            if (pubDate != null) {
                Node firstChild = pubDate.getFirstChild();
                if (firstChild != null) pubDateValue = firstChild.getNodeValue();
            }
            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("YYYY-MM-dd");
            Date date = new Date();
            newsItem = titleValue + "-" + simpleDateFormat.format(date.parse(pubDateValue));//제목 날짜로 String만들기
        } catch (DOMException e) {
            e.printStackTrace();
        }
        return newsItem; //데이터 리턴
    }

    public void onClick3(View v){
        thread.start();
    }

}
