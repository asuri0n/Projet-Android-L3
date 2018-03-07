package com.example.asuri.projet_android_l3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter pour la liste des annonces
 */

class AnnoncesAdapter extends BaseAdapter {
    private Context context;
    private List<Annonce> annoncesList;
    private LayoutInflater inflater = null;

    public AnnoncesAdapter(Context context, List<Annonce> list) {
        this.context = context;
        this.annoncesList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return annoncesList.size();
    }

    @Override
    public Object getItem(int position) {
        return annoncesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Annonce annonce = annoncesList.get(position);

        convertView = inflater.inflate(R.layout.list_item_annonces_template, null);

        TextView title = convertView.findViewById(R.id.titreListeAnnonce);
        TextView prix = convertView.findViewById(R.id.prixListeAnnonce);
        TextView desc = convertView.findViewById(R.id.descriptionListeAnnonce);

        title.setText(annonce.getTitre());
        prix.setText(String.valueOf(annonce.getPrix()));
        desc.setText(annonce.getDescription());

        ImageView image = convertView.findViewById(R.id.imgListeAnnonce);
        Picasso.with(context).load(annonce.getImage()).into(image);

        // Sur chaque item de la liste, ajoute un listener qui redirige vers VoirAnnonce
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityVoirAnnonce.class);
                // transmet l'objet annonce a l'activité VoirAnnonce
                intent.putExtra("annonce", annonce);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}