package com.example.asuri.projet_android_l3;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asuri on 04/02/2018.
 */

public class AnnonceJSONParser {

    static List<Annonce> annoncesList;

    public static List<Annonce> parseDataList(String content) {

        JSONArray annonces_arry = null;
        Annonce annonce = null;
        try {

            annonces_arry = new JSONObject(content).getJSONArray("response");
            annoncesList = new ArrayList<>();

            for (int i = 0; i < annonces_arry.length(); i++) {

                JSONObject obj = annonces_arry.getJSONObject(i);
                annonce = new Annonce();

                annonce.setId(obj.getString("id"));
                annonce.setImages(obj.getJSONArray("images"));
                annonce.setTitre(obj.getString("titre"));
                annonce.setPrix(obj.getInt("prix"));
                annonce.setDescription(obj.getString("description"));

                annoncesList.add(annonce);
            }
            return annoncesList;

        }
        catch (JSONException ex) {
            Log.e("test", ex.toString());
            ex.printStackTrace();
            return null;
        }
    }
    public static Annonce parseData(String content) {

        try {
            Annonce annonce = new Annonce();
            JSONObject annonces_arry = new JSONObject(content).getJSONObject("response");

            annonce.setId(annonces_arry.getString("id"));
            annonce.setTitre(annonces_arry.getString("titre"));
            annonce.setImages(annonces_arry.getJSONArray("images"));
            annonce.setPrix(annonces_arry.getInt("prix"));
            annonce.setCp(annonces_arry.getString("cp"));
            annonce.setVille(annonces_arry.getString("ville"));
            annonce.setDescription(annonces_arry.getString("description"));
            annonce.setDate(annonces_arry.getString("date"));
            annonce.setPseudo(annonces_arry.getString("pseudo"));
            annonce.setEmailContact(annonces_arry.getString("emailContact"));
            annonce.setTelContact(annonces_arry.getString("telContact"));

            return annonce;
        }
        catch (JSONException ex) {
            Log.e("test", ex.toString());
            ex.printStackTrace();
            return null;
        }
    }
}