package com.schottenTotten.view;

import com.schottenTotten.controller.Coup;
import com.schottenTotten.controller.Jeu;
import com.schottenTotten.model.Borne;
import com.schottenTotten.model.Carte;
import com.schottenTotten.model.Joueur;

import java.util.List;
import java.util.Scanner;

/** Interface en mode texte. */
public class VueConsole implements Vue {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Version volontairement simple : une ligne par borne.
     *   Borne 1 : [7R 2B    ] | [5V       ]   (Alice)
     *
     * TODO (étape 4, facultatif) : un affichage plus joli (bornes en colonnes,
     * couleurs ANSI : "\u001B[31m" + texte + "\u001B[0m" écrit en rouge).
     */
    @Override
    public void afficherPlateau(Jeu jeu) {
        Joueur j1 = jeu.getJoueur(0);
        Joueur j2 = jeu.getJoueur(1);
        System.out.println("\n        " + j1.getNom() + "  |  " + j2.getNom()
                + "        (pioche : " + jeu.getTaillePioche() + ")");
        for (Borne b : jeu.getBornes()) {
            String proprio = b.estRevendiquee() ? "  -> " + b.getProprietaire().getNom() : "";
            System.out.printf("Borne %d : [%-9s] | [%-9s]%s%n",
                    b.getNumero() + 1, texte(b.getCartes(j1)), texte(b.getCartes(j2)), proprio);
        }
    }

    @Override
    public void afficherMain(Joueur joueur) {
        StringBuilder sb = new StringBuilder("Main de " + joueur.getNom() + " : ");
        List<Carte> main = joueur.getMain();
        for (int i = 0; i < main.size(); i++) {
            sb.append(i + 1).append(")").append(main.get(i)).append("  ");
        }
        System.out.println(sb);
    }

    @Override
    public Coup demanderCoup(Joueur joueur) {
        int carte = lireEntier("Quelle carte ? ", 1, joueur.getMain().size());
        int borne = lireEntier("Sur quelle borne ? ", 1, Jeu.NB_BORNES);
        return new Coup(carte - 1, borne - 1);   // l'utilisateur compte à partir de 1, Java à partir de 0
    }

    @Override
    public void afficherMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void afficherGagnant(Joueur joueur) {
        System.out.println("\n*** " + joueur.getNom() + " remporte la partie ! ***");
    }

    // ---------- Saisies : jamais de crash si l'utilisateur tape n'importe quoi ----------

    /** Redemande tant que la saisie n'est pas un entier entre min et max. */
    public int lireEntier(String question, int min, int max) {
        while (true) {
            System.out.print(question + "(" + min + "-" + max + ") : ");
            String ligne = scanner.nextLine().trim();
            try {
                int n = Integer.parseInt(ligne);
                if (n >= min && n <= max) {
                    return n;
                }
            } catch (NumberFormatException e) {
                // pas un nombre : on tombe sur le message ci-dessous
            }
            System.out.println("Saisie invalide.");
        }
    }

    public String lireTexte(String question) {
        while (true) {
            System.out.print(question);
            String ligne = scanner.nextLine().trim();
            if (!ligne.isEmpty()) {
                return ligne;
            }
        }
    }

    private static String texte(List<Carte> cartes) {
        StringBuilder sb = new StringBuilder();
        for (Carte c : cartes) {
            sb.append(c).append(' ');
        }
        return sb.toString().trim();
    }
}
