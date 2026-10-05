package mx.tecnm.culiacan;

import java.util.List;
import java.util.Map;

public interface Reglas {

    int PUNTAJE_BLACKJACK = 21;
    int PUNTAJE_MINIMO_CROUPIER = 17;

    int obtenerPuntaje(List<String> baraja);

    boolean esBlackJack(List<String> baraja, int puntaje);

    DictamenJugador calcularDictamen(int puntajeJugador, int puntajeCroupier);

    boolean tieneFichas(Map<Ficha, Integer> fichas);

    void validarAccion(int opcionAccion);

    void procesarApuesta(int tipoApuesta, Map<Ficha, Integer> fichas, Croupier croupier, VistaJuego vistaJuego);

    void distribucionApuestas(DictamenJugador dictamenJugador, JugadorApostador jugadorApostador, Croupier croupier);
}