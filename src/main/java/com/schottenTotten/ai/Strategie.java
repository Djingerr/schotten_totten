package com.schottenTotten.ai;

import com.schottenTotten.controller.Coup;
import com.schottenTotten.controller.Jeu;
import com.schottenTotten.model.Joueur;

/**
 * Le "cerveau" d'une IA. Chaque IA implémente cette interface.
 * Pour ajouter une IA : 1) une classe qui implémente Strategie,
 * 2) une ligne dans TypeJoueur, 3) un case dans JeuFactory.creerStrategie.
 */
public interface Strategie {

    /** @return le coup choisi, ou null si aucun coup n'est possible. */
    Coup choisirCoup(Jeu jeu, Joueur moi);

    String getNom();
}
