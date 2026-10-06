package mx.tecnm.culiacan;

/**
 * Representa una excepción asociada al incumplimiento de las reglas
 * definidas para el juego de BlackJack.
 * <p>
 * Esta excepción se utiliza cuando se detectan acciones, cantidades
 * o condiciones inválidas durante la ejecución de la partida.
 * </p>
 *
 * @see Reglas
 * @see ReglasBlackJack
 */
public class ReglasException extends RuntimeException {

    /**
     * Crea una nueva excepción con un mensaje descriptivo del error.
     *
     * @param message mensaje que describe la causa de la excepción
     */
    public ReglasException(String message) {
        super(message);
    }
}