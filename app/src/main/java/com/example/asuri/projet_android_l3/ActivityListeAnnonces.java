package com.example.asuri.projet_android_l3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;
import java.util.Objects;

public class ActivityListeAnnonces extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Classe singleton pour mettre les variables globales comme la clé API et l'url API
    GlobalsVariables global = GlobalsVariables.getInstance();

    List<Annonce> annoncesList;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_annonces);
        setTitle("Liste");

        // Affichage de la toolbar avec drawer layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Affichage du drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Affichage du layout du NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.lv = findViewById(R.id.listView);
        String apiUrlGetAll = global.getAPIURL() + "?apikey=" + global.getAPIKEY() + "&method=listAll&fake=image";
        requestAllAnnonces(apiUrlGetAll);
    }

    /**
     * Récupère toutes les annonces ave Volley et les mets dans un adaptateur pour les affichers
     */
    public void requestAllAnnonces(String apiUrlGetAll) {

        StringRequest request = new StringRequest(apiUrlGetAll,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // On récupère les données recu par l'API Rest pour mettre a jour la liste des Annonces
                        annoncesList = AnnonceJSONParser.parseAnnoncesList(response);
                        // Utilisation de l'adapter pour faire la liste des annonces
                        AnnoncesAdapter adapter = new AnnoncesAdapter(ActivityListeAnnonces.this, annoncesList);
                        // Affichage de l'adapter sur la vue
                        lv.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityListeAnnonces.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    /**
     * Gestion de la navigation du panneau de gauche
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Si bouton A Propos est appuyé, démarre l'activité
        if (id == R.id.nav_camera)
            newIntent(ActivityAPropos.class);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

