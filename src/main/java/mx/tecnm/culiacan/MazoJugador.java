package mx.tecnm.culiacan;

import java.util.Deque;

/**
 * Representa el mazo utilizado por los jugadores durante una partida.
 * <p>
 * Esta clase actúa como intermediaria entre los jugadores y la
 * {@link Baraja}, permitiendo acceder a la baraja completa y obtener
 * la pila de cartas que será administrada por el croupier.
 * </p>
 *
 * @see Baraja
 * @see Croupier
 * @see Jugador
 */
public class MazoJugador {

    /**
     * Baraja asociada al mazo del jugador.
     */
    private Baraja baraja;

    /**
     * Crea un nuevo mazo a partir de una baraja existente.
     *
     * @param baraja baraja que será utilizada durante la partida
     */
    public MazoJugador(Baraja baraja) { this.baraja = baraja; }

    /**
     * Devuelve la baraja asociada al mazo.
     *
     * @return baraja utilizada durante la partida
     */
    public Baraja getBaraja() { return baraja; }

    /**
     * Devuelve la pila de cartas que será utilizada y administrada
     * por el croupier durante la partida.
     * <p>
     * La estructura retornada corresponde directamente a la pila
     * almacenada dentro de la {@link Baraja}.
     * </p>
     *
     * @return pila de cartas disponibles para el croupier
     */
    public Deque<String> obtenerBarajaCroupier() { return baraja.getPilaCartas(); }
}