package mx.tecnm.culiacan;

public enum Carta {
    CORAZON  ('♥'),
    DIAMANTE ('♦'),
    PICA ('♠'),
    TREBOL ('♣');

    private final char simbolo;

    Carta(char simbolo) {
        this.simbolo = simbolo;
    }

    public char getSimbolo() {
        return simbolo;
    }
}
