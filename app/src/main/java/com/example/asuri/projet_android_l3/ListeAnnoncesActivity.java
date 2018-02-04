package com.example.asuri.projet_android_l3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class ListeAnnoncesActivity extends AppCompatActivity {

    ImageView imgListeAnnonce;
    TextView titreListeAnnonce;
    TextView prixListeAnnonce;
    TextView descriptionListeAnnonce;

    List<Annonce> annoncesList;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_annonces);

        this.lv = findViewById(R.id.listView);

        String url = "https://ensweb.users.info.unicaen.fr/android-api/mock-api/liste.json";
        requestData(url);
    }

    public void requestData(String uri) {

        StringRequest request = new StringRequest(uri,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        annoncesList = AnnoncesJSONParser.parseData(response);
                        AnnoncesAdapter adapter = new AnnoncesAdapter(ListeAnnoncesActivity.this, annoncesList);
                        lv.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ListeAnnoncesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}