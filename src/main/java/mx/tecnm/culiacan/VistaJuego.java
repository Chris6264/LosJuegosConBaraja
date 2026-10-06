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

    private static final String FONDO_CARTA = "\u001B[48;5;231m";
    private static final String TINTA_NEGRA = "\u001B[38;5;16m";
    private static final String TINTA_ROJA = "\u001B[38;5;160m";
    private static final String TINTA_AZUL = "\u001B[38;5;25m";

    private static final int ANCHO = 36;
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
        System.out.println(filaOpcion(1, "Minima", "5 fichas blancas"));
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

    public int pedirAccion() {
        abrirTabla("Accion", ANCHO);
        System.out.println(filaOpcion(1, "Pedir carta", ""));
        System.out.println(filaOpcion(2, "Pasar", ""));
        cerrarTabla(ANCHO);
        System.out.print(CYAN + "➜ " + RESET + "Que accion desea realizar: ");
        return Keyboard.readInt();
    }

    public void mostrarPuntaje(int puntajeCartas) {
        System.out.println(NEGRITA + "Puntaje de cartas: " + AMARILLO + puntajeCartas + RESET + "\n");
    }

    public void mostrarMensajeAJugador() {
        System.out.println(ROJO + NEGRITA + "Has perdido, sacaste mas de 21" + RESET);
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
        dibujarMano("Tu mano", barajaJugador, false);
    }

    public void mostrarBarajaCroupier(List<String> barajaCroupier, boolean juegoEnCurso) {
        dibujarMano("Mano del croupier", barajaCroupier, juegoEnCurso);
    }

    public void mostrarDictamen(DictamenJugador dictamen, int puntajeJugador, int puntajeCroupier) {
        if (dictamen == DictamenJugador.VICTORIA) {
            String motivo;
            if (puntajeJugador == 21) motivo = "Has obtenido 21 puntos";
            else if (puntajeCroupier > 21) motivo = "El croupier se paso de 21 puntos";
            else motivo = "Obtuviste un puntaje mas cercano a 21 puntos";
            System.out.println(VERDE + NEGRITA + "Felicidades, Has ganado. " + motivo + RESET);
        } else if (dictamen == DictamenJugador.DERROTA) {
            String motivo;
            if (puntajeCroupier == 21) motivo = "El croupier obtuvo 21 puntos";
            else if (puntajeJugador > 21) motivo = "Te pasaste de 21 puntos";
            else motivo = "El croupier obtuvo un puntaje mas cercano a 21 puntos";
            System.out.println(ROJO + NEGRITA + "Has perdido, " + motivo + RESET);
        } else {
            String mensaje = (puntajeJugador == 21)
                    ? "Es un empate, ambos obtuvieron 21 puntos"
                    : "Es un empate";
            System.out.println(GRIS + NEGRITA + mensaje + RESET);
        }
    }

    public int opcionDeJuego() {
        abrirTabla("Que deseas hacer", ANCHO);
        System.out.println(filaOpcion(1, "Jugar De Nuevo", ""));
        System.out.println(filaOpcion(2, "Terminar", ""));
        cerrarTabla(ANCHO);
        System.out.print(CYAN + "➜ " + RESET + "Que opcion deseas: ");
        return Keyboard.readInt();
    }

    public void mostrarSinFichas(){
        System.out.println(ROJO + NEGRITA + "Te quedaste sin fichas" + RESET);
    }

    public void mostrarMensajeBlackJack() {
        System.out.println(VERDE + NEGRITA + "Felicidades, Has ganado. Obtuviste blackjack" + RESET);
    }

    public void mostrarMensajeCroupierBlackJack(){
        System.out.println(ROJO + NEGRITA + "Has perdido, El croupier obtuvo blackjack" + RESET);
    }

    public void mostrarError(String mensaje) {
        System.out.println(ROJO + NEGRITA + mensaje + RESET + "\n");
    }

    private void dibujarMano(String titulo, List<String> cartas, boolean ocultarSegunda) {
        int ancho = Math.max(ANCHO, 1 + cartas.size() * ANCHO_CARTA);

        String[] lineas = new String[7];
        for (int i = 0; i < lineas.length; i++) lineas[i] = "";

        for (int c = 0; c < cartas.size(); c++) {
            String[] carta = (ocultarSegunda && c == 1)
                    ? lineasCartaOculta()
                    : lineasCarta(cartas.get(c));
            for (int i = 0; i < lineas.length; i++) lineas[i] += carta[i];
        }

        abrirTabla(titulo, ancho);
        int relleno = ancho - 1 - cartas.size() * ANCHO_CARTA;
        for (String linea : lineas) {
            System.out.println("│ " + linea + " ".repeat(relleno) + "│");
        }
        cerrarTabla(ancho);
    }

    private String[] lineasCarta(String carta) {
        String palo = carta.substring(0, 1);
        String valor = carta.substring(1);
        String color = (palo.equals("♥") || palo.equals("♦")) ? TINTA_ROJA : TINTA_NEGRA;

        return new String[]{
                pintar(color, "╭───────╮"),
                pintar(color, String.format("│ %-2s    │", valor)),
                pintar(color, "│       │"),
                pintar(color, String.format("│   %s   │", palo)),
                pintar(color, "│       │"),
                pintar(color, String.format("│    %2s │", valor)),
                pintar(color, "╰───────╯")
        };
    }

    private String[] lineasCartaOculta() {
        return new String[]{
                pintar(TINTA_AZUL, "╭───────╮"),
                pintar(TINTA_AZUL, "│░░░░░░░│"),
                pintar(TINTA_AZUL, "│░░░░░░░│"),
                pintar(TINTA_AZUL, "│░░░░░░░│"),
                pintar(TINTA_AZUL, "│░░░░░░░│"),
                pintar(TINTA_AZUL, "│░░░░░░░│"),
                pintar(TINTA_AZUL, "╰───────╯")
        };
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
        System.out.println("│" + NEGRITA + String.format(" %-3s %-21s %8s ", "#", "Ficha", "Cant.") + RESET + "│");
        System.out.println("├" + borde + "┤");
    }

    private String filaFicha(int numero, Ficha ficha, int cantidad) {
        return "│"
                + String.format(" %-3d ", numero)
                + colorFicha(ficha) + String.format("%-21s", ficha) + RESET
                + String.format(" %8d ", cantidad)
                + "│";
    }

    private String filaOpcion(int numero, String nombre, String detalle) {
        return "│ "
                + AMARILLO + "[" + numero + "]" + RESET + " "
                + String.format("%-14s", nombre)
                + TENUE + String.format("%16s", detalle) + RESET
                + " │";
    }

    private String pintar(String color, String texto) {
        return FONDO_CARTA + color + texto + RESET + " ";
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