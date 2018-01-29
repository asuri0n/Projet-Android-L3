package com.example.asuri.projet_android_l3;
import android.util.Log;

import java.util.Date;

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
    private String emailContact;
    private String telContact;
    private String ville;
    private String cp;
    private String images[];
    private Date date;

    public Annonce() {
        this.id = "000";
        this.titre = "rien";
        this.description = "rien";
        this.prix = 000;
        this.pseudo = "rien";
        this.emailContact = "rien";
        this.telContact = "rien";
        this.ville = "rien";
        this.cp = "000";
        this.images = null;
        this.date = null;
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

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
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

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}