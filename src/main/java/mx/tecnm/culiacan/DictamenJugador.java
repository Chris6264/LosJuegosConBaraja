package mx.tecnm.culiacan;

/**
 * Representa el resultado final obtenido por un jugador
 * después de comparar su mano con la del croupier.
 * <p>
 * El dictamen puede indicar que el jugador ganó,
 * empató o perdió la partida.
 * </p>
 */
public enum DictamenJugador {

    /**
     * Indica que el jugador ganó la partida.
     */
    VICTORIA,

    /**
     * Indica que el jugador obtuvo el mismo resultado que el croupier.
     */
    EMPATE,

    /**
     * Indica que el jugador perdió la partida.
     */
    DERROTA
}