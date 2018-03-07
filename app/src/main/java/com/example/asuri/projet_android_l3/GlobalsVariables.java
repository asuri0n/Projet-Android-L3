package com.example.asuri.projet_android_l3;

/**
 * Classe singleton qui est appelé pour récupérer les variables globales.
 * Created by Nathan on 07/03/2018.
 */

public class GlobalsVariables {
    private static GlobalsVariables instance;

    private String APIKEY = "21404260";
    private String APIURL = "https://ensweb.users.info.unicaen.fr/android-api/";

    private GlobalsVariables() {
    }

    public String getAPIKEY() {
        return APIKEY;
    }

    public String getAPIURL() {
        return APIURL;
    }

    public static synchronized GlobalsVariables getInstance() {
        if (instance == null)
            instance = new GlobalsVariables();
        return instance;
    }
}
