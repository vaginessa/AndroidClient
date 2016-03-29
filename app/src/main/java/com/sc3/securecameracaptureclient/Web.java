package com.sc3.securecameracaptureclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.extras.Base64;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Nathan on 3/27/2016.
 */
public class Web {

    public String GLOBALIPADDRESS = "139.71.78.159";
    public String myLoginParameters = "";
    public String G_myParameters = "";
    public String G_URI = "";
    public JSONObject jO = null;
    public String mostRecentPictureName = "";

    public Web(JSONObject jO) {
        this.jO = jO;
    }

/*
  This is move to somewhere soon...
    private String getIpFromFile() {
        String directory = "";//Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location);
        String iniFile = directory + "\\config.ini";
        if (File.Exists(iniFile)) {
            String configFile = System.IO.File.ReadAllText(iniFile);
            String ip = Regex.Match(configFile, "(?=<ip>)(.*?)(?=</ip>)").ToString().Substring(4);

            //LOGIN FORM
            //TODO

            return ip;
        } else {
            //Type in IP
            //TODO
            return "";
        }
    }

    public void setIP(String IP, boolean showLoginForm) {
        GLOBALIPADDRESS = IP;
        String directory = "";//Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location);
        String iniFile = directory + "\\config.ini";
        System.IO.File.WriteAllText(iniFile, "<ip>" + IP + "</ip>");

        if (showLoginForm) {
            //LOGIN FORM
            //TODO
        }
    }*/

    private class getPicture extends AsyncTask< String, Void, Void > {
        protected Void doInBackground(String...params) {
            // Create a new HttpClient and Post Header

            // Create an HostnameVerifier that hardwires the expected hostname.
            // Note that is different than the URL's hostname:
            // example.com versus example.org
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify("example.com", session);
                }
            };

            try {
                // Tell the URLConnection to use our HostnameVerifier
                String URI = "http://" + GLOBALIPADDRESS + "/serve.php";
                URL url = new URL(URI);
                HttpsURLConnection urlConnection =
                        (HttpsURLConnection) url.openConnection();
                urlConnection.setHostnameVerifier(hostnameVerifier);

                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                List<NameValuePair> options = new ArrayList<>();
                options.add(new BasicNameValuePair("picture", params[0]));

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(options));
                writer.flush();
                writer.close();
                os.close();

                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                byte[] inputBytes = new byte[in.available()];
                in.read(inputBytes);

                byte[] decoded = Base64.decode( inputBytes, Base64.DEFAULT );

                Bitmap bitmap = BitmapFactory.decodeByteArray(decoded , 0, decoded .length);

                //Set bitmap to somewhere cool
                //TODO

                //Optional
                //publishProgress((int) ((i / (float) count) * 100));

                String str = new String(inputBytes, "UTF-8");

                System.out.println(str);

            } catch (Exception e) { }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //showDialog("Downloaded " + result + " bytes");
        }

        private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (NameValuePair pair : params)
            {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }

            return result.toString();
        }
    }



    private class Login extends AsyncTask< String, Void, Void > {
        protected Void doInBackground(String...params) {
            // Create a new HttpClient and Post Header

            // Create an HostnameVerifier that hardwires the expected hostname.
            // Note that is different than the URL's hostname:
            // example.com versus example.org
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify("example.com", session);
                }
            };

            try {
                // Tell the URLConnection to use our HostnameVerifier
                String URI = "http://" + GLOBALIPADDRESS + "/login.php";
                URL url = new URL(URI);
                HttpsURLConnection urlConnection =
                        (HttpsURLConnection) url.openConnection();
                urlConnection.setHostnameVerifier(hostnameVerifier);

                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                List<NameValuePair> options = new ArrayList<>();
                options.add(new BasicNameValuePair("username", params[0]));
                options.add(new BasicNameValuePair("password", params[1]));

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(options));
                writer.flush();
                writer.close();
                os.close();

                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                byte[] inputBytes = new byte[in.available()];
                in.read(inputBytes);

                //Optional
                //publishProgress((int) ((i / (float) count) * 100));

                String str = new String(inputBytes, "UTF-8");

                System.out.println(str);

            } catch (Exception e) { }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //showDialog("Downloaded " + result + " bytes");
        }

        private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (NameValuePair pair : params)
            {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }

            return result.toString();
        }
    }

    //How to call
    //new registerAccount().execute(url1, url2, url3);

    private class registerAccount extends AsyncTask< String, Void, Void > {
        protected Void doInBackground(String...params) {
            // Create a new HttpClient and Post Header

            // Create an HostnameVerifier that hardwires the expected hostname.
            // Note that is different than the URL's hostname:
            // example.com versus example.org
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify("example.com", session);
                }
            };

            try {
                // Tell the URLConnection to use our HostnameVerifier
                String URI = "http://" + GLOBALIPADDRESS + "/login.php";
                URL url = new URL(URI);
                HttpsURLConnection urlConnection =
                        (HttpsURLConnection) url.openConnection();
                urlConnection.setHostnameVerifier(hostnameVerifier);

                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                List<NameValuePair> options = new ArrayList<>();
                options.add(new BasicNameValuePair("username", params[0]));
                options.add(new BasicNameValuePair("password", params[1]));
                options.add(new BasicNameValuePair("number", params[2]));

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(options));
                writer.flush();
                writer.close();
                os.close();

                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                byte[] inputBytes = new byte[in.available()];
                in.read(inputBytes);

                //Optional
                //publishProgress((int) ((i / (float) count) * 100));

                String str = new String(inputBytes, "UTF-8");

                System.out.println(str);

            } catch (Exception e) { }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //showDialog("Downloaded " + result + " bytes");
        }

        private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (NameValuePair pair : params)
            {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }

            return result.toString();
        }
    }
}
