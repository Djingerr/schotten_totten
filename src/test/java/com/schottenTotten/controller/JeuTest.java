package com.schottenTotten.controller;

import com.schottenTotten.model.Borne;
import com.schottenTotten.model.Carte;
import com.schottenTotten.model.Couleur;
import com.schottenTotten.model.Joueur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Étape 8 : tests des revendications (gagnantDeLaBorne) et de la victoire (aGagne). */
class JeuTest {

    private Jeu jeu;
    private Joueur alice;
    private Joueur bob;
    private Borne borne;

    @BeforeEach
    void setUp() {
        alice = new Joueur("Alice", 0, null);
        bob = new Joueur("Bob", 1, null);
        jeu = new JeuBase(alice, bob, null);   // pas de vue : ces méthodes n'affichent rien
        borne = jeu.bornes.get(0);
    }

    private void poser(Joueur j, Carte... cartes) {
        for (Carte c : cartes) {
            borne.ajouterCarte(j, c);
        }
    }

    // ----- gagnantDeLaBorne -----

    @Test
    void pasDeGagnantSiUnCoteIncomplet() {
        poser(alice, new Carte(Couleur.ROUGE, 7), new Carte(Couleur.ROUGE, 8), new Carte(Couleur.ROUGE, 9));
        poser(bob, new Carte(Couleur.BLEU, 1), new Carte(Couleur.BLEU, 2));
        assertNull(jeu.gagnantDeLaBorne(borne));
    }

    @Test
    void combinaisonPlusForteGagne() {
        poser(alice, new Carte(Couleur.ROUGE, 1), new Carte(Couleur.BLEU, 1), new Carte(Couleur.VERT, 1)); // brelan
        poser(bob, new Carte(Couleur.ROUGE, 9), new Carte(Couleur.BLEU, 7), new Carte(Couleur.VERT, 3));   // somme
        assertSame(alice, jeu.gagnantDeLaBorne(borne));
    }

    @Test
    void memeCombinaisonLaSommeDepartage() {
        poser(alice, new Carte(Couleur.ROUGE, 1), new Carte(Couleur.BLEU, 5), new Carte(Couleur.VERT, 3));
        poser(bob, new Carte(Couleur.ROUGE, 9), new Carte(Couleur.BLEU, 7), new Carte(Couleur.VERT, 3));
        assertSame(bob, jeu.gagnantDeLaBorne(borne));
    }

    @Test
    void egaliteParfaiteLePremierAAvoirCompleteGagne() {
        // Alice pose en premier, mais Bob complète son côté avant elle : c'est Bob qui gagne.
        poser(alice, new Carte(Couleur.JAUNE, 2));
        poser(bob, new Carte(Couleur.ROUGE, 2), new Carte(Couleur.BLEU, 5), new Carte(Couleur.VERT, 8));
        poser(alice, new Carte(Couleur.VIOLET, 5), new Carte(Couleur.MARRON, 8));
        assertSame(bob, jeu.gagnantDeLaBorne(borne));
    }

    // ----- aGagne -----

    private void donner(Joueur j, int... indices) {
        for (int i : indices) {
            jeu.bornes.get(i).revendiquer(j);
        }
    }

    @Test
    void troisBornesAdjacentesGagnent() {
        donner(alice, 3, 4, 5);
        assertTrue(jeu.aGagne(alice));
        assertFalse(jeu.aGagne(bob));
    }

    @Test
    void troisBornesNonAdjacentesNeGagnentPas() {
        donner(alice, 0, 2, 4);
        donner(bob, 1);
        assertFalse(jeu.aGagne(alice));
    }

    @Test
    void cinqBornesNonAdjacentesGagnent() {
        donner(alice, 0, 2, 4, 6, 8);
        assertTrue(jeu.aGagne(alice));
        assertSame(alice, jeu.determinerGagnant());
    }
}
