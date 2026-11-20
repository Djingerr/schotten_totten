package com.schottenTotten.controller;

import com.schottenTotten.model.Borne;
import com.schottenTotten.model.Carte;
import com.schottenTotten.model.CarteTactique;
import com.schottenTotten.model.Combinaison;
import com.schottenTotten.model.Couleur;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.model.Pioche;
import com.schottenTotten.view.VueConsole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.schottenTotten.model.CarteTactique.Type.*;
import static org.junit.jupiter.api.Assertions.*;

/** Étape 9 : troupes d'élite, modes de combat et règles de la variante tactique. */
class JeuTactiqueTest {

    private JeuTactique jeu;
    private Joueur alice;
    private Joueur bob;
    private Borne borne;

    @BeforeEach
    void setUp() {
        alice = new Joueur("Alice", 0, null);
        bob = new Joueur("Bob", 1, null);
        jeu = new JeuTactique(alice, bob, new VueConsole());   // la vue ne sert qu'à afficher des messages ici
        borne = jeu.bornes.get(0);
    }

    private void poser(Joueur j, Carte... cartes) {
        for (Carte c : cartes) {
            borne.ajouterCarte(j, c);
        }
    }

    private static Carte c(Couleur couleur, int valeur) {
        return new Carte(couleur, valeur);
    }

    private static CarteTactique t(CarteTactique.Type type) {
        return new CarteTactique(type);
    }

    /** Met ces cartes dans la main du joueur. */
    private static void donner(Joueur j, Carte... cartes) {
        Pioche p = new Pioche(List.of(cartes));
        for (int i = 0; i < cartes.length; i++) {
            j.piocher(p);
        }
    }

    // ----- Troupes d'élite : la meilleure valeur est choisie -----

    @Test
    void jokerCompleteUneSuiteCouleur() {
        poser(alice, c(Couleur.ROUGE, 7), t(JOKER), c(Couleur.ROUGE, 8));
        assertEquals(Combinaison.SUITE_COULEUR, borne.evaluer(alice));
        assertEquals(24, borne.somme(alice));   // le Joker devient 9R (mieux que 6R)
    }

    @Test
    void espionFaitUnBrelanDeSept() {
        poser(alice, c(Couleur.BLEU, 7), c(Couleur.VERT, 7), t(ESPION));
        assertEquals(Combinaison.BRELAN, borne.evaluer(alice));
    }

    @Test
    void porteBouclierNePeutPasDepasserTrois() {
        poser(alice, c(Couleur.ROUGE, 1), c(Couleur.ROUGE, 2), t(PORTE_BOUCLIER));
        assertEquals(Combinaison.SUITE_COULEUR, borne.evaluer(alice));   // 1R 2R 3R
        poser(bob, c(Couleur.BLEU, 8), c(Couleur.BLEU, 9), t(PORTE_BOUCLIER));
        assertEquals(Combinaison.COULEUR, borne.evaluer(bob));           // pas de 7 possible : juste une couleur
    }

    // ----- Modes de combat -----

    @Test
    void colinMaillardSeuleLaSommeCompte() {
        poser(alice, c(Couleur.ROUGE, 1), c(Couleur.BLEU, 1), c(Couleur.VERT, 1));  // brelan, somme 3
        borne.ajouterCarte(alice, t(COLIN_MAILLARD));
        poser(bob, c(Couleur.ROUGE, 9), c(Couleur.BLEU, 7), c(Couleur.VERT, 3));    // somme 19
        assertSame(bob, jeu.gagnantDeLaBorne(borne));
    }

    @Test
    void combatDeBoueIlFautQuatreCartes() {
        borne.ajouterCarte(alice, t(COMBAT_DE_BOUE));
        poser(alice, c(Couleur.ROUGE, 2), c(Couleur.ROUGE, 3), c(Couleur.ROUGE, 4));
        poser(bob, c(Couleur.BLEU, 9), c(Couleur.VERT, 9), c(Couleur.JAUNE, 9));
        assertNull(jeu.gagnantDeLaBorne(borne));   // 3 cartes ne suffisent plus
        poser(alice, c(Couleur.ROUGE, 5));
        poser(bob, c(Couleur.VIOLET, 1));
        assertEquals(Combinaison.SUITE_COULEUR, borne.evaluer(alice));
        assertSame(alice, jeu.gagnantDeLaBorne(borne));
    }

    @Test
    void modeDeCombatPosableMemeSiLeCoteEstPlein() {
        poser(alice, c(Couleur.ROUGE, 2), c(Couleur.ROUGE, 3), c(Couleur.ROUGE, 4));
        donner(alice, t(COMBAT_DE_BOUE));
        assertTrue(jeu.estCoupValide(alice, new Coup(0, 0)));
    }

    // ----- Règles de jeu des cartes tactiques -----

    @Test
    void pasPlusDeCartesTactiquesQueLAdversaire() {
        donner(alice, t(ESPION), t(PORTE_BOUCLIER));
        jeu.jouerCoup(alice, new Coup(0, 1));
        assertFalse(jeu.estCoupValide(alice, new Coup(0, 2)));   // 1 tactique contre 0
        donner(bob, t(JOKER));
        jeu.jouerCoup(bob, new Coup(0, 1));
        assertTrue(jeu.estCoupValide(alice, new Coup(0, 2)));    // 1 contre 1 : de nouveau permis
    }

    @Test
    void unSeulJokerParJoueur() {
        donner(alice, t(JOKER), t(JOKER));
        jeu.jouerCoup(alice, new Coup(0, 1));
        donner(bob, t(ESPION));
        jeu.jouerCoup(bob, new Coup(0, 1));
        assertEquals("un seul Joker par joueur", jeu.raisonCoupInvalide(alice, new Coup(0, 2)));
    }
}
