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
        vue.afficherPlateau(joueurs[0], joueurs[1], bornes, getTaillePioche());
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

        // On vérifie AVANT de demander quoi que ce soit. Si aucun coup n'est possible
        // (main vide, ou toutes les bornes pleines/revendiquées de son côté), on passe le tour.
        // Sans ça, un humain resterait bloqué pour toujours dans la boucle de demanderCoupValide,
        // puisque aucune réponse ne pourrait être acceptée.
        if (!aUnCoupPossible(joueur)) {
            vue.afficherMessage(joueur.getNom() + " ne peut pas jouer et passe son tour.");
            return;
        }

        Coup coup;
        if (joueur.estHumain()) {
            vue.afficherPlateau(joueurs[0], joueurs[1], bornes, getTaillePioche());
            vue.afficherMain(joueur);
            coup = demanderCoupValide(joueur);   // ne revient qu'avec un coup valide
        } else {
            coup = joueur.choisirCoup(this);
            // Une IA, c'est NOTRE code : si elle propose un coup invalide, c'est un bug à corriger,
            // pas une faute de frappe. On lève une exception pour le voir tout de suite, plutôt que
            // de redemander (une IA déterministe redonnerait le même coup : boucle infinie).
            if (coup == null || !estCoupValide(joueur, coup)) {
                throw new IllegalStateException("L'IA de " + joueur.getNom() + " a proposé un coup invalide : " + coup);
            }
        }

        joueur.jouerCarte(coup.indexCarte(), bornes.get(coup.indexBorne()));
        vue.afficherMessage(joueur.getNom() + " pose une carte sur la borne " + (coup.indexBorne() + 1));

        // NOTE règle : on pioche après avoir revendiqué. Ici c'est avant, sans conséquence
        // pour l'instant ; tu peux déplacer la pioche si tu veux coller exactement aux règles.
        if (!pioche.estVide()) {
            joueur.piocher(pioche);
        }
    }

    /**
     * Étape 5 : redemande à l'humain tant que son coup n'est pas valide,
     * en lui expliquant à chaque fois POURQUOI le coup est refusé.
     * La vue vérifie déjà que l'utilisateur tape un nombre dans le bon intervalle ;
     * ici on vérifie les RÈGLES du jeu (borne revendiquée, côté plein...).
     */
    private Coup demanderCoupValide(Joueur joueur) {
        while (true) {
            Coup coup = new Coup(vue.demanderCarte(joueur), vue.demanderBorne(NB_BORNES));
            String erreur = raisonCoupInvalide(joueur, coup);
            if (erreur == null) {          // null = aucune raison de refuser : le coup est valide
                return coup;
            }
            vue.afficherMessage("Coup refusé : " + erreur + ". Recommence.");
        }
    }

    /**
     * Explique pourquoi un coup est interdit, ou renvoie null s'il est valide.
     * C'est le SEUL endroit qui contient les règles de validité : estCoupValide l'utilise aussi,
     * donc si on ajoute une règle (variante tactique...), il suffit de la mettre ici.
     */
    public String raisonCoupInvalide(Joueur joueur, Coup coup) {
        if (coup.indexCarte() < 0 || coup.indexCarte() >= joueur.getMain().size()) {
            return "cette carte n'est pas dans ta main";
        }
        if (coup.indexBorne() < 0 || coup.indexBorne() >= NB_BORNES) {
            return "cette borne n'existe pas";
        }
        Borne borne = bornes.get(coup.indexBorne());
        int numero = coup.indexBorne() + 1;   // pour l'affichage : l'utilisateur compte à partir de 1
        if (borne.estRevendiquee()) {
            return "la borne " + numero + " est déjà revendiquée par " + borne.getProprietaire().getNom();
        }
        if (borne.estComplete(joueur)) {
            return "ton côté de la borne " + numero + " est déjà plein (" + Borne.CAPACITE + " cartes)";
        }
        return null;
    }

    /** Un coup est valide s'il n'y a aucune raison de le refuser. Utilisé aussi par les IA. */
    public boolean estCoupValide(Joueur joueur, Coup coup) {
        return raisonCoupInvalide(joueur, coup) == null;
    }

    /** Vrai si le joueur a au moins un coup valide : on essaie chaque carte sur chaque borne. */
    private boolean aUnCoupPossible(Joueur joueur) {
        for (int c = 0; c < joueur.getMain().size(); c++) {
            for (int b = 0; b < NB_BORNES; b++) {
                if (estCoupValide(joueur, new Coup(c, b))) {
                    return true;
                }
            }
        }
        return false;
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

    Joueur gagnantDeLaBorne(Borne b){
        Joueur j1 = joueurs[0];
        Joueur j2 = joueurs[1];
        if (!b.estComplete(j1) || !b.estComplete(j2)){
            return null;
        }
        int force1 = b.evaluer(j1).getForce();
        int force2 = b.evaluer(j2).getForce();
        if (force1 > force2){
            return j1;
        }
        else {
            return j2;
        }
        return null;
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
