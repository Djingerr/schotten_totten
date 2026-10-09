package com.schottenTotten.controller;

import com.schottenTotten.ai.Strategie;
import com.schottenTotten.ai.StrategieAleatoire;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.view.Vue;

/**
 * Pattern Factory demandé par le sujet : c'est le SEUL endroit qui fait "new JeuBase()",
 * "new StrategieAleatoire()"... Le reste du code ne connaît que Jeu et Strategie.
 * => ajouter une variante ou une IA ne touche qu'ici (+ l'enum correspondante).
 */
public final class JeuFactory {

    private JeuFactory() {
    }

    public static Jeu creerJeu(Variante variante, Joueur j1, Joueur j2, Vue vue) {
        switch (variante) {
            case BASE:
                return new JeuBase(j1, j2, vue);
            case TACTIQUE:
                return new JeuTactique(j1, j2, vue);
            default:
                throw new IllegalArgumentException("Variante inconnue : " + variante);
        }
    }

    public static Joueur creerJoueur(String nom, int position, TypeJoueur type) {
        return new Joueur(nom, position, creerStrategie(type));
    }

    /** @return null pour un humain (voir la note dans Joueur). */
    public static Strategie creerStrategie(TypeJoueur type) {
        switch (type) {
            case HUMAIN:
                return null;
            case IA_ALEATOIRE:
                return new StrategieAleatoire();
            default:
                throw new IllegalArgumentException("Type de joueur inconnu : " + type);
        }
    }
}
