package com.example.asuri.projet_android_l3;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
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
    String imageURL;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposer_annonce);
        setTitle("Déposer");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefPseudo = prefs.getString("pseudo", "");
        this.prefMail = prefs.getString("email", "");
        this.prefTel = prefs.getString("phone", "");

        if (Objects.equals(this.prefPseudo, "") || Objects.equals(this.prefMail, "") || Objects.equals(this.prefTel, "")) {
            finish();
        }

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
                // Si il y a une image a upload, alors il faut faire en sorte d'attendre l'upload de l'image avant de lancer le requestData
                if (selectedimg != null) {
                    uploadImage(getRealPathFromURI(selectedimg));
                } else {
                    requestDataSave("https://ensweb.users.info.unicaen.fr/android-api/");
                }
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

        Picasso.with(getApplicationContext()).load(R.drawable.photo_default).into(image);
    }

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

    protected void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Vous n'avez pas choisi d'image", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                selectedimg = data.getData();
                image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
            } else {
                Toast.makeText(this, "Erreur", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadImage(String imagePath) {
        final String IMGUR_CLIENT_ID = "0e42caf062a28e5";
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        String encodedFile;
        OkHttpClient client = new OkHttpClient();

        try {
            URL url = new URL("file://" + imagePath);

            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            encodedFile = Base64.encodeToString(b, Base64.DEFAULT);

            // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("title", "Image Projet L3 android")
                    .addFormDataPart("image", encodedFile)
                    .build();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                    .url("https://api.imgur.com/3/image")
                    .post(requestBody)
                    .build();


            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("erreur imgur", response.body().string());
                    } else {
                        try {
                            JSONObject jsonobj = new JSONObject(response.body().string());
                            imageURL = jsonobj.getJSONObject("data").getString("link");
                            requestDataSave("https://ensweb.users.info.unicaen.fr/android-api/");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException e) {
            Log.e("qzd", "" + e);
        }
    }

    public void requestDataSave(final String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                // Pour avoir acces a l'id
                                annonce = AnnonceJSONParser.parseAnnonce(response);

                                // Si il y a une image a upload sur l'API REST
                                if(imageURL != null){
                                    requestDataImage(uri);
                                } else {
                                    // Sinon on créer l'object annonce et on le met dans transmet a l'activité VoirAnnonce
                                    Intent intent = new Intent(getApplicationContext(), ActivityVoirAnnonce.class);
                                    intent.putExtra("annonce", annonce);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "ERREUR 1: " + jsonObject.getString("response"), Toast.LENGTH_LONG).show();
                            }
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
                params.put("apikey", "21404260");
                params.put("method", "save");

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

    public void requestDataImage(String uri) {

        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                annonce = AnnonceJSONParser.parseAnnonce(response);
                                Intent intent = new Intent(getApplicationContext(), ActivityVoirAnnonce.class);
                                intent.putExtra("annonce", annonce);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "ERREUR 2: " + jsonObject.getString("response"), Toast.LENGTH_LONG).show();
                            }
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
                params.put("apikey", "21404260");
                params.put("method", "addImage");

                params.put("id", annonce.getId());
                params.put("photo", imageURL);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "multipart/form-data");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

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

    public void newIntent(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
