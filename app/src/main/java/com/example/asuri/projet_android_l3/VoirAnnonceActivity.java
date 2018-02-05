package com.example.asuri.projet_android_l3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class VoirAnnonceActivity extends AppCompatActivity {

    //association avec la vue
    String idAnnonce;
    TextView pseudo;
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

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                this.idAnnonce= null;
            } else {
                this.idAnnonce= extras.getString("id");
            }
        } else {
            Log.e("error","Error, pas d'id en param");
        }

        this.titreAnnonce = findViewById(R.id.titreAnnonce);
        this.imgAnnonce = findViewById(R.id.imgAnnonce);
        this.prixAnnonce = findViewById(R.id.prixAnnonce);
        this.adresseAnnonce = findViewById(R.id.adresseAnnonce);
        this.descriptionAnnonce = findViewById(R.id.descriptionAnnonce);
        this.dateAnnonce = findViewById(R.id.dateAnnonce);
        this.contactAnnonce = findViewById(R.id.contactAnnonce);
        this.mailAnnonce = findViewById(R.id.mailAnnonce);
        this.telAnnonce = findViewById(R.id.telAnnonce);

        String url = "https://ensweb.users.info.unicaen.fr/android-api/mock-api/details/"+this.idAnnonce+".json";

        // Module picasso pour charger image par défault
        Picasso.with(getApplicationContext()).load(R.drawable.photo_default).into(imgAnnonce);

        Controller controller = new Controller(this, annonce);
        controller.getJsonURL(url);
    }

    public void setVoirAnnonceValues(JSONObject json) throws JSONException {
        annonce.setId(json.getJSONObject("response").getString("id"));
        annonce.setTitre(json.getJSONObject("response").getString("titre"));
        annonce.setImages(json.getJSONObject("response").getJSONArray("images"));
        annonce.setPrix(json.getJSONObject("response").getInt("prix"));
        annonce.setCp(json.getJSONObject("response").getString("cp"));
        annonce.setVille(json.getJSONObject("response").getString("ville"));
        annonce.setDescription(json.getJSONObject("response").getString("description"));
        annonce.setDate(json.getJSONObject("response").getString("date"));
        annonce.setPseudo(json.getJSONObject("response").getString("pseudo"));
        annonce.setEmailContact(json.getJSONObject("response").getString("emailContact"));
        annonce.setTelContact(json.getJSONObject("response").getString("telContact"));


        titreAnnonce.setText(annonce.getTitre());
        Picasso.with(getApplicationContext()).load(annonce.getImage()).into(imgAnnonce);
        prixAnnonce.setText(annonce.getPrix()+"€");
        adresseAnnonce.setText(annonce.getCp() + " " + annonce.getVille());
        descriptionAnnonce.setText(annonce.getDescription());
        dateAnnonce.setText(annonce.getFormatedDate(annonce.getDate()));
        contactAnnonce.setText("Contacter "+annonce.getPseudo());
        mailAnnonce.setText(annonce.getEmailContact());
        telAnnonce.setText(annonce.getTelContact());
    }
}


