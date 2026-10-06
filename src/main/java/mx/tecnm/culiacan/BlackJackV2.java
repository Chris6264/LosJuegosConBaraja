package mx.tecnm.culiacan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlackJackV2 implements VersionJuego {

    private Reglas reglas;
    private List<JugadorApostador> jugadores;

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

    private List<JugadorApostador> crearJugadores(int cantidadJugadores, MazoJugador mazoJugador) {
        List<JugadorApostador> nuevosJugadores = new ArrayList<>();

        for (int i = 0; i < cantidadJugadores; i++) {
            nuevosJugadores.add(new JugadorApostador(new GeneradorDeFichas(), mazoJugador));
        }

        return nuevosJugadores;
    }

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

    private boolean algunJugadorPuedeGanar(List<JugadorApostador> jugadoresPendientes) {
        boolean puedeGanar = false;

        for (JugadorApostador jugadorApostador : jugadoresPendientes) {
            if (reglas.obtenerPuntaje(jugadorApostador.getBarajaJugador()) < Reglas.PUNTAJE_BLACKJACK) puedeGanar = true;
        }

        return puedeGanar;
    }

    private void liquidarApuesta(DictamenJugador dictamen, JugadorApostador jugadorApostador, Croupier croupier,
                                 Map<JugadorApostador, Map<Ficha, Integer>> apuestas) {
        croupier.getApuesta().putAll(apuestas.remove(jugadorApostador));
        reglas.distribucionApuestas(dictamen, jugadorApostador, croupier);
    }

    private void retirarJugadoresSinFichas(List<JugadorApostador> jugadoresEnJuego, VistaJuego vistaJuego) {
        for (JugadorApostador jugadorApostador : new ArrayList<>(jugadoresEnJuego)) {
            if (!reglas.tieneFichas(jugadorApostador.getFichasJugador())) {
                vistaJuego.mostrarJugadorSinFichas(numeroDeJugador(jugadorApostador));
                jugadoresEnJuego.remove(jugadorApostador);
            }
        }
    }

    private int numeroDeJugador(JugadorApostador jugadorApostador) {
        return jugadores.indexOf(jugadorApostador) + 1;
    }
}