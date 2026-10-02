package mx.tecnm.culiacan;

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
}