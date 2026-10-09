package com.schottenTotten.controller;

import com.schottenTotten.model.Borne;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.model.Pioche;
import com.schottenTotten.view.Vue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Le déroulement d'une partie, commun à toutes les variantes.
 *
 * "abstract" : on ne peut pas faire new Jeu(). Les sous-classes (JeuBase, JeuTactique)
 * doivent fournir ce qui change d'une variante à l'autre : creerPioche() et nbCartesMain().
 * Tout le reste (tours, revendications, victoire) est écrit UNE fois ici => réutilisation par héritage.
 *
 * "protected" : visible par les sous-classes, pas par le reste du programme.
 */
public abstract class Jeu {

    public static final int NB_BORNES = 9;

    protected final List<Borne> bornes = new ArrayList<>();
    protected final Joueur[] joueurs;
    protected final Vue vue;
    protected Pioche pioche;
    protected int joueurCourant = 0;

    protected Jeu(Joueur j1, Joueur j2, Vue vue) {
        this.joueurs = new Joueur[]{j1, j2};
        this.vue = vue;
        for (int i = 0; i < NB_BORNES; i++) {
            bornes.add(new Borne(i));
        }
        // PIÈGE JAVA : ne PAS appeler creerPioche() ici. Dans un constructeur parent,
        // les champs de la sous-classe ne sont pas encore initialisés (ex : piocheTactique
        // de JeuTactique vaudrait null). On le fait dans initialiser(), appelé par jouerPartie().
    }

    // ---------- Ce que chaque variante doit fournir ----------

    protected abstract Pioche creerPioche();

    protected abstract int nbCartesMain();

    // ---------- Déroulement ----------

    private void initialiser() {
        pioche = creerPioche();
        pioche.melanger();
        for (int i = 0; i < nbCartesMain(); i++) {
            joueurs[0].piocher(pioche);
            joueurs[1].piocher(pioche);
        }
    }

    public void jouerPartie() {
        initialiser();
        Joueur gagnant = null;
        while (gagnant == null && partiePeutContinuer()) {
            jouerTour();
            verifierRevendications();
            gagnant = determinerGagnant();
            joueurCourant = 1 - joueurCourant;   // 0 -> 1 -> 0 -> ...
        }
        vue.afficherPlateau(this);
        if (gagnant != null) {
            vue.afficherGagnant(gagnant);
        } else {
            vue.afficherMessage("Fin de partie sans gagnant (normal tant que les étapes 6 et 7 ne sont pas faites).");
        }
    }

    /** Un tour : poser une carte puis piocher. */
    protected void jouerTour() {
        Joueur joueur = joueurs[joueurCourant];
        vue.afficherMessage("\n--- Tour de " + joueur.getNom() + " ---");

        Coup coup;
        if (joueur.estHumain()) {
            vue.afficherPlateau(this);
            vue.afficherMain(joueur);
            coup = vue.demanderCoup(joueur);
        } else {
            coup = joueur.choisirCoup(this);
        }

        if (coup == null) {
            vue.afficherMessage(joueur.getNom() + " ne peut pas jouer et passe son tour.");
            return;
        }

        // TODO (étape 5) : au lieu de passer le tour, REDEMANDER tant que le coup est invalide.
        //   Indice : une boucle while (!estCoupValide(joueur, coup)) { message ; coup = ... }
        //   (pour un humain on redemande à la vue, pour une IA à joueur.choisirCoup(this))
        if (!estCoupValide(joueur, coup)) {
            vue.afficherMessage("Coup invalide, tour perdu ! (étape 5 à faire)");
            return;
        }

        joueur.jouerCarte(coup.indexCarte(), bornes.get(coup.indexBorne()));
        vue.afficherMessage(joueur.getNom() + " pose une carte sur la borne " + (coup.indexBorne() + 1));

        // NOTE règle : on pioche après avoir revendiqué. Ici c'est avant, sans conséquence
        // pour l'instant ; tu peux déplacer la pioche si tu veux coller exactement aux règles.
        if (!pioche.estVide()) {
            joueur.piocher(pioche);
        }
    }

    /** Un coup est valide si les index existent, que la borne est libre et que son côté n'est pas plein. */
    public boolean estCoupValide(Joueur joueur, Coup coup) {
        if (coup.indexCarte() < 0 || coup.indexCarte() >= joueur.getMain().size()) return false;
        if (coup.indexBorne() < 0 || coup.indexBorne() >= NB_BORNES) return false;
        Borne borne = bornes.get(coup.indexBorne());
        return !borne.estRevendiquee() && !borne.estComplete(joueur);
    }

    /**
     * TODO (étape 6) : parcourir les bornes non revendiquées et attribuer celles qui sont gagnées.
     *
     * Commence SIMPLE : une borne est gagnée quand les DEUX côtés sont complets.
     *   - compare borne.evaluer(j1).getForce() et borne.evaluer(j2).getForce()
     *   - si égalité : compare borne.somme(...)
     *   - si encore égalité : borne.getPremierAComplete() gagne
     *   - puis borne.revendiquer(gagnant) et vue.afficherMessage(...)
     *
     * Conseil : écris d'abord une méthode "Joueur gagnantDeLaBorne(Borne b)" qui renvoie
     * le gagnant ou null. Elle sera facile à tester avec JUnit (le sujet le demande).
     *
     * Plus tard (bonus) : la vraie règle permet de revendiquer AVANT que l'adversaire ait fini,
     * si on prouve qu'il ne peut plus gagner avec les cartes qui ne sont pas encore posées.
     * Et dans la vraie règle c'est le joueur qui CHOISIT de revendiquer (demander via la Vue).
     */
    protected void verifierRevendications() {
        // à compléter
    }

    /**
     * TODO (étape 7) : renvoyer le gagnant, ou null si personne n'a encore gagné.
     * Un joueur gagne s'il possède 5 bornes, OU 3 bornes côte à côte.
     *   Indice pour "côte à côte" : parcourir les bornes avec un compteur qui
     *   augmente si la borne est à lui et retombe à 0 sinon ; s'il atteint 3 -> gagné.
     */
    protected Joueur determinerGagnant() {
        return null;
    }

    /** La partie continue tant qu'au moins un joueur a des cartes. */
    private boolean partiePeutContinuer() {
        return !joueurs[0].getMain().isEmpty() || !joueurs[1].getMain().isEmpty();
    }

    // ---------- Getters (utilisés par la Vue et les IA) ----------

    public List<Borne> getBornes() {
        return Collections.unmodifiableList(bornes);
    }

    public Joueur getJoueur(int position) {
        return joueurs[position];
    }

    public Joueur getAdversaire(Joueur j) {
        return joueurs[1 - j.getPosition()];
    }

    public int getTaillePioche() {
        return pioche == null ? 0 : pioche.taille();
    }
}
