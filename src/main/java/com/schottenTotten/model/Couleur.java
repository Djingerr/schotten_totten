package com.schottenTotten.model;

/** Les 6 clans. Une enum = une liste finie de valeurs connues à l'avance. */
public enum Couleur {
    ROUGE("R"), BLEU("B"), VERT("V"), JAUNE("J"), VIOLET("P"), MARRON("M");

    private final String symbole;

    Couleur(String symbole) {
        this.symbole = symbole;
    }

    public String getSymbole() {
        return symbole;
    }
}
