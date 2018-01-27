package com.example.asuri.projet_android_l3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnVoirAnnonce;
    Button btnDeposerAnnonce;
    Button btnListerAnnonce;
    Button btnMonProfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnVoirAnnonce=findViewById(R.id.BtnVoirAnnonce);
        this.btnDeposerAnnonce=findViewById(R.id.BtnDepoAnnonce);
        this.btnListerAnnonce=findViewById(R.id.BtnListAnnonce);
        this.btnMonProfil=findViewById(R.id.BtnMonProfil);

        this.btnVoirAnnonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voirAnnonce(v);
            }
        });
    }

    public void voirAnnonce(View view){
        Intent intent=new Intent(this,VoirAnnonceActivity.class);
        startActivity(intent);
    }
}
