package mx.tecnm.culiacan;

import java.util.Collections;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;

/**
 * Representa una baraja de cartas utilizada durante el juego.
 * <p>
 * La baraja se almacena internamente mediante una estructura {@link Deque},
 * lo que permite trabajar con las cartas como una pila. La clase se encarga
 * de crear la baraja completa, mezclarla y partirla.
 * </p>
 *
 * <p>
 * Cada carta se construye combinando el símbolo de un palo definido en
 * {@link Carta} con uno de los valores disponibles en {@link #VALOR_CARTAS}.
 * </p>
 *
 * @see Carta
 */
public class Baraja {

    /**
     * Contiene las cartas disponibles de la baraja.
     */
    private Deque<String> pilaCartas;

    /**
     * Valores posibles que puede tener una carta dentro de cada palo.
     */
    private static final String[] VALOR_CARTAS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    /**
     * Crea una nueva baraja e inicializa todas sus cartas.
     * <p>
     * Al construir el objeto se crea automáticamente la baraja completa
     * mediante el método {@link #crearBaraja()}.
     * </p>
     */
    public Baraja() {
        this.pilaCartas = new ArrayDeque<>();
        crearBaraja();
    }

    /**
     * Mezcla aleatoriamente las cartas de la baraja.
     * <p>
     * Para realizar la mezcla, las cartas se copian temporalmente a una lista,
     * se utiliza {@link Collections#shuffle(List)} y posteriormente se vuelven
     * a insertar en la pila de cartas.
     * </p>
     */
    public void barajar() {
        List<String> listaTemporal = new ArrayList<>(pilaCartas);
        Collections.shuffle(listaTemporal);
        pilaCartas.clear();
        pilaCartas.addAll(listaTemporal);
    }

    /**
     * Parte la baraja aproximadamente por la mitad.
     * <p>
     * Las cartas de la primera mitad se desplazan al final de la baraja
     * mediante {@link Collections#rotate(List, int)}.
     * </p>
     */
    public void partir() {
        List<String> listaTemporal = new ArrayList<>(pilaCartas);
        Collections.rotate(listaTemporal, -listaTemporal.size() / 2);
        pilaCartas.clear();
        pilaCartas.addAll(listaTemporal);
    }

    /**
     * Devuelve la pila que contiene las cartas de la baraja.
     *
     * @return estructura {@link Deque} con las cartas disponibles
     */
    public Deque<String> getPilaCartas() { return pilaCartas; }

    /**
     * Crea todas las cartas que forman la baraja.
     * <p>
     * Recorre todos los palos definidos en {@link Carta} y delega la creación
     * de las cartas correspondientes a cada palo al método
     * {@link #crearPalo(Carta)}.
     * </p>
     */
    private void crearBaraja() { for (Carta palo : Carta.values()) crearPalo(palo); }

    /**
     * Crea todas las cartas correspondientes a un palo.
     * <p>
     * Cada carta se obtiene concatenando el símbolo del palo con cada uno
     * de los valores contenidos en {@link #VALOR_CARTAS}.
     * </p>
     *
     * @param palo palo del que se crearán las cartas
     */
    private void crearPalo(Carta palo) { for (String valor : VALOR_CARTAS) pilaCartas.add( palo.getSimbolo() + valor ); }
}