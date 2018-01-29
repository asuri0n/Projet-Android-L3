package com.example.asuri.projet_android_l3;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class VoirAnnonceActivity extends AppCompatActivity {


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

        String myurl = "https://ensweb.users.info.unicaen.fr/android-api/mock-api/completeAd.json";

        Annonce annonce = new Annonce();
        new GetJSONFromUrl(annonce).execute(myurl);

        this.idAnnonce.setText(annonce.getId());
        this.titreAnnonce.setText(annonce.getTitre());
    }
}


