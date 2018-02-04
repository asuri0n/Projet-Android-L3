package com.example.asuri.projet_android_l3;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asuri on 04/02/2018.
 */

public class Controller {

    VoirAnnonceActivity voirAnnonceActivity;
    Annonce annonce;

    public Controller(VoirAnnonceActivity voirAnnonceActivity, Annonce annonce) {
        this.voirAnnonceActivity = voirAnnonceActivity;
        this.annonce = annonce;
    }

    public void getResponse(int method, String url, JSONObject jsonValue, final VolleyCallback callback) {
        StringRequest strreq = new StringRequest(Request.Method.GET, url, new Response.Listener < String > () {

            @Override
            public void onResponse(String Response) {
                callback.onSuccessResponse(Response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                Toast.makeText(voirAnnonceActivity.getApplicationContext(), e + "error", Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(voirAnnonceActivity.getApplicationContext()).addToRequestQueue(strreq);
    }

    public void getJsonURL(String url) {
        getResponse(Request.Method.GET, url, null, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    Toast.makeText(voirAnnonceActivity.getApplicationContext(), json.getJSONObject("response").getString("titre") + "", Toast.LENGTH_LONG).show();
                    // do your work with response object

                    annonce.setId(json.getJSONObject("response").getString("id"));
                    annonce.setTitre(json.getJSONObject("response").getString("titre"));
                    annonce.addImage(json.getJSONObject("response").getString("images"));
                    annonce.setPrix(json.getJSONObject("response").getInt("prix"));
                    annonce.setCp(json.getJSONObject("response").getString("cp"));
                    annonce.setVille(json.getJSONObject("response").getString("ville"));
                    annonce.setDescription(json.getJSONObject("response").getString("description"));
                    annonce.setDate(json.getJSONObject("response").getString("date"));
                    annonce.setPseudo(json.getJSONObject("response").getString("pseudo"));
                    annonce.setEmailContact(json.getJSONObject("response").getString("emailContact"));
                    annonce.setTelContact(json.getJSONObject("response").getString("telContact"));


                    voirAnnonceActivity.titreAnnonce.setText(annonce.getTitre());
                    voirAnnonceActivity.imgAnnonce.setImageResource(R.drawable.photo_default); // En attendant
                    voirAnnonceActivity.prixAnnonce.setText(annonce.getPrix()+"€");
                    voirAnnonceActivity.adresseAnnonce.setText(annonce.getCp() + " " + annonce.getVille());
                    voirAnnonceActivity.descriptionAnnonce.setText(annonce.getDescription());
                    voirAnnonceActivity.dateAnnonce.setText(annonce.getFormatedDate(annonce.getDate()));
                    voirAnnonceActivity.contactAnnonce.setText("Contacter "+annonce.getPseudo());
                    voirAnnonceActivity.mailAnnonce.setText(annonce.getEmailContact());
                    voirAnnonceActivity.telAnnonce.setText(annonce.getTelContact());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
