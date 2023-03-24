public class Troncon {

    private final Ligne ligne;
    private final Station depart;
    private final Station arrivee;
    private final int duree;

    public Troncon(Ligne ligne, Station depart, Station arrivee, int duree) {
        this.ligne = ligne;
        this.depart = depart;
        this.arrivee = arrivee;
        this.duree = duree;
    }

    @Override
    public String toString() {
        return "Troncon[" +
                "départ='" + depart + '\'' +
                ", arrivée='" + arrivee + '\'' +
                ", durée=" + duree +
                ", ligne=" + ligne +
                ']';
    }

    public Station getDepart() {
        return depart;
    }

    public Station getArrivee() {
        return arrivee;
    }

    public int getDuree() {
        return duree;
    }

    public Ligne getLigne() {
        return ligne;
    }
}
