package mx.tecnm.culiacan;

import java.util.List;
import java.util.Map;

/**
 * Implementa las reglas específicas de una partida de BlackJack.
 * <p>
 * Esta clase se encarga de calcular puntajes, determinar si una mano
 * corresponde a BlackJack, establecer el resultado de una ronda,
 * validar acciones y jugadores, procesar apuestas y distribuir
 * las ganancias correspondientes.
 * </p>
 *
 * <p>
 * También contiene la lógica necesaria para manejar apuestas mínimas,
 * máximas y personalizadas.
 * </p>
 *
 * @see Reglas
 * @see DictamenJugador
 * @see JugadorApostador
 * @see Croupier
 * @see Ficha
 * @see ReglasException
 */
public class ReglasBlackJack implements Reglas {

    /**
     * Cantidad mínima de fichas blancas requerida para realizar
     * una apuesta mínima.
     */
    private static final int APUESTA_MINIMA = 5;

    /**
     * Cantidad de fichas blancas registrada como pago
     * al realizar una apuesta mínima.
     */
    private static final int PAGO_APUESTA_MINIMA = 10;

    /**
     * Calcula el puntaje total de una mano de cartas.
     * <p>
     * Las cartas numéricas conservan su valor. Las cartas J, Q y K
     * tienen un valor de 10 puntos, mientras que inicialmente cada
     * as tiene un valor de 11.
     * </p>
     *
     * <p>
     * Si el puntaje supera 21 y existen ases en la mano,
     * el valor de los ases se reduce de 11 a 1 hasta obtener
     * el mejor puntaje posible.
     * </p>
     *
     * @param baraja cartas que forman la mano
     * @return puntaje total obtenido
     */
    @Override
    public int obtenerPuntaje(List<String> baraja) {
        int puntaje = 0;
        int cantidadAses = 0;

        for (String carta : baraja) {
            String valorCarta = carta.substring(1).trim();

            try {
                puntaje += Integer.parseInt(valorCarta);
            } catch (NumberFormatException e) {
                if (valorCarta.equalsIgnoreCase("A")) {
                    cantidadAses++;
                    puntaje += 11;
                } else {
                    puntaje += 10;
                }
            }
        }

        while (puntaje > PUNTAJE_BLACKJACK && cantidadAses > 0) {
            puntaje -= 10;
            cantidadAses--;
        }

        return puntaje;
    }

    /**
     * Determina si una mano corresponde a un BlackJack.
     * <p>
     * Una mano es BlackJack cuando contiene exactamente dos cartas
     * y obtiene un puntaje total de 21.
     * </p>
     *
     * @param baraja cartas que forman la mano
     * @param puntaje puntaje obtenido por la mano
     * @return {@code true} si la mano es BlackJack;
     *         {@code false} en caso contrario
     */
    @Override
    public boolean esBlackJack(List<String> baraja, int puntaje) { return baraja.size() == 2 && puntaje == PUNTAJE_BLACKJACK; }

    /**
     * Determina el resultado obtenido por el jugador comparando
     * su puntaje con el puntaje del croupier.
     *
     * @param puntajeJugador puntaje obtenido por el jugador
     * @param puntajeCroupier puntaje obtenido por el croupier
     * @return resultado de la ronda
     */
    @Override
    public DictamenJugador calcularDictamen(int puntajeJugador, int puntajeCroupier) {
        DictamenJugador dictamenJugador = DictamenJugador.EMPATE;
        if (puntajeJugador > PUNTAJE_BLACKJACK) dictamenJugador = DictamenJugador.DERROTA;
        else if (puntajeCroupier > PUNTAJE_BLACKJACK) dictamenJugador = DictamenJugador.VICTORIA;
        else if (puntajeJugador > puntajeCroupier) dictamenJugador = DictamenJugador.VICTORIA;
        else if (puntajeJugador < puntajeCroupier) dictamenJugador = DictamenJugador.DERROTA;
        return dictamenJugador;
    }

    /**
     * Verifica si el jugador todavía posee fichas disponibles.
     *
     * @param fichas mapa que contiene las fichas y sus cantidades
     * @return {@code true} si existe al menos una ficha disponible;
     *         {@code false} en caso contrario
     */
    @Override
    public boolean tieneFichas(Map<Ficha, Integer> fichas) {
        return fichas.values().stream().anyMatch(cantidad -> cantidad > 0);
    }

    /**
     * Valida que la acción seleccionada por el jugador sea válida.
     *
     * @param opcionAccion acción seleccionada
     * @throws ReglasException si la acción no es válida
     */
    @Override
    public void validarAccion(int opcionAccion) {
        if (opcionAccion != 1 && opcionAccion != 2) throw new ReglasException("Accion invalida");
    }

    /**
     * Valida que la cantidad de jugadores se encuentre dentro
     * del rango permitido.
     *
     * @param cantidadJugadores cantidad de jugadores que participarán
     * @throws ReglasException si la cantidad de jugadores no es válida
     */
    @Override
    public void validarCantidadDeJugadores(int cantidadJugadores) {
        if (cantidadJugadores < MINIMO_JUGADORES || cantidadJugadores > MAXIMO_JUGADORES)
            throw new ReglasException("Cantidad de jugadores invalida, debe ser de " + MINIMO_JUGADORES + " a " + MAXIMO_JUGADORES);
    }

    /**
     * Procesa una apuesta de acuerdo con la opción seleccionada.
     * <p>
     * La opción 1 realiza una apuesta mínima, la opción 2 una apuesta
     * máxima y la opción 3 permite realizar una apuesta personalizada.
     * </p>
     *
     * @param tipoApuesta tipo de apuesta seleccionada
     * @param fichas fichas disponibles del jugador
     * @param croupier croupier encargado de almacenar la apuesta
     * @param vistaJuego vista utilizada para solicitar información
     * @throws ReglasException si la opción seleccionada no es válida
     */
    @Override
    public void procesarApuesta(int tipoApuesta, Map<Ficha, Integer> fichas, Croupier croupier, VistaJuego vistaJuego) {
        if (tipoApuesta == 1) realizarApuestaMinima(fichas, croupier);
        else if (tipoApuesta == 2) realizarApuestaMaxima(fichas, croupier);
        else if (tipoApuesta == 3) realizarApuestaPersonalizada(fichas, croupier, vistaJuego);
        else throw new ReglasException("Opcion no valida");
    }

    /**
     * Distribuye las apuestas de acuerdo con el resultado obtenido
     * por el jugador.
     * <p>
     * En caso de victoria se agregan las cantidades registradas
     * en la apuesta a las fichas del jugador. En caso de empate
     * se devuelve la mitad de la cantidad registrada.
     * </p>
     *
     * <p>
     * Si el jugador pierde, no recibe fichas. Al finalizar,
     * la apuesta almacenada por el croupier se elimina.
     * </p>
     *
     * @param dictamenJugador resultado obtenido por el jugador
     * @param jugadorApostador jugador que realizó la apuesta
     * @param croupier croupier que contiene la apuesta actual
     */
    @Override
    public void distribucionApuestas(DictamenJugador dictamenJugador, JugadorApostador jugadorApostador, Croupier croupier) {
        Map<Ficha, Integer> apuestaActual = croupier.getApuesta();
        Map<Ficha, Integer> fichasJugador = jugadorApostador.getFichasJugador();

        if (dictamenJugador == DictamenJugador.VICTORIA) {
            apuestaActual.forEach((ficha, cantidad) -> fichasJugador.merge(ficha, cantidad, Integer::sum));
        } else if (dictamenJugador == DictamenJugador.EMPATE) {
            apuestaActual.forEach((ficha, cantidad) -> fichasJugador.merge(ficha, cantidad / 2, Integer::sum));
        }

        apuestaActual.clear();
    }

    /**
     * Realiza la apuesta mínima utilizando fichas blancas.
     * <p>
     * El jugador debe disponer de al menos cinco fichas blancas.
     * Después se descuentan las fichas utilizadas y el croupier
     * registra el pago correspondiente.
     * </p>
     *
     * @param fichas fichas disponibles del jugador
     * @param croupier croupier encargado de almacenar la apuesta
     * @throws ReglasException si no existen suficientes fichas blancas
     */
    private void realizarApuestaMinima(Map<Ficha, Integer> fichas, Croupier croupier) {
        int disponible = fichas.getOrDefault(Ficha.BLANCA, 0);
        if (disponible < APUESTA_MINIMA) throw new ReglasException("Fichas insuficientes");

        fichas.put(Ficha.BLANCA, disponible - APUESTA_MINIMA);
        croupier.añadirApuesta(Ficha.BLANCA, PAGO_APUESTA_MINIMA);
    }

    /**
     * Realiza una apuesta utilizando todas las fichas disponibles
     * del jugador.
     * <p>
     * Cada cantidad apostada se registra duplicada dentro de la
     * apuesta del croupier y posteriormente las fichas disponibles
     * del jugador se establecen en cero.
     * </p>
     *
     * @param fichas fichas disponibles del jugador
     * @param croupier croupier encargado de almacenar la apuesta
     */
    private void realizarApuestaMaxima(Map<Ficha, Integer> fichas, Croupier croupier) {
        for (Map.Entry<Ficha, Integer> entry : fichas.entrySet()) {
            if (entry.getValue() > 0) croupier.añadirApuesta(entry.getKey(), entry.getValue() * 2);
        }

        fichas.replaceAll((ficha, cantidad) -> 0);
    }

    /**
     * Realiza una apuesta personalizada.
     * <p>
     * Solicita al jugador el tipo y cantidad de fichas que desea
     * apostar, valida que la cantidad esté disponible, descuenta
     * las fichas seleccionadas y registra la apuesta en el croupier.
     * </p>
     *
     * @param fichas fichas disponibles del jugador
     * @param croupier croupier encargado de almacenar la apuesta
     * @param vistaJuego vista utilizada para interactuar con el jugador
     */
    private void realizarApuestaPersonalizada(Map<Ficha, Integer> fichas, Croupier croupier, VistaJuego vistaJuego) {
        vistaJuego.mostrarFichas(fichas);
        int tipoFicha = vistaJuego.pedirTipoDeFicha();
        Ficha ficha = obtenerFicha(tipoFicha);
        int cantidadDeApuesta = vistaJuego.pedirCantidadDeFichasApostar();
        validarCantidadDeApuesta(ficha, cantidadDeApuesta, fichas);
        fichas.put(ficha, fichas.get(ficha) - cantidadDeApuesta);
        croupier.añadirApuesta(ficha, cantidadDeApuesta * 2);
    }

    /**
     * Obtiene una ficha a partir de la opción seleccionada.
     *
     * @param tipoFicha número correspondiente al tipo de ficha
     * @return ficha seleccionada
     * @throws ReglasException si el número indicado no corresponde
     *                         a una ficha existente
     */
    private Ficha obtenerFicha(int tipoFicha) {
        Ficha[] opciones = Ficha.values();
        int indice = tipoFicha - 1;
        if (indice < 0 || indice >= opciones.length) throw new ReglasException("Ficha Invalida");
        return opciones[indice];
    }

    /**
     * Valida la cantidad de fichas que el jugador desea apostar.
     *
     * @param ficha tipo de ficha seleccionada
     * @param cantidad cantidad de fichas que se desean apostar
     * @param fichas fichas disponibles del jugador
     * @throws ReglasException si la cantidad no es válida o supera
     *                         las fichas disponibles
     */
    private void validarCantidadDeApuesta(Ficha ficha, int cantidad, Map<Ficha, Integer> fichas) {
        int disponible = fichas.getOrDefault(ficha, 0);
        if (cantidad <= 0 || cantidad > disponible) throw new ReglasException("Cantidad invalida o insuficiente");
    }
}