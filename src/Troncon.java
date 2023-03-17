public class Troncon {

    private Ligne ligne;
    private Station depart;
    private Station arrivee;
    private int duree;

    public Troncon(Ligne ligne, Station depart, Station arrivee, int duree) {
        this.ligne = ligne;
        this.depart = depart;
        this.arrivee = arrivee;
        this.duree = duree;
    }

    @Override
    public String toString() {
        return "Troncon{" +
                "ligne=" + ligne +
                ", depart='" + depart + '\'' +
                ", arrivee='" + arrivee + '\'' +
                ", duree=" + duree +
                '}';
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
