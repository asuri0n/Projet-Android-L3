package com.example.asuri.projet_android_l3;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.Thread.sleep;

/**
 * Created by asuri on 28/01/2018.
 */

class GetJSONFromUrl extends AsyncTask<String, Integer, JSONObject> {

    private Annonce annonce;
    private VoirAnnonceActivity VoirAnnonceActivity;

    public GetJSONFromUrl(VoirAnnonceActivity voirAnnonceActivity, Annonce annonce) {
        this.VoirAnnonceActivity = voirAnnonceActivity;
        this.annonce = annonce;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        try {
            VoirAnnonceActivity.idAnnonce.setText(result.getJSONObject("response").getString("id"));
            VoirAnnonceActivity.titreAnnonce.setText(result.getJSONObject("response").getString("titre"));
            VoirAnnonceActivity.descriptionAnnonce.setText(result.getJSONObject("response").getString("description"));
            VoirAnnonceActivity.prixAnnonce.setText(result.getJSONObject("response").getString("prix"));
            VoirAnnonceActivity.imgAnnonce.setImageResource(R.drawable.annonce);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }
    @Override
    protected JSONObject doInBackground(String... urls) {

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
            return new JSONObject(sb.toString());
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        return null;
    }

}
