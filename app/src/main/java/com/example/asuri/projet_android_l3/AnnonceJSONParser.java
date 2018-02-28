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

class AnnonceJSONParser {

    static List<Annonce> parseAnnoncesList(String content) {

        JSONArray annonces_arry = null;
        Annonce annonce = null;

        try {
            annonces_arry = new JSONObject(content).getJSONArray("response");
            List<Annonce> annoncesList = new ArrayList<>();

            for (int i = 0; i < annonces_arry.length(); i++) {

                JSONObject obj = annonces_arry.getJSONObject(i);
                annonce = new Annonce();

                setAttributs(annonce, obj);

                annoncesList.add(annonce);
            }
            return annoncesList;

        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    static Annonce parseAnnonce(String content) {

        try {
            Annonce annonce = new Annonce();
            JSONObject annonces_arry = new JSONObject(content).getJSONObject("response");
            setAttributs(annonce, annonces_arry);

            return annonce;
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void setAttributs(Annonce annonce, JSONObject annonces_arry) throws JSONException {

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
    }
}