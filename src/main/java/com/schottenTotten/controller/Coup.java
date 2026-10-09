package com.schottenTotten.controller;

/**
 * Un coup = "je pose la carte n°indexCarte de ma main sur la borne n°indexBorne".
 * Index à partir de 0 (la Vue convertit : l'utilisateur tape 1, on stocke 0).
 *
 * NOTE : pas dans le diagramme, mais il faut bien un type de retour pour
 * choisirCoup() et demanderCoup(). Pense à l'ajouter au diagramme.
 * "record" = petite classe immuable dont Java écrit tout seul le constructeur et les getters
 * (coup.indexCarte(), coup.indexBorne()).
 */
public record Coup(int indexCarte, int indexBorne) {
}
