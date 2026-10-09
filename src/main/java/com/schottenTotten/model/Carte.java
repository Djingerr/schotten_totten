package com.schottenTotten.model;

import java.util.Objects;

/**
 * Une carte clan : une couleur et une valeur (1 à 9).
 *
 * NOTE : la classe est "immuable" (champs final, pas de setter).
 * Une carte ne change jamais, ça évite plein de bugs.
 */

public class Carte{
    private Couleur couleur;
    private int valeur;

    public class getCouleur(){
        return couleur;
    }

    public class getValeur(){
        return valeur;
    }

    public String toString(){

    }

    public void compareTO(){
        
    }
}
