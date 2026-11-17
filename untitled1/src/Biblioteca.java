import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

/** Administra libros, personas y préstamos usando arreglos de tamaño fijo. */
public class Biblioteca {
    private final String archivoDatos = "biblioteca.txt";
    private Libro[] libros = new Libro[100];
    private Persona[] personas = new Persona[100];
    private Prestamo[] prestamos = new Prestamo[200];
    private int cantidadLibros = 0;
    private int cantidadPersonas = 0;
    private int cantidadPrestamos = 0;

    /** Crea la biblioteca y carga los datos guardados, si el archivo existe. */
    public Biblioteca() {
        cargarDatos();
    }

    /** Registra un libro si sus datos son válidos y su código no está repetido. */
    public boolean registrarLibro(Libro libro) {
        if (libro == null || libro.getCodigo().trim().isEmpty()
                || libro.getTitulo().trim().isEmpty() || libro.getAutor().trim().isEmpty()
                || libro.getCantidadDisponible() < 0 || libro.getCodigo().contains("|")
                || libro.getTitulo().contains("|") || libro.getAutor().contains("|")) {
            System.out.println("Error: los datos del libro no son válidos.");
            return false;
        }
        if (buscarLibro(libro.getCodigo()) != null) {
            System.out.println("Error: ya existe un libro con ese código.");
            return false;
        }
        if (cantidadLibros == libros.length) {
            System.out.println("Error: no hay espacio para registrar más libros.");
            return false;
        }
        libros[cantidadLibros] = libro;
        cantidadLibros++;
        guardarDatos();
        System.out.println("Libro registrado correctamente.");
        return true;
    }

    /** Registra una persona si su documento no está repetido. */
    public boolean registrarPersona(Persona persona) {
        if (persona == null || persona.getNombre().trim().isEmpty() || persona.getDocumento() <= 0
                || persona.getNombre().contains("|") || obtenerDatoAdicional(persona).contains("|")) {
            System.out.println("Error: los datos de la persona no son válidos.");
            return false;
        }
        if (buscarPersona(persona.getDocumento()) != null) {
            System.out.println("Error: ya existe una persona con ese documento.");
            return false;
        }
        if (cantidadPersonas == personas.length) {
            System.out.println("Error: no hay espacio para registrar más personas.");
            return false;
        }
        personas[cantidadPersonas] = persona;
        cantidadPersonas++;
        guardarDatos();
        System.out.println("Persona registrada correctamente.");
        return true;
    }

    /** Busca y devuelve un libro por código, o null si no existe. */
    public Libro buscarLibro(String codigo) {
        if (codigo == null) return null;
        for (int i = 0; i < cantidadLibros; i++) {
            if (libros[i].getCodigo().equalsIgnoreCase(codigo.trim())) return libros[i];
        }
        return null;
    }

    /** Busca y devuelve una persona por documento, o null si no existe. */
    public Persona buscarPersona(int documento) {
        for (int i = 0; i < cantidadPersonas; i++) {
            if (personas[i].getDocumento() == documento) return personas[i];
        }
        return null;
    }

    /** Registra un préstamo después de validar usuario, libro y disponibilidad. */
    public boolean prestarLibro(int documento, String codigo) {
        Persona persona = buscarPersona(documento);
        Libro libro = buscarLibro(codigo);
        if (persona == null) {
            System.out.println("Error: no se encontró una persona con ese documento.");
            return false;
        }
        if (libro == null) {
            System.out.println("Error: no se encontró un libro con ese código.");
            return false;
        }
        if (!persona.puedePrestar()) {
            System.out.println("Error: la persona está suspendida o alcanzó su límite de préstamos.");
            return false;
        }
        if (!libro.estaDisponible()) {
            System.out.println("Error: no hay ejemplares disponibles de ese libro.");
            return false;
        }
        if (cantidadPrestamos == prestamos.length) {
            System.out.println("Error: no hay espacio para registrar más préstamos.");
            return false;
        }
        prestamos[cantidadPrestamos] = new Prestamo(persona, libro, java.time.LocalDate.now());
        cantidadPrestamos++;
        libro.marcarComoPrestado();
        persona.incrementarPrestamos();
        guardarDatos();
        System.out.println("Préstamo registrado. Fecha límite: "
                + prestamos[cantidadPrestamos - 1].getFechaLimiteDevolucion());
        return true;
    }

    /** Devuelve un ejemplar y aplica suspensión cuando la devolución tiene mora. */
    public boolean devolverLibro(int documento, String codigo) {
        Persona persona = buscarPersona(documento);
        Libro libro = buscarLibro(codigo);
        if (persona == null || libro == null) {
            System.out.println("Error: no se encontró la persona o el libro indicado.");
            return false;
        }
        for (int i = 0; i < cantidadPrestamos; i++) {
            Prestamo prestamo = prestamos[i];
            if (prestamo.isActivo() && prestamo.getUsuario() == persona && prestamo.getLibro() == libro) {
                int diasSuspension = prestamo.cerrarDevolucion(java.time.LocalDate.now());
                libro.marcarComoDisponible();
                persona.decrementarPrestamos();
                persona.aplicarMulta(diasSuspension);
                guardarDatos();
                System.out.println("Devolución registrada. Días de suspensión aplicados: " + diasSuspension);
                return true;
            }
        }
        System.out.println("Error: no hay un préstamo activo de ese libro para esa persona.");
        return false;
    }

    /** Muestra todos los libros registrados. */
    public void mostrarLibros() {
        if (cantidadLibros == 0) {
            System.out.println("No hay libros registrados.");
            return;
        }
        for (int i = 0; i < cantidadLibros; i++) {
            libros[i].mostrarInformacion();
            System.out.println();
        }
    }

    /** Muestra todas las personas registradas. */
    public void mostrarPersonas() {
        if (cantidadPersonas == 0) {
            System.out.println("No hay personas registradas.");
            return;
        }
        for (int i = 0; i < cantidadPersonas; i++) {
            personas[i].mostrarInformacion();
            System.out.println("Tipo: " + personas[i].getTipoUsuario());
            System.out.println();
        }
    }

    /** Muestra los préstamos que todavía están activos. */
    public void mostrarPrestamosActivos() {
        int encontrados = 0;
        for (int i = 0; i < cantidadPrestamos; i++) {
            if (prestamos[i].isActivo()) {
                prestamos[i].mostrarDetallePrestamo();
                System.out.println();
                encontrados++;
            }
        }
        if (encontrados == 0) System.out.println("No hay préstamos activos.");
    }

    /** Muestra todos los préstamos del historial, activos y devueltos. */
    public void mostrarHistorialPrestamos() {
        if (cantidadPrestamos == 0) {
            System.out.println("No hay préstamos registrados.");
            return;
        }
        for (int i = 0; i < cantidadPrestamos; i++) {
            prestamos[i].mostrarDetallePrestamo();
            System.out.println();
        }
    }

    /** Muestra los préstamos activos cuya fecha límite ya pasó. */
    public void mostrarPrestamosVencidos() {
        int encontrados = 0;
        for (int i = 0; i < cantidadPrestamos; i++) {
            if (prestamos[i].isActivo()
                    && java.time.LocalDate.now().isAfter(prestamos[i].getFechaLimiteDevolucion())) {
                prestamos[i].mostrarDetallePrestamo();
                System.out.println("Días de mora: " + prestamos[i].calcularDiasMora(java.time.LocalDate.now()));
                System.out.println();
                encontrados++;
            }
        }
        if (encontrados == 0) System.out.println("No hay préstamos vencidos.");
    }

    /** Busca una persona y muestra sus préstamos activos y suspensión. */
    public void mostrarEstadoPersona(int documento) {
        Persona persona = buscarPersona(documento);
        if (persona == null) {
            System.out.println("Error: no se encontró una persona con ese documento.");
            return;
        }
        persona.mostrarInformacion();
        System.out.println("Tipo: " + persona.getTipoUsuario());
        System.out.println("Límite de libros: " + persona.getlimiteLibros());
        System.out.println("Plazo máximo (días): " + persona.getDiasMaximosPrestamo());
    }

    /** Cuenta los ejemplares disponibles recorriendo todos los libros. */
    public int contarEjemplaresDisponibles() {
        int total = 0;
        for (int i = 0; i < cantidadLibros; i++) total += libros[i].getCantidadDisponible();
        return total;
    }

    /** Devuelve el inventario en texto para mostrarlo en una interfaz. */
    public String obtenerInventario() {
        if (cantidadLibros == 0) return "No hay libros registrados.";
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < cantidadLibros; i++) {
            Libro libro = libros[i];
            texto.append("Código: ").append(libro.getCodigo()).append("\n")
                    .append("Título: ").append(libro.getTitulo()).append("\n")
                    .append("Autor: ").append(libro.getAutor()).append("\n")
                    .append("Ejemplares disponibles: ").append(libro.getCantidadDisponible())
                    .append("\n\n");
        }
        return texto.toString();
    }

    /** Devuelve los usuarios registrados en texto para mostrar en una interfaz. */
    public String obtenerUsuarios() {
        if (cantidadPersonas == 0) return "No hay usuarios registrados.";
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < cantidadPersonas; i++) {
            Persona persona = personas[i];
            texto.append("Nombre: ").append(persona.getNombre()).append("\n")
                    .append("Documento: ").append(persona.getDocumento()).append("\n")
                    .append("Tipo: ").append(persona.getTipoUsuario()).append("\n")
                    .append("Préstamos activos: ").append(persona.getPrestamosActivos()).append("\n\n");
        }
        return texto.toString();
    }

    /** Devuelve el historial de préstamos, activos o vencidos según el filtro. */
    public String obtenerPrestamos(String filtro) {
        StringBuilder texto = new StringBuilder();
        int encontrados = 0;
        for (int i = 0; i < cantidadPrestamos; i++) {
            Prestamo prestamo = prestamos[i];
            boolean mostrar = filtro.equals("historial")
                    || (filtro.equals("activos") && prestamo.isActivo())
                    || (filtro.equals("vencidos") && prestamo.isActivo()
                    && LocalDate.now().isAfter(prestamo.getFechaLimiteDevolucion()));
            if (mostrar) {
                texto.append("Usuario: ").append(prestamo.getUsuario().getNombre())
                        .append(" (Documento: ").append(prestamo.getUsuario().getDocumento()).append(")\n")
                        .append("Libro: ").append(prestamo.getLibro().getTitulo())
                        .append(" (Código: ").append(prestamo.getLibro().getCodigo()).append(")\n")
                        .append("Fecha de préstamo: ").append(prestamo.getFechaPrestamo()).append("\n")
                        .append("Fecha límite: ").append(prestamo.getFechaLimiteDevolucion()).append("\n")
                        .append("Estado: ").append(prestamo.isActivo() ? "Activo" : "Devuelto").append("\n\n");
                encontrados++;
            }
        }
        if (encontrados == 0) return "No hay préstamos para mostrar.";
        return texto.toString();
    }

    /** Devuelve el estado de un usuario en texto para mostrarlo en una interfaz. */
    public String obtenerEstadoPersona(int documento) {
        Persona persona = buscarPersona(documento);
        if (persona == null) return "No se encontró un usuario con ese documento.";
        return "Nombre: " + persona.getNombre() + "\n"
                + "Documento: " + persona.getDocumento() + "\n"
                + "Tipo: " + persona.getTipoUsuario() + "\n"
                + "Préstamos activos: " + persona.getPrestamosActivos() + " de "
                + persona.getlimiteLibros() + "\n"
                + "Días de suspensión pendientes: " + persona.getDiasSuspension();
    }

    /** Devuelve un resumen de libros, usuarios y ejemplares disponibles. */
    public String obtenerResumen() {
        return "Libros registrados: " + cantidadLibros + "\n"
                + "Usuarios registrados: " + cantidadPersonas + "\n"
                + "Ejemplares disponibles: " + contarEjemplaresDisponibles();
    }

    /** Cuenta cuántos libros distintos hay registrados. */
    public int getCantidadLibros() {
        return cantidadLibros;
    }

    /** Cuenta cuántas personas hay registradas. */
    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    /** Devuelve un libro por su posición registrada, o null si la posición no existe. */
    public Libro obtenerLibroRegistrado(int posicion) {
        if (posicion < 0 || posicion >= cantidadLibros) return null;
        return libros[posicion];
    }

    /** Devuelve una persona por su posición registrada, o null si la posición no existe. */
    public Persona obtenerPersonaRegistrada(int posicion) {
        if (posicion < 0 || posicion >= cantidadPersonas) return null;
        return personas[posicion];
    }

    /** Devuelve un préstamo por su posición registrada, o null si la posición no existe. */
    public Prestamo obtenerPrestamoRegistrado(int posicion) {
        if (posicion < 0 || posicion >= cantidadPrestamos) return null;
        return prestamos[posicion];
    }

    /** Devuelve cuántos préstamos hay en el historial. */
    public int getCantidadPrestamos() {
        return cantidadPrestamos;
    }

    /** Guarda libros, usuarios y préstamos en filas de un archivo de texto. */
    public void guardarDatos() {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoDatos))) {
            for (int i = 0; i < cantidadLibros; i++) {
                Libro libro = libros[i];
                escritor.write("LIBRO|" + libro.getCodigo() + "|" + libro.getTitulo() + "|"
                        + libro.getAutor() + "|" + libro.getCantidadDisponible());
                escritor.newLine();
            }
            for (int i = 0; i < cantidadPersonas; i++) {
                Persona persona = personas[i];
                escritor.write("PERSONA|" + persona.getTipoUsuario() + "|" + persona.getNombre() + "|"
                        + persona.getDocumento() + "|" + obtenerDatoAdicional(persona) + "|"
                        + persona.getDiasSuspension());
                escritor.newLine();
            }
            for (int i = 0; i < cantidadPrestamos; i++) {
                Prestamo prestamo = prestamos[i];
                String fechaEntrega = prestamo.getFechaEntrega() == null
                        ? "" : prestamo.getFechaEntrega().toString();
                escritor.write("PRESTAMO|" + prestamo.getUsuario().getDocumento() + "|"
                        + prestamo.getLibro().getCodigo() + "|" + prestamo.getFechaPrestamo()
                        + "|" + fechaEntrega);
                escritor.newLine();
            }
        } catch (IOException error) {
            System.out.println("Error: no se pudieron guardar los datos en " + archivoDatos + ".");
        }
    }

    /** Lee las filas del archivo y reconstruye los libros, usuarios y préstamos. */
    private void cargarDatos() {
        File archivo = new File(archivoDatos);
        if (!archivo.exists()) return;
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String fila;
            while ((fila = lector.readLine()) != null) {
                String[] dato = fila.split("\\|", -1);
                try {
                    if (dato[0].equals("LIBRO") && dato.length == 5 && cantidadLibros < libros.length) {
                        if (buscarLibro(dato[1]) == null) {
                            libros[cantidadLibros] = new Libro(dato[1], dato[2], dato[3], Integer.parseInt(dato[4]));
                            cantidadLibros++;
                        }
                    } else if (dato[0].equals("PERSONA") && dato.length == 6
                            && cantidadPersonas < personas.length) {
                        int documento = Integer.parseInt(dato[3]);
                        if (buscarPersona(documento) == null) {
                            Persona persona = crearPersona(dato[1], dato[2], documento, dato[4]);
                            if (persona != null) {
                                persona.aplicarMulta(Integer.parseInt(dato[5]));
                                personas[cantidadPersonas] = persona;
                                cantidadPersonas++;
                            }
                        }
                    } else if (dato[0].equals("PRESTAMO") && dato.length == 5
                            && cantidadPrestamos < prestamos.length) {
                        Persona persona = buscarPersona(Integer.parseInt(dato[1]));
                        Libro libro = buscarLibro(dato[2]);
                        if (persona != null && libro != null) {
                            Prestamo prestamo = new Prestamo(persona, libro, LocalDate.parse(dato[3]));
                            if (!dato[4].isEmpty()) prestamo.cerrarDevolucion(LocalDate.parse(dato[4]));
                            else persona.incrementarPrestamos();
                            prestamos[cantidadPrestamos] = prestamo;
                            cantidadPrestamos++;
                        }
                    }
                } catch (RuntimeException error) {
                    System.out.println("Aviso: se ignoró una fila inválida del archivo de datos.");
                }
            }
        } catch (IOException error) {
            System.out.println("Error: no se pudieron cargar los datos de " + archivoDatos + ".");
        }
    }

    /** Crea la subclase de persona indicada en una fila del archivo. */
    private Persona crearPersona(String tipo, String nombre, int documento, String datoAdicional) {
        switch (tipo) {
            case "Estudiante": return new Estudiante(nombre, documento, datoAdicional);
            case "Docente": return new Docente(nombre, documento, datoAdicional);
            case "Directivo": return new Directivo(nombre, documento, datoAdicional);
            case "Investigador": return new Investigador(nombre, documento, datoAdicional);
            default: return null;
        }
    }

    /** Devuelve el dato particular de cada tipo de persona para guardarlo. */
    private String obtenerDatoAdicional(Persona persona) {
        if (persona instanceof Estudiante) return ((Estudiante) persona).getPrograma();
        if (persona instanceof Docente) return ((Docente) persona).getArea();
        if (persona instanceof Directivo) return ((Directivo) persona).getDepartamento();
        if (persona instanceof Investigador) return ((Investigador) persona).getLineaInvestigacion();
        return "";
    }
}
