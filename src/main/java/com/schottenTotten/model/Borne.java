package com.schottenTotten.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public Combinaison evaluer(Joueur j) {
        List<Carte> c = new ArrayList<>(cartes.get(j.getPosition())); // On copie la liste pour pas modifié celle du joueur 
        if(c.size() != CAPACITE){ // pour la version normal capa = 3 
            throw new IllegalStateException("Il faut 3 carte sur la borne pour l'évaluer");
        }
        Collections.sort(c); //On trie les cartes 
        
        boolean memeCouleur = c.get(0).getCouleur() == c.get(1).getCouleur() && c.get(1).getCouleur() == c.get(2).getCouleur();
        boolean memeValeur = c.get(0).getValeur() == c.get(1).getValeur() && c.get(1).getValeur() == c.get(2).getValeur();
        boolean suite = c.get(1).getValeur() == c.get(0).getValeur() + 1 && c.get(2).getValeur() == c.get(1).getValeur() + 1;

        if (suite && memeCouleur) {
         return Combinaison.SUITE_COULEUR;
        }
        if (memeValeur) {
            return Combinaison.BRELAN;
        }
        if (memeCouleur) {
            return Combinaison.COULEUR;
        }   
        if (suite) {
            return Combinaison.SUITE;
        }
        return Combinaison.SOMME;
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
