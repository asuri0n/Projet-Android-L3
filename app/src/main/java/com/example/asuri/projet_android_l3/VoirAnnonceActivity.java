package com.example.asuri.projet_android_l3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

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
        new GetJSONFromUrl(this, annonce).execute(myurl);
    }
}


