package mx.tecnm.culiacan;

/**
 * Representa los diferentes palos disponibles para las cartas de la baraja.
 * <p>
 * Cada constante del enum corresponde a un palo y almacena el símbolo
 * utilizado para representarlo visualmente durante el juego.
 * </p>
 *
 * <p>
 * Los palos disponibles son:
 * corazones, diamantes, picas y tréboles.
 * </p>
 */
public enum Carta {

    /**
     * Representa el palo de corazones.
     */
    CORAZON('♥'),

    /**
     * Representa el palo de diamantes.
     */
    DIAMANTE('♦'),

    /**
     * Representa el palo de picas.
     */
    PICA('♠'),

    /**
     * Representa el palo de tréboles.
     */
    TREBOL('♣');

    /**
     * Símbolo utilizado para representar visualmente el palo.
     */
    private final char simbolo;

    /**
     * Inicializa un palo con su símbolo correspondiente.
     *
     * @param simbolo símbolo que representa al palo
     */
    Carta(char simbolo) { this.simbolo = simbolo; }

    /**
     * Devuelve el símbolo asociado al palo.
     *
     * @return símbolo que representa al palo
     */
    public char getSimbolo() { return simbolo; }
}