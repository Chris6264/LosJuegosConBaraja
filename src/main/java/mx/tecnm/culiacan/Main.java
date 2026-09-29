package mx.tecnm.culiacan;

public class Main {
    public static void main(String[] args) {
        Baraja baraja = new Baraja();
        System.out.println(baraja.getPilaCartas());
        baraja.partir();
        System.out.println(baraja.getPilaCartas());
        baraja.barajar();
        System.out.println(baraja.getPilaCartas().size());
    }
}