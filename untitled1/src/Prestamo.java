import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Relaciona una persona y un libro durante un préstamo. */
public class Prestamo {
    private Persona usuario;
    private Libro libro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaEntrega;
    private LocalDate fechaLimiteDevolucion;
    private boolean activo;

    /** Crea un préstamo y calcula su fecha límite según el tipo de usuario. */
    public Prestamo(Persona usuario, Libro libro, LocalDate fechaPrestamo) {
        this.usuario = usuario;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimiteDevolucion = fechaPrestamo.plusDays(usuario.getDiasMaximosPrestamo());
        this.fechaEntrega = null;
        this.activo = true;
    }

    /** Devuelve la persona asociada al préstamo. */
    public Persona getUsuario() { return usuario; }

    /** Devuelve el libro asociado al préstamo. */
    public Libro getLibro() { return libro; }

    /** Devuelve la fecha en que se realizó el préstamo. */
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }

    /** Devuelve la fecha límite de devolución. */
    public LocalDate getFechaLimiteDevolucion() { return fechaLimiteDevolucion; }

    /** Devuelve la fecha de entrega o null si sigue activo. */
    public LocalDate getFechaEntrega() { return fechaEntrega; }

    /** Indica si el préstamo todavía está activo. */
    public boolean isActivo() { return activo; }

    /** Calcula los días de retraso para una fecha de entrega. */
    public int calcularDiasMora(LocalDate fechaEntrega) {
        if (fechaEntrega.isAfter(this.fechaLimiteDevolucion)) {
            return (int) ChronoUnit.DAYS.between(this.fechaLimiteDevolucion, fechaEntrega);
        }
        return 0;
    }

    /** Registra la devolución y devuelve dos días de suspensión por cada día de mora. */
    public int cerrarDevolucion(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
        int diasMora = calcularDiasMora(fechaEntrega);
        this.activo = false;
        return diasMora * 2;
    }

    /** Imprime el detalle y estado actual del préstamo. */
    public void mostrarDetallePrestamo() {
        System.out.println("Usuario: " + usuario.getNombre() + " (Doc: " + usuario.getDocumento() + ")");
        System.out.println("Libro: " + libro.getTitulo() + " (Cód: " + libro.getCodigo() + ")");
        System.out.println("Fecha préstamo: " + fechaPrestamo);
        System.out.println("Fecha límite: " + fechaLimiteDevolucion);
        if (activo) System.out.println("Estado: Activo (Pendiente de entrega)");
        else System.out.println("Estado: Devuelto el " + fechaEntrega);
    }
}
