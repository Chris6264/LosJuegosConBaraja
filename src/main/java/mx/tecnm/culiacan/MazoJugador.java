package mx.tecnm.culiacan;

import java.util.Deque;

public class MazoJugador {
    private Baraja baraja;

    public MazoJugador(Baraja baraja) {
        this.baraja = baraja;
    }

    public Deque<String> obtenerBarajaCroupier(){
        return baraja.getPilaCartas();
    }
}
