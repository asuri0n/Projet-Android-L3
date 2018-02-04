package com.example.asuri.projet_android_l3;

import android.text.format.DateUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by asuri on 04/02/2018.
 */

public class Controller {

    VoirAnnonceActivity voirAnnonceActivity;
    Annonce annonce;

    Timestamp timestamp1;
    Date date;

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
        date = new Date();
        timestamp1 = new Timestamp(date.getTime());

        getResponse(Request.Method.GET, url, null, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                date = new Date();
                Timestamp timestamp2 = new Timestamp(date.getTime());
                long milliseconds = timestamp2.getTime() - timestamp1.getTime();
                try {
                    JSONObject json = new JSONObject(result);
                    Toast.makeText(voirAnnonceActivity.getApplicationContext(), "["+json.getJSONObject("response").getString("id")+"] - Chargée en " + milliseconds + " millisecondes", Toast.LENGTH_LONG).show();
                    voirAnnonceActivity.setVoirAnnonceValues(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
