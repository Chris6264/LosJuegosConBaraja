package mx.tecnm.culiacan;

import java.util.TreeMap;
import java.util.Map;

public class GeneradorDeFichas {
    private Map<Ficha,Integer> fichas;

    public GeneradorDeFichas() {
        this.fichas = new TreeMap<>();
        generarFichasJugadorApostador();
    }

    public void generarFichasJugadorApostador() {
        fichas.put(Ficha.MORADA, 1);
        fichas.put(Ficha.NEGRA, 2);
        fichas.put(Ficha.VERDE, 2);
        fichas.put(Ficha.ROJA, 5);
        fichas.put(Ficha.BLANCA, 10);
      }

    public Map<Ficha, Integer> getFichas() {
        return fichas;
    }
}