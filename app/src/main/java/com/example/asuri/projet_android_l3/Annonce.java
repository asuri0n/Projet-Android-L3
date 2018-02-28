package com.example.asuri.projet_android_l3;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by thomas on 26/01/2018.
 */

public class Annonce implements Serializable {
    //composition d'une annonce
    private String id;
    private String titre;
    private String description;
    private int prix;
    private String pseudo;
    private String telContact;
    private String ville;
    private String cp;
    private List<String> images = new ArrayList<String>();
    private String date;
    private String emailContact;

    public Annonce() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getTelContact() {
        return telContact;
    }

    public void setTelContact(String telContact) {
        this.telContact = telContact;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public List<String> getImages() {
        return images;
    }

    public String getImage() {
        int size = this.images.size();
        int index;
        if(size > 0)
            index = new Random().nextInt(size);
        else {
            this.addImage("https://myaco.lemans.org/GED/content/4FA6F788-4ACF-4B1A-A150-C127C75E3D14.jpg");
            index = 0;
        }
        return images.get(index);
    }

    public void setImages(JSONArray imagesArray) throws JSONException {
        for(int i = 0; i < imagesArray.length(); i++){
            this.images.add(imagesArray.get(i).toString());
        }
    }

    public void addImage(String image) {
        this.images.add(image);
    }

    public String getDate() {
        return date;
    }

    public String getFormatedDate(String timeStamp){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("'Publié le' dd MMMM yyyy 'à' HH:mm");
            Date netDate = (new Date(Long.parseLong(timeStamp) * 1000L));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String setMailContact) {
        this.emailContact = setMailContact;
    }

    @Override
    public String toString() {
        return "Annonce{" +
                "id='" + id + '\'' +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", pseudo='" + pseudo + '\'' +
                ", telContact='" + telContact + '\'' +
                ", ville='" + ville + '\'' +
                ", cp='" + cp + '\'' +
                ", images=" + images +
                ", date='" + date + '\'' +
                ", emailContact='" + emailContact + '\'' +
                '}';
    }
}