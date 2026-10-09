package com.schottenTotten.controller;

import com.schottenTotten.model.Carte;
import com.schottenTotten.model.Couleur;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.model.Pioche;
import com.schottenTotten.view.Vue;

import java.util.ArrayList;
import java.util.List;

/** Variante de base : 54 cartes (6 couleurs x 1..9), 6 cartes en main. */
public class JeuBase extends Jeu {

    public JeuBase(Joueur j1, Joueur j2, Vue vue) {
        super(j1, j2, vue);   // appelle le constructeur de Jeu
    }

    @Override
    protected Pioche creerPioche() {
        List<Carte> cartes = new ArrayList<>();
        for (Couleur couleur : Couleur.values()) {
            for (int valeur = 1; valeur <= 9; valeur++) {
                cartes.add(new Carte(couleur, valeur));
            }
        }
        return new Pioche(cartes);
    }

    @Override
    protected int nbCartesMain() {
        return 6;
    }
}
