package mx.tecnm.culiacan;

import java.util.List;
import java.util.Map;

/**
 * Implementa la primera versión del juego de BlackJack.
 * <p>
 * Esta clase coordina el flujo principal de una partida individual,
 * incluyendo la creación de la baraja, el mazo, el croupier y el jugador
 * apostador.
 * </p>
 *
 * <p>
 * También administra las apuestas, el reparto de cartas, las decisiones
 * del jugador, la actuación del croupier y la determinación del resultado
 * final de cada ronda.
 * </p>
 *
 * @see VersionJuego
 * @see Reglas
 * @see VistaJuego
 * @see JugadorApostador
 * @see Croupier
 */
public class BlackJackV1 implements VersionJuego {

    /**
     * Reglas utilizadas durante la ejecución de la partida.
     */
    private Reglas reglas;

    /**
     * Inicia y ejecuta una partida de BlackJack.
     * <p>
     * Crea las dependencias necesarias para la partida, prepara la baraja,
     * inicializa al croupier y al jugador apostador y controla el ciclo
     * principal del juego.
     * </p>
     *
     * <p>
     * Cada ronda permite realizar una apuesta, repartir las cartas,
     * desarrollar la partida y posteriormente decidir si el jugador
     * desea continuar jugando.
     * </p>
     *
     * @param reglas reglas utilizadas durante la partida
     * @param vistaJuego vista encargada de la interacción con el usuario
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

        JugadorApostador jugadorApostador = new JugadorApostador(generadorDeFichas, mazoJugador);

        vistaJuego.mostrarEncabezado();

        Map<Ficha, Integer> fichasJugador = jugadorApostador.getFichasJugador();

        boolean seguirJugando;

        do {
            croupier.partirCartas();
            croupier.barajearCartas();

            boolean apuestaValida = false;

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

            croupier.repartirCartaAJugador(jugadorApostador);
            croupier.tomarCarta();
            croupier.tomarCarta();

            iniciarJuego(jugadorApostador, croupier, vistaJuego);

            if (reglas.tieneFichas(fichasJugador)) seguirJugando = vistaJuego.opcionDeJuego() == 1;
            else {
                vistaJuego.mostrarSinFichas();
                seguirJugando = false;
            }
        } while (seguirJugando);
    }

    /**
     * Controla el desarrollo de una ronda después de repartir
     * las cartas iniciales.
     * <p>
     * Calcula los puntajes del jugador y del croupier, verifica
     * la existencia de BlackJack y permite al jugador decidir
     * entre pedir otra carta o pasar.
     * </p>
     *
     * <p>
     * La ronda continúa mientras el jugador pueda seguir tomando
     * decisiones y no se alcance una condición de finalización.
     * </p>
     *
     * @param jugadorApostador jugador que participa en la ronda
     * @param croupier croupier encargado de administrar las cartas
     * @param vistaJuego vista utilizada para mostrar y solicitar información
     */
    private void iniciarJuego(JugadorApostador jugadorApostador, Croupier croupier, VistaJuego vistaJuego) {
        boolean juegoEnCurso = true;
        List<String> barajaJugador = jugadorApostador.getBarajaJugador();

        do {
            int puntajeJugador = reglas.obtenerPuntaje(barajaJugador);
            int puntajeCroupier = reglas.obtenerPuntaje(croupier.getBarajaCroupier());

            boolean hayBlackJack = reglas.esBlackJack(barajaJugador, puntajeJugador)
                    || reglas.esBlackJack(croupier.getBarajaCroupier(), puntajeCroupier);

            vistaJuego.mostrarBarajaCroupier(croupier.getBarajaCroupier(), !hayBlackJack);
            vistaJuego.mostrarBarajaJugador(barajaJugador);

            if (comprobarBlackJack(barajaJugador, puntajeJugador, puntajeCroupier, vistaJuego, jugadorApostador, croupier)) break;

            vistaJuego.mostrarPuntaje(puntajeJugador);

            if (puntajeJugador >= Reglas.PUNTAJE_BLACKJACK || puntajeCroupier >= Reglas.PUNTAJE_BLACKJACK) {
                terminarPartida(puntajeJugador, jugadorApostador, croupier, barajaJugador, vistaJuego);
                break;
            }

            try {
                int opcionAccion = vistaJuego.pedirAccion();
                reglas.validarAccion(opcionAccion);

                if (opcionAccion == 1) jugadorApostador.pedirCarta(croupier);
                else {
                    juegoEnCurso = false;
                    terminarPartida(puntajeJugador, jugadorApostador, croupier, barajaJugador, vistaJuego);
                }
            } catch (ReglasException e) {
                vistaJuego.mostrarError(e.getMessage());
            }

        } while (juegoEnCurso);
    }

    /**
     * Finaliza una ronda y determina el resultado entre el jugador
     * y el croupier.
     * <p>
     * Si es necesario, el croupier continúa tomando cartas hasta alcanzar
     * el puntaje mínimo establecido por las reglas.
     * </p>
     *
     * <p>
     * Posteriormente se muestran ambas manos y sus puntajes,
     * se calcula el dictamen de la ronda, se distribuyen las apuestas
     * y finalmente se recogen las cartas utilizadas.
     * </p>
     *
     * @param puntajeJugador puntaje final obtenido por el jugador
     * @param jugadorApostador jugador participante de la ronda
     * @param croupier croupier participante de la ronda
     * @param barajaJugador cartas pertenecientes al jugador
     * @param vistaJuego vista encargada de mostrar el resultado
     */
    private void terminarPartida(int puntajeJugador, JugadorApostador jugadorApostador, Croupier croupier,
                                 List<String> barajaJugador, VistaJuego vistaJuego) {

        int puntajeCroupier = reglas.obtenerPuntaje(croupier.getBarajaCroupier());

        if (puntajeJugador < Reglas.PUNTAJE_BLACKJACK) {
            while (puntajeCroupier < Reglas.PUNTAJE_MINIMO_CROUPIER) {
                croupier.tomarCarta();
                puntajeCroupier = reglas.obtenerPuntaje(croupier.getBarajaCroupier());
            }
        }

        vistaJuego.mostrarBarajaCroupier(croupier.getBarajaCroupier(), false);
        vistaJuego.mostrarPuntaje(puntajeCroupier);

        vistaJuego.mostrarBarajaJugador(barajaJugador);
        vistaJuego.mostrarPuntaje(puntajeJugador);

        DictamenJugador dictamen = reglas.calcularDictamen(puntajeJugador, puntajeCroupier);
        vistaJuego.mostrarDictamen(dictamen, puntajeJugador, puntajeCroupier);

        reglas.distribucionApuestas(dictamen, jugadorApostador, croupier);

        croupier.recogerCartas(barajaJugador);
    }

    /**
     * Comprueba si el jugador o el croupier obtuvieron BlackJack
     * con sus cartas iniciales.
     * <p>
     * Si ambos obtienen BlackJack se establece un empate. Si únicamente
     * el jugador obtiene BlackJack se establece una victoria, mientras
     * que si únicamente el croupier lo obtiene se establece una derrota.
     * </p>
     *
     * <p>
     * Cuando existe BlackJack se distribuyen las apuestas correspondientes
     * y se recogen las cartas utilizadas en la ronda.
     * </p>
     *
     * @param barajaJugador cartas que posee el jugador
     * @param puntajeJugador puntaje obtenido por el jugador
     * @param puntajeCroupier puntaje obtenido por el croupier
     * @param vistaJuego vista utilizada para mostrar los resultados
     * @param jugadorApostador jugador participante en la ronda
     * @param croupier croupier participante en la ronda
     * @return {@code true} si el jugador o el croupier obtuvieron BlackJack;
     *         {@code false} en caso contrario
     */
    private boolean comprobarBlackJack(List<String> barajaJugador, int puntajeJugador, int puntajeCroupier,
                                       VistaJuego vistaJuego, JugadorApostador jugadorApostador, Croupier croupier) {

        boolean blackJackJugador = reglas.esBlackJack(barajaJugador, puntajeJugador);
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

            reglas.distribucionApuestas(dictamen, jugadorApostador, croupier);
            croupier.recogerCartas(barajaJugador);
        }

        return hayBlackJack;
    }
}