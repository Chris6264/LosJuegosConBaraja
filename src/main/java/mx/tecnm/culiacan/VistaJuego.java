package mx.tecnm.culiacan;

import java.util.List;
import java.util.Map;

public class VistaJuego {

    public void mostrarEncabezado(){
        System.out.println("---------- BlackJack ----------");
        System.out.println();
        System.out.println("Jugador #1: ");
        System.out.println();
    }

    public void mostrarFichas(Map<Ficha,Integer> fichas){
        System.out.println("Fichas:");
        for (Map.Entry<Ficha,Integer> entry : fichas.entrySet()){
            Ficha key = entry.getKey();
            int value = entry.getValue();
            System.out.println(key + ": " + value);
        }
        System.out.println();
    }

    public void mostrarTipoDeApuestas(){
        System.out.println("[1] Apuesta Minima: 5 Fichas Blancas");
        System.out.println("[2] Apuesta Maxima: Todas tus fichas");
        System.out.println("[3] Apuesta Personalizada");
    }

    public void mostrarBarajaJugador(List<String> barajaJugador) {
        System.out.println("\n" + "Mazo Actual:");
        StringBuilder[] lineas = new StringBuilder[5];
        for (int i = 0; i < lineas.length; i++) {
            lineas[i] = new StringBuilder();
        }

        for (String carta : barajaJugador) {
            String palo  = carta.substring(0, 1);
            String valor = carta.substring(1);

            lineas[0].append("┌───────┐ ");
            lineas[1].append(String.format("│%-2s     │ ", valor));
            lineas[2].append(String.format("│   %s   │ ", palo));
            lineas[3].append(String.format("│     %2s│ ", valor));
            lineas[4].append("└───────┘ ");
        }

        System.out.println();
        for (StringBuilder linea : lineas) {
            System.out.println(linea);
        }
    }
}
