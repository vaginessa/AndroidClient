package com.sc3.securecameracaptureclient;

/**
 * Created by Nathan on 3/28/2016.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TestSignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    public String GLOBALIPADDRESS = "";
    public boolean ipaddressSettingExits = false;

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_signup_activity);
        ButterKnife.inject(this);

        final SharedPreferences settings = getSharedPreferences("MySettingsFile", 0);
        String IPAddress = settings.getString("ipaddress", "");
        ipaddressSettingExits = !IPAddress.equals("");
        GLOBALIPADDRESS = IPAddress;

        final Context c = this;

        _signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(ipaddressSettingExits)
                    signup();
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(c);
                    builder.setMessage("IP Address of Server not Set.")
                            .setPositiveButton("Set Now", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(c);
                                    // Get the layout inflater
                                    LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                    // Inflate and set the layout for the dialog
                                    // Pass null as the parent view because its going in the dialog layout
                                    final View v_iew = inflater.inflate(R.layout.ip_address, null);
                                    builder.setView(v_iew)
                                            //.setTitle("IP Address Of the Server")
                                            // Add action buttons
                                            .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //Save the ip Address
                                                    EditText _ipaddress = (EditText) v_iew.findViewById(R.id.ipaddress_editText);

                                                    SharedPreferences.Editor e = settings.edit();
                                                    //Probably should valdate these settings first
                                                    //TODO validate
                                                    if(_ipaddress.getText() != null && !_ipaddress.getText().toString().equals("")) {
                                                        e.putString("ipaddress", _ipaddress.getText().toString());
                                                        e.apply();
                                                        ipaddressSettingExits = true;
                                                    } else {
                                                        _ipaddress.setError("Please enter valid IP Address");
                                                    }
                                                }
                                            })
                                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    dialog.cancel();
                                    builder.show();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    builder.show();
                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(TestSignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String number = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        (new UserRegisterTask(email, password, number, progressDialog)).execute();
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void onRegisterSuccess(String JSON) {
        //progressDialog.dismiss();
        //_loginButton.setEnabled(true);
        JSONObject jo = new JSONParser(JSON).jO;
        Intent yearIntent = new Intent(getBaseContext(), YearViewActivity.class);
        yearIntent.putParcelableArrayListExtra("JSONTREE", jo.year);
        startActivity(yearIntent);
        finish();
    }

    public void onRegisterFailed() {
        //progressDialog.dismiss();
        Toast.makeText(getBaseContext(), "Unable to Create Account", Toast.LENGTH_LONG).show();
        //_loginButton.setEnabled(true);
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mNumber;
        private final ProgressDialog mProgressDialog;
        StringBuffer response;

        UserRegisterTask(String email, String password, String number, ProgressDialog progressDialog) {
            mEmail = email;
            mPassword = password;
            mNumber = number;
            mProgressDialog = progressDialog;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            response = new StringBuffer();

            try {
                // Create a new HttpClient and Post Header

                String URI = "https://" + GLOBALIPADDRESS + "/login.php";

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

                String myLoginParameters = "username=" + mEmail + "&password=" + mPassword;
                String myRegParameters = "username=" + mEmail + "&password=" + mPassword + "&number=" + mNumber;
                String myInitialParameters = "username=" + "user" + "&password=" + "password" + "&number=" + mNumber;
                os.write(myInitialParameters.getBytes("UTF-8"));//getQuery(options));
                os.flush();
                os.close();

                int responseCode = urlConnection.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + myInitialParameters);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                //StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //System.out.println(response);

                if (response.substring(0,1).equals("0")) {
                    response = new StringBuffer();
                    os = urlConnection.getOutputStream();

                    os.write(myRegParameters.getBytes("UTF-8"));//getQuery(options));
                    os.flush();
                    os.close();

                    responseCode = urlConnection.getResponseCode();
                    System.out.println("\nSending 'POST' request to URL : " + url);
                    System.out.println("Post parameters : " + myRegParameters);
                    System.out.println("Response Code : " + responseCode);

                    in = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream()));
                    //StringBuffer response = new StringBuffer();
                    inputLine = "";

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    if (response.substring(0,1).equals("0")) {
                        response = new StringBuffer();
                        os = urlConnection.getOutputStream();

                        os.write(myLoginParameters.getBytes("UTF-8"));//getQuery(options));
                        os.flush();
                        os.close();

                        responseCode = urlConnection.getResponseCode();
                        System.out.println("\nSending 'POST' request to URL : " + url);
                        System.out.println("Post parameters : " + myLoginParameters);
                        System.out.println("Response Code : " + responseCode);

                        in = new BufferedReader(
                                new InputStreamReader(urlConnection.getInputStream()));
                        //StringBuffer response = new StringBuffer();
                        inputLine = "";

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        if (response.substring(0,1).equals("0")) {
                            return true;
                        } else {
                            return false;
                        }

                    } else {
                        return false;
                    }

                }
                else {
                    return false;
                }

            } catch (Exception e) {
                System.out.println(e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mProgressDialog.dismiss();
            if( success ){
                onRegisterSuccess(response.substring(1));
            } else {
                onRegisterFailed();
            }

        }

        @Override
        protected void onCancelled() {
            onRegisterFailed();
        }
    }
}
