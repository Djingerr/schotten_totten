package com.schottenTotten.view;

import com.schottenTotten.model.Borne;
import com.schottenTotten.model.Joueur;

import java.util.List;

/**
 * Tout ce que le jeu peut demander à l'interface.
 * Le controller ne connaît QUE cette interface : demain on peut écrire une VueGraphique
 * sans toucher une ligne du jeu (polymorphisme).
 *
 * La vue ne dépend que du model : elle affiche et elle lit, elle ne vérifie aucune règle.
 * Tous les index renvoyés commencent à 0 (la vue convertit : l'utilisateur tape 1, on renvoie 0).
 */
public interface Vue {

    void afficherMessage(String message);

    /**
     * Menu générique (variante, type de joueur, type d'IA, rôle...) : le controller passe les libellés,
     * la vue les numérote et renvoie l'index choisi. Ajouter une variante ne change donc rien ici.
     */
    int choisirOption(String titre, List<String> options);

    /** Saisie d'un texte non vide (ex : nom d'un joueur). */
    String demanderTexte(String question);

    void afficherPlateau(Joueur j1, Joueur j2, List<Borne> bornes, int taillePioche);

    void afficherMain(Joueur joueur);

    /** Index (dans la main) de la carte à jouer. */
    int demanderCarte(Joueur joueur);

    /** Index de la borne visée. La validité du coup est vérifiée par le controller. */
    int demanderBorne(int nbBornes);

    /** gagnant == null : partie terminée sans gagnant. */
    void afficherGagnant(Joueur gagnant);
}
