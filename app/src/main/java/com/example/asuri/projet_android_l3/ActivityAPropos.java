package com.example.asuri.projet_android_l3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Objects;

/**
 * Classe qui gère l'activité APropos
 */

public class ActivityAPropos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apropos);
        setTitle("A Propos");

        // Affichage de la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Affichage du bouton retour
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
