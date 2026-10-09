package com.schottenTotten.model;

/**
 * Les combinaisons possibles sur une borne, de la plus forte à la plus faible.   [FINI]
 *
 * ATTENTION (piège Java) : une enum a DÉJÀ un compareTo(), et on ne peut pas le redéfinir.
 * Il compare l'ordre de déclaration : SUITE_COULEUR (déclarée en 1er) est considérée
 * comme la plus PETITE. Pour comparer la force, utilise getForce() :
 *     a.getForce() > b.getForce()   => a gagne
 *
 * NOTE : à combinaison égale, on départage par la SOMME des cartes.
 * L'enum seule ne la contient pas -> voir Borne.somme().
 */
public enum Combinaison {
    SUITE_COULEUR(5),   // 3 cartes qui se suivent, même couleur   ex : 4R 5R 6R
    BRELAN(4),          // 3 cartes de même valeur                  ex : 7R 7B 7V
    COULEUR(3),         // 3 cartes de même couleur                 ex : 1B 5B 9B
    SUITE(2),           // 3 cartes qui se suivent                  ex : 3J 4R 5B
    SOMME(1);           // rien de tout ça : on compte les points

    private final int force;

    Combinaison(int force) {
        this.force = force;
    }

    public int getForce() {
        return force;
    }
}
