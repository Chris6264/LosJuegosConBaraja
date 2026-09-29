package mx.tecnm.culiacan;

import java.util.*;

public class Baraja {

    private final Deque<String> pilaCartas;
    private static final String[] VALOR_CARTAS = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};

    public Baraja() {
        this.pilaCartas = new ArrayDeque<>();
        crearBaraja();
    }

    public void barajar(){
        List<String> listaTemporal = new ArrayList<>(pilaCartas);
        Collections.shuffle(listaTemporal);
        pilaCartas.clear();
        pilaCartas.addAll(listaTemporal);
    }

    public void partir() {
        List<String> listaTemporal = new ArrayList<>(pilaCartas);
        Collections.rotate(listaTemporal, -listaTemporal.size() / 2);
        pilaCartas.clear();
        pilaCartas.addAll(listaTemporal);
    }

    public Deque<String> getPilaCartas() {
        return pilaCartas;
    }

    private void crearBaraja() {
        for (Carta palo : Carta.values()) crearPalo(palo);
    }

    private void crearPalo(Carta palo) { for (String valor : VALOR_CARTAS) pilaCartas.add(palo.getSimbolo() + valor); }
}