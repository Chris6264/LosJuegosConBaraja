package mx.tecnm.culiacan;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class JugadorApostador extends Jugador{

    private List<String> barajaJugador;
    private Map<Ficha,Integer> fichasJugador;

    public JugadorApostador(GeneradorDeFichas generadorDeFichas, MazoJugador mazoJugador) {
        super(generadorDeFichas, mazoJugador);
        this.barajaJugador = new ArrayList<>();
        this.fichasJugador = generadorDeFichas.getFichas();
    }

    public void pedirCarta(Croupier croupier){
        Deque<String> pilaCartas = croupier.getPilaCartas();
        if(!pilaCartas.isEmpty()) barajaJugador.add(pilaCartas.pop());
    }

    public List<String> getBarajaJugador() {
        return barajaJugador;
    }

    public void setBarajaJugador(List<String> barajaJugador) {
        this.barajaJugador = barajaJugador;
    }

    public Map<Ficha,Integer> getFichasJugador(){ return fichasJugador; }
}
