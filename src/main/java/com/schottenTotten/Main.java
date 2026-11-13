package com.schottenTotten;

import com.schottenTotten.controller.Jeu;
import com.schottenTotten.controller.JeuFactory;
import com.schottenTotten.controller.TypeJoueur;
import com.schottenTotten.controller.Variante;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.view.VueConsole;

import java.util.ArrayList;
import java.util.List;

/** Point d'entrée : menus de configuration, puis lancement de la partie. */
public class Main {

    public static void main(String[] args) {
        VueConsole vue = new VueConsole();
        vue.afficherMessage("===== SCHOTTEN-TOTTEN =====");

        // 1) Variante (le menu se construit tout seul à partir de l'enum)
        List<String> libellesVariantes = new ArrayList<>();
        for (Variante v : Variante.values()) {
            libellesVariantes.add(v.getLibelle());
        }
        Variante variante = Variante.values()[vue.choisirOption("Variante :", libellesVariantes)];

        // 2) Joueurs
        List<String> libellesTypes = new ArrayList<>();
        for (TypeJoueur t : TypeJoueur.values()) {
            libellesTypes.add(t.getLibelle());
        }
        Joueur[] joueurs = new Joueur[2];
        for (int p = 0; p < 2; p++) {
            String nom = vue.demanderTexte("\nNom du joueur " + (p + 1) + " : ");
            TypeJoueur type = TypeJoueur.values()[vue.choisirOption("Type :", libellesTypes)];
            joueurs[p] = JeuFactory.creerJoueur(nom, p, type);
        }

        // 3) Partie
        Jeu jeu = JeuFactory.creerJeu(variante, joueurs[0], joueurs[1], vue);
        jeu.jouerPartie();
    }
}
