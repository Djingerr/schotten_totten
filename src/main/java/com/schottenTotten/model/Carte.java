package com.schottenTotten.model;
import java.util.Objects;

public class Carte implements Comparable<Carte>{ //implements pour dire a java qu'on va ecrire cette méthode 
    private final Couleur couleur;
    private final int valeur;

    public Carte(Couleur couleur, int valeur){ // Test des cas extreme + association d'une couleur et d'une valeur
        if(couleur == null){
            throw new IllegalArgumentException("il faut une couleur");
        }
        if(valeur < 1 || valeur > 9){
            throw new IllegalArgumentException("il faut que la valeur soit comprise entre 1 et 9");
        }
        this.couleur = couleur; 
        this.valeur = valeur;
    }

    public Couleur getCouleur(){
        return couleur;
    }

    public int getValeur(){ 
        return valeur;
    }

    @Override 
    public String toString(){ // concatene le format de la carte 7R pour un 7 rouge 
        return valeur + couleur.getSymbole();
    }
    @Override 
    public int compareTo(Carte autre){ // fonction qui permet de classer dans l'ordre croissant les cartes sur la Borne plus tard
        int resultat = Integer.compare(valeur, autre.valeur); //compare permet de faire a - b sans les bugs

        if(resultat != 0){
            return resultat;
        }
        return couleur.compareTo(autre.couleur);

    }

    @Override
    public boolean equals(Object o){
        if(this == o){ // meme objet en mémoire => meme objet
            return true;
        }
        if(!(o instanceof Carte)){ // o est null ou n'est pas une Carte : pas égal
            return false;
        }
        Carte autre = (Carte) o; // on sait maintenant que o est une Carte, on peut le convertir
        return valeur == autre.valeur && couleur == autre.couleur; // == marche pour un int et pour une enum
    }

    // Règle de Java : si deux objets sont égaux avec equals, ils DOIVENT avoir le même hashCode.

    @Override
    public int hashCode(){
        return Objects.hash(couleur, valeur);
    }

}