import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ListeDAdjacence {
    private Map<Station, Set<Troncon>> correspondanceStationsTroncons;

    public ListeDAdjacence() {
        this.correspondanceStationsTroncons = new HashMap<>();
    }

    protected void ajouterStation(Station s) {
        if(!correspondanceStationsTroncons.containsKey(s)) correspondanceStationsTroncons.put(s,new HashSet<>());
    }

    protected void ajouterTroncon(Troncon t) {
        correspondanceStationsTroncons.get(t.getDepart()).add(t);
    }

    public Set<Troncon> tronconsSortants(Station s) {
        return correspondanceStationsTroncons.get(s);
    }

    public boolean sontAdjacents(Station a1, Station a2) {
        for (Troncon troncon : correspondanceStationsTroncons.get(a1)) {
            if (troncon.getArrivee().equals(a2)) return true;
        }
        for (Troncon flight : correspondanceStationsTroncons.get(a2)) {
            if (flight.getArrivee().equals(a2)) return true;
        }
        return false;
    }
}
