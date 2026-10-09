package com.schottenTotten.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** La pioche.                                                  [FINI] */
public class Pioche {

    private final List<Carte> cartes = new ArrayList<>();

    /** Crée une pioche à partir d'une liste de cartes (c'est le Jeu qui décide lesquelles). */
    public Pioche(List<Carte> cartes) {
        this.cartes.addAll(cartes);
    }

    public void melanger() {
        Collections.shuffle(cartes);
    }

    /** Prend la carte du dessus. Lève une exception si la pioche est vide (cas exceptionnel du sujet). */
    public Carte piocher() {
        if (cartes.isEmpty()) {
            throw new IllegalStateException("La pioche est vide");
        }
        return cartes.remove(cartes.size() - 1);
    }

    public boolean estVide() {
        return cartes.isEmpty();
    }

    public int taille() {
        return cartes.size();
    }
}
