package mx.tecnm.culiacan;

import java.util.Map;
import java.util.TreeMap;

public class BlackJackV1 implements VersionJuego{
    @Override
    public void jugar(Reglas reglas, VistaJuego vistaJuego) {
        Baraja baraja = new Baraja();

        MazoJugador mazoJugador = new MazoJugador(baraja);
        GeneradorDeFichas generadorDeFichas = new GeneradorDeFichas();

        Croupier croupier = new Croupier(generadorDeFichas,mazoJugador);

        croupier.barajearCartas();
        croupier.partirCartas();

        JugadorApostador jugadorApostador = new JugadorApostador(generadorDeFichas,mazoJugador);

        vistaJuego.mostrarEncabezado();

        Map<Ficha,Integer> fichas = new TreeMap<>();
        for (Ficha ficha: jugadorApostador.getFichas()){
            fichas.put(ficha, fichas.getOrDefault(ficha, 0) + 1);
        }

        vistaJuego.mostrarFichas(fichas);

        vistaJuego.mostrarTipoDeApuestas();

        System.out.print("Como deseas apostar: ");
        int opcionApuesta = Keyboard.readInt();
/*
        System.out.print("Que ficha desea apostar: ");
        String colorDeFicha = Keyboard.readString();

        System.out.print("Cuantas fichas deseas apostar: ");
        int cantidadDeApuesta = Keyboard.readInt();

        comprobarApuesta(colorDeFicha,cantidadDeApuesta,fichas);
*/
        croupier.repartirCartaAJugador(jugadorApostador);

        jugadorApostador.pedirCarta(croupier);

        vistaJuego.mostrarBarajaJugador(jugadorApostador.getBarajaJugador());
    }

    private boolean comprobarApuesta(String colorDeFicha, int cantidadDeApuesta, Map<Ficha,Integer> fichas) {
        Ficha ficha = obtenerColorFicha(colorDeFicha);
        if(fichas.containsKey(ficha) && fichas.get(ficha) >= cantidadDeApuesta)
        {
            fichas.put(ficha,fichas.get(ficha) - cantidadDeApuesta);
        }
        return true;
    }

    private Ficha obtenerColorFicha(String colorDeFicha) {
        Ficha ficha = null;
        if(colorDeFicha.toUpperCase().equals("BLANCA")) ficha = Ficha.BLANCA;
        else if(colorDeFicha.toUpperCase().equals("ROJA")) ficha = Ficha.ROJA;
        else if(colorDeFicha.toUpperCase().equals("VERDE")) ficha = Ficha.VERDE;
        else if(colorDeFicha.toUpperCase().equals("NEGRA")) ficha = Ficha.NEGRA;
        else if(colorDeFicha.toUpperCase().equals("MORADA")) ficha = Ficha.MORADA;
        else throw new ReglasException("Ficha Invalida");
        return ficha;
    }
}
