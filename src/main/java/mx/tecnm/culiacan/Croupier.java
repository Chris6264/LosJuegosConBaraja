package mx.tecnm.culiacan;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Representa al croupier dentro de una partida de BlackJack.
 * <p>
 * El croupier hereda el comportamiento general de {@link Jugador} y se
 * encarga principalmente de administrar la baraja utilizada durante la
 * partida, repartir cartas a los jugadores, tomar cartas para su propia mano,
 * recoger las cartas al finalizar una ronda y controlar las apuestas
 * relacionadas con el croupier.
 * </p>
 *
 * <p>
 * También permite barajar y partir las cartas mediante el
 * {@link MazoJugador} asociado.
 * </p>
 *
 * @see Jugador
 * @see JugadorApostador
 * @see MazoJugador
 * @see GeneradorDeFichas
 */
public class Croupier extends Jugador {

    /**
     * Pila que contiene las cartas disponibles para ser repartidas
     * durante la partida.
     */
    private Deque<String> pilaCartas;

    /**
     * Lista que contiene las cartas que actualmente posee el croupier.
     */
    private List<String> barajaCroupier;

    /**
     * Almacena las fichas y cantidades correspondientes a la apuesta
     * asociada al croupier.
     */
    private Map<Ficha, Integer> apuesta;

    /**
     * Crea un nuevo croupier con un generador de fichas y un mazo de juego.
     * <p>
     * Inicializa la pila de cartas utilizando la baraja proporcionada por
     * {@link MazoJugador}, crea una mano vacía para el croupier y prepara
     * una estructura para almacenar las apuestas.
     * </p>
     *
     * @param generadorDeFichas generador encargado de proporcionar las fichas
     *                          utilizadas por el jugador
     * @param mazoJugador mazo que contiene la baraja utilizada durante
     *                    la partida
     */
    public Croupier(
            GeneradorDeFichas generadorDeFichas,
            MazoJugador mazoJugador) {

        super(generadorDeFichas, mazoJugador);

        this.pilaCartas = mazoJugador.obtenerBarajaCroupier();
        this.barajaCroupier = new ArrayList<>();
        this.apuesta = new HashMap<>();
    }

    /**
     * Reparte dos cartas a un jugador apostador.
     * <p>
     * Las cartas se extraen de la parte superior de la pila y se almacenan
     * temporalmente en una nueva lista. Posteriormente, dicha lista se asigna
     * como la mano del jugador apostador.
     * </p>
     *
     * @param jugadorApostador jugador que recibirá las dos cartas iniciales
     */
    public void repartirCartaAJugador(JugadorApostador jugadorApostador) {
        List<String> barajaJugador = new ArrayList<>();

        barajaJugador.add(pilaCartas.pop());
        barajaJugador.add(pilaCartas.pop());

        jugadorApostador.setBarajaJugador(barajaJugador);
    }

    /**
     * Recoge las cartas utilizadas al finalizar una ronda.
     * <p>
     * Devuelve a la pila tanto las cartas del jugador como las cartas
     * utilizadas por el croupier. Después de devolverlas, la mano del
     * croupier queda vacía.
     * </p>
     *
     * @param barajaJugador cartas pertenecientes al jugador que serán
     *                      devueltas a la pila
     */
    public void recogerCartas(List<String> barajaJugador) {
        pilaCartas.addAll(barajaJugador);
        pilaCartas.addAll(barajaCroupier);
        barajaCroupier.clear();
    }

    /**
     * Devuelve las cartas que actualmente posee el croupier.
     *
     * @return lista de cartas del croupier
     */
    public List<String> getBarajaCroupier() { return barajaCroupier; }

    /**
     * Permite al croupier tomar una carta de la pila.
     * <p>
     * La carta únicamente se extrae si todavía existen cartas disponibles
     * dentro de la pila.
     * </p>
     */
    public void tomarCarta() { if (!pilaCartas.isEmpty())  barajaCroupier.add(pilaCartas.pop()); }

    /**
     * Mezcla las cartas de la baraja utilizada durante la partida.
     * <p>
     * La operación se delega a la instancia de {@link Baraja} contenida
     * dentro del {@link MazoJugador}.
     * </p>
     */
    public void barajearCartas() { getMazoJugador().getBaraja().barajar(); }

    /**
     * Parte las cartas de la baraja.
     * <p>
     * La operación se delega a la instancia de {@link Baraja} contenida
     * dentro del {@link MazoJugador}.
     * </p>
     */
    public void partirCartas() { getMazoJugador().getBaraja().partir(); }

    /**
     * Devuelve la pila que contiene las cartas disponibles para repartir.
     *
     * @return pila de cartas disponibles
     */
    public Deque<String> getPilaCartas() { return pilaCartas; }

    /**
     * Devuelve las fichas y cantidades que forman la apuesta actual.
     *
     * @return mapa que relaciona cada tipo de ficha con la cantidad apostada
     */
    public Map<Ficha, Integer> getApuesta() { return apuesta; }

    /**
     * Añade una ficha y su cantidad correspondiente a la apuesta.
     * <p>
     * Si el tipo de ficha ya existe dentro de la apuesta, su cantidad
     * anterior será reemplazada por la nueva cantidad indicada.
     * </p>
     *
     * @param ficha tipo de ficha que será añadida a la apuesta
     * @param cantidadFichas cantidad de fichas que serán registradas
     */
    public void añadirApuesta(Ficha ficha, int cantidadFichas) { apuesta.put(ficha, cantidadFichas); }
}