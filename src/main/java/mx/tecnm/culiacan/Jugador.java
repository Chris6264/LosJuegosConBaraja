package mx.tecnm.culiacan;

public abstract class Jugador {
   protected GeneradorDeFichas generadorDeFichas;
   protected MazoJugador mazoJugador;

    public Jugador(GeneradorDeFichas generadorDeFichas, MazoJugador mazoJugador) {
        this.generadorDeFichas = generadorDeFichas;
        this.mazoJugador = mazoJugador;
    }

    public GeneradorDeFichas getGeneradorDeFichas() { return generadorDeFichas; }

    public MazoJugador getMazoJugador() {
        return mazoJugador;
    }
}
