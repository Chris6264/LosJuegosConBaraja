package mx.tecnm.culiacan;

import java.util.List;
import java.util.Map;

public class VistaJuego {

    private static final String RESET = "\u001B[0m";
    private static final String NEGRITA = "\u001B[1m";
    private static final String TENUE = "\u001B[2m";
    private static final String VERDE = "\u001B[32m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String ROJO = "\u001B[31m";
    private static final String AZUL = "\u001B[34m";
    private static final String MORADO = "\u001B[35m";
    private static final String GRIS = "\u001B[90m";
    private static final String BLANCO = "\u001B[97m";
    private static final String NEGRO = "\u001B[30m";
    private static final String FONDO_BLANCO = "\u001B[107m";

    private static final int ANCHO = 27;
    private static final int ANCHO_CARTA = 10;

    public void mostrarEncabezado() {
        String borde = "═".repeat(ANCHO);
        System.out.println(VERDE + NEGRITA + "╔" + borde + "╗");
        System.out.println("║" + centrar("♠ ♥  BLACKJACK  ♦ ♣", ANCHO) + "║");
        System.out.println("╚" + borde + "╝" + RESET);
        System.out.println(CYAN + "  Jugador #1" + RESET + "\n");
    }

    public void mostrarFichas(Map<Ficha, Integer> fichasJugador) {
        abrirTabla("Tus fichas", ANCHO);
        encabezadoColumnas();
        int i = 1;
        for (Map.Entry<Ficha, Integer> entry : fichasJugador.entrySet()) {
            System.out.println(filaFicha(i, entry.getKey(), entry.getValue()));
            i++;
        }
        cerrarTabla(ANCHO);
    }

    public void mostrarTipoDeApuestas() {
        abrirTabla("Tipo de apuesta", ANCHO);
        System.out.println(filaOpcion(1, "Minima", "5 fichas"));
        System.out.println(filaOpcion(2, "Maxima", "todas"));
        System.out.println(filaOpcion(3, "Personalizada", ""));
        cerrarTabla(ANCHO);
    }

    public int pedirTipoDeApuesta() {
        System.out.print(CYAN + "➜ " + RESET + "Como deseas apostar: ");
        return Keyboard.readInt();
    }

    public int pedirTipoDeFicha() {
        System.out.print(CYAN + "➜ " + RESET + "Que ficha desea apostar: ");
        return Keyboard.readInt();
    }

    public int pedirCantidadDeFichasApostar() {
        System.out.print(CYAN + "➜ " + RESET + "Cuantas fichas deseas apostar: ");
        return Keyboard.readInt();
    }

    public void mostrarApuestaActual(Map<Ficha, Integer> apuestaActual) {
        abrirTabla("Apuesta actual", ANCHO);
        encabezadoColumnas();
        int i = 1;
        for (Map.Entry<Ficha, Integer> entry : apuestaActual.entrySet()) {
            if (entry.getValue() > 0) {
                System.out.println(filaFicha(i, entry.getKey(), entry.getValue()));
                i++;
            }
        }
        cerrarTabla(ANCHO);
    }

    public void mostrarBarajaJugador(List<String> barajaJugador) {
        int ancho = Math.max(ANCHO, 1 + barajaJugador.size() * ANCHO_CARTA);

        String[] lineas = new String[7];
        for (int i = 0; i < lineas.length; i++) lineas[i] = "";

        for (String carta : barajaJugador) {
            String palo = carta.substring(0, 1);
            String valor = carta.substring(1);
            String color = (palo.equals("♥") || palo.equals("♦")) ? ROJO : NEGRO;

            lineas[0] += pintar(color, "╭───────╮");
            lineas[1] += pintar(color, String.format("│ %-2s    │", valor));
            lineas[2] += pintar(color, "│       │");
            lineas[3] += pintar(color, String.format("│   %s   │", palo));
            lineas[4] += pintar(color, "│       │");
            lineas[5] += pintar(color, String.format("│    %2s │", valor));
            lineas[6] += pintar(color, "╰───────╯");
        }

        abrirTabla("Tu mano", ancho);
        int relleno = ancho - 1 - barajaJugador.size() * ANCHO_CARTA;
        for (String linea : lineas) {
            System.out.println("│ " + linea + " ".repeat(relleno) + "│");
        }
        cerrarTabla(ancho);
    }

    private void abrirTabla(String titulo, int ancho) {
        String borde = "─".repeat(ancho);
        System.out.println("┌" + borde + "┐");
        System.out.println("│" + NEGRITA + String.format(" %-" + (ancho - 2) + "s ", titulo) + RESET + "│");
        System.out.println("├" + borde + "┤");
    }

    private void cerrarTabla(int ancho) {
        System.out.println("└" + "─".repeat(ancho) + "┘\n");
    }

    private void encabezadoColumnas() {
        String borde = "─".repeat(ANCHO);
        System.out.println("│" + NEGRITA + String.format(" %-3s %-12s %8s ", "#", "Ficha", "Cant.") + RESET + "│");
        System.out.println("├" + borde + "┤");
    }

    private String filaFicha(int numero, Ficha ficha, int cantidad) {
        return "│"
                + String.format(" %-3d ", numero)
                + colorFicha(ficha) + String.format("%-12s", ficha) + RESET
                + String.format(" %8d ", cantidad)
                + "│";
    }

    private String filaOpcion(int numero, String nombre, String detalle) {
        return "│ "
                + AMARILLO + "[" + numero + "]" + RESET + " "
                + String.format("%-13s", nombre)
                + TENUE + String.format("%8s", detalle) + RESET
                + " │";
    }

    private String pintar(String color, String texto) {
        return FONDO_BLANCO + color + texto + RESET + " ";
    }

    private String centrar(String texto, int ancho) {
        int total = ancho - texto.length();
        int izq = total / 2;
        return " ".repeat(izq) + texto + " ".repeat(total - izq);
    }

    private String colorFicha(Ficha ficha) {
        switch (ficha.name()) {
            case "BLANCA": return BLANCO;
            case "ROJA":   return ROJO;
            case "AZUL":   return AZUL;
            case "VERDE":  return VERDE;
            case "NEGRA":  return GRIS;
            case "MORADA": return MORADO;
            default:       return AMARILLO;
        }
    }
}