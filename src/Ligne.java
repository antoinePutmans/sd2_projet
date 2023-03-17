public class Ligne {

    private int id;
    private String numero;
    private String premiereStation;
    private String destination;
    private String type;
    private int tempsAttenteMoyen;


    public Ligne(int id, String numero, String premiereStation, String destination, String type, int tempsAttenteMoyen) {
        this.id = id;
        this.numero = numero;
        this.premiereStation = premiereStation;
        this.destination = destination;
        this.type = type;
        this.tempsAttenteMoyen = tempsAttenteMoyen;
    }

    public int getTempsAttenteMoyen() {
        return tempsAttenteMoyen;
    }

    public int getId() {
        return id;
    }


    public String getNumero() {
        return numero;
    }


    public String getPremiereStation() {
        return premiereStation;
    }


    public String getDestination() {
        return destination;
    }


    public String getType() {
        return type;
    }


    @Override
    public String toString() {
        return "Ligne{" +
                "id=" + id +
                ", numero=" + numero +
                ", premiereStation='" + premiereStation + '\'' +
                ", destination='" + destination + '\'' +
                ", type='" + type + '\'' +
                ", tempsAttenteMoyen=" + tempsAttenteMoyen +
                '}';
    }
}
