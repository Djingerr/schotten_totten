package com.schottenTotten;

import com.schottenTotten.controller.Jeu;
import com.schottenTotten.controller.JeuFactory;
import com.schottenTotten.controller.TypeJoueur;
import com.schottenTotten.controller.Variante;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.view.VueConsole;

/** Point d'entrée : menus de configuration, puis lancement de la partie. */
public class Main {

    public static void main(String[] args) {
        VueConsole vue = new VueConsole();
        vue.afficherMessage("===== SCHOTTEN-TOTTEN =====");

        // 1) Variante (le menu se construit tout seul à partir de l'enum)
        Variante[] variantes = Variante.values();
        for (int i = 0; i < variantes.length; i++) {
            vue.afficherMessage((i + 1) + ") " + variantes[i].getLibelle());
        }
        Variante variante = variantes[vue.lireEntier("Variante ", 1, variantes.length) - 1];

        // 2) Joueurs
        Joueur[] joueurs = new Joueur[2];
        TypeJoueur[] types = TypeJoueur.values();
        for (int p = 0; p < 2; p++) {
            String nom = vue.lireTexte("\nNom du joueur " + (p + 1) + " : ");
            for (int i = 0; i < types.length; i++) {
                vue.afficherMessage((i + 1) + ") " + types[i].getLibelle());
            }
            TypeJoueur type = types[vue.lireEntier("Type ", 1, types.length) - 1];
            joueurs[p] = JeuFactory.creerJoueur(nom, p, type);
        }

        // 3) Partie
        Jeu jeu = JeuFactory.creerJeu(variante, joueurs[0], joueurs[1], vue);
        jeu.jouerPartie();
    }
}
