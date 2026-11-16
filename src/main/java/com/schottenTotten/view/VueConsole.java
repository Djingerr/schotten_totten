package com.schottenTotten.view;

import com.schottenTotten.model.Borne;
import com.schottenTotten.model.Carte;
import com.schottenTotten.model.Joueur;

import java.util.List;
import java.util.Scanner;

/**
 * On veut cette interface
 *
 *            Alice │ Borne │ Bob
 *     ─────────────┼───────┼─────────
 *          7R 2B   │ < 1   │ 5V 5B 5J
 *                  │   2 > │ 9P
 *
 * < = borne revendiquée par le joueur de gauche, > = par celui de droite.
 */
public class VueConsole implements Vue {

    private static final String RESET = "\u001B[0m";

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void afficherMessage(String message) {
        System.out.println(message);
    }

    @Override
    public int choisirOption(String titre, List<String> options) {
        System.out.println(titre);
        for (int i = 0; i < options.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + options.get(i));
        }
        return lireEntier("Choix ", 1, options.size()) - 1;
    }
    @Override
    public String demanderTexte(String question) {
        while (true) {
            System.out.print(question);
            String ligne = scanner.nextLine().trim();
            if (!ligne.isEmpty()) {
                return ligne;
            }
        }
    }
    @Override
    public void afficherPlateau(Joueur j1, Joueur j2, List<Borne> bornes, int taillePioche) {
        // Largeur de chaque colonne = le plus long contenu (nom ou cartes), pour que tout reste aligné
        // même si une borne a plus de 3 cartes (variante tactique) ou si un nom est long.
        int largeurG = j1.getNom().length();
        int largeurD = j2.getNom().length();
        for (Borne b : bornes) {
            largeurG = Math.max(largeurG, brut(b.getCartes(j1)).length());
            largeurD = Math.max(largeurD, brut(b.getCartes(j2)).length());
        }

        System.out.println();
        System.out.println(" ".repeat(largeurG - j1.getNom().length()) + j1.getNom() + " │ Borne │ " + j2.getNom());
        System.out.println("─".repeat(largeurG) + "─┼───────┼─" + "─".repeat(largeurD));

        int bornesJ1 = 0;
        int bornesJ2 = 0;
        for (Borne b : bornes) {
            Joueur proprio = b.getProprietaire();
            if (proprio == j1) bornesJ1++;
            if (proprio == j2) bornesJ2++;

            List<Carte> gauche = b.getCartes(j1);
            // On ne peut pas aligner avec printf("%9s") : les codes couleur sont des caractères
            // invisibles qui seraient comptés. On calcule le décalage sur le texte sans couleur.
            String decalage = " ".repeat(largeurG - brut(gauche).length());
            // Toujours 5 caractères, comme "Borne" dans l'en-tête, sinon la ligne se décale.
            String milieu = (proprio == j1 ? "< " : "  ") + (b.getNumero() + 1) + (proprio == j2 ? " >" : "  ");
            System.out.println(decalage + colore(gauche) + " │ " + milieu + " │ " + colore(b.getCartes(j2)));
        }

        System.out.println("Pioche : " + taillePioche + " carte(s)   |   "
                + j1.getNom() + " : " + bornesJ1 + " borne(s) <   "
                + j2.getNom() + " : " + bornesJ2 + " borne(s) >");
    }

    @Override
    public void afficherMain(Joueur joueur) {
        StringBuilder sb = new StringBuilder("Main de " + joueur.getNom() + " :");
        List<Carte> main = joueur.getMain();
        for (int i = 0; i < main.size(); i++) {
            sb.append("   ").append(i + 1).append(") ").append(colorer(main.get(i)));
        }
        System.out.println(sb);
    }

    @Override
    public int demanderCarte(Joueur joueur) {
        int taille = joueur.getMain().size();
        if (taille == 0) {
            throw new IllegalStateException(joueur.getNom() + " n'a plus de carte");
        }
        return lireEntier("Quelle carte ? ", 1, taille) - 1;
    }

    @Override
    public int demanderBorne(int nbBornes) {
        return lireEntier("Sur quelle borne ? ", 1, nbBornes) - 1;
    }

    @Override
    public void afficherGagnant(Joueur gagnant) {
        System.out.println(gagnant == null
                ? "\n*** Fin de partie sans gagnant ***"
                : "\n*** " + gagnant.getNom() + " remporte la partie ! ***");
    }

    /** En gros assert */
    private int lireEntier(String question, int min, int max) {
        while (true) {
            System.out.print(question + "(" + min + "-" + max + ") : ");
            try {
                int n = Integer.parseInt(scanner.nextLine().trim());
                if (n >= min && n <= max) {
                    return n;
                }
            } catch (NumberFormatException e) {
                // pas un nombre : on tombe sur le message ci-dessous
            }
            System.out.println("Saisie invalide.");
        }
    }

    private String colorer(Carte c) {
        String code = switch (c.getCouleur()) {
            case ROUGE -> "31";
            case VERT -> "32";
            case JAUNE -> "33";
            case BLEU -> "34";
            case VIOLET -> "35";
            case MARRON -> "38;5;130";   // pas de marron en ANSI de base : couleur n°130 sur 256
        };
        return "\u001B[" + code + "m" + c + RESET;
    }

    /** "7R 2B" sans couleur : sert à mesurer la largeur réellement visible. */
    private String brut(List<Carte> cartes) {
        StringBuilder sb = new StringBuilder();
        for (Carte c : cartes) 
        {
            sb.append(sb.length() == 0 ? "" : " ").append(c);
        }
        return sb.toString();
    }

    private String colore(List<Carte> cartes) {
        StringBuilder sb = new StringBuilder();
        for (Carte c : cartes) {
            sb.append(sb.length() == 0 ? "" : " ").append(colorer(c));
        }
        return sb.toString();
    }
}
