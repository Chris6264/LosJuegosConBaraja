package mx.tecnm.culiacan;

import java.util.List;
import java.util.Map;

public class BlackJackV1 implements VersionJuego {
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

        vistaJuego.mostrarFichas(fichasJugador);
        vistaJuego.mostrarTipoDeApuestas();

        int tipoApuesta = vistaJuego.pedirTipoDeApuesta();

        procesarApuesta(tipoApuesta, fichasJugador, croupier, vistaJuego);

        vistaJuego.mostrarFichas(fichasJugador);

        Map<Ficha, Integer> apuestaActual = croupier.getApuesta();
        vistaJuego.mostrarApuestaActual(apuestaActual);

        croupier.repartirCartaAJugador(jugadorApostador);

        croupier.tomarCarta();
        croupier.tomarCarta();

        iniciarJuego(jugadorApostador,croupier,vistaJuego);
    }

    private void iniciarJuego(JugadorApostador jugadorApostador, Croupier croupier, VistaJuego vistaJuego) {
        boolean seguirJugando = true;
        List<String> barajaJugador = jugadorApostador.getBarajaJugador();

        do {
            vistaJuego.mostrarBarajaCroupier(croupier.getBarajaCroupier(), seguirJugando);
            vistaJuego.mostrarBarajaJugador(barajaJugador);

            int puntajeJugador = obtenerPuntaje(barajaJugador);
            vistaJuego.mostrarPuntaje(puntajeJugador);

            if (puntajeJugador >= 21) {
                seguirJugando = false;
                terminarPartida(puntajeJugador, croupier, barajaJugador, vistaJuego, seguirJugando);
                break;
            }

            int opcionAccion = vistaJuego.pedirAccion();

            if (opcionAccion == 1) jugadorApostador.pedirCarta(croupier);
            else if (opcionAccion == 2) {
                seguirJugando = false;
                terminarPartida(puntajeJugador, croupier, barajaJugador, vistaJuego, seguirJugando);
            }
            else throw new ReglasException("Accion invalida");

        } while (seguirJugando);
    }

    private void terminarPartida(int puntajeJugador, Croupier croupier, List<String> barajaJugador, VistaJuego vistaJuego, boolean seguirJugando) {
        int puntajeCroupier = obtenerPuntaje(croupier.getBarajaCroupier());

        if (puntajeJugador <= 21) {
            while (puntajeCroupier < 17) {
                croupier.tomarCarta();
                puntajeCroupier = obtenerPuntaje(croupier.getBarajaCroupier());
            }
        }

        vistaJuego.mostrarBarajaCroupier(croupier.getBarajaCroupier(), seguirJugando);
        vistaJuego.mostrarPuntaje(puntajeCroupier);

        vistaJuego.mostrarBarajaJugador(barajaJugador);
        vistaJuego.mostrarPuntaje(puntajeJugador);

        vistaJuego.mostrarDictamen(puntajeJugador, puntajeCroupier);
    }

    private void procesarApuesta(int tipoApuesta, Map<Ficha, Integer> fichas, Croupier croupier, VistaJuego vistaJuego) {
        if (tipoApuesta == 1) realizarApuestaMinima(fichas, croupier);
        else if (tipoApuesta == 2) realizarApuestaMaxima(fichas, croupier);
        else if (tipoApuesta == 3) realizarApuestaPersonalizada(fichas, croupier, vistaJuego);
        else throw new ReglasException("Opcion no valida");
    }

    private void realizarApuestaMinima(Map<Ficha, Integer> fichas, Croupier croupier) {
        int disponible = fichas.getOrDefault(Ficha.BLANCA, 0);
        if (disponible < 5) throw new ReglasException("Fichas insuficientes");

        fichas.put(Ficha.BLANCA, disponible - 5);
        croupier.añadirApuesta(Ficha.BLANCA, 5);
    }

    private void realizarApuestaMaxima(Map<Ficha, Integer> fichas, Croupier croupier) {
        for (Map.Entry<Ficha, Integer> entry : fichas.entrySet()) {
            if (entry.getValue() > 0) croupier.añadirApuesta(entry.getKey(), entry.getValue());
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

        while (puntaje > 21 && cantidadAses > 0) {
            puntaje -= 10;
            cantidadAses--;
        }

        return puntaje;
    }
}