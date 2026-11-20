package com.schottenTotten.controller;

import com.schottenTotten.model.Borne;
import com.schottenTotten.model.Carte;
import com.schottenTotten.model.CarteTactique;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.model.Pioche;
import com.schottenTotten.view.Vue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Variante tactique : mêmes cartes que la base + une pioche de cartes tactiques, 7 cartes en main.
 *
 * "extends JeuBase" (au lieu de Jeu) : on récupère creerPioche() de la base gratuitement.
 *
 * Ce qui change par rapport à la base (et rien d'autre, grâce à l'héritage) :
 *  - raisonCoupInvalide : règles propres aux cartes tactiques ;
 *  - jouerCoup          : on compte les cartes tactiques jouées ;
 *  - piocherApresCoup   : on choisit sa pioche.
 *
 * Le choix CarteTactique extends Carte est expliqué dans CarteTactique.
 * TODO (étape 9, suite) : les ruses (Chasseur de tête, Stratège, Banshee, Traître).
 */
public class JeuTactique extends JeuBase {

    private final Pioche piocheTactique;
    private final int[] tactiquesJouees = new int[2];       // par position de joueur
    private final boolean[] jokerJoue = new boolean[2];
    private final Random random = new Random();

    public JeuTactique(Joueur j1, Joueur j2, Vue vue) {
        super(j1, j2, vue);
        piocheTactique = new Pioche(creerCartesTactiques());
        piocheTactique.melanger();
    }

    static List<Carte> creerCartesTactiques() {
        List<Carte> cartes = new ArrayList<>();
        cartes.add(new CarteTactique(CarteTactique.Type.JOKER));
        cartes.add(new CarteTactique(CarteTactique.Type.JOKER));
        cartes.add(new CarteTactique(CarteTactique.Type.ESPION));
        cartes.add(new CarteTactique(CarteTactique.Type.PORTE_BOUCLIER));
        cartes.add(new CarteTactique(CarteTactique.Type.COLIN_MAILLARD));
        cartes.add(new CarteTactique(CarteTactique.Type.COMBAT_DE_BOUE));
        return cartes;
    }

    @Override
    protected int nbCartesMain() {
        return 7;
    }

    @Override
    public String raisonCoupInvalide(Joueur joueur, Coup coup) {
        if (coup.indexCarte() < 0 || coup.indexCarte() >= joueur.getMain().size()) {
            return super.raisonCoupInvalide(joueur, coup);
        }
        if (!(joueur.getMain().get(coup.indexCarte()) instanceof CarteTactique carte)) {
            return super.raisonCoupInvalide(joueur, coup);   // carte normale : règles de base
        }

        int moi = joueur.getPosition();
        if (tactiquesJouees[moi] > tactiquesJouees[1 - moi]) {
            return "tu as déjà joué plus de cartes tactiques que ton adversaire";
        }
        if (carte.getType() == CarteTactique.Type.JOKER && jokerJoue[moi]) {
            return "un seul Joker par joueur";
        }
        if (!carte.estModeCombat()) {
            return super.raisonCoupInvalide(joueur, coup);   // troupe d'élite : se pose comme une carte normale
        }

        // Mode de combat : il va sur la borne, pas sur un côté, donc "côté plein" ne compte pas.
        if (coup.indexBorne() < 0 || coup.indexBorne() >= NB_BORNES) {
            return "cette borne n'existe pas";
        }
        Borne borne = bornes.get(coup.indexBorne());
        if (borne.estRevendiquee()) {
            return "la borne " + (coup.indexBorne() + 1) + " est déjà revendiquée";
        }
        if (borne.aLeMode(carte.getType())) {
            return carte + " est déjà sur la borne " + (coup.indexBorne() + 1);
        }
        return null;
    }

    @Override
    protected void jouerCoup(Joueur joueur, Coup coup) {
        if (joueur.getMain().get(coup.indexCarte()) instanceof CarteTactique carte) {
            tactiquesJouees[joueur.getPosition()]++;
            if (carte.getType() == CarteTactique.Type.JOKER) {
                jokerJoue[joueur.getPosition()] = true;
            }
        }
        super.jouerCoup(joueur, coup);
    }

    @Override
    protected void piocherApresCoup(Joueur joueur) {
        boolean clans = !pioche.estVide();
        boolean tactique = !piocheTactique.estVide();
        if (!clans && !tactique) {
            return;
        }
        Pioche choix = clans ? pioche : piocheTactique;   // une seule pioche non vide : pas de choix
        if (clans && tactique) {
            int i;
            if (joueur.estHumain()) {
                i = vue.choisirOption("Dans quelle pioche ?", List.of(
                        "Clans (" + pioche.taille() + " cartes)",
                        "Tactique (" + piocheTactique.taille() + " cartes)"));
            } else {
                i = random.nextInt(2);   // ponytail: l'IA pioche au hasard ; à mettre dans Strategie si une IA doit choisir
            }
            choix = (i == 0) ? pioche : piocheTactique;
        }
        joueur.piocher(choix);
    }
}
