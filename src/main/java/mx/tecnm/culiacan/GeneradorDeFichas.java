package mx.tecnm.culiacan;

import java.util.Map;
import java.util.TreeMap;

/**
 * Se encarga de generar y almacenar las fichas iniciales
 * que recibe un jugador apostador.
 * <p>
 * Las fichas se organizan en un {@link Map}, donde cada tipo de
 * {@link Ficha} se relaciona con la cantidad disponible de ese tipo.
 * </p>
 *
 * <p>
 * Al crear una instancia de esta clase se generan automáticamente
 * las fichas iniciales del jugador apostador.
 * </p>
 *
 * @see Ficha
 */
public class GeneradorDeFichas {

    /**
     * Contiene los tipos de fichas y la cantidad disponible
     * correspondiente a cada una.
     */
    private Map<Ficha, Integer> fichas;

    /**
     * Crea un nuevo generador de fichas.
     * <p>
     * Inicializa la estructura que almacena las fichas y genera
     * automáticamente la cantidad inicial asignada al jugador apostador.
     * </p>
     */
    public GeneradorDeFichas() {
        this.fichas = new TreeMap<>();
        generarFichasJugadorApostador();
    }

    /**
     * Genera las fichas iniciales que recibe un jugador apostador.
     * <p>
     * Se asignan las siguientes cantidades:
     * </p>
     * <ul>
     *     <li>1 ficha morada.</li>
     *     <li>2 fichas negras.</li>
     *     <li>2 fichas verdes.</li>
     *     <li>5 fichas rojas.</li>
     *     <li>10 fichas blancas.</li>
     * </ul>
     */
    public void generarFichasJugadorApostador() {
        fichas.put(Ficha.MORADA, 1);
        fichas.put(Ficha.NEGRA, 2);
        fichas.put(Ficha.VERDE, 2);
        fichas.put(Ficha.ROJA, 5);
        fichas.put(Ficha.BLANCA, 10);
    }

    /**
     * Devuelve las fichas generadas junto con la cantidad
     * disponible de cada tipo.
     *
     * @return mapa que relaciona cada tipo de ficha con su cantidad
     */
    public Map<Ficha, Integer> getFichas() {
        return fichas;
    }
}