package mx.tecnm.culiacan;

public class Main {
    public static void main(String[] args) {
        Baraja baraja = new Baraja();
        baraja.partir();
        baraja.barajar();
        System.out.println(baraja.getPilaCartas());

        MazoJugador mazoJugador = new MazoJugador(baraja);
        GeneradorDeFichas generadorDeFichas = new GeneradorDeFichas();

        Croupier croupier = new Croupier(generadorDeFichas,mazoJugador);
        JugadorApostador jugadorApostador = new JugadorApostador(generadorDeFichas,mazoJugador);

        croupier.repartirCartaAJugador(jugadorApostador);

        System.out.println();

        System.out.println("Juego Jugador: ");
        System.out.println(jugadorApostador.getBarajaJugador());
        System.out.println(jugadorApostador.getFichas());

        System.out.println();

        System.out.println("Juego Croupier: ");
        System.out.println(croupier.getPilaCartas());
        System.out.println(croupier.getFichas());

        System.out.println();

        System.out.println("Juego Jugador: ");
        jugadorApostador.pedirCarta(croupier);
        System.out.println(jugadorApostador.getBarajaJugador());
        System.out.println(jugadorApostador.getFichas());

        System.out.println();

        System.out.println("Juego Croupier: ");
        System.out.println(croupier.getPilaCartas());
        System.out.println(croupier.getFichas());

    }
}