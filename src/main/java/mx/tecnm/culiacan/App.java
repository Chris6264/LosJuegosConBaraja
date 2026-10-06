package mx.tecnm.culiacan;

/**
 * Clase principal de la aplicación.
 * <p>
 * Se encarga de crear las dependencias necesarias para iniciar una partida
 * de BlackJack y seleccionar la versión del juego que será ejecutada.
 * </p>
 *
 * <p>
 * En este punto se crean las reglas del BlackJack, la vista encargada de la
 * interacción con el usuario y la implementación concreta de la versión del
 * juego.
 * </p>
 */
public class App {

    /**
     * Punto de entrada principal de la aplicación.
     * <p>
     * Inicializa las reglas del juego, la vista y la versión de BlackJack,
     * para posteriormente comenzar la partida.
     * </p>
     *
     * @param args argumentos recibidos desde la línea de comandos
     */
    public static void main(String[] args) {
        Reglas reglas = new ReglasBlackJack();
        VistaJuego vistaJuego = new VistaJuego();
        VersionJuego versionJuego = new BlackJackV2();

        versionJuego.jugar(reglas, vistaJuego);
    }
}