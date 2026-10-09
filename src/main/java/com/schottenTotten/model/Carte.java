package com.schottenTotten.model;

import java.util.Objects;

/**
 * Une carte clan : une couleur et une valeur (1 à 9).        [FINI]
 *
 * NOTE : la classe est "immuable" (champs final, pas de setter).
 * Une carte ne change jamais, ça évite plein de bugs.
 */
public class Carte implements Comparable<Carte> {

    private final Couleur couleur;
    private final int valeur;

    public Carte(Couleur couleur, int valeur) {
        // Robustesse demandée par le sujet : on refuse une carte absurde dès sa création.
        if (couleur == null) {
            throw new IllegalArgumentException("Une carte doit avoir une couleur");
        }
        if (valeur < 1 || valeur > 9) {
            throw new IllegalArgumentException("Valeur invalide : " + valeur);
        }
        this.couleur = couleur;
        this.valeur = valeur;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public int getValeur() {
        return valeur;
    }

    /** Trie par valeur (pratique pour détecter une suite : on trie puis on regarde si ça se suit). */
    @Override
    public int compareTo(Carte autre) {
        return Integer.compare(this.valeur, autre.valeur);
    }

    /**
     * NOTE : sans equals(), deux "new Carte(ROUGE, 7)" seraient considérées différentes.
     * Indispensable pour list.contains(), list.remove(carte) et les assertEquals des tests.
     * Règle Java : si on redéfinit equals, on redéfinit aussi hashCode.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carte)) return false;
        Carte c = (Carte) o;
        return valeur == c.valeur && couleur == c.couleur;
    }

    @Override
    public int hashCode() {
        return Objects.hash(couleur, valeur);
    }

    @Override
    public String toString() {
        return valeur + couleur.getSymbole();   // ex : "7R"
    }
}
