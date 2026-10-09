package com.schottenTotten.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Exemple de test JUnit : sers-t'en de modèle pour les autres. */
class CarteTest {

    @Test
    void creationCarte() {
        Carte c = new Carte(Couleur.ROUGE, 7);
        assertEquals(7, c.getValeur());
        assertEquals(Couleur.ROUGE, c.getCouleur());
        assertEquals("7R", c.toString());
    }

    @Test
    void valeurInvalideRefusee() {
        // assertThrows vérifie qu'une exception est bien levée
        assertThrows(IllegalArgumentException.class, () -> new Carte(Couleur.ROUGE, 10));
        assertThrows(IllegalArgumentException.class, () -> new Carte(null, 5));
    }

    @Test
    void deuxCartesIdentiquesSontEgales() {
        assertEquals(new Carte(Couleur.BLEU, 3), new Carte(Couleur.BLEU, 3));
    }
}
