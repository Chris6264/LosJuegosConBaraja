package mx.tecnm.culiacan;

import java.util.List;
import java.util.Map;

public class BlackJackV1 implements VersionJuego {

    private static final int APUESTA_MINIMA = 5;
    private static final int PAGO_APUESTA_MINIMA = 10;
    private static final int PUNTAJE_BLACKJACK = 21;
    private static final int PUNTAJE_MINIMO_CROUPIER = 17;

    @Override
    public void jugar(Reglas reglas, VistaJuego vistaJuego) {
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

            vistaJuego.mostrarFichas(fichasJugador);
            vistaJuego.mostrarTipoDeApuestas();

            int tipoApuesta = vistaJuego.pedirTipoDeApuesta();
            procesarApuesta(tipoApuesta, fichasJugador, croupier, vistaJuego);

            vistaJuego.mostrarFichas(fichasJugador);
            vistaJuego.mostrarApuestaActual(croupier.getApuesta());

            croupier.repartirCartaAJugador(jugadorApostador);
            croupier.tomarCarta();
            croupier.tomarCarta();

            iniciarJuego(jugadorApostador, croupier, vistaJuego);

            if (tieneFichas(fichasJugador)) seguirJugando = vistaJuego.opcionDeJuego() == 1;
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
            vistaJuego.mostrarBarajaCroupier(croupier.getBarajaCroupier(), juegoEnCurso);
            vistaJuego.mostrarBarajaJugador(barajaJugador);

            int puntajeJugador = obtenerPuntaje(barajaJugador);
            int puntajeCroupier = obtenerPuntaje(croupier.getBarajaCroupier());

            if (comprobarBlackJack(barajaJugador, puntajeJugador, puntajeCroupier, vistaJuego, jugadorApostador, croupier)) break;

            vistaJuego.mostrarPuntaje(puntajeJugador);

            if (puntajeJugador >= PUNTAJE_BLACKJACK || puntajeCroupier >= PUNTAJE_BLACKJACK) {
                terminarPartida(puntajeJugador, jugadorApostador, croupier, barajaJugador, vistaJuego);
                break;
            }

            int opcionAccion = vistaJuego.pedirAccion();

            if (opcionAccion == 1) jugadorApostador.pedirCarta(croupier);
            else if (opcionAccion == 2) {
                juegoEnCurso = false;
                terminarPartida(puntajeJugador, jugadorApostador, croupier, barajaJugador, vistaJuego);
            }
            else throw new ReglasException("Accion invalida");

        } while (juegoEnCurso);
    }

    private void terminarPartida(int puntajeJugador, JugadorApostador jugadorApostador, Croupier croupier,
                                 List<String> barajaJugador, VistaJuego vistaJuego) {
        int puntajeCroupier = obtenerPuntaje(croupier.getBarajaCroupier());

        if (puntajeJugador < PUNTAJE_BLACKJACK) {
            while (puntajeCroupier < PUNTAJE_MINIMO_CROUPIER) {
                croupier.tomarCarta();
                puntajeCroupier = obtenerPuntaje(croupier.getBarajaCroupier());
            }
        }

        vistaJuego.mostrarBarajaCroupier(croupier.getBarajaCroupier(), false);
        vistaJuego.mostrarPuntaje(puntajeCroupier);

        vistaJuego.mostrarBarajaJugador(barajaJugador);
        vistaJuego.mostrarPuntaje(puntajeJugador);

        DictamenJugador dictamen = calcularDictamen(puntajeJugador, puntajeCroupier);
        vistaJuego.mostrarDictamen(dictamen, puntajeJugador, puntajeCroupier);

        distribucionApuestas(dictamen, jugadorApostador, croupier);

        croupier.recogerCartas(barajaJugador);
    }

    private boolean comprobarBlackJack(List<String> barajaJugador, int puntajeJugador, int puntajeCroupier,
                                       VistaJuego vistaJuego, JugadorApostador jugadorApostador, Croupier croupier) {
        boolean blackJackJugador = esBlackJack(barajaJugador, puntajeJugador);
        boolean blackJackCroupier = esBlackJack(croupier.getBarajaCroupier(), puntajeCroupier);

        if (!blackJackJugador && !blackJackCroupier) return false;

        DictamenJugador dictamen;

        if (blackJackJugador && blackJackCroupier) {
            vistaJuego.mostrarBarajaCroupier(croupier.getBarajaCroupier(), false);
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
            vistaJuego.mostrarBarajaCroupier(croupier.getBarajaCroupier(), false);
            vistaJuego.mostrarPuntaje(puntajeCroupier);
            vistaJuego.mostrarMensajeCroupierBlackJack();
            dictamen = DictamenJugador.DERROTA;
        }

        distribucionApuestas(dictamen, jugadorApostador, croupier);
        croupier.recogerCartas(barajaJugador);
        return true;
    }

    private boolean esBlackJack(List<String> baraja, int puntaje) {
        return baraja.size() == 2 && puntaje == PUNTAJE_BLACKJACK;
    }

    private void procesarApuesta(int tipoApuesta, Map<Ficha, Integer> fichas, Croupier croupier, VistaJuego vistaJuego) {
        if (tipoApuesta == 1) realizarApuestaMinima(fichas, croupier);
        else if (tipoApuesta == 2) realizarApuestaMaxima(fichas, croupier);
        else if (tipoApuesta == 3) realizarApuestaPersonalizada(fichas, croupier, vistaJuego);
        else throw new ReglasException("Opcion no valida");
    }

    private void distribucionApuestas(DictamenJugador dictamenJugador, JugadorApostador jugadorApostador, Croupier croupier) {
        Map<Ficha, Integer> apuestaActual = croupier.getApuesta();
        Map<Ficha, Integer> fichasJugador = jugadorApostador.getFichasJugador();

        if (dictamenJugador == DictamenJugador.VICTORIA || dictamenJugador == DictamenJugador.EMPATE) {
            apuestaActual.forEach((ficha, cantidad) -> fichasJugador.merge(ficha, cantidad, Integer::sum));
        }

        apuestaActual.clear();
    }

    private void realizarApuestaMinima(Map<Ficha, Integer> fichas, Croupier croupier) {
        int disponible = fichas.getOrDefault(Ficha.BLANCA, 0);
        if (disponible < APUESTA_MINIMA) throw new ReglasException("Fichas insuficientes");

        fichas.put(Ficha.BLANCA, disponible - APUESTA_MINIMA);
        croupier.añadirApuesta(Ficha.BLANCA, PAGO_APUESTA_MINIMA);
    }

    private void realizarApuestaMaxima(Map<Ficha, Integer> fichas, Croupier croupier) {
        for (Map.Entry<Ficha, Integer> entry : fichas.entrySet()) {
            if (entry.getValue() > 0) croupier.añadirApuesta(entry.getKey(), entry.getValue() * 2);
        }
        fichas.replaceAll((ficha, cantidad) -> 0);
    }

    private void realizarApuestaPersonalizada(Map<Ficha, Integer> fichas, Croupier croupier, VistaJuego vistaJuego) {
        vistaJuego.mostrarFichas(fichas);

        int tipoFicha = vistaJuego.pedirTipoDeFicha();
        Ficha ficha = obtenerFicha(tipoFicha);

        int cantidadDeApuesta = vistaJuego.pedirCantidadDeFichasApostar();
        validarCantidadDeApuesta(ficha, cantidadDeApuesta, fichas);

        fichas.put(ficha, fichas.get(ficha) - cantidadDeApuesta);
        croupier.añadirApuesta(ficha, cantidadDeApuesta);
    }

    private Ficha obtenerFicha(int tipoFicha) {
        Ficha[] opciones = Ficha.values();
        int indice = tipoFicha - 1;
        if (indice < 0 || indice >= opciones.length) throw new ReglasException("Ficha Invalida");
        return opciones[indice];
    }

    private void validarCantidadDeApuesta(Ficha ficha, int cantidad, Map<Ficha, Integer> fichas) {
        int disponible = fichas.getOrDefault(ficha, 0);
        if (cantidad <= 0 || cantidad > disponible) {
            throw new ReglasException("Cantidad invalida o insuficiente");
        }
    }

    private int obtenerPuntaje(List<String> baraja) {
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

    private boolean tieneFichas(Map<Ficha, Integer> fichas) {
        return fichas.values().stream().anyMatch(cantidad -> cantidad > 0);
    }

    private DictamenJugador calcularDictamen(int puntajeJugador, int puntajeCroupier) {
        if (puntajeJugador > PUNTAJE_BLACKJACK) return DictamenJugador.DERROTA;
        if (puntajeCroupier > PUNTAJE_BLACKJACK) return DictamenJugador.VICTORIA;
        if (puntajeJugador > puntajeCroupier) return DictamenJugador.VICTORIA;
        if (puntajeJugador < puntajeCroupier) return DictamenJugador.DERROTA;
        return DictamenJugador.EMPATE;
    }
}