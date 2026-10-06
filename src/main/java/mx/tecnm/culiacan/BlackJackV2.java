package mx.tecnm.culiacan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementa la segunda versión del juego de BlackJack.
 * <p>
 * Esta versión permite que varios jugadores apostadores participen
 * dentro de una misma mesa compartiendo el mismo mazo y croupier.
 * </p>
 *
 * <p>
 * La clase administra la creación de jugadores, las apuestas individuales,
 * los turnos, la resolución de BlackJack, la actuación del croupier,
 * la distribución de apuestas y la eliminación de jugadores que se
 * quedan sin fichas.
 * </p>
 *
 * @see VersionJuego
 * @see Reglas
 * @see VistaJuego
 * @see JugadorApostador
 * @see Croupier
 */
public class BlackJackV2 implements VersionJuego {

    /**
     * Reglas utilizadas durante la ejecución de la partida.
     */
    private Reglas reglas;

    /**
     * Lista original de jugadores que participan en la mesa.
     * <p>
     * Esta lista permite conservar la posición inicial de cada jugador
     * para identificarlo mediante su número durante toda la partida.
     * </p>
     */
    private List<JugadorApostador> jugadores;

    /**
     * Inicia y ejecuta una partida multijugador de BlackJack.
     * <p>
     * Crea la baraja, el mazo y el croupier, solicita la cantidad
     * de jugadores y genera los jugadores que participarán en la mesa.
     * </p>
     *
     * <p>
     * Durante cada ronda se procesan las apuestas de todos los jugadores,
     * se reparten las cartas, se ejecutan los turnos, se resuelven
     * los resultados y se eliminan de la mesa los jugadores que ya
     * no poseen fichas.
     * </p>
     *
     * @param reglas reglas utilizadas durante la partida
     * @param vistaJuego vista encargada de la interacción con los jugadores
     */
    @Override
    public void jugar(Reglas reglas, VistaJuego vistaJuego) {
        this.reglas = reglas;

        Baraja baraja = new Baraja();

        MazoJugador mazoJugador = new MazoJugador(baraja);
        GeneradorDeFichas generadorDeFichas = new GeneradorDeFichas();

        Croupier croupier = new Croupier(generadorDeFichas, mazoJugador);

        croupier.barajearCartas();
        croupier.partirCartas();

        vistaJuego.mostrarTitulo();

        int cantidadJugadores = obtenerCantidadDeJugadores(vistaJuego);

        this.jugadores = crearJugadores(cantidadJugadores, mazoJugador);
        List<JugadorApostador> jugadoresEnJuego = new ArrayList<>(jugadores);

        vistaJuego.mostrarMesa(cantidadJugadores);

        boolean seguirJugando;

        do {
            croupier.partirCartas();
            croupier.barajearCartas();

            Map<JugadorApostador, Map<Ficha, Integer>> apuestas = realizarApuestas(jugadoresEnJuego, croupier, vistaJuego);

            for (JugadorApostador jugadorApostador : jugadoresEnJuego) croupier.repartirCartaAJugador(jugadorApostador);
            croupier.tomarCarta();
            croupier.tomarCarta();

            iniciarJuego(jugadoresEnJuego, croupier, vistaJuego, apuestas);

            for (JugadorApostador jugadorApostador : jugadoresEnJuego) croupier.recogerCartas(jugadorApostador.getBarajaJugador());

            retirarJugadoresSinFichas(jugadoresEnJuego, vistaJuego);

            if (!jugadoresEnJuego.isEmpty()) seguirJugando = vistaJuego.opcionDeJuego() == 1;
            else {
                vistaJuego.mostrarSinJugadores();
                seguirJugando = false;
            }
        } while (seguirJugando);
    }

    /**
     * Solicita y valida la cantidad de jugadores que participarán
     * en la partida.
     * <p>
     * La solicitud se repite hasta que el usuario ingrese una cantidad
     * válida de acuerdo con las reglas del juego.
     * </p>
     *
     * @param vistaJuego vista utilizada para solicitar la cantidad
     *                   de jugadores
     * @return cantidad válida de jugadores
     */
    private int obtenerCantidadDeJugadores(VistaJuego vistaJuego) {
        int cantidadJugadores = 0;
        boolean cantidadValida = false;

        do {
            try {
                cantidadJugadores = vistaJuego.pedirCantidadDeJugadores(Reglas.MINIMO_JUGADORES, Reglas.MAXIMO_JUGADORES);
                reglas.validarCantidadDeJugadores(cantidadJugadores);
                cantidadValida = true;
            } catch (ReglasException e) {
                vistaJuego.mostrarError(e.getMessage());
            }
        } while (!cantidadValida);

        return cantidadJugadores;
    }

    /**
     * Crea los jugadores apostadores que participarán en la partida.
     * <p>
     * Cada jugador recibe su propio generador de fichas, mientras
     * que todos comparten el mismo mazo de cartas.
     * </p>
     *
     * @param cantidadJugadores cantidad de jugadores que serán creados
     * @param mazoJugador mazo compartido entre los jugadores
     * @return lista de jugadores creados
     */
    private List<JugadorApostador> crearJugadores(int cantidadJugadores, MazoJugador mazoJugador) {
        List<JugadorApostador> nuevosJugadores = new ArrayList<>();

        for (int i = 0; i < cantidadJugadores; i++) {
            nuevosJugadores.add(new JugadorApostador(new GeneradorDeFichas(), mazoJugador));
        }

        return nuevosJugadores;
    }

    /**
     * Procesa las apuestas de todos los jugadores que permanecen
     * dentro de la mesa.
     * <p>
     * Cada jugador selecciona individualmente el tipo de apuesta
     * que desea realizar. Una vez validada, la apuesta se guarda
     * asociada al jugador correspondiente.
     * </p>
     *
     * <p>
     * Después de almacenar una apuesta, la estructura temporal
     * del croupier se limpia para poder procesar la apuesta
     * del siguiente jugador.
     * </p>
     *
     * @param jugadoresEnJuego jugadores que continúan participando
     * @param croupier croupier encargado de recibir las apuestas
     * @param vistaJuego vista utilizada para interactuar con los jugadores
     * @return mapa que relaciona cada jugador con su apuesta
     */
    private Map<JugadorApostador, Map<Ficha, Integer>> realizarApuestas(List<JugadorApostador> jugadoresEnJuego,
                                                                        Croupier croupier, VistaJuego vistaJuego) {
        Map<JugadorApostador, Map<Ficha, Integer>> apuestas = new HashMap<>();

        for (JugadorApostador jugadorApostador : jugadoresEnJuego) {
            Map<Ficha, Integer> fichasJugador = jugadorApostador.getFichasJugador();
            boolean apuestaValida = false;

            vistaJuego.mostrarJugador(numeroDeJugador(jugadorApostador));

            do {
                vistaJuego.mostrarFichas(fichasJugador);
                vistaJuego.mostrarTipoDeApuestas();

                try {
                    int tipoApuesta = vistaJuego.pedirTipoDeApuesta();
                    reglas.procesarApuesta(tipoApuesta, fichasJugador, croupier, vistaJuego);
                    apuestaValida = true;
                } catch (ReglasException e) {
                    vistaJuego.mostrarError(e.getMessage());
                }
            } while (!apuestaValida);

            vistaJuego.mostrarFichas(fichasJugador);
            vistaJuego.mostrarApuestaActual(croupier.getApuesta());

            apuestas.put(jugadorApostador, new HashMap<>(croupier.getApuesta()));
            croupier.getApuesta().clear();
        }

        return apuestas;
    }

    /**
     * Controla los turnos de todos los jugadores durante una ronda.
     * <p>
     * Cada jugador ejecuta su turno individualmente. Si su partida
     * no queda resuelta inmediatamente por BlackJack, se agrega
     * a la lista de jugadores pendientes.
     * </p>
     *
     * <p>
     * Una vez terminados los turnos, los jugadores pendientes
     * se comparan con la mano final del croupier.
     * </p>
     *
     * @param jugadoresEnJuego jugadores que participan en la ronda
     * @param croupier croupier encargado de administrar las cartas
     * @param vistaJuego vista encargada de mostrar la partida
     * @param apuestas apuestas realizadas por cada jugador
     */
    private void iniciarJuego(List<JugadorApostador> jugadoresEnJuego, Croupier croupier, VistaJuego vistaJuego,
                              Map<JugadorApostador, Map<Ficha, Integer>> apuestas) {
        List<JugadorApostador> jugadoresPendientes = new ArrayList<>();

        for (JugadorApostador jugadorApostador : jugadoresEnJuego) {
            vistaJuego.mostrarJugador(numeroDeJugador(jugadorApostador));

            boolean resueltoPorBlackJack = jugarTurno(jugadorApostador, croupier, vistaJuego, apuestas);

            if (!resueltoPorBlackJack) jugadoresPendientes.add(jugadorApostador);
        }

        if (!jugadoresPendientes.isEmpty()) terminarPartida(jugadoresPendientes, croupier, vistaJuego, apuestas);
    }

    /**
     * Ejecuta el turno individual de un jugador.
     * <p>
     * Durante el turno se calcula el puntaje del jugador, se muestran
     * las cartas y se verifica si existe un BlackJack.
     * </p>
     *
     * <p>
     * Mientras la partida continúe, el jugador puede pedir cartas
     * adicionales o decidir pasar.
     * </p>
     *
     * @param jugadorApostador jugador cuyo turno se está ejecutando
     * @param croupier croupier encargado de proporcionar las cartas
     * @param vistaJuego vista utilizada durante el turno
     * @param apuestas apuestas correspondientes a los jugadores
     * @return {@code true} si el turno fue resuelto por BlackJack;
     *         {@code false} en caso contrario
     */
    private boolean jugarTurno(JugadorApostador jugadorApostador, Croupier croupier, VistaJuego vistaJuego,
                               Map<JugadorApostador, Map<Ficha, Integer>> apuestas) {
        boolean juegoEnCurso = true;
        boolean resueltoPorBlackJack = false;
        List<String> barajaJugador = jugadorApostador.getBarajaJugador();

        do {
            int puntajeJugador = reglas.obtenerPuntaje(barajaJugador);
            int puntajeCroupier = reglas.obtenerPuntaje(croupier.getBarajaCroupier());

            boolean blackJackCroupier = reglas.esBlackJack(croupier.getBarajaCroupier(), puntajeCroupier);

            vistaJuego.mostrarBarajaCroupier(croupier.getBarajaCroupier(), !blackJackCroupier);
            vistaJuego.mostrarBarajaJugador(barajaJugador);

            if (comprobarBlackJack(jugadorApostador, puntajeJugador, puntajeCroupier, vistaJuego, croupier, apuestas)) {
                resueltoPorBlackJack = true;
                break;
            }

            vistaJuego.mostrarPuntaje(puntajeJugador);

            if (puntajeJugador >= Reglas.PUNTAJE_BLACKJACK) break;

            try {
                int opcionAccion = vistaJuego.pedirAccion();
                reglas.validarAccion(opcionAccion);

                if (opcionAccion == 1) jugadorApostador.pedirCarta(croupier);
                else juegoEnCurso = false;
            } catch (ReglasException e) {
                vistaJuego.mostrarError(e.getMessage());
            }

        } while (juegoEnCurso);

        return resueltoPorBlackJack;
    }

    /**
     * Finaliza la ronda para los jugadores que no fueron resueltos
     * previamente por BlackJack.
     * <p>
     * Si existe al menos un jugador que todavía puede ganar,
     * el croupier toma cartas hasta alcanzar el puntaje mínimo
     * establecido por las reglas.
     * </p>
     *
     * <p>
     * Posteriormente se compara el puntaje de cada jugador pendiente
     * con el puntaje final del croupier y se liquida su apuesta.
     * </p>
     *
     * @param jugadoresPendientes jugadores que deben compararse con el croupier
     * @param croupier croupier encargado de finalizar su mano
     * @param vistaJuego vista utilizada para mostrar los resultados
     * @param apuestas apuestas realizadas por cada jugador
     */
    private void terminarPartida(List<JugadorApostador> jugadoresPendientes, Croupier croupier, VistaJuego vistaJuego,
                                 Map<JugadorApostador, Map<Ficha, Integer>> apuestas) {
        int puntajeCroupier = reglas.obtenerPuntaje(croupier.getBarajaCroupier());

        if (algunJugadorPuedeGanar(jugadoresPendientes)) {
            while (puntajeCroupier < Reglas.PUNTAJE_MINIMO_CROUPIER) {
                croupier.tomarCarta();
                puntajeCroupier = reglas.obtenerPuntaje(croupier.getBarajaCroupier());
            }
        }

        vistaJuego.mostrarBarajaCroupier(croupier.getBarajaCroupier(), false);
        vistaJuego.mostrarPuntaje(puntajeCroupier);

        for (JugadorApostador jugadorApostador : jugadoresPendientes) {
            List<String> barajaJugador = jugadorApostador.getBarajaJugador();
            int puntajeJugador = reglas.obtenerPuntaje(barajaJugador);

            vistaJuego.mostrarJugador(numeroDeJugador(jugadorApostador));
            vistaJuego.mostrarBarajaJugador(barajaJugador);
            vistaJuego.mostrarPuntaje(puntajeJugador);

            DictamenJugador dictamen = reglas.calcularDictamen(puntajeJugador, puntajeCroupier);
            vistaJuego.mostrarDictamen(dictamen, puntajeJugador, puntajeCroupier);

            liquidarApuesta(dictamen, jugadorApostador, croupier, apuestas);
        }
    }

    /**
     * Comprueba si el jugador o el croupier obtuvieron BlackJack.
     * <p>
     * Si ambos obtienen BlackJack se establece un empate. Si únicamente
     * el jugador obtiene BlackJack se considera una victoria y si solo
     * el croupier lo obtiene se considera una derrota.
     * </p>
     *
     * <p>
     * Cuando existe BlackJack, la apuesta del jugador se liquida
     * inmediatamente.
     * </p>
     *
     * @param jugadorApostador jugador que está realizando su turno
     * @param puntajeJugador puntaje actual del jugador
     * @param puntajeCroupier puntaje actual del croupier
     * @param vistaJuego vista utilizada para mostrar el resultado
     * @param croupier croupier participante en la ronda
     * @param apuestas apuestas almacenadas de los jugadores
     * @return {@code true} si existe BlackJack;
     *         {@code false} en caso contrario
     */
    private boolean comprobarBlackJack(JugadorApostador jugadorApostador, int puntajeJugador, int puntajeCroupier,
                                       VistaJuego vistaJuego, Croupier croupier,
                                       Map<JugadorApostador, Map<Ficha, Integer>> apuestas) {

        boolean blackJackJugador = reglas.esBlackJack(jugadorApostador.getBarajaJugador(), puntajeJugador);
        boolean blackJackCroupier = reglas.esBlackJack(croupier.getBarajaCroupier(), puntajeCroupier);
        boolean hayBlackJack = blackJackJugador || blackJackCroupier;

        if (hayBlackJack) {
            DictamenJugador dictamen;

            if (blackJackJugador && blackJackCroupier) {
                vistaJuego.mostrarPuntaje(puntajeCroupier);
                vistaJuego.mostrarMensajeCroupierBlackJack();
                vistaJuego.mostrarPuntaje(puntajeJugador);
                vistaJuego.mostrarMensajeBlackJack();
                dictamen = DictamenJugador.EMPATE;
            } else if (blackJackJugador) {
                vistaJuego.mostrarPuntaje(puntajeJugador);
                vistaJuego.mostrarMensajeBlackJack();
                dictamen = DictamenJugador.VICTORIA;
            } else {
                vistaJuego.mostrarMensajeCroupierBlackJack();
                dictamen = DictamenJugador.DERROTA;
            }

            liquidarApuesta(dictamen, jugadorApostador, croupier, apuestas);
        }

        return hayBlackJack;
    }

    /**
     * Determina si existe al menos un jugador pendiente
     * con posibilidad de ganar la ronda.
     *
     * @param jugadoresPendientes jugadores que todavía deben
     *                            compararse con el croupier
     * @return {@code true} si existe un jugador con menos de 21 puntos;
     *         {@code false} en caso contrario
     */
    private boolean algunJugadorPuedeGanar(List<JugadorApostador> jugadoresPendientes) {
        boolean puedeGanar = false;

        for (JugadorApostador jugadorApostador : jugadoresPendientes) {
            if (reglas.obtenerPuntaje(jugadorApostador.getBarajaJugador()) < Reglas.PUNTAJE_BLACKJACK) puedeGanar = true;
        }

        return puedeGanar;
    }

    /**
     * Liquida la apuesta correspondiente a un jugador.
     * <p>
     * Recupera la apuesta previamente almacenada para el jugador,
     * la coloca temporalmente dentro del croupier y posteriormente
     * utiliza las reglas para realizar la distribución correspondiente.
     * </p>
     *
     * @param dictamen resultado obtenido por el jugador
     * @param jugadorApostador jugador cuya apuesta será liquidada
     * @param croupier croupier encargado de administrar la apuesta
     * @param apuestas mapa que contiene las apuestas de los jugadores
     */
    private void liquidarApuesta(DictamenJugador dictamen, JugadorApostador jugadorApostador, Croupier croupier,
                                 Map<JugadorApostador, Map<Ficha, Integer>> apuestas) {
        croupier.getApuesta().putAll(apuestas.remove(jugadorApostador));
        reglas.distribucionApuestas(dictamen, jugadorApostador, croupier);
    }

    /**
     * Retira de la mesa a los jugadores que se hayan quedado
     * sin fichas disponibles.
     * <p>
     * Se recorre una copia de la lista para permitir eliminar
     * jugadores de la lista original mientras se realiza
     * la iteración.
     * </p>
     *
     * @param jugadoresEnJuego jugadores que permanecen actualmente
     *                         dentro de la mesa
     * @param vistaJuego vista utilizada para informar la eliminación
     *                   de un jugador
     */
    private void retirarJugadoresSinFichas(List<JugadorApostador> jugadoresEnJuego, VistaJuego vistaJuego) {
        for (JugadorApostador jugadorApostador : new ArrayList<>(jugadoresEnJuego)) {
            if (!reglas.tieneFichas(jugadorApostador.getFichasJugador())) {
                vistaJuego.mostrarJugadorSinFichas(numeroDeJugador(jugadorApostador));
                jugadoresEnJuego.remove(jugadorApostador);
            }
        }
    }

    /**
     * Obtiene el número asignado originalmente a un jugador.
     *
     * @param jugadorApostador jugador cuyo número se desea obtener
     * @return número del jugador comenzando desde 1
     */
    private int numeroDeJugador(JugadorApostador jugadorApostador) {
        return jugadores.indexOf(jugadorApostador) + 1;
    }
}