package com.schottenTotten.model;

import com.schottenTotten.ai.Strategie;
import com.schottenTotten.controller.Coup;
import com.schottenTotten.controller.Jeu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Un joueur : un nom, une position (0 ou 1), une main, et une stratégie.   [FINI pour l'instant]
 *
 * NOTE position : c'est l'index du joueur dans le tableau du Jeu (0 ou 1).
 * La Borne s'en sert pour savoir de quel côté poser la carte.
 *
 * NOTE strategie : null = joueur humain (c'est la Vue qui lui demande son coup).
 * Si non null = IA, on lui délègue la décision (c'est ça le polymorphisme :
 * on appelle strategie.choisirCoup(...) sans savoir QUELLE IA c'est).
 *
 * NOTE conception (à mentionner dans le rapport) : avec ce diagramme, le model dépend
 * de ai et de controller (imports ci-dessus). Ça marche, mais une alternative plus
 * "propre" serait de sortir la stratégie du Joueur. À garder en tête, pas urgent.
 */

public class Joueur {

    private final String nom;
    //private final int position;
    //private final List<Carte> main = new ArrayList<>();
    private final Strategie strategie;
    
    public piocher(){

    }

    public jouerCarte(int, Borne){

    }

    public retirerCarte(int){

    }

    public choisirCoup(jeu){

    }
}
