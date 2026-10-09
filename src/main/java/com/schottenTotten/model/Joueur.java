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
    private final int position;
    private final List<Carte> main = new ArrayList<>();
    private final Strategie strategie;
    // TODO (plus tard, Schotten-Totten 2) : private Role role;  (ASSAILLANT / DEFENSEUR)

    public Joueur(String nom, int position, Strategie strategie) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Nom vide");
        }
        this.nom = nom;
        this.position = position;
        this.strategie = strategie;
    }

    public void piocher(Pioche pioche) {
        main.add(pioche.piocher());
    }

    /** Pose la carte n°index de sa main sur la borne. */
    public void jouerCarte(int index, Borne borne) {
        Carte carte = main.get(index);
        borne.ajouterCarte(this, carte);   // si ça lève une exception, la carte reste dans la main
        main.remove(index);
    }

    public Carte retirerCarte(int index) {
        return main.remove(index);
    }

    /** Uniquement pour une IA. */
    public Coup choisirCoup(Jeu jeu) {
        return strategie.choisirCoup(jeu, this);
    }

    public boolean estHumain() {
        return strategie == null;
    }

    public String getNom() {
        return nom;
    }

    public int getPosition() {
        return position;
    }

    /** Lecture seule : impossible de faire getMain().add(...) depuis l'extérieur (encapsulation). */
    public List<Carte> getMain() {
        return Collections.unmodifiableList(main);
    }

    @Override
    public String toString() {
        return nom;
    }
}
