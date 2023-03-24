import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Graph {

  private Map<Integer, Ligne> correspondanceIDLigne;
  private ListeDAdjacence listeDAdjacence;
  private Set<Station> stations;

  /**
   * Constructeur de la classe Graph. Construit le graphe du réseau à partir de deux fichiers
   * contenant les informations sur les lignes et les tronçons.
   *
   * @param lignesFichier   Fichier contenant les informations sur les lignes.
   * @param tronconsFichier Fichier contenant les informations sur les tronçons.
   * @throws FileNotFoundException Si les fichiers ne sont pas trouvés.
   */
  public Graph(File lignesFichier, File tronconsFichier) throws FileNotFoundException {
    String[] words;
    correspondanceIDLigne = new HashMap<>();
    listeDAdjacence = new ListeDAdjacence();
    stations = new HashSet<>();
    Scanner scan = new Scanner(lignesFichier);
    while (scan.hasNext()) {
      words = scan.nextLine().split(",");
      Ligne ligne = new Ligne(Integer.parseInt(words[0]), words[1], words[2], words[3], words[4],
          Integer.parseInt(words[5]));
      correspondanceIDLigne.put(Integer.parseInt(words[0]), ligne);
    }
    scan = new Scanner(tronconsFichier);
    while (scan.hasNext()) {
      words = scan.nextLine().split(",");
      Station stationDepart = new Station(words[1]);
      Station stationArrivee = new Station(words[2]);
      stations.add(stationArrivee);
      stations.add(stationDepart);
      listeDAdjacence.ajouterStation(stationDepart);
      listeDAdjacence.ajouterTroncon(
          (new Troncon(correspondanceIDLigne.get(Integer.parseInt(words[0])), stationDepart,
              stationArrivee, Integer.parseInt(words[3]))));
    }
    scan.close();

  }

  /**
   * Trouve UN chemin (parmi plusieurs possibles) de la Station de départ à la station d'arrivée en
   * le moins de tronçons possibles. Algorithme utilisé = BFS
   *
   * @param depart  Station de départ (String)
   * @param arrivee Station d'arrivée (String)
   */
  public void calculerCheminMinimisantNombreTroncons(String depart, String arrivee) {
    Station stationDep = new Station(depart);
    Station stationArr = new Station(arrivee);
    Station stationCourante = stationDep;
    Deque<Station> file = new ArrayDeque<>();
    Set<Station> stationsVisitees = new HashSet<>();
    HashMap<Station, Station> arriveeSource = new HashMap<>();
    stationsVisitees.add(stationCourante);
    file.add(stationCourante);

    while (!file.isEmpty()) {
      stationCourante = file.pollFirst();
      if (stationCourante.equals(stationArr)) {
        break;
      }
      Set<Troncon> tronconsSortants = listeDAdjacence.tronconsSortants(stationCourante);
      if (tronconsSortants == null) {
        continue;
      }
      for (Troncon e : tronconsSortants) {
        if (!stationsVisitees.contains(e.getArrivee())) {
          stationsVisitees.add(e.getArrivee());
          file.add(e.getArrivee());
          arriveeSource.put(e.getArrivee(), stationCourante);
        }
      }
    }

    Deque<Troncon> chemin = new ArrayDeque<>();
    int nbTroncons;
    int dureeTransport = 0;
    int dureeTotale = 0;
    while (!stationCourante.equals(stationDep)) {
      Station stationSource = arriveeSource.get(stationCourante);
      Troncon troncon = listeDAdjacence.getTroncon(stationSource, stationCourante);
      if (chemin.isEmpty()
          || (!troncon.getLigne().equals(chemin.peekFirst().getLigne()))) {
        // Je regarde si il y a un changement de ligne. Si oui, j'ajoute son temps d'attente moyen à la durée totale.
        dureeTotale += troncon.getLigne().getTempsAttenteMoyen();
      }
      chemin.push(troncon);
      dureeTransport += troncon.getDuree();
      dureeTotale += troncon.getDuree();
      stationCourante = stationSource;
    }
    System.out.println("Chemin trouvé (calculerCheminMinimisantNombreTroncons):");
    nbTroncons = chemin.size();
    while (!chemin.isEmpty()) {
      System.out.println(chemin.pop());
    }
    System.out.println("nbTroncons : " + nbTroncons);
    System.out.println("dureeTransport: " + dureeTransport + " dureeTotale: " + dureeTotale);

  }

  /**
   * Trouve le chemin minimisant le temps de transport en utilisant l'algorithme de Dijkstra.
   *
   * @param depart  Station de départ (String)
   * @param arrivee Station d'arrivée (String)
   */
  public void calculerCheminMinimisantTempsTransport(String depart, String arrivee) {
    // -----------------------------  Initialisations ---------------------------------------

    Station stationDepart = new Station(depart);
    Station stationArrivee = new Station(arrivee);
    // Map utilisé pour retracer le chemin parcouru pour le plus court chemin
    Map<Station, Troncon> precedents = new HashMap<>();

    Map<Station, Integer> distances = new HashMap<>(); // les distances entre départ et Station...
    distances.put(stationDepart, 0);

    // Comparateur de distance utilisé pour le TreeSet
    Comparator<Station> stationDistanceComparator = (s1, s2) -> {
      Integer d1 = distances.get(s1);
      Integer d2 = distances.get(s2);

      if (d1 < d2) {
        return -1;
      } else if (d1 > d2) {
        return 1;
      } else {
        return s1.getNom().compareTo(s2.getNom());
      }
    };

    // utilisation de TreeMap pour retrouver le sommet au cout minimal directement
    // TreeSet "etiquettesProvisoires" & Hashmap "distances" doivent être manipulées en même temps
    TreeSet<Station> etiquettesProvisoires = new TreeSet<>(
        stationDistanceComparator); // stations non encore visitées
    etiquettesProvisoires.add(stationDepart);

    Map<Station, Integer> etiquettesDefinitives = new HashMap<>(); // les stations déjà visitées
    etiquettesDefinitives.put(stationDepart, 0);

    // --------------------------- Corps de l'algorithme de Dijkstra -----------------------------

    while (!etiquettesProvisoires.isEmpty()) { // parcourir tous les elements de mes etiquettes provisoires
      Station stationCourante = etiquettesProvisoires.first();

      etiquettesProvisoires.remove(stationCourante);
      int distance = distances.get(stationCourante);

      if ((stationCourante != stationDepart) && etiquettesDefinitives.containsKey(
          stationCourante)) {
        continue;
      }

      etiquettesDefinitives.put(stationCourante, distance);

      // si je trouve mon Sommet d'arrivée je sors, car j'ai trouvé le plus court chemin.
      if (stationCourante.equals(stationArrivee)) {
        break;
      }

      Set<Troncon> tronconsSortants = listeDAdjacence.tronconsSortants(stationCourante);
      for (Troncon t : tronconsSortants) { // pour tous les adjacents de stationCourante
        Station stationAdjacente = t.getArrivee();
        int distanceAdd = distance + t.getDuree();

        if (distances.get(stationAdjacente)
            == null) { // par extension, la station n'est pas présente dans TreeSet non plus.
          distances.put(stationAdjacente, distanceAdd);
          etiquettesProvisoires.add(stationAdjacente);
          // Ajouter le tronçon précédent dans la HashMap "precedents"
          precedents.put(stationAdjacente, t);
        } else {
          int distanceDeDepartATronconActuel = distances.get(stationAdjacente);
          // mise à jour étiquettes si trouvé distance plus courte
          if (distanceDeDepartATronconActuel > distanceAdd) {
            etiquettesProvisoires.remove(stationAdjacente);
            distances.put(stationAdjacente, distanceAdd);
            etiquettesProvisoires.add(stationAdjacente);

            // Ajouter le tronçon précédent dans la HashMap "precedents"
            precedents.put(stationAdjacente, t);
          }
        }

      }

    }

    // ------------------- Sortie console. Retraçage du plus court chemin  -------------------------

    int dureeTransport = 0;
    int dureeTotale = 0;
    int nbTroncons;

    // Créer une pile pour stocker les tronçons du chemin
    Deque<Troncon> chemin = new ArrayDeque<>();

    // Retracer le chemin à partir de la station d'arrivée
    Station stationActuelle = stationArrivee;
    Ligne lignePrecedente = null;

    System.out.println("Chemin trouvé (calculerCheminMinimisantTempsTransport):");
    // Parcourir les stations jusqu'à la station de départ
    while (!stationActuelle.equals(stationDepart)) {
      Troncon tronconPrecedent = precedents.get(stationActuelle);
      chemin.push(tronconPrecedent); // Ajouter le tronçon à la pile
      dureeTransport += tronconPrecedent.getDuree();
      dureeTotale += tronconPrecedent.getDuree();
      // si changement de ligne, j'ajoute son temps moyen au total durée de transport
      if (!tronconPrecedent.getLigne().equals(lignePrecedente)) dureeTotale += tronconPrecedent.getLigne().getTempsAttenteMoyen();
      stationActuelle = tronconPrecedent.getDepart(); // Passer à la station précédente
      lignePrecedente = tronconPrecedent.getLigne();
    }

    nbTroncons = chemin.size();
    while (!chemin.isEmpty()) {
      System.out.println(chemin.pop());
    }

    System.out.println("nbTroncons : " + nbTroncons);
    System.out.println("dureeTransport: " + dureeTransport + " dureeTotale: " + dureeTotale);

  }
}
