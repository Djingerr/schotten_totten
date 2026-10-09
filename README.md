# Schotten-Totten en Java (PROG1)

Squelette du projet, basé sur notre diagramme de classes (`main.tex`).
Ce qui marche déjà : menus, distribution, tours de jeu (humain ou IA aléatoire), affichage console.
Ce qui reste : cherche `TODO (étape N)` dans le code (VSCodium : Ctrl+Maj+F → "TODO").

## Lancer

```bash
sudo dnf install java-21-openjdk-devel maven   # une seule fois
mvn test                                        # tests JUnit
mvn package && java -jar target/schotten-totten-0.1.0.jar
```

Sans Maven : `javac -d out $(find src/main/java -name '*.java') && java -cp out com.schottenTotten.Main`

## Où est quoi

| Package | Classes | État |
|---|---|---|
| `model` | `Carte`, `Couleur`, `Pioche`, `Combinaison` | fini |
| `model` | `Joueur`, `Borne` | fini sauf `Borne.evaluer()` |
| `controller` | `Jeu` (abstrait), `JeuBase`, `JeuTactique` | tours OK, revendications et victoire à faire |
| `controller` | `JeuFactory`, `Variante`, `TypeJoueur`, `Coup` | fini |
| `view` | `Vue` (interface), `VueConsole` | fini (affichage basique) |
| `ai` | `Strategie` (interface), `StrategieAleatoire` | fini |

Écarts avec le diagramme (à reporter dans `main.tex`) :
- ajout de `Coup` (type de retour de `choisirCoup` / `demanderCoup`), `Variante` et `TypeJoueur` (enums) ;
- `Joueur` a une `position` (0 ou 1) : la `Borne` s'en sert pour savoir de quel côté poser ;
- `Borne` utilise `List<List<Carte>>` au lieu de `List<Carte>[2]` (les tableaux de génériques posent problème en Java) ;
- `Combinaison` est dans `model` (dans le diagramme elle a la couleur de `view`) ;
- `JeuTactique` hérite de `JeuBase` pour réutiliser `creerPioche()` (à discuter) ;
- `CarteTactique` n'est pas encore créée : voir la question de conception dans `JeuTactique.java`.

## Feuille de route (dans l'ordre)

1. **Lire** `Carte` → `Borne` → `Jeu.jouerPartie()` / `jouerTour()` → `Main`. Lancer une partie IA contre IA.
2. **Lancer les tests** (`mvn test`) et lire `CarteTest` : c'est le modèle pour écrire les autres.
3. **`Borne.evaluer()`** : détecter la combinaison. Puis enlever les `@Disabled` de `BorneTest`.
4. *(facultatif)* Un plus bel affichage dans `VueConsole.afficherPlateau()`.
5. **Redemander un coup invalide** dans `Jeu.jouerTour()` au lieu de faire perdre le tour.
6. **`Jeu.verifierRevendications()`** : d'abord la version simple (les deux côtés complets).
7. **`Jeu.determinerGagnant()`** : 5 bornes ou 3 adjacentes. À partir d'ici une partie a un gagnant.
8. **Tests JUnit** des revendications et de la victoire (exigé par le sujet), puis une IA un peu meilleure.
9. **Variante tactique** (`JeuTactique`, `CarteTactique`).

## Concepts Java utilisés (pour le rapport)

- **Encapsulation** : attributs `private`, listes renvoyées en lecture seule (`Collections.unmodifiableList`).
- **Héritage** : `JeuBase` et `JeuTactique` héritent de `Jeu` (abstrait) et ne redéfinissent que ce qui change.
- **Polymorphisme** : `Jeu` appelle `strategie.choisirCoup()` et `vue.afficherPlateau()` sans connaître la classe réelle.
- **Factory** : `JeuFactory` est le seul endroit qui instancie les variantes et les IA.
- **Extensibilité** : nouvelle IA = 1 classe + 1 ligne dans `TypeJoueur` + 1 `case` dans la factory. Nouvelle variante = idem avec `Variante`.
