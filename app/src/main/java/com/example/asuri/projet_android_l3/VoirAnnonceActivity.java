package com.example.asuri.projet_android_l3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class VoirAnnonceActivity extends AppCompatActivity {


    //association avec la vue

    String idAnnonce;
    String pseudo;

    TextView titreAnnonce;
    ImageView imgAnnonce;
    TextView prixAnnonce;
    TextView adresseAnnonce;
    TextView descriptionAnnonce;

    TextView dateAnnonce;
    TextView contactAnnonce;
    TextView mailAnnonce;
    TextView telAnnonce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_annonce);

        this.idAnnonce = "3mdzx";
        this.pseudo = "Carl";

        this.titreAnnonce = findViewById(R.id.titreAnnonce);
        this.imgAnnonce = findViewById(R.id.imgAnnonce);
        this.prixAnnonce = findViewById(R.id.prixAnnonce);
        this.adresseAnnonce = findViewById(R.id.adresseAnnonce);
        this.descriptionAnnonce = findViewById(R.id.descriptionAnnonce);

        this.dateAnnonce = findViewById(R.id.dateAnnonce);
        this.contactAnnonce = findViewById(R.id.contactAnnonce);
        this.mailAnnonce = findViewById(R.id.mailAnnonce);
        this.telAnnonce = findViewById(R.id.telAnnonce);

        String myurl = "https://ensweb.users.info.unicaen.fr/android-api/mock-api/completeAd.json";

        Annonce annonce = new Annonce();
        new GetJSONFromUrl(this, annonce).execute(myurl);
    }
}


