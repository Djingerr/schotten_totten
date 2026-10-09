package com.schottenTotten.controller;

/** Types de joueurs proposés dans le menu. Nouvelle IA => nouvelle ligne + un case dans JeuFactory.creerStrategie. */
public enum TypeJoueur {
    HUMAIN("Humain"),
    IA_ALEATOIRE("IA aléatoire");

    private final String libelle;

    TypeJoueur(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
