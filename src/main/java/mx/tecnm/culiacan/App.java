package mx.tecnm.culiacan;

public class App {
    public static void main(String[] args){
        Reglas reglas = new ReglasBlackJack();
        VistaJuego vistaJuego = new VistaJuego();
        VersionJuego versionJuego = new BlackJackV2();
        versionJuego.jugar(reglas,vistaJuego);
    }
}
