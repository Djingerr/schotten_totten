package com.schottenTotten.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BorneTest {

    private Borne borne;
    private Joueur alice;

    @BeforeEach   // exécuté avant CHAQUE test : chaque test repart d'une borne neuve
    void setUp() {
        borne = new Borne(0);
        alice = new Joueur("Alice", 0, null);
    }

    private void poser(Carte... cartes) {
        for (Carte c : cartes) {
            borne.ajouterCarte(alice, c);
        }
    }

    @Test
    void borneCompleteApresTroisCartes() {
        poser(new Carte(Couleur.ROUGE, 1), new Carte(Couleur.ROUGE, 2));
        assertFalse(borne.estComplete(alice));
        poser(new Carte(Couleur.ROUGE, 3));
        assertTrue(borne.estComplete(alice));
    }

    @Test
    void quatriemeCarteRefusee() {
        poser(new Carte(Couleur.ROUGE, 1), new Carte(Couleur.ROUGE, 2), new Carte(Couleur.ROUGE, 3));
        assertThrows(IllegalStateException.class, () -> poser(new Carte(Couleur.BLEU, 4)));
    }

    // ----- Étape 3 : tests de Borne.evaluer (les @Disabled ont été enlevés : evaluer est écrite) -----
    // Chaque test pose 3 cartes du côté d'Alice, puis vérifie la combinaison détectée.
    // Les cartes sont volontairement posées dans le désordre : evaluer doit les trier elle-même.

    @Test
    void suiteCouleur() {
        poser(new Carte(Couleur.ROUGE, 5), new Carte(Couleur.ROUGE, 4), new Carte(Couleur.ROUGE, 6));
        assertEquals(Combinaison.SUITE_COULEUR, borne.evaluer(alice));
    }

    @Test
    void brelan() {
        poser(new Carte(Couleur.ROUGE, 7), new Carte(Couleur.BLEU, 7), new Carte(Couleur.VERT, 7));
        assertEquals(Combinaison.BRELAN, borne.evaluer(alice));
    }

    @Test
    void somme() {
        poser(new Carte(Couleur.ROUGE, 1), new Carte(Couleur.BLEU, 5), new Carte(Couleur.VERT, 9));
        assertEquals(Combinaison.SOMME, borne.evaluer(alice));
        assertEquals(15, borne.somme(alice));
    }

    @Test
    void couleur() {
        // 3 cartes bleues qui ne se suivent pas : même couleur, mais pas une suite
        poser(new Carte(Couleur.BLEU, 9), new Carte(Couleur.BLEU, 1), new Carte(Couleur.BLEU, 5));
        assertEquals(Combinaison.COULEUR, borne.evaluer(alice));
    }

    @Test
    void suite() {
        // 3, 4, 5 qui se suivent, mais de couleurs différentes : une suite simple, pas une suite couleur
        poser(new Carte(Couleur.BLEU, 5), new Carte(Couleur.JAUNE, 3), new Carte(Couleur.ROUGE, 4));
        assertEquals(Combinaison.SUITE, borne.evaluer(alice));
    }
}
