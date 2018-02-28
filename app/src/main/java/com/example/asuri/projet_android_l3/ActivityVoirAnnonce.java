package com.example.asuri.projet_android_l3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Set;

public class ActivityVoirAnnonce extends AppCompatActivity {

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

    Annonce annonce = new Annonce();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_annonce);

        this.titreAnnonce = findViewById(R.id.titreAnnonce);
        this.imgAnnonce = findViewById(R.id.imgAnnonce);
        this.prixAnnonce = findViewById(R.id.prixAnnonce);
        this.adresseAnnonce = findViewById(R.id.adresseAnnonce);
        this.descriptionAnnonce = findViewById(R.id.descriptionAnnonce);
        this.dateAnnonce = findViewById(R.id.dateAnnonce);
        this.contactAnnonce = findViewById(R.id.contactAnnonce);
        this.mailAnnonce = findViewById(R.id.mailAnnonce);
        this.telAnnonce = findViewById(R.id.telAnnonce);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            String url = "https://ensweb.users.info.unicaen.fr/android-api/?apikey=21404260&method=randomAd";
            // Module picasso pour charger image par défault
            Picasso.with(getApplicationContext()).load(R.drawable.photo_default).into(imgAnnonce);
            requestData(url);
        } else {
            annonce = (Annonce)getIntent().getSerializableExtra("annonce"); //Obtaining data
            fillAnnonceData(annonce);
        }
        if(getIntent().getStringExtra("message") != null){
            Toast.makeText(ActivityVoirAnnonce.this, getIntent().getStringExtra("message"), Toast.LENGTH_SHORT).show();
        }

        if(!Objects.equals(annonce.getEmailContact(), "") || annonce.getEmailContact() != null ) {
            this.mailAnnonce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:" + annonce.getEmailContact());
                    intent.setData(data);
                    startActivity(intent);
                }
            });
        }

        if(!Objects.equals(annonce.getTelContact(), "") || annonce.getTelContact() != null ) {
            this.telAnnonce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("tel:"+annonce.getTelContact());
                    intent.setData(data);
                    startActivity(intent);
                }
            });
        }
    }

    public void requestData(String uri) {

        StringRequest request = new StringRequest(uri,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        annonce = AnnonceJSONParser.parseAnnonce(response);
                        fillAnnonceData(annonce);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityVoirAnnonce.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void fillAnnonceData(Annonce annonce) {
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

    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


