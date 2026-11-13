package com.schottenTotten.model;

import com.schottenTotten.ai.Strategie;
import com.schottenTotten.controller.Coup;
import com.schottenTotten.controller.Jeu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Joueur {

    private final String nom;
    private final int position;
    private final List<Carte> main = new ArrayList<>();
    private final Strategie strategie;
    
    public Joueur(String nom, int position, Strategie strategie){
        if(position != 0 && position != 1){
            throw new IllegalArgumentException("position doit être 0 ou 1");
        }
        this.nom = nom;
        this.position = position; 
        this.strategie = strategie;
    }
    public void piocher(Pioche pioche){
        main.add(pioche.piocher());
    }

    public void jouerCarte(int indexCarte, Borne borne){
        Carte carte = retirerCarte(indexCarte);
        borne.ajouterCarte(this , carte);
    }

    public Carte retirerCarte(int indexCarte){
        if(indexCarte < 0 || indexCarte >= main.size()){
            throw new IllegalArgumentException("on ne peut retirer une carte qui n'est pas dans la main");
        }
        return main.remove(indexCarte);
    }

    public Coup choisirCoup(Jeu jeu){
        if(strategie == null){
            throw new IllegalStateException("C'est a l'humain de choisir");
        }
        else{
            return strategie.choisirCoup(jeu, this);
        }
    }

    public String getNom(){
        return nom;
    }

    public int getPosition(){
        return position;
    }

    public List<Carte> getMain(){ // lecture seule : personne ne peut modifier la main de l'extérieur
        return Collections.unmodifiableList(main);
    }

    public boolean estHumain(){
        return strategie == null;
    }
}
