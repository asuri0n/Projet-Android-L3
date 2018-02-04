package com.example.asuri.projet_android_l3;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by thomas on 26/01/2018.
 */

public class Annonce {
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

    public Annonce(String id, String titre, String description, int prix, String pseudo, String emailContact, String telContact, String ville, String cp, ArrayList<String> images, String date) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.pseudo = pseudo;
        this.emailContact = emailContact;
        this.telContact = telContact;
        this.ville = ville;
        this.cp = cp;
        this.images = images;
        this.date = date;
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

    public String getImage() {
        int index = new Random().nextInt(this.images.size());
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
}