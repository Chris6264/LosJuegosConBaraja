package mx.tecnm.culiacan;

/**
 * Define el comportamiento que debe implementar cualquier versión
 * disponible del juego de BlackJack.
 * <p>
 * Cada implementación concreta será responsable de ejecutar la lógica
 * correspondiente a su versión del juego utilizando las reglas y la vista
 * proporcionadas.
 * </p>
 *
 * @see Reglas
 * @see VistaJuego
 */
public interface VersionJuego {

    /**
     * Inicia y ejecuta una partida utilizando las reglas y la vista
     * proporcionadas.
     *
     * @param reglas reglas que se aplicarán durante la partida
     * @param vista vista utilizada para interactuar con el usuario
     */
    void jugar(Reglas reglas, VistaJuego vista);
}