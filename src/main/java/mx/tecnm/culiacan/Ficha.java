package mx.tecnm.culiacan;

public enum Ficha {
    BLANCA (1),
    ROJA (5),
    VERDE (25),
    NEGRA (100),
    MORADA (500);

    private final int valorFicha;

    Ficha(int valorFicha) {
        this.valorFicha = valorFicha;
    }

    public int getValorFicha() {
        return valorFicha;
    }
}
