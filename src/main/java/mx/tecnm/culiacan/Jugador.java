package mx.tecnm.culiacan;

/**
 * Representa la estructura base de un jugador dentro del juego.
 * <p>
 * Esta clase abstracta define las dependencias comunes que comparten
 * los diferentes tipos de jugadores, como el generador de fichas y
 * el mazo utilizado durante la partida.
 * </p>
 *
 * <p>
 * Las clases concretas que hereden de {@code Jugador} podrán utilizar
 * estas dependencias para administrar fichas, cartas y acciones propias
 * de cada tipo de jugador.
 * </p>
 *
 * @see GeneradorDeFichas
 * @see MazoJugador
 */
public abstract class Jugador {

    /**
     * Generador encargado de proporcionar y administrar
     * las fichas asociadas al jugador.
     */
    private GeneradorDeFichas generadorDeFichas;

    /**
     * Mazo utilizado por el jugador durante la partida.
     */
    private MazoJugador mazoJugador;

    /**
     * Inicializa las dependencias comunes de un jugador.
     *
     * @param generadorDeFichas generador de fichas asociado al jugador
     * @param mazoJugador mazo utilizado durante la partida
     */
    public Jugador(
            GeneradorDeFichas generadorDeFichas,
            MazoJugador mazoJugador) {

        this.generadorDeFichas = generadorDeFichas;
        this.mazoJugador = mazoJugador;
    }

    public GeneradorDeFichas getGeneradorDeFichas() {
        return generadorDeFichas;
    }

    public MazoJugador getMazoJugador() {
        return mazoJugador;
    }
}