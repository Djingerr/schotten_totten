package com.schottenTotten.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Une borne : chaque joueur pose jusqu'à 3 cartes de son côté.
 *
 * NOTE : le diagramme dit "List<Carte>[2]". En Java, les tableaux de génériques
 * (new List<Carte>[2]) donnent des warnings. On utilise une liste de 2 listes :
 *     cartes.get(0) = côté du joueur en position 0
 *     cartes.get(1) = côté du joueur en position 1
 */
public class Borne {

    public static final int CAPACITE = 3;

    private final int numero;
    private final List<List<Carte>> cartes = new ArrayList<>();
    private Joueur proprietaire = null;      // null = pas encore revendiquée
    private Joueur premierAComplete = null;  // sert à départager une égalité parfaite

    public Borne(int numero) {
        this.numero = numero;
        cartes.add(new ArrayList<>());
        cartes.add(new ArrayList<>());
    }

    public void ajouterCarte(Joueur j, Carte c) {
        if (estRevendiquee()) {
            throw new IllegalStateException("Borne " + numero + " déjà revendiquée");
        }
        if (estComplete(j)) {
            throw new IllegalStateException("Côté plein sur la borne " + numero);
        }
        cartes.get(j.getPosition()).add(c);
        if (estComplete(j) && premierAComplete == null) {
            premierAComplete = j;
        }
    }

    public boolean estComplete(Joueur j) {
        return cartes.get(j.getPosition()).size() == CAPACITE;
    }

    /**
     * TODO (étape 3) : renvoyer la combinaison formée par les 3 cartes du joueur j.
     *
     * Méthode conseillée :
     *   1. Récupérer les 3 cartes : List<Carte> c = new ArrayList<>(cartes.get(j.getPosition()));
     *   2. Les trier : Collections.sort(c);   (ça marche grâce à Carte.compareTo)
     *   3. Calculer 3 booléens :
     *        memeCouleur = les 3 ont la même couleur
     *        memeValeur  = les 3 ont la même valeur
     *        suite       = c[1] == c[0] + 1  et  c[2] == c[1] + 1   (sur les valeurs triées)
     *   4. Tester dans l'ordre du plus fort au plus faible :
     *        suite && memeCouleur -> SUITE_COULEUR, memeValeur -> BRELAN, etc.
     *
     * Pense à tester avec BorneTest (il y a déjà des tests @Disabled à activer).
     */
    public Combinaison evaluer(Joueur j) {
        throw new UnsupportedOperationException("TODO étape 3 : Borne.evaluer");
    }

    /** Somme des valeurs du côté de j : départage deux combinaisons identiques. */
    public int somme(Joueur j) {
        int total = 0;
        for (Carte c : cartes.get(j.getPosition())) {
            total += c.getValeur();
        }
        return total;
    }

    /** Attribue la borne. Les RÈGLES (a-t-il le droit ?) sont vérifiées dans Jeu, pas ici. */
    public void revendiquer(Joueur j) {
        if (estRevendiquee()) {
            throw new IllegalStateException("Borne " + numero + " déjà revendiquée");
        }
        proprietaire = j;
    }

    public boolean estRevendiquee() {
        return proprietaire != null;
    }

    public Joueur getProprietaire() {
        return proprietaire;
    }

    public Joueur getPremierAComplete() {
        return premierAComplete;
    }

    public List<Carte> getCartes(Joueur j) {
        return Collections.unmodifiableList(cartes.get(j.getPosition()));
    }

    public int getNumero() {
        return numero;
    }
}
