package mx.tecnm.culiacan;

import java.util.List;
import java.util.Map;

public class ReglasBlackJack implements Reglas {

    private static final int APUESTA_MINIMA = 5;
    private static final int PAGO_APUESTA_MINIMA = 10;

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

    @Override
    public boolean esBlackJack(List<String> baraja, int puntaje) {
        return baraja.size() == 2 && puntaje == PUNTAJE_BLACKJACK;
    }

    @Override
    public DictamenJugador calcularDictamen(int puntajeJugador, int puntajeCroupier) {
        if (puntajeJugador > PUNTAJE_BLACKJACK) return DictamenJugador.DERROTA;
        if (puntajeCroupier > PUNTAJE_BLACKJACK) return DictamenJugador.VICTORIA;
        if (puntajeJugador > puntajeCroupier) return DictamenJugador.VICTORIA;
        if (puntajeJugador < puntajeCroupier) return DictamenJugador.DERROTA;
        return DictamenJugador.EMPATE;
    }

    @Override
    public boolean tieneFichas(Map<Ficha, Integer> fichas) {
        return fichas.values().stream().anyMatch(cantidad -> cantidad > 0);
    }

    @Override
    public void validarAccion(int opcionAccion) {
        if (opcionAccion != 1 && opcionAccion != 2) throw new ReglasException("Accion invalida");
    }

    @Override
    public void validarCantidadDeJugadores(int cantidadJugadores) {
        if (cantidadJugadores < MINIMO_JUGADORES || cantidadJugadores > MAXIMO_JUGADORES) {
            throw new ReglasException("Cantidad de jugadores invalida, debe ser de " + MINIMO_JUGADORES + " a " + MAXIMO_JUGADORES);
        }
    }

    @Override
    public void procesarApuesta(int tipoApuesta, Map<Ficha, Integer> fichas, Croupier croupier, VistaJuego vistaJuego) {
        if (tipoApuesta == 1) realizarApuestaMinima(fichas, croupier);
        else if (tipoApuesta == 2) realizarApuestaMaxima(fichas, croupier);
        else if (tipoApuesta == 3) realizarApuestaPersonalizada(fichas, croupier, vistaJuego);
        else throw new ReglasException("Opcion no valida");
    }

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
        croupier.añadirApuesta(ficha, cantidadDeApuesta * 2);
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
}