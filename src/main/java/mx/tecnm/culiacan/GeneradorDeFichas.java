package mx.tecnm.culiacan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneradorDeFichas {
    private List<Ficha> fichas;

    public GeneradorDeFichas() {
        this.fichas = new ArrayList<>();
    }

    public List<Ficha> obtenerFichasJugadorApostador() {
        fichas.add(Ficha.MORADA);
        fichas.addAll(Collections.nCopies(2, Ficha.NEGRA));
        fichas.addAll(Collections.nCopies(2, Ficha.VERDE));
        fichas.addAll(Collections.nCopies(5, Ficha.ROJA));
        fichas.addAll(Collections.nCopies(10, Ficha.BLANCA));
        return fichas;
    }
}