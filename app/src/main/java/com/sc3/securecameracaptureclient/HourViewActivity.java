package com.sc3.securecameracaptureclient;

import android.content.Context;
import android.content.SharedPreferences;
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
public class HourViewActivity extends AppCompatActivity {

    private List<Card> years;
    private RecyclerView rv;
    ArrayList<Hour> hourArrayList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle("Hour");
        context = getBaseContext();

        hourArrayList = this.getIntent().getParcelableArrayListExtra("JSONTREE");

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
        for(int i = 0; i < hourArrayList.size(); i++) {
            years.add(new Card(formatHour(hourArrayList.get(i).hour), R.drawable.folder));
        }
    }

    private void initializeAdapter(){
        HourAdapter adapter = new HourAdapter(years, hourArrayList, this);
        rv.setAdapter(adapter);
    }

    private String formatHour(int hour) {

        if(hour<12) {
            if(hour==0) {
                hour=12;
            }
            return hour + ":00 am";
        } else {
            hour-=12;
            return hour + ":00 pm";
        }
    }

    protected void onDestroy() {
        //new UserLogout().execute();
        super.onDestroy();
    }

    public class UserLogout extends AsyncTask<Void, Void, Boolean> {
        StringBuffer response;
        private String key;
        private String ip;

        UserLogout( ) {
            final SharedPreferences settings = context.getSharedPreferences("MySettingsFile", 0);
            key = settings.getString("key", "");
            ip = settings.getString("ipaddress", "");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            response = new StringBuffer();

            try {
                // Create a new HttpClient and Post Header

                String URI = "https://" + ip + "/logout.php";

                // Create an HostnameVerifier that hardwires the expected hostname.

                HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        HostnameVerifier hv =
                                HttpsURLConnection.getDefaultHostnameVerifier();
                        return true;//hv.verify(GLOBALIPADDRESS, session);
                    }
                };

                // Load CAs from an InputStream
                // (could be from a resource or ByteArrayInputStream or ...)
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                // From https://www.washington.edu/itconnect/security/ca/load-der.crt

                InputStream caInput = new BufferedInputStream(getBaseContext().getAssets().open("secure.crt"));
                Certificate ca;
                try {
                    ca = cf.generateCertificate(caInput);
                    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                } finally {
                    caInput.close();
                }

                // Create a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);

                // Create a TrustManager that trusts the CAs in our KeyStore
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                // Create an SSLContext that uses our TrustManager
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);


                // Tell the URLConnection to use a SocketFactory from our SSLContext
                URL url = new URL(URI);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

                urlConnection.setSSLSocketFactory(context.getSocketFactory());
                urlConnection.setHostnameVerifier(hostnameVerifier);

                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();

                if(key.equals("")) { return false; }

                String myParameters = "key=" + key;
                os.write(myParameters.getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = urlConnection.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + myParameters);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.substring(0,1).equals("0");

            } catch (Exception e) {
                System.out.println(e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if( success ){
                //onSuccess(response.toString());
                response = null;
            } else {
                //onFailed();
            }
        }
        @Override
        protected void onCancelled() {
        }
    }
}
