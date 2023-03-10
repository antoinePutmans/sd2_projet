import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {

    private Map<Integer,Ligne> correspondanceIDLigne;
    private ListeDAdjacence listeDAdjacence;
    private Set<Station> stations;

    public Graph(File tronconsFichier, File lignesFichier) throws FileNotFoundException {
        String[] words;
        correspondanceIDLigne = new HashMap<>();
        listeDAdjacence = new ListeDAdjacence();
        stations = new HashSet<>();
        Scanner scan = new Scanner(lignesFichier);
        while (scan.hasNext()){
            words = scan.nextLine().split(",");
            Ligne ligne = new Ligne(Integer.parseInt(words[0]),words[1],words[2],words[3],words[4],Integer.parseInt(words[5]));
            correspondanceIDLigne.put(Integer.parseInt(words[0]),ligne);
        }
        scan = new Scanner(tronconsFichier);
        while(scan.hasNext()){
            words = scan.nextLine().split(",");
            Station stationDepart = new Station(words[1]);
            Station stationArrivee = new Station(words[2]);
            stations.add(stationArrivee);
            stations.add(stationDepart);
            listeDAdjacence.ajouterStation(stationDepart);
            listeDAdjacence.ajouterTroncon((new Troncon(correspondanceIDLigne.get(Integer.parseInt(words[0])),stationDepart,stationArrivee,Integer.parseInt(words[3]))));
        }
        scan.close();


        for (Station station : stations) {
            listeDAdjacence.ajouterStation(station);
        }

    }

    public void calculerCheminMinimisantNombreTroncons(String depart, String arrivee) {
        Station stationDepart = new Station(depart);
        Station stationArrivee = new Station(arrivee);

        //bfs ou dfs
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
