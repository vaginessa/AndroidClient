package com.sc3.securecameracaptureclient;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Nathan on 3/29/2016.
 */
public class YearViewActivity extends AppCompatActivity {

    private List<Card> years;
    private RecyclerView rv;
    ArrayList<Year> yearArrayList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle("Year");
        context = getBaseContext();

        yearArrayList = this.getIntent().getParcelableArrayListExtra("JSONTREE");

        setContentView(R.layout.recycler_view);

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
        years = new ArrayList<>();
        for(int i = 0; i < yearArrayList.size(); i ++) {
            years.add(new Card(yearArrayList.get(i).year_name + "", R.drawable.folder));
        }
    }

    private void initializeAdapter(){
        YearAdapter adapter = new YearAdapter(years, yearArrayList, this);
        rv.setAdapter(adapter);
    }


    @Override
    protected void onDestroy( ) {
        final SharedPreferences settings = context.getSharedPreferences("MySettingsFile", 0);
        String key = settings.getString("key", "");
        String ip = settings.getString("ipaddress", "");

        Intent mServiceIntent = new Intent(context, LogOutService.class);
        mServiceIntent.putExtra("key", key);
        mServiceIntent.putExtra("ip", ip);
        // Starts the IntentService
        context.startService(mServiceIntent);
        super.onDestroy();
    }
}
