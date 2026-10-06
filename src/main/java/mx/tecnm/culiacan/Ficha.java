package mx.tecnm.culiacan;

/**
 * Representa los diferentes tipos de fichas disponibles en el juego.
 * <p>
 * Cada ficha tiene asociado un valor monetario que se utiliza para
 * calcular las apuestas realizadas por los jugadores.
 * </p>
 */
public enum Ficha {

    /**
     * Ficha blanca con valor de 1.
     * Cantidad inicial sugerida: 10 fichas.
     */
    BLANCA(1),

    /**
     * Ficha roja con valor de 5.
     * Cantidad inicial sugerida: 5 fichas.
     */
    ROJA(5),

    /**
     * Ficha verde con valor de 25.
     * Cantidad inicial sugerida: 2 fichas.
     */
    VERDE(25),

    /**
     * Ficha negra con valor de 100.
     * Cantidad inicial sugerida: 2 fichas.
     */
    NEGRA(100),

    /**
     * Ficha morada con valor de 500.
     * Cantidad inicial sugerida: 1 ficha.
     */
    MORADA(500);

    /**
     * Valor monetario asociado a la ficha.
     */
    private final int valorFicha;

    /**
     * Inicializa una ficha con su valor correspondiente.
     *
     * @param valorFicha valor monetario de la ficha
     */
    Ficha(int valorFicha) {
        this.valorFicha = valorFicha;
    }

    /**
     * Devuelve el valor monetario asociado a la ficha.
     *
     * @return valor de la ficha
     */
    public int getValorFicha() {
        return valorFicha;
    }
}