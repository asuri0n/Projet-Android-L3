package com.example.asuri.projet_android_l3;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by asuri on 28/01/2018.
 */

class GetJSONFromUrl extends AsyncTask<String, Void, Void> {

    private Annonce annonce;

    // a constructor so that you can pass the object and use
    GetJSONFromUrl(Annonce annonce){
        this.annonce = annonce;
    }

    protected Void doInBackground(String... urls) {

        HttpsURLConnection con = null;
        try {
            URL u = new URL(urls[0]);
            con = (HttpsURLConnection) u.openConnection();
            con.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            annonce.setTitre("test");
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        return null;
    }

}
