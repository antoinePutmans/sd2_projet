import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    Set<Station> stationsVisitees;
    int nbStations = stations.size();
    int plusPetitTpsTransport = Integer.MAX_VALUE;

    Set<Troncon> sortants = listeDAdjacence.tronconsSortants(stationDepart);

    // utiliser TreeSet ou TreeMap pour faire le tableau de dijkstra
    //Map<Station,Integer> stationChemin = new TreeMap<>(Comparator.comparing((a,b) -> a.get))

    // au secour

  }
}
