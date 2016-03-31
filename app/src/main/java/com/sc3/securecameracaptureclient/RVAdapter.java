package com.sc3.securecameracaptureclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Target;
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
 * Created by Nathan on 3/24/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder> {

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView photo;
        TextView title;
        TextView subTitle;

        CardViewHolder(View itemView, final Context c) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            photo = (ImageView) itemView.findViewById(R.id.iv);
            title = (TextView) itemView.findViewById(R.id.image_card_title);
            subTitle = (TextView) itemView.findViewById(R.id.image_card_subTitle);

            assert cv != null;
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent yearIntent = new Intent(c, ImageViewActivity.class);
                    yearIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    yearIntent.putExtra("picture", subTitle.getText().toString());
                    c.startActivity(yearIntent);
                }
            });
        }
    }

    List<imageHolder> cards;
    Context context;
    ArrayList<Integer> drawables = new ArrayList<>();
    public String GLOBALIPADDRESS = "";
    public boolean ipaddressSettingExits = false;

    RVAdapter(List<imageHolder> cards, Context context){
        this.cards = cards;
        this.context = context;
        drawables.add(R.drawable.nature1);
        drawables.add(R.drawable.nature2);
        drawables.add(R.drawable.nature3);

        final SharedPreferences settings = context.getSharedPreferences("MySettingsFile", 0);
        String IPAddress = settings.getString("ipaddress", "");
        ipaddressSettingExits = !IPAddress.equals("");
        GLOBALIPADDRESS = IPAddress;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        CardViewHolder pvh = new CardViewHolder(v, context);
        return pvh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        //cardViewHolder.personName.setText(persons.get(i).name);
        //cardViewHolder.personAge.setText(persons.get(i).age);
        assert cardViewHolder.photo != null;
       /* Picasso.with(cardViewHolder.photo.getContext())
                .load(drawables.get(0))
                .resize(dp2px(220), 0)
                .into(cardViewHolder.photo); */

        cardViewHolder.title.setText(cards.get(i).name);
        cardViewHolder.subTitle.setText(cards.get(i).age);
        new UserLoginTask(cards.get(i).age, cardViewHolder).execute();
    }

    public int dp2px(int dp) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);
        return (int) (dp * displaymetrics.density + 0.5f);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPicture;
        StringBuffer response;
        private CardViewHolder cardViewHolder;

        UserLoginTask(String picture, CardViewHolder cardViewHolder) {
            mPicture = picture;
            this.cardViewHolder = cardViewHolder;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            response = new StringBuffer();

            try {
                // Create a new HttpClient and Post Header

                String URI = "https://" + GLOBALIPADDRESS + "/serve.php";

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

                InputStream caInput = new BufferedInputStream(context.getAssets().open("secure.crt"));
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


                String myParameters = "picture=" + mPicture  + "&type=1";
                os.write(myParameters.getBytes("UTF-8"));//getQuery(options));
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

                System.out.println("Got Image");

                return true;

            } catch (Exception e) {
                System.out.println(e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if( success ){

                byte[] imageAsBytes = Base64.decode(response.toString().getBytes(), Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

                /*Picasso.with(cardViewHolder.photo.getContext())
                        .load(bmp)
                        .resize(dp2px(220), 0)
                        .into(cardViewHolder.photo); */

                cardViewHolder.photo.setImageBitmap(bmp);
            } else {
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
