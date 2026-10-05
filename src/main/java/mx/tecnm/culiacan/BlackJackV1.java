package mx.tecnm.culiacan;

import java.util.List;
import java.util.Map;

public class BlackJackV1 implements VersionJuego {

    private Reglas reglas;

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

    private void iniciarJuego(JugadorApostador jugadorApostador, Croupier croupier, VistaJuego vistaJuego) {
        boolean juegoEnCurso = true;
        List<String> barajaJugador = jugadorApostador.getBarajaJugador();

        do {
            int puntajeJugador = reglas.obtenerPuntaje(barajaJugador);
            int puntajeCroupier = reglas.obtenerPuntaje(croupier.getBarajaCroupier());

            boolean hayBlackJack = reglas.esBlackJack(barajaJugador, puntajeJugador)
                    || reglas.esBlackJack(croupier.getBarajaCroupier(), puntajeCroupier);

            vistaJuego.mostrarBarajaCroupier(croupier.getBarajaCroupier(), juegoEnCurso && !hayBlackJack);
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