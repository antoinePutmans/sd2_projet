import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Graph {

  private Map<Integer, Ligne> correspondanceIDLigne;
  private ListeDAdjacence listeDAdjacence;
  private Set<Station> stations;

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

    Deque<Troncon> cheminInverse = new ArrayDeque<>();
    int nbTroncons;
    int dureeTransport = 0;
    int dureeTotale = 0;
    while (!stationCourante.equals(stationDep)) {
      Station stationSource = arriveeSource.get(stationCourante);
      Troncon troncon = listeDAdjacence.getTroncon(stationSource, stationCourante);
        if (cheminInverse.isEmpty()
            || (!troncon.getLigne().equals(cheminInverse.peekFirst().getLigne()))) {
            dureeTotale += troncon.getLigne().getTempsAttenteMoyen();
        }
      cheminInverse.push(troncon);
      dureeTransport += troncon.getDuree();
      dureeTotale += troncon.getDuree();
      stationCourante = stationSource;
    }
    System.out.println("Chemin trouvé (calculerCheminMinimisantNombreTroncons):");
    nbTroncons = cheminInverse.size();
    while (!cheminInverse.isEmpty()) {
      System.out.println(cheminInverse.pop());
    }
    System.out.println("nbTroncons : " + nbTroncons);
    System.out.println("dureeTransport: " + dureeTransport + " dureeTotale: " + dureeTotale);

  }

  public void calculerCheminMinimisantTempsTransport(String depart, String arrivee) {
    // LA REPONSE DE DIJKSTRA
    Station stationDepart = new Station(depart);
    Station stationArrivee = new Station(arrivee);
    Set<Station> stationsVisitees = new HashSet<>();

    Map<Station,Station> arriveeSource = new HashMap<>();

    Map<Station,Integer> distances = new HashMap<>(); // les distances entre départ et Station...
    for (Station s : stations){
      distances.put(s, Integer.MAX_VALUE);
    }
    distances.put(stationDepart, 0); // départ cout 0

    Comparator<Station> stationDistanceComparator = (s1, s2) -> {
      int d1 = distances.get(s1);
      int d2 = distances.get(s2);

      if (d1 < d2) {
        return -1;
      } else if (d1 > d2) {
        return 1;
      } else {
        return s1.getNom().compareTo(s2.getNom()); // en cas d'égalité des distances, le fait par nom de station
      }
    };

    // utilisation de treemap pour éviter de reparcourir et trouver distance minimale !
    TreeMap<Station, Integer> stationsNonVisitees = new TreeMap<>(stationDistanceComparator);
    stationsNonVisitees.putAll(distances);


    //Set<Troncon> sortants = listeDAdjacence.tronconsSortants(stationDepart);


    // au secour

  }
}
