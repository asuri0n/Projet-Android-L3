package com.example.asuri.projet_android_l3;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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
            annonce.setId(result.getJSONObject("response").getString("id"));
            annonce.setTitre(result.getJSONObject("response").getString("titre"));
            annonce.addImage(result.getJSONObject("response").getString("images"));
            annonce.setPrix(result.getJSONObject("response").getInt("prix"));
            annonce.setCp(result.getJSONObject("response").getString("cp"));
            annonce.setVille(result.getJSONObject("response").getString("ville"));
            annonce.setDescription(result.getJSONObject("response").getString("description"));
            annonce.setDate(result.getJSONObject("response").getString("date"));
            annonce.setPseudo(result.getJSONObject("response").getString("pseudo"));
            annonce.setEmailContact(result.getJSONObject("response").getString("emailContact"));
            annonce.setTelContact(result.getJSONObject("response").getString("telContact"));

            VoirAnnonceActivity.titreAnnonce.setText(annonce.getTitre());
            VoirAnnonceActivity.imgAnnonce.setImageResource(R.drawable.annonce); // En attendant
            VoirAnnonceActivity.prixAnnonce.setText(annonce.getPrix()+"€");
            VoirAnnonceActivity.adresseAnnonce.setText(annonce.getCp() + " " + annonce.getVille());
            VoirAnnonceActivity.descriptionAnnonce.setText(annonce.getDescription());
            VoirAnnonceActivity.dateAnnonce.setText(annonce.getFormatedDate(annonce.getDate()));
            VoirAnnonceActivity.contactAnnonce.setText("Contacter "+annonce.getPseudo());
            VoirAnnonceActivity.mailAnnonce.setText(annonce.getEmailContact());
            VoirAnnonceActivity.telAnnonce.setText(annonce.getTelContact());
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
