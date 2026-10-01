package mx.tecnm.culiacan;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BlackJackV1 implements VersionJuego{
    @Override
    public void jugar(Reglas reglas, VistaJuego vistaJuego) {
        Baraja baraja = new Baraja();
        baraja.partir();
        baraja.barajar();

        MazoJugador mazoJugador = new MazoJugador(baraja);
        GeneradorDeFichas generadorDeFichas = new GeneradorDeFichas();

        Croupier croupier = new Croupier(generadorDeFichas,mazoJugador);
        JugadorApostador jugadorApostador = new JugadorApostador(generadorDeFichas,mazoJugador);

        croupier.repartirCartaAJugador(jugadorApostador);

        System.out.println("---------- BlackJack ----------");
        System.out.println();
        System.out.println("Jugador #1: ");
        System.out.println();

        Map<Ficha,Integer> cantidadFichas = new TreeMap<>();
        for (Ficha ficha: jugadorApostador.getFichas()){
            cantidadFichas.put(ficha, cantidadFichas.getOrDefault(ficha, 0) + 1);
        }

        System.out.println("Fichas:");
        for (Map.Entry<Ficha,Integer> entry : cantidadFichas.entrySet()){
            Ficha key = entry.getKey();
            int value = entry.getValue();
            System.out.println(key + ": " + value);
        }

        System.out.print("Cuanto quieres apostar: ");
        Keyboard.readInt();
    }
}
