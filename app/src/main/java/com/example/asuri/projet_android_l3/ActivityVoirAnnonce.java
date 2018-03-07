package com.example.asuri.projet_android_l3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ActivityVoirAnnonce extends AppCompatActivity {

    // Classe singleton pour mettre les variables globales comme la clé API et l'url API
    GlobalsVariables global = GlobalsVariables.getInstance();

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

    Annonce annonce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_annonce);
        setTitle("Chargement ...");

        // Affichage de la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Affichage du bouton retour
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.titreAnnonce = findViewById(R.id.titreAnnonce);
        this.imgAnnonce = findViewById(R.id.imgAnnonce);
        this.prixAnnonce = findViewById(R.id.prixAnnonce);
        this.adresseAnnonce = findViewById(R.id.adresseAnnonce);
        this.descriptionAnnonce = findViewById(R.id.descriptionAnnonce);
        this.dateAnnonce = findViewById(R.id.dateAnnonce);
        this.contactAnnonce = findViewById(R.id.contactAnnonce);
        this.mailAnnonce = findViewById(R.id.mailAnnonce);
        this.telAnnonce = findViewById(R.id.telAnnonce);

        // Récupère les variables passées en paramètre lors d'une nouvelle activité
        Bundle extras = getIntent().getExtras();
        annonce = new Annonce();


        // Si il n'y a pas de paramètres, alors on charge une annonce aléatoire
        if (extras == null) {
            String apiUrlRandomAnnonce = global.getAPIURL() + "?apikey=" + global.getAPIKEY() + "&method=randomAd";
            // Module picasso pour charger image par défault
            Picasso.with(getApplicationContext()).load(R.drawable.photo_default).into(imgAnnonce);
            requestRandomAnnonce(apiUrlRandomAnnonce);
        } else {
            // Sinon on récupère l'objet sérialisé Annonce
            annonce = (Annonce) getIntent().getSerializableExtra("annonce");
            // On affiches ses attributs sur le layout
            fillAnnonceData(annonce);
        }

        // Si il y a un message en paramètre, on affiche le message en Toast
        if (getIntent().getStringExtra("message") != null) {
            Toast.makeText(ActivityVoirAnnonce.this, getIntent().getStringExtra("message"), Toast.LENGTH_SHORT).show();
        }

        // Si l'email a été indiqué dans le profil ...
        if (!Objects.equals(annonce.getEmailContact(), "") || annonce.getEmailContact() != null) {
            // ... alors on créer un listener sur le mail ...
            this.mailAnnonce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    // ... qui affiche l'application mail avec le mail du propriétaire de l'annonce en destinataire
                    Uri data = Uri.parse("mailto:" + annonce.getEmailContact());
                    intent.setData(data);
                    startActivity(intent);
                }
            });
        }

        // Si le telephone a été indiqué dans le profil ...
        if (!Objects.equals(annonce.getTelContact(), "") || annonce.getTelContact() != null) {
            // ... alors on créer un listener sur le telephone ...
            this.telAnnonce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    // ... qui affiche l'application téléphone avec le numéro du propriétaire de l'annonce
                    Uri data = Uri.parse("tel:" + annonce.getTelContact());
                    intent.setData(data);
                    startActivity(intent);
                }
            });
        }
    }

    public void requestRandomAnnonce(String uri) {

        StringRequest request = new StringRequest(uri,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // On transforme le string reçu par l'API Rest en Objet JSON
                            JSONObject jsonObject = new JSONObject(response);
                            // Si pas d'erreurs retournées par l'API Rest
                            if (jsonObject.getBoolean("success")) {
                                // On récupère les données recu par l'API Rest pour mettre a jour l'objet Annonce
                                annonce = AnnonceJSONParser.parseAnnonce(response);
                                fillAnnonceData(annonce);
                                // On modifie le titre de l'activité pour afficher l'ID de l'annonce
                                setTitle("Annonce N°" + annonce.getId());
                            } else
                                Toast.makeText(getApplicationContext(), "ERREUR:" + jsonObject.getString("response"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    /**
     * Prend un objet Annonce en paramètre et affiches ses attributs sur le layout
     * En profite pour modifier le titre de l'activité pour afficher l'ID de l'annonce
     *
     * @param annonce
     */
    private void fillAnnonceData(Annonce annonce) {
        setTitle("Annonce N°" + annonce.getId());
        titreAnnonce.setText(annonce.getTitre());
        Picasso.with(getApplicationContext()).load(annonce.getImage()).into(imgAnnonce);
        prixAnnonce.setText(annonce.getPrix() + "€");
        adresseAnnonce.setText(annonce.getCp() + " " + annonce.getVille());
        descriptionAnnonce.setText(annonce.getDescription());
        dateAnnonce.setText(annonce.getFormatedDate(annonce.getDate()));
        contactAnnonce.setText("Contacter " + annonce.getPseudo());
        mailAnnonce.setText(annonce.getEmailContact());
        telAnnonce.setText(annonce.getTelContact());
    }

    /**
     * Gestion du clique sur le menu.
     *
     * @param item MenuItem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.voir_annonce:
                newIntent(ActivityVoirAnnonce.class);
                return true;

            case R.id.deposer_annonce:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String prefPseudo = prefs.getString("pseudo", "");
                String prefMail = prefs.getString("email", "");
                String prefTel = prefs.getString("phone", "");

                if (Objects.equals(prefPseudo, "") || Objects.equals(prefMail, "") || Objects.equals(prefTel, "")) {
                    Toast.makeText(getApplicationContext(), "Avant de déposer une annonce, vous devez renseignez vos informations dans 'MON PROFIL'", Toast.LENGTH_LONG).show();
                } else {
                    newIntent(ActivityDeposerAnnonce.class);
                }
                return true;

            case R.id.list_annonce:
                newIntent(ActivityListeAnnonces.class);
                return true;

            case R.id.mon_profil:
                newIntent(ActivityMonProfil.class);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Ajoute le layout menu à l'ActionBar
     *
     * @param menu Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Si le bouton Retour (<-) est cliqué, on termine l'activité
     */
    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    /**
     * Lancement d'une nouvelle activité
     *
     * @param activity Activité a lancer
     */
    public void newIntent(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
