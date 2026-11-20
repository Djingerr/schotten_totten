package com.schottenTotten.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Carte tactique (variante tactique).
 *
 * Choix de conception : CarteTactique HÉRITE de Carte. La main, la Borne et la Pioche restent
 * des List<Carte> et n'ont presque pas changé (polymorphisme). Prix à payer : une carte tactique
 * n'a pas de couleur/valeur fixe, d'où le constructeur protégé "vide" de Carte.
 *
 * Deux familles pour l'instant :
 *  - troupes d'élite (Joker, Espion, Porte-bouclier) : posées sur un côté de borne comme une carte
 *    normale ; leur couleur/valeur est choisie au moment d'évaluer la borne (la meilleure possible) ;
 *  - modes de combat (Colin-Maillard, Combat de boue) : posés sur la borne, ils en changent la règle.
 */
public class CarteTactique extends Carte {

    public enum Type {
        JOKER("Jk", 1, 9),            // n'importe quelle couleur et valeur
        ESPION("Es", 7, 7),           // un 7 de n'importe quelle couleur
        PORTE_BOUCLIER("PB", 1, 3),   // un 1, 2 ou 3 de n'importe quelle couleur
        COLIN_MAILLARD("CM", 0, 0),   // sur cette borne, seule la somme compte
        COMBAT_DE_BOUE("CB", 0, 0);   // sur cette borne, il faut 4 cartes de chaque côté

        private final String symbole;
        private final int min;
        private final int max;

        Type(String symbole, int min, int max) {
            this.symbole = symbole;
            this.min = min;
            this.max = max;
        }
    }

    private final Type type;

    public CarteTactique(Type type) {
        super();   // pas de couleur ni de valeur
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean estModeCombat() {
        return type == Type.COLIN_MAILLARD || type == Type.COMBAT_DE_BOUE;
    }

    /** Les cartes normales que cette troupe d'élite peut remplacer. */
    public List<Carte> remplacementsPossibles() {
        if (estModeCombat()) {
            throw new IllegalStateException(type + " n'est pas une troupe d'élite");
        }
        List<Carte> possibles = new ArrayList<>();
        for (Couleur couleur : Couleur.values()) {
            for (int v = type.min; v <= type.max; v++) {
                possibles.add(new Carte(couleur, v));
            }
        }
        return possibles;
    }

    @Override
    public String toString() {
        return type.symbole;
    }

    // Carte.equals compare couleur et valeur, qui sont vides ici : on compare le type à la place.
    @Override
    public boolean equals(Object o) {
        return o instanceof CarteTactique autre && autre.type == type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
