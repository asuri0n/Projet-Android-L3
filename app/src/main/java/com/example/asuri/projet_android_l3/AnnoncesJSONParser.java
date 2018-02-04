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

public class AnnoncesJSONParser {

    static List<Annonce> annoncesList;

    public static List<Annonce> parseData(String content) {

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

}