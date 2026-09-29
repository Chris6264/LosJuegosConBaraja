package mx.tecnm.culiacan;

import java.util.List;

public class Croupier extends Jugador{

    public Croupier(List<Ficha> fichas, MazoJugador mazoJugador) {
        super(fichas, mazoJugador);
    }

    private void generarFichas(){
        //Blancas: 20
        //ROJA: 15
        //VERDE: 10
        //NEGRA: 8
        //MORADA: 5
    }

}
