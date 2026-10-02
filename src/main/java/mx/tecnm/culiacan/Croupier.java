package mx.tecnm.culiacan;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Croupier extends Jugador{

    private Deque<String> pilaCartas;
    private Map<Ficha,Integer> apuesta;

    public Croupier(GeneradorDeFichas generadorDeFichas, MazoJugador mazoJugador) {
        super(generadorDeFichas, mazoJugador);
        this.pilaCartas = mazoJugador.obtenerBarajaCroupier();
        this.apuesta = new HashMap<>();
    }

    public void repartirCartaAJugador(JugadorApostador jugadorApostador){
        List<String> barajaJugador = new ArrayList<>();
        barajaJugador.add(pilaCartas.pop());
        barajaJugador.add(pilaCartas.pop());
        jugadorApostador.setBarajaJugador(barajaJugador);
    }

    public void barajearCartas(){ mazoJugador.getBaraja().barajar(); }

    public void partirCartas(){ mazoJugador.getBaraja().partir(); }

    public Deque<String> getPilaCartas() {
        return pilaCartas;
    }

    public Map<Ficha,Integer> getApuesta() {
        return apuesta;
    }

    public void añadirApuesta(Ficha ficha, int cantidadFichas){
        apuesta.put(ficha,cantidadFichas);
    }
}
