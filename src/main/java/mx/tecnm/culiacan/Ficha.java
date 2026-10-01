package mx.tecnm.culiacan;

public enum Ficha {
    BLANCA (1),// 10
    ROJA (5), // 5
    VERDE (25), //2
    NEGRA (100), // 2
    MORADA (500); //1

    private final int valorFicha;

    Ficha(int valorFicha) {
        this.valorFicha = valorFicha;
    }

    public int getValorFicha() {
        return valorFicha;
    }
}
