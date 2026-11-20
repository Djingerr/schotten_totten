package com.schottenTotten.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Borne {

    public static final int CAPACITE = 3;

    private final int numero;
    private final List<List<Carte>> cartes = new ArrayList<>();
    private final List<CarteTactique> modes = new ArrayList<>(); // modes de combat posés sur la borne (variante tactique)
    private Joueur proprietaire = null;      // null = pas encore revendiquée
    private Joueur premierAComplete = null;  // premier à avoir complété son côté : départage une égalité parfaite

    public Borne(int numero) {
        this.numero = numero;
        cartes.add(new ArrayList<>());
        cartes.add(new ArrayList<>());
    }

    /**
     * Pose une carte du côté de j. Un mode de combat ne va d'aucun côté : il s'applique à la borne.
     * (Ainsi Joueur.jouerCarte et Coup n'ont pas besoin de savoir quel genre de carte est jouée.)
     */
    public void ajouterCarte(Joueur j, Carte c) {
        if (estRevendiquee()) {
            throw new IllegalStateException("Borne " + numero + " déjà revendiquée");
        }
        if (c instanceof CarteTactique t && t.estModeCombat()) {
            ajouterMode(t);
            return;
        }
        if (estComplete(j)) {
            throw new IllegalStateException("Côté plein sur la borne " + numero);
        }
        cartes.get(j.getPosition()).add(c);
        if (estComplete(j) && premierAComplete == null) {
            premierAComplete = j;
        }
    }

    private void ajouterMode(CarteTactique mode) {
        if (aLeMode(mode.getType())) {
            throw new IllegalStateException(mode.getType() + " est déjà sur la borne " + numero);
        }
        modes.add(mode);
        if (mode.getType() == CarteTactique.Type.COMBAT_DE_BOUE) {
            // Il faut maintenant 4 cartes : personne n'a encore complété son côté.
            premierAComplete = null;
        }
    }

    public boolean aLeMode(CarteTactique.Type type) {
        for (CarteTactique m : modes) {
            if (m.getType() == type) {
                return true;
            }
        }
        return false;
    }

    /** 3 cartes par côté, 4 avec Combat de boue. */
    public int capacite() {
        return aLeMode(CarteTactique.Type.COMBAT_DE_BOUE) ? CAPACITE + 1 : CAPACITE;
    }

    public boolean estComplete(Joueur j) {
        return cartes.get(j.getPosition()).size() == capacite();
    }

    public Combinaison evaluer(Joueur j) {
        if (!estComplete(j)) {
            throw new IllegalStateException("Il faut " + capacite() + " cartes sur la borne pour l'évaluer");
        }
        return evaluerCartes(meilleuresCartes(j));
    }

    /** Somme des valeurs du côté de j : départage deux combinaisons identiques. */
    public int somme(Joueur j) {
        return sommeCartes(meilleuresCartes(j));
    }

    /**
     * Les cartes du côté de j, où chaque troupe d'élite est remplacée par la carte normale
     * qui donne la meilleure combinaison (puis la meilleure somme).
     * On essaie toutes les possibilités : au pire ~6000 cas (Joker + Espion + Porte-bouclier), c'est instantané.
     */
    private List<Carte> meilleuresCartes(Joueur j) {
        return meilleures(new ArrayList<>(cartes.get(j.getPosition())), 0);
    }

    /** Remplace (récursivement) les troupes d'élite à partir de l'index i, et garde le meilleur résultat. */
    private List<Carte> meilleures(List<Carte> c, int i) {
        if (i == c.size()) {
            return c;
        }
        if (!(c.get(i) instanceof CarteTactique elite)) {
            return meilleures(c, i + 1);
        }
        List<Carte> meilleur = null;
        for (Carte remplacement : elite.remplacementsPossibles()) {
            List<Carte> essai = new ArrayList<>(c);
            essai.set(i, remplacement);
            essai = meilleures(essai, i + 1);
            if (meilleur == null || estMeilleur(essai, meilleur)) {
                meilleur = essai;
            }
        }
        return meilleur;
    }

    private boolean estMeilleur(List<Carte> a, List<Carte> b) {
        if (a.size() < capacite()) {        // côté incomplet : seule la somme a un sens
            return sommeCartes(a) > sommeCartes(b);
        }
        int fa = evaluerCartes(a).getForce();
        int fb = evaluerCartes(b).getForce();
        return fa > fb || (fa == fb && sommeCartes(a) > sommeCartes(b));
    }

    /** Combinaison de cartes normales (3, ou 4 avec Combat de boue). */
    private Combinaison evaluerCartes(List<Carte> cartesNormales) {
        if (aLeMode(CarteTactique.Type.COLIN_MAILLARD)) {
            return Combinaison.SOMME;           // Colin-Maillard : les combinaisons ne comptent plus
        }
        List<Carte> c = new ArrayList<>(cartesNormales); // On copie la liste pour ne pas modifier celle du joueur
        Collections.sort(c); // On trie les cartes

        boolean memeCouleur = true;
        boolean memeValeur = true;
        boolean suite = true;
        for (int k = 1; k < c.size(); k++) {   // marche pour 3 comme pour 4 cartes
            memeCouleur &= c.get(k).getCouleur() == c.get(0).getCouleur();
            memeValeur &= c.get(k).getValeur() == c.get(0).getValeur();
            suite &= c.get(k).getValeur() == c.get(k - 1).getValeur() + 1;
        }

        if (suite && memeCouleur) {
            return Combinaison.SUITE_COULEUR;
        }
        if (memeValeur) {
            return Combinaison.BRELAN;
        }
        if (memeCouleur) {
            return Combinaison.COULEUR;
        }
        if (suite) {
            return Combinaison.SUITE;
        }
        return Combinaison.SOMME;
    }

    private static int sommeCartes(List<Carte> c) {
        int total = 0;
        for (Carte carte : c) {
            total += carte.getValeur();
        }
        return total;
    }

    /** Attribue la borne. Les RÈGLES (a-t-il le droit ?) sont vérifiées dans Jeu, pas ici. */
    public void revendiquer(Joueur j) {
        if (estRevendiquee()) {
            throw new IllegalStateException("Borne " + numero + " déjà revendiquée");
        }
        proprietaire = j;
    }

    public boolean estRevendiquee() {
        return proprietaire != null;
    }

    public Joueur getProprietaire() {
        return proprietaire;
    }

    public Joueur getPremierAComplete() {
        return premierAComplete;
    }

    public List<Carte> getCartes(Joueur j) {
        return Collections.unmodifiableList(cartes.get(j.getPosition()));
    }

    public List<CarteTactique> getModes() {
        return Collections.unmodifiableList(modes);
    }

    public int getNumero() {
        return numero;
    }
}
