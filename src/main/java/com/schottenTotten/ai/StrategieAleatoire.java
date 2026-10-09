package com.schottenTotten.ai;

import com.schottenTotten.controller.Coup;
import com.schottenTotten.controller.Jeu;
import com.schottenTotten.model.Joueur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** L'IA minimale demandée par le sujet : un coup valide au hasard. */
public class StrategieAleatoire implements Strategie {

    private final Random random = new Random();

    @Override
    public Coup choisirCoup(Jeu jeu, Joueur moi) {
        // On liste tous les coups valides, puis on en tire un au hasard.
        List<Coup> possibles = new ArrayList<>();
        for (int c = 0; c < moi.getMain().size(); c++) {
            for (int b = 0; b < Jeu.NB_BORNES; b++) {
                Coup coup = new Coup(c, b);
                if (jeu.estCoupValide(moi, coup)) {
                    possibles.add(coup);
                }
            }
        }
        if (possibles.isEmpty()) {
            return null;
        }
        return possibles.get(random.nextInt(possibles.size()));
    }

    @Override
    public String getNom() {
        return "Aléatoire";
    }

    // TODO (étape 8, bonus) : une IA un peu meilleure, ex : "StrategieGloutonne" qui pose
    // sur la borne où sa carte améliore le plus sa combinaison. Puis Minimax si motivé.
}
