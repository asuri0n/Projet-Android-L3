package com.example.asuri.projet_android_l3;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ActivityDeposerAnnonce extends AppCompatActivity {

    // Classe singleton pour mettre les variables globales comme la clé API et l'url API
    GlobalsVariables global = GlobalsVariables.getInstance();

    String prefPseudo;
    String prefMail;
    String prefTel;

    TextView pseudoAnnonce;
    TextView mailAnnonce;
    TextView telAnnonce;

    TextView titreAnnonce;
    TextView prixAnnonce;
    TextView villeAnnonce;
    TextView CPAnnonce;
    TextView descriptionAnnonce;
    ImageView image;

    Button deposerAnnonce;
    Button uploadImage;

    Annonce annonce;
    Uri selectedimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposer_annonce);
        setTitle("Déposer");


        // Affichage de la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Affichage du bouton retour
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Récupération des préférences du Profil pour remplir le formulaire automatiquement
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefPseudo = prefs.getString("pseudo", "");
        this.prefMail = prefs.getString("email", "");
        this.prefTel = prefs.getString("phone", "");

        // Deuxième vérification. Normalement si c'est vide, il ne doit pas avoir accès a cette Activité
        if (Objects.equals(this.prefPseudo, "") || Objects.equals(this.prefMail, "") || Objects.equals(this.prefTel, ""))
            finish();

        this.pseudoAnnonce = findViewById(R.id.pseudo);
        this.mailAnnonce = findViewById(R.id.mail);
        this.telAnnonce = findViewById(R.id.tel);
        pseudoAnnonce.setText(prefPseudo);
        mailAnnonce.setText(prefMail);
        telAnnonce.setText(prefTel);

        this.titreAnnonce = findViewById(R.id.titre);
        this.descriptionAnnonce = findViewById(R.id.description);
        this.prixAnnonce = findViewById(R.id.prix);
        this.villeAnnonce = findViewById(R.id.ville);
        this.CPAnnonce = findViewById(R.id.cp);
        this.image = findViewById(R.id.image);

        this.deposerAnnonce = findViewById(R.id.buttonDeposer);
        this.uploadImage = findViewById(R.id.buttonUpload);

        this.deposerAnnonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle("Chargement ...");
                requestSaveAnnonce();
            }
        });

        this.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        this.uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        // Affichage image par défaut
        Picasso.with(getApplicationContext()).load(R.drawable.photo_default).into(image);
    }

    /**
     * Récupèration d'une fonction (dispo ici : https://stackoverflow.com/a/10564727) afin de récupérer le path local d'une URI
     *
     * @param contentUri Uri de l'image
     * @return path local de l'image
     */
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    /**
     * Méthode executé lors du clique sur la zone d'affichage de l'image ou sur le bouton
     * Utilise un provider pour récuperer une image de la bibliothéque d'image de l'utilisateur.
     * Le résultat de startActivityForResult est géré par la méthode onActivityResult()
     */
    protected void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    /**
     * Méthode récupèrant le résultat de startActivityForResult de la méthode getImage();
     * Gestion de l'erreur et affichage de l'image selectionnée sur la page de dépot d'une annonce
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "Vous n'avez pas choisi d'image", Toast.LENGTH_LONG).show();
            else if (resultCode == RESULT_OK) {
                selectedimg = data.getData();
                image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
            } else
                Toast.makeText(this, "Erreur récupèration de l'image", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utilisation de Volley pour sauvegarder l'annonce sur l'API Rest
     */
    public void requestSaveAnnonce() {

        StringRequest request = new StringRequest(Request.Method.POST, global.getAPIURL(),

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
                                // Si il y a une image de selectionnée alors on l'envoie sur l'API Rest
                                if (selectedimg != null)
                                    requestSaveImage();
                                else {
                                    // Sinon on créer l'object annonce et on le transmet a l'activité VoirAnnonce
                                    Intent intent = new Intent(getApplicationContext(), ActivityVoirAnnonce.class);
                                    intent.putExtra("annonce", annonce);
                                    startActivity(intent);
                                }
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
                        Toast.makeText(ActivityDeposerAnnonce.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // On passe en paramètre la clé de dev et la méthode
                params.put("apikey", global.getAPIKEY());
                params.put("method", "save");

                // ainsi que les données de l'annonce a sauvegarder
                params.put("titre", titreAnnonce.getText().toString());
                params.put("description", descriptionAnnonce.getText().toString());
                params.put("prix", prixAnnonce.getText().toString());
                params.put("pseudo", pseudoAnnonce.getText().toString());
                params.put("emailContact", mailAnnonce.getText().toString());
                params.put("telContact", telAnnonce.getText().toString());
                params.put("ville", villeAnnonce.getText().toString());
                params.put("ville", villeAnnonce.getText().toString());
                params.put("cp", CPAnnonce.getText().toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    /**
     * Utilisation de OKHTTP3 pour envoyer l'image sur l'API REST
     */
    public void requestSaveImage() {

        OkHttpClient client = new OkHttpClient();

        // Création de la requete post pour l'image.
        File file = new File(getRealPathFromURI(selectedimg));
        RequestBody requsetFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part bodyImage = MultipartBody.Part.createFormData("photo", "photo", requsetFile);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                // On ajoute l'image en POST
                .addPart(bodyImage)
                // On passe en paramètre la clé de dev, la méthode et l'ID en Multipart
                .addFormDataPart("apikey", global.getAPIKEY())
                .addFormDataPart("method", "addImage")
                .addFormDataPart("id", annonce.getId())
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(global.getAPIURL())
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (!response.isSuccessful())
                    Toast.makeText(ActivityDeposerAnnonce.this, "ERREUR:" + response.body().string(), Toast.LENGTH_SHORT).show();
                else {
                    try {
                        // On transforme le string reçu par l'API Rest en Objet JSON
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        // Si pas d'erreurs retournées par l'API Rest
                        if (jsonObject.getBoolean("success")) {
                            // On créer l'object annonce et on le transmet a l'activité VoirAnnonce
                            annonce = AnnonceJSONParser.parseAnnonce(jsonObject.toString());
                            Intent intent = new Intent(getApplicationContext(), ActivityVoirAnnonce.class);
                            intent.putExtra("annonce", annonce);
                            startActivity(intent);
                        } else
                            Toast.makeText(getApplicationContext(), "ERREUR: " + jsonObject.getString("response"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
