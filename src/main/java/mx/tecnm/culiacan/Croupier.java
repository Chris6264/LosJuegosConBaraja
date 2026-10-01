package mx.tecnm.culiacan;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Croupier extends Jugador{

    private Deque<String> pilaCartas;
    private List<Ficha> fichas;

    public Croupier(GeneradorDeFichas generadorDeFichas, MazoJugador mazoJugador) {
        super(generadorDeFichas, mazoJugador);
        this.pilaCartas = mazoJugador.obtenerBarajaCroupier();
        this.fichas = new ArrayList<>();
    }

    public void repartirCartaAJugador(JugadorApostador jugadorApostador){
        List<String> barajaJugador = new ArrayList<>();
        barajaJugador.add(pilaCartas.pop());
        barajaJugador.add(pilaCartas.pop());
        jugadorApostador.setBarajaJugador(barajaJugador);
    }

    public Deque<String> getPilaCartas() {
        return pilaCartas;
    }

    public List<Ficha> getFichas() {
        return fichas;
    }
}
