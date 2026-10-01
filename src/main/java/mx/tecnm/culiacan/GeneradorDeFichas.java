package mx.tecnm.culiacan;

import java.util.ArrayList;
import java.util.List;

public class GeneradorDeFichas {
    private List<Ficha> fichas;

    public GeneradorDeFichas() {
        this.fichas = new ArrayList<>();
    }

    public List<Ficha> obtenerFichasJugadorApostador(){
        for (int i = 1; i <= 10; i++){
            if(i == 1) fichas.add(Ficha.MORADA);

            if(i >= 1 && i <= 2) {
                fichas.add(Ficha.NEGRA);
                fichas.add(Ficha.VERDE);
            }

            if(i >= 1 && i <= 5) fichas.add(Ficha.ROJA);
            if(i >= 1) fichas.add(Ficha.BLANCA);
        }
        return fichas;
    }

    public List<Ficha> obtenerFichasCroupier() {
        return fichas;
    }
}