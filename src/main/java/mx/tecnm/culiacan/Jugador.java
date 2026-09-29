package mx.tecnm.culiacan;

import java.util.List;

public abstract class Jugador {
    List<Ficha> fichas;
    MazoJugador mazoJugador;

    public Jugador(List<Ficha> fichas, MazoJugador mazoJugador) {
        this.fichas = fichas;
        this.mazoJugador = mazoJugador;
    }
}
