package com.schottenTotten.view;

import com.schottenTotten.controller.Coup;
import com.schottenTotten.controller.Jeu;
import com.schottenTotten.model.Joueur;

/**
 * Tout ce que le Jeu a besoin de demander à l'interface.
 * Le Jeu ne connaît QUE cette interface : demain on peut écrire une VueGraphique
 * sans toucher une ligne du Jeu.
 */
public interface Vue {

    void afficherPlateau(Jeu jeu);

    void afficherMain(Joueur joueur);

    /** Demande à un humain quelle carte poser et où. */
    Coup demanderCoup(Joueur joueur);

    void afficherMessage(String message);

    void afficherGagnant(Joueur joueur);
}
