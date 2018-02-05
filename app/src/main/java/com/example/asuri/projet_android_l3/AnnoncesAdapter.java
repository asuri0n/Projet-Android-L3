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
 * Created by asuri on 04/02/2018.
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
        final ViewHolder holder;

        convertView = inflater.inflate(R.layout.list_item_annonces_template,null);
        holder = new ViewHolder();

        holder._title = convertView.findViewById(R.id.titreListeAnnonce);
        holder._prix = convertView.findViewById(R.id.prixListeAnnonce);
        holder._desc = convertView.findViewById(R.id.descriptionListeAnnonce);
        holder._title.setText(annonce.getTitre());
        holder._prix.setText(String.valueOf(annonce.getPrix()));
        holder._desc.setText(annonce.getDescription());

        holder._image = convertView.findViewById(R.id.imgListeAnnonce);
        Picasso.with(context).load(annonce.getImage()).into(holder._image);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,VoirAnnonceActivity.class);
                intent.putExtra("id",annonce.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public class ViewHolder {

        TextView _title;
        TextView _prix;
        TextView _desc;
        ImageView _image;

    }

}