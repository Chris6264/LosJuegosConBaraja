package mx.tecnm.culiacan;

import java.util.List;
import java.util.Map;

/**
 * Se encarga de toda la interacción visual entre el juego de BlackJack
 * y el usuario mediante la consola.
 * <p>
 * Esta clase muestra títulos, cartas, fichas, apuestas, puntajes,
 * resultados y mensajes relacionados con el desarrollo de la partida.
 * También solicita al usuario las diferentes opciones necesarias
 * durante el juego.
 * </p>
 *
 * <p>
 * Para mejorar la presentación utiliza códigos ANSI que permiten
 * aplicar colores y estilos al texto mostrado en consola.
 * </p>
 *
 * @see Ficha
 * @see DictamenJugador
 * @see Keyboard
 */
public class VistaJuego {

    /**
     * Restablece el formato y color de la consola.
     */
    private static final String RESET = "\u001B[0m";

    /**
     * Aplica formato de texto en negrita.
     */
    private static final String NEGRITA = "\u001B[1m";

    /**
     * Aplica un estilo tenue al texto.
     */
    private static final String TENUE = "\u001B[2m";

    /**
     * Color verde utilizado en diferentes mensajes.
     */
    private static final String VERDE = "\u001B[32m";

    /**
     * Color amarillo utilizado principalmente para opciones y puntajes.
     */
    private static final String AMARILLO = "\u001B[33m";

    /**
     * Color cyan utilizado principalmente en mensajes de interacción.
     */
    private static final String CYAN = "\u001B[36m";

    /**
     * Color rojo utilizado para errores, derrotas y cartas rojas.
     */
    private static final String ROJO = "\u001B[31m";

    /**
     * Color azul utilizado principalmente para representar cartas ocultas.
     */
    private static final String AZUL = "\u001B[34m";

    /**
     * Color morado utilizado para representar fichas moradas.
     */
    private static final String MORADO = "\u001B[35m";

    /**
     * Color gris utilizado para ciertos mensajes y fichas negras.
     */
    private static final String GRIS = "\u001B[90m";

    /**
     * Color blanco utilizado para representar fichas blancas.
     */
    private static final String BLANCO = "\u001B[97m";

    /**
     * Fondo utilizado para representar visualmente las cartas.
     */
    private static final String FONDO_CARTA = "\u001B[48;5;231m";

    /**
     * Color de tinta utilizado para los palos negros.
     */
    private static final String TINTA_NEGRA = "\u001B[38;5;16m";

    /**
     * Color de tinta utilizado para corazones y diamantes.
     */
    private static final String TINTA_ROJA = "\u001B[38;5;160m";

    /**
     * Color de tinta utilizado para representar una carta oculta.
     */
    private static final String TINTA_AZUL = "\u001B[38;5;25m";

    /**
     * Ancho general utilizado para las tablas mostradas en consola.
     */
    private static final int ANCHO = 36;

    /**
     * Espacio horizontal ocupado por cada carta al dibujarse.
     */
    private static final int ANCHO_CARTA = 10;

    /**
     * Muestra el encabezado inicial del juego y la identificación
     * del primer jugador.
     */
    public void mostrarEncabezado() {
        mostrarTitulo();
        System.out.println(CYAN + "  Jugador #1" + RESET + "\n");
    }

    /**
     * Muestra el título principal del juego de BlackJack.
     */
    public void mostrarTitulo() {
        String borde = "═".repeat(ANCHO);
        System.out.println(VERDE + NEGRITA + "╔" + borde + "╗");
        System.out.println("║" + centrar("♠ ♥  BLACKJACK  ♦ ♣", ANCHO) + "║");
        System.out.println("╚" + borde + "╝" + RESET);
    }

    /**
     * Muestra la cantidad de jugadores que participan en la mesa.
     *
     * @param cantidadJugadores número de jugadores participantes
     */
    public void mostrarMesa(int cantidadJugadores) {
        System.out.println(CYAN + "  Mesa de " + cantidadJugadores + " jugadores" + RESET + "\n");
    }

    /**
     * Muestra el número correspondiente al jugador cuyo turno
     * está siendo procesado.
     *
     * @param numeroJugador número identificador del jugador
     */
    public void mostrarJugador(int numeroJugador) {
        System.out.println(CYAN + NEGRITA + "▶ Jugador #" + numeroJugador + RESET + "\n");
    }

    /**
     * Muestra en forma de tabla las fichas disponibles del jugador.
     *
     * @param fichasJugador mapa que contiene los tipos de ficha
     *                      y sus cantidades disponibles
     */
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

    /**
     * Muestra los tipos de apuesta disponibles para el jugador.
     */
    public void mostrarTipoDeApuestas() {
        abrirTabla("Tipo de apuesta", ANCHO);
        System.out.println(filaOpcion(1, "Minima", "5 fichas blancas"));
        System.out.println(filaOpcion(2, "Maxima", "todas"));
        System.out.println(filaOpcion(3, "Personalizada", ""));
        cerrarTabla(ANCHO);
    }

    /**
     * Solicita al usuario la cantidad de jugadores que participarán.
     *
     * @param minimo cantidad mínima de jugadores permitida
     * @param maximo cantidad máxima de jugadores permitida
     * @return cantidad ingresada por el usuario
     */
    public int pedirCantidadDeJugadores(int minimo, int maximo) {
        System.out.print(CYAN + "➜ " + RESET + "Cuantos jugadores van a jugar (" + minimo + " a " + maximo + "): ");
        return Keyboard.readInt();
    }

    /**
     * Solicita al jugador seleccionar el tipo de apuesta
     * que desea realizar.
     *
     * @return opción de apuesta seleccionada
     */
    public int pedirTipoDeApuesta() {
        System.out.print(CYAN + "➜ " + RESET + "Como deseas apostar: ");
        return Keyboard.readInt();
    }

    /**
     * Solicita al jugador seleccionar el tipo de ficha
     * que desea utilizar en una apuesta.
     *
     * @return opción correspondiente al tipo de ficha
     */
    public int pedirTipoDeFicha() {
        System.out.print(CYAN + "➜ " + RESET + "Que ficha desea apostar: ");
        return Keyboard.readInt();
    }

    /**
     * Solicita la cantidad de fichas que el jugador desea apostar.
     *
     * @return cantidad de fichas seleccionada
     */
    public int pedirCantidadDeFichasApostar() {
        System.out.print(CYAN + "➜ " + RESET + "Cuantas fichas deseas apostar: ");
        return Keyboard.readInt();
    }

    /**
     * Muestra las acciones disponibles durante el turno
     * y solicita al jugador seleccionar una.
     *
     * @return acción seleccionada por el jugador
     */
    public int pedirAccion() {
        abrirTabla("Accion", ANCHO);
        System.out.println(filaOpcion(1, "Pedir carta", ""));
        System.out.println(filaOpcion(2, "Pasar", ""));
        cerrarTabla(ANCHO);
        System.out.print(CYAN + "➜ " + RESET + "Que accion desea realizar: ");
        return Keyboard.readInt();
    }

    /**
     * Muestra el puntaje actual obtenido por las cartas del jugador.
     *
     * @param puntajeCartas puntaje total de la mano
     */
    public void mostrarPuntaje(int puntajeCartas) {
        System.out.println(NEGRITA + "Puntaje de cartas: " + AMARILLO + puntajeCartas + RESET + "\n");
    }

    /**
     * Muestra un mensaje indicando que el jugador perdió
     * por superar los 21 puntos.
     */
    public void mostrarMensajeAJugador() {
        System.out.println(ROJO + NEGRITA + "Has perdido, sacaste mas de 21" + RESET);
    }

    /**
     * Muestra las fichas que forman la apuesta actual.
     *
     * @param apuestaActual mapa que contiene los tipos de fichas
     *                      y las cantidades apostadas
     */
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

    /**
     * Muestra gráficamente las cartas que posee el jugador.
     *
     * @param barajaJugador cartas que forman la mano del jugador
     */
    public void mostrarBarajaJugador(List<String> barajaJugador) {
        dibujarMano("Tu mano", barajaJugador, false);
    }

    /**
     * Muestra gráficamente las cartas que posee el croupier.
     * <p>
     * Mientras el juego esté en curso puede ocultarse la segunda
     * carta del croupier.
     * </p>
     *
     * @param barajaCroupier cartas que forman la mano del croupier
     * @param juegoEnCurso indica si debe ocultarse la segunda carta
     */
    public void mostrarBarajaCroupier(List<String> barajaCroupier, boolean juegoEnCurso) {
        dibujarMano("Mano del croupier", barajaCroupier, juegoEnCurso);
    }

    /**
     * Muestra el resultado final obtenido por el jugador y una
     * explicación relacionada con los puntajes alcanzados.
     *
     * @param dictamen resultado obtenido por el jugador
     * @param puntajeJugador puntaje final del jugador
     * @param puntajeCroupier puntaje final del croupier
     */
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

    /**
     * Solicita al usuario decidir si desea iniciar otra ronda
     * o terminar el juego.
     *
     * @return opción seleccionada por el usuario
     */
    public int opcionDeJuego() {
        abrirTabla("Que deseas hacer", ANCHO);
        System.out.println(filaOpcion(1, "Jugar De Nuevo", ""));
        System.out.println(filaOpcion(2, "Terminar", ""));
        cerrarTabla(ANCHO);
        System.out.print(CYAN + "➜ " + RESET + "Que opcion deseas: ");
        return Keyboard.readInt();
    }

    /**
     * Muestra un mensaje indicando que el jugador se quedó
     * sin fichas disponibles.
     */
    public void mostrarSinFichas() {
        System.out.println(ROJO + NEGRITA + "Te quedaste sin fichas" + RESET);
    }

    /**
     * Informa que un jugador determinado se quedó sin fichas
     * y debe abandonar la mesa.
     *
     * @param numeroJugador número del jugador que quedó sin fichas
     */
    public void mostrarJugadorSinFichas(int numeroJugador) {
        System.out.println(ROJO + NEGRITA + "El jugador #" + numeroJugador + " se quedo sin fichas y sale de la mesa" + RESET + "\n");
    }

    /**
     * Informa que todos los jugadores se han quedado sin fichas.
     */
    public void mostrarSinJugadores() {
        System.out.println(ROJO + NEGRITA + "Todos los jugadores se quedaron sin fichas" + RESET);
    }

    /**
     * Muestra un mensaje indicando que el jugador obtuvo BlackJack.
     */
    public void mostrarMensajeBlackJack() {
        System.out.println(VERDE + NEGRITA + "Felicidades, Has ganado. Obtuviste blackjack" + RESET);
    }

    /**
     * Muestra un mensaje indicando que el croupier obtuvo BlackJack.
     */
    public void mostrarMensajeCroupierBlackJack() {
        System.out.println(ROJO + NEGRITA + "Has perdido, El croupier obtuvo blackjack" + RESET);
    }

    /**
     * Muestra un mensaje de error al usuario.
     *
     * @param mensaje descripción del error ocurrido
     */
    public void mostrarError(String mensaje) {
        System.out.println(ROJO + NEGRITA + mensaje + RESET + "\n");
    }

    /**
     * Dibuja gráficamente una mano de cartas dentro de una tabla.
     *
     * @param titulo título que se mostrará sobre la mano
     * @param cartas cartas que forman la mano
     * @param ocultarSegunda indica si debe ocultarse la segunda carta
     */
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

    /**
     * Genera las líneas necesarias para representar gráficamente
     * una carta visible.
     *
     * @param carta representación textual de la carta
     * @return líneas que forman la representación visual de la carta
     */
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

    /**
     * Genera las líneas necesarias para representar gráficamente
     * una carta cuyo valor debe permanecer oculto.
     *
     * @return líneas que forman la carta oculta
     */
    private String[] lineasCartaOculta() {
        return new String[]{
                pintar(TINTA_NEGRA, "╭───────╮"),
                pintar(TINTA_NEGRA, "│░░░░░░░│"),
                pintar(TINTA_NEGRA, "│░░░░░░░│"),
                pintar(TINTA_NEGRA, "│░░░░░░░│"),
                pintar(TINTA_NEGRA, "│░░░░░░░│"),
                pintar(TINTA_NEGRA, "│░░░░░░░│"),
                pintar(TINTA_NEGRA, "╰───────╯")
        };
    }

    /**
     * Abre una tabla de consola mostrando su borde superior
     * y el título correspondiente.
     *
     * @param titulo título de la tabla
     * @param ancho ancho total de la tabla
     */
    private void abrirTabla(String titulo, int ancho) {
        String borde = "─".repeat(ancho);
        System.out.println("┌" + borde + "┐");
        System.out.println("│" + NEGRITA + String.format(" %-" + (ancho - 2) + "s ", titulo) + RESET + "│");
        System.out.println("├" + borde + "┤");
    }

    /**
     * Cierra una tabla mostrada en consola.
     *
     * @param ancho ancho total de la tabla
     */
    private void cerrarTabla(int ancho) {
        System.out.println("└" + "─".repeat(ancho) + "┘\n");
    }

    /**
     * Muestra los encabezados utilizados en las tablas de fichas.
     */
    private void encabezadoColumnas() {
        String borde = "─".repeat(ANCHO);
        System.out.println("│" + NEGRITA + String.format(" %-3s %-21s %8s ", "#", "Ficha", "Cant.") + RESET + "│");
        System.out.println("├" + borde + "┤");
    }

    /**
     * Construye una fila para representar una ficha dentro de una tabla.
     *
     * @param numero número de la opción
     * @param ficha tipo de ficha
     * @param cantidad cantidad disponible de la ficha
     * @return fila formateada para mostrar en consola
     */
    private String filaFicha(int numero, Ficha ficha, int cantidad) {
        return "│"
                + String.format(" %-3d ", numero)
                + colorFicha(ficha) + String.format("%-21s", ficha) + RESET
                + String.format(" %8d ", cantidad)
                + "│";
    }

    /**
     * Construye una fila utilizada para mostrar una opción al usuario.
     *
     * @param numero número de la opción
     * @param nombre nombre de la opción
     * @param detalle información adicional de la opción
     * @return fila formateada para mostrar en consola
     */
    private String filaOpcion(int numero, String nombre, String detalle) {
        return "│ "
                + AMARILLO + "[" + numero + "]" + RESET + " "
                + String.format("%-14s", nombre)
                + TENUE + String.format("%16s", detalle) + RESET
                + " │";
    }

    /**
     * Aplica el fondo de carta y el color indicado a un texto.
     *
     * @param color color utilizado para representar la carta
     * @param texto texto que será coloreado
     * @return texto con los códigos ANSI correspondientes
     */
    private String pintar(String color, String texto) {
        return FONDO_CARTA + color + texto + RESET + " ";
    }

    /**
     * Centra un texto dentro de un ancho determinado.
     *
     * @param texto texto que será centrado
     * @param ancho espacio total disponible
     * @return texto con los espacios necesarios para quedar centrado
     */
    private String centrar(String texto, int ancho) {
        int total = ancho - texto.length();
        int izq = total / 2;
        return " ".repeat(izq) + texto + " ".repeat(total - izq);
    }

    /**
     * Obtiene el color ANSI correspondiente a un tipo de ficha.
     *
     * @param ficha ficha cuyo color se desea obtener
     * @return código ANSI utilizado para representar el color
     */
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