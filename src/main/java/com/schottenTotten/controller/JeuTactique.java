package com.schottenTotten.controller;

import com.schottenTotten.model.Joueur;
import com.schottenTotten.view.Vue;

/**
 * Variante tactique : mêmes cartes que la base + 10 cartes tactiques, 7 cartes en main.
 *
 * "extends JeuBase" (au lieu de Jeu) : on récupère creerPioche() de la base gratuitement.
 * (Dans ton diagramme elle hérite de Jeu : les deux marchent, à toi de choisir.)
 *
 * TODO (étape 9, quand la base marche) :
 *   - private Pioche piocheTactique;   et   private List<CarteTactique> tactiquesJouees;
 *   - jouerCarteTactique()
 *   - redéfinir jouerTour() pour laisser le choix : piocher dans l'une ou l'autre pioche.
 *
 * QUESTION DE CONCEPTION (CarteTactique, voir README) : une carte tactique n'a pas forcément
 * une couleur et une valeur (ex : "Colin-Maillard" change la règle d'une borne).
 * Est-ce vraiment une Carte ? Deux options :
 *   a) interface commune "CarteJouable" implémentée par Carte et CarteTactique
 *   b) CarteTactique extends Carte, mais il faut alors autoriser une couleur/valeur "vide"
 */
public class JeuTactique extends JeuBase {

    public JeuTactique(Joueur j1, Joueur j2, Vue vue) {
        super(j1, j2, vue);
    }

    @Override
    protected int nbCartesMain() {
        return 7;
    }
}
