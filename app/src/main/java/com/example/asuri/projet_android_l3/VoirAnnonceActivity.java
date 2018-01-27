package com.example.asuri.projet_android_l3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VoirAnnonceActivity extends AppCompatActivity {


    Annonce annonce=new Annonce();
    //association avec la vue

    TextView idAnnonce;
    TextView titreAnnonce;
    TextView descriptionAnnonce;
    ImageView imgAnnonce;
    TextView prixAnnonce;
    TextView adresseAnnonce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_annonce);

        this.idAnnonce = findViewById(R.id.idAnnonce);
        this.titreAnnonce = findViewById(R.id.titreAnnonce);
        this.descriptionAnnonce = findViewById(R.id.descriptionAnnonce);
        this.imgAnnonce = findViewById(R.id.imgAnnonce);
        this.prixAnnonce = findViewById(R.id.prixAnnonce);
        this.adresseAnnonce = findViewById(R.id.adresseAnnonce);

        this.annonce=recupJson();

        this.idAnnonce.setText(this.annonce.getId());
        this.titreAnnonce.setText(this.annonce.getTitre());

    }

    protected Annonce recupJson() {
        try {
            String myurl = "https://ensweb.users.info.unicaen.fr/android-api/mock-api/completeAd.json";

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            /*
             * InputStreamOperations est une classe complémentaire:
             * Elle contient une méthode InputStreamToString.
             */
            String result = InputStreamOperations.InputStreamToString(inputStream);

            // On récupère le JSON complet
            JSONObject jsonObject = new JSONObject(result);
            // On récupère le tableau d'objets qui nous concernent
            JSONArray array = new JSONArray(jsonObject.getString("response"));
            // Pour tous les objets on récupère les infos
            // On récupère un objet JSON du tableau
            JSONObject obj = new JSONObject(array.getString(0));
            // On fait le lien Personne - Objet JSON
            Annonce annonce = new Annonce();
            annonce.setId(obj.getString("id"));
            annonce.setTitre(obj.getString("titre"));
            return annonce;




        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}


