package com.example.asuri.projet_android_l3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

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

    final Annonce annonce = new Annonce();

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

        // Module picasso pour charger image par défault
        Picasso.with(getApplicationContext()).load(R.drawable.photo_default).into(imgAnnonce);

        
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://ensweb.users.info.unicaen.fr/android-api/mock-api/completeAdWithImages.json";
        Controller controller = new Controller(this, annonce);
        controller.getJsonURL(url);
    }
}


