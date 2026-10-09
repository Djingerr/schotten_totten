package com.schottenTotten.controller;

/** Les variantes proposées dans le menu. Ajouter une variante = ajouter une ligne ici + un case dans JeuFactory. */
public enum Variante {
    BASE("Base"),
    TACTIQUE("Tactique (à faire)");

    private final String libelle;

    Variante(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
