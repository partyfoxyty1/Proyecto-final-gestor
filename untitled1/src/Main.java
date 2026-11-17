import java.util.Scanner;

/** Ejecuta el menú de consola del gestor de biblioteca. */
public class Main {
    /** Inicia el programa y repite el menú hasta que el usuario elige salir. */
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        Biblioteca biblioteca = new Biblioteca();
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero(entrada, "Elige una opción: ");
            switch (opcion) {
                case 1:
                    registrarLibro(entrada, biblioteca);
                    break;
                case 2:
                    registrarPersona(entrada, biblioteca);
                    break;
                case 3:
                    biblioteca.prestarLibro(leerDocumento(entrada), leerTexto(entrada, "Código del libro: "));
                    break;
                case 4:
                    biblioteca.devolverLibro(leerDocumento(entrada), leerTexto(entrada, "Código del libro: "));
                    break;
                case 5:
                    buscarLibro(entrada, biblioteca);
                    break;
                case 6:
                    biblioteca.mostrarLibros();
                    break;
                case 7:
                    biblioteca.mostrarPersonas();
                    break;
                case 8:
                    biblioteca.mostrarPrestamosActivos();
                    break;
                case 9:
                    System.out.println("Libros registrados: " + biblioteca.getCantidadLibros());
                    System.out.println("Personas registradas: " + biblioteca.getCantidadPersonas());
                    System.out.println("Ejemplares disponibles: " + biblioteca.contarEjemplaresDisponibles());
                    break;
                case 10:
                    biblioteca.mostrarHistorialPrestamos();
                    break;
                case 11:
                    biblioteca.mostrarPrestamosVencidos();
                    break;
                case 12:
                    biblioteca.mostrarEstadoPersona(leerDocumento(entrada));
                    break;
                case 0:
                    System.out.println("Hasta pronto.");
                    break;
                default:
                    System.out.println("Opción inválida. Elige un número del 0 al 12.");
            }
            System.out.println();
        } while (opcion != 0);
        entrada.close();
    }

    /** Imprime las opciones disponibles en el programa. */
    private static void mostrarMenu() {
        System.out.println("=== SISTEMA INTERNO DE BIBLIOTECA ===");
        System.out.println("1. Registrar libro en el inventario");
        System.out.println("2. Registrar usuario");
        System.out.println("3. Registrar préstamo de libro");
        System.out.println("4. Registrar devolución de libro");
        System.out.println("5. Consultar libro por código");
        System.out.println("6. Consultar inventario de libros");
        System.out.println("7. Consultar usuarios registrados");
        System.out.println("8. Consultar préstamos activos");
        System.out.println("9. Consultar resumen de inventario");
        System.out.println("10. Consultar historial de préstamos");
        System.out.println("11. Consultar préstamos vencidos");
        System.out.println("12. Consultar estado de un usuario");
        System.out.println("0. Salir");
    }

    /** Solicita y registra los datos de un libro. */
    private static void registrarLibro(Scanner entrada, Biblioteca biblioteca) {
        String codigo = leerTexto(entrada, "Código: ");
        String titulo = leerTexto(entrada, "Título: ");
        String autor = leerTexto(entrada, "Autor: ");
        int cantidad = leerEntero(entrada, "Cantidad de ejemplares (0 o más): ");
        if (cantidad < 0) {
            System.out.println("Error: la cantidad no puede ser negativa.");
            return;
        }
        biblioteca.registrarLibro(new Libro(codigo, titulo, autor, cantidad));
    }

    /** Solicita los datos de la persona y crea la subclase elegida. */
    private static void registrarPersona(Scanner entrada, Biblioteca biblioteca) {
        String nombre = leerTexto(entrada, "Nombre: ");
        int documento = leerDocumento(entrada);
        System.out.println("Tipo: 1. Estudiante  2. Docente  3. Directivo  4. Investigador");
        int tipo = leerEntero(entrada, "Elige el tipo: ");
        Persona persona;
        switch (tipo) {
            case 1:
                persona = new Estudiante(nombre, documento, leerTexto(entrada, "Programa: "));
                break;
            case 2:
                persona = new Docente(nombre, documento, leerTexto(entrada, "Área: "));
                break;
            case 3:
                persona = new Directivo(nombre, documento, leerTexto(entrada, "Departamento: "));
                break;
            case 4:
                persona = new Investigador(nombre, documento, leerTexto(entrada, "Línea de investigación: "));
                break;
            default:
                System.out.println("Tipo inválido. No se registró la persona.");
                return;
        }
        biblioteca.registrarPersona(persona);
    }

    /** Busca un libro por código y muestra sus datos si existe. */
    private static void buscarLibro(Scanner entrada, Biblioteca biblioteca) {
        String codigo = leerTexto(entrada, "Código del libro: ");
        Libro libro = biblioteca.buscarLibro(codigo);
        if (libro == null) System.out.println("No se encontró un libro con ese código.");
        else libro.mostrarInformacion();
    }

    /** Lee un entero y vuelve a preguntar si la entrada no es numérica. */
    private static int leerEntero(Scanner entrada, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = entrada.nextLine().trim();
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException error) {
                System.out.println("Error: ingresa un número entero válido.");
            }
        }
    }

    /** Lee un texto no vacío y vuelve a preguntar si está en blanco. */
    private static String leerTexto(Scanner entrada, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = entrada.nextLine().trim();
            if (!valor.isEmpty()) return valor;
            System.out.println("Error: este dato no puede quedar vacío.");
        }
    }

    /** Lee un documento entero positivo. */
    private static int leerDocumento(Scanner entrada) {
        while (true) {
            int documento = leerEntero(entrada, "Documento: ");
            if (documento > 0) return documento;
            System.out.println("Error: el documento debe ser positivo.");
        }
    }
}
