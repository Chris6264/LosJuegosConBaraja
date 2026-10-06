package mx.tecnm.culiacan;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * Representa a un jugador que participa activamente realizando apuestas
 * dentro de una partida de BlackJack.
 * <p>
 * Esta clase hereda las dependencias comunes definidas en {@link Jugador}
 * y mantiene tanto la mano actual del jugador como las fichas disponibles
 * para realizar apuestas.
 * </p>
 *
 * <p>
 * El jugador apostador puede solicitar cartas adicionales al
 * {@link Croupier}, consultar su mano y acceder a las fichas que posee.
 * </p>
 *
 * @see Jugador
 * @see Croupier
 * @see Ficha
 * @see GeneradorDeFichas
 * @see MazoJugador
 */
public class JugadorApostador extends Jugador {

    /**
     * Contiene las cartas que actualmente posee el jugador.
     */
    private List<String> barajaJugador;

    /**
     * Contiene los tipos de fichas y la cantidad disponible
     * de cada una para el jugador.
     */
    private Map<Ficha, Integer> fichasJugador;

    /**
     * Crea un nuevo jugador apostador.
     * <p>
     * Inicializa una mano vacía y obtiene las fichas generadas por
     * {@link GeneradorDeFichas}.
     * </p>
     *
     * @param generadorDeFichas generador que proporciona las fichas iniciales
     *                          del jugador
     * @param mazoJugador mazo utilizado durante la partida
     */
    public JugadorApostador(
            GeneradorDeFichas generadorDeFichas,
            MazoJugador mazoJugador) {

        super(generadorDeFichas, mazoJugador);

        this.barajaJugador = new ArrayList<>();
        this.fichasJugador = generadorDeFichas.getFichas();
    }

    /**
     * Solicita una carta adicional al croupier.
     * <p>
     * Obtiene la pila de cartas administrada por el croupier y,
     * si todavía existen cartas disponibles, extrae la carta superior
     * y la agrega a la mano del jugador.
     * </p>
     *
     * @param croupier croupier encargado de administrar y repartir las cartas
     */
    public void pedirCarta(Croupier croupier) {
        Deque<String> pilaCartas = croupier.getPilaCartas();
        if (!pilaCartas.isEmpty()) barajaJugador.add(pilaCartas.pop());
    }

    /**
     * Devuelve las cartas que actualmente posee el jugador.
     *
     * @return lista de cartas de la mano actual
     */
    public List<String> getBarajaJugador() { return barajaJugador; }

    /**
     * Reemplaza la mano actual del jugador por una nueva lista de cartas.
     * <p>
     * Este método puede utilizarse, por ejemplo, cuando el croupier
     * reparte las cartas iniciales de una ronda.
     * </p>
     *
     * @param barajaJugador nueva lista de cartas del jugador
     */
    public void setBarajaJugador(List<String> barajaJugador) { this.barajaJugador = barajaJugador; }

    /**
     * Devuelve las fichas disponibles del jugador.
     *
     * @return mapa que relaciona cada tipo de ficha con la cantidad disponible
     */
    public Map<Ficha, Integer> getFichasJugador() { return fichasJugador; }
}