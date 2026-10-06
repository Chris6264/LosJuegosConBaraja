package mx.tecnm.culiacan;

import java.util.List;
import java.util.Map;

/**
 * Define las reglas generales que deben cumplirse durante una partida
 * de BlackJack.
 * <p>
 * Esta interfaz establece las constantes principales del juego y los
 * métodos necesarios para calcular puntajes, validar acciones, procesar
 * apuestas y determinar el resultado de una ronda.
 * </p>
 *
 * <p>
 * Las implementaciones concretas de esta interfaz son responsables de
 * aplicar la lógica específica de las reglas del BlackJack.
 * </p>
 *
 * @see ReglasBlackJack
 * @see DictamenJugador
 * @see JugadorApostador
 * @see Croupier
 * @see Ficha
 */
public interface Reglas {

    /**
     * Puntaje exacto necesario para obtener BlackJack.
     */
    int PUNTAJE_BLACKJACK = 21;

    /**
     * Puntaje mínimo con el que el croupier debe dejar de pedir cartas.
     */
    int PUNTAJE_MINIMO_CROUPIER = 17;

    /**
     * Cantidad mínima de jugadores permitida en una partida.
     */
    int MINIMO_JUGADORES = 2;

    /**
     * Cantidad máxima de jugadores permitida en una partida.
     */
    int MAXIMO_JUGADORES = 6;

    /**
     * Calcula el puntaje total de una mano de cartas.
     *
     * @param baraja cartas que forman la mano del jugador
     * @return puntaje total obtenido por la mano
     */
    int obtenerPuntaje(List<String> baraja);

    /**
     * Determina si una mano corresponde a un BlackJack.
     *
     * @param baraja cartas que forman la mano
     * @param puntaje puntaje total obtenido por la mano
     * @return {@code true} si la mano cumple las condiciones de BlackJack;
     *         {@code false} en caso contrario
     */
    boolean esBlackJack(List<String> baraja, int puntaje);

    /**
     * Determina el resultado del jugador comparando su puntaje
     * con el puntaje obtenido por el croupier.
     *
     * @param puntajeJugador puntaje obtenido por el jugador
     * @param puntajeCroupier puntaje obtenido por el croupier
     * @return dictamen correspondiente al resultado de la ronda
     */
    DictamenJugador calcularDictamen(int puntajeJugador, int puntajeCroupier);

    /**
     * Verifica si el jugador todavía posee fichas disponibles.
     *
     * @param fichas mapa que contiene los tipos de fichas y sus cantidades
     * @return {@code true} si existe al menos una ficha disponible;
     *         {@code false} en caso contrario
     */
    boolean tieneFichas(Map<Ficha, Integer> fichas);

    /**
     * Valida que la acción seleccionada por el jugador sea válida
     * dentro de las opciones permitidas por el juego.
     *
     * @param opcionAccion opción seleccionada por el jugador
     */
    void validarAccion(int opcionAccion);

    /**
     * Valida que la cantidad de jugadores se encuentre dentro
     * del rango permitido por el juego.
     *
     * @param cantidadJugadores cantidad de jugadores que participarán
     *                          en la partida
     */
    void validarCantidadDeJugadores(int cantidadJugadores);

    /**
     * Procesa una apuesta realizada durante la partida.
     * <p>
     * Utiliza el tipo de apuesta seleccionado, las fichas disponibles
     * del jugador, el croupier y la vista para ejecutar la lógica
     * correspondiente.
     * </p>
     *
     * @param tipoApuesta tipo de apuesta seleccionada
     * @param fichas fichas disponibles del jugador
     * @param croupier croupier encargado de administrar la apuesta
     * @param vistaJuego vista utilizada para interactuar con el usuario
     */
    void procesarApuesta(int tipoApuesta, Map<Ficha, Integer> fichas,  Croupier croupier,  VistaJuego vistaJuego);

    /**
     * Distribuye las apuestas después de conocer el resultado de la ronda.
     * <p>
     * La distribución depende del dictamen obtenido por el jugador,
     * pudiendo representar una victoria, empate o derrota.
     * </p>
     *
     * @param dictamenJugador resultado obtenido por el jugador
     * @param jugadorApostador jugador que realizó la apuesta
     * @param croupier croupier encargado de administrar las apuestas
     */
    void distribucionApuestas(DictamenJugador dictamenJugador, JugadorApostador jugadorApostador,  Croupier croupier );
}