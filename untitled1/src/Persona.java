import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Clase base con los datos y reglas comunes de las personas usuarias. */
public class Persona {
    private String nombre;
    private int documento;
    private int prestamosActivos;
    private int diasSuspension;
    private LocalDate fechaInicioSuspension;

    /** Crea una persona sin préstamos activos ni días de suspensión. */
    public Persona(String nombre, int documento) {
        this.nombre = nombre;
        this.documento = documento;
        this.prestamosActivos = 0;
        this.diasSuspension = 0;
        this.fechaInicioSuspension = null;
    }

    /** Devuelve el nombre de la persona. */
    public String getNombre() { return nombre; }

    /** Devuelve el documento de la persona. */
    public int getDocumento() { return documento; }

    /** Devuelve cuántos préstamos activos tiene. */
    public int getPrestamosActivos() { return prestamosActivos; }

    /** Devuelve los días de suspensión pendientes. */
    public int getDiasSuspension() {
        if (diasSuspension == 0 || fechaInicioSuspension == null) return 0;
        int diasTranscurridos = (int) ChronoUnit.DAYS.between(fechaInicioSuspension, LocalDate.now());
        return Math.max(0, diasSuspension - diasTranscurridos);
    }

    /** Devuelve el límite de libros permitido por el tipo de usuario. */
    public int getlimiteLibros() { return 0; }

    /** Devuelve el plazo de préstamo permitido por el tipo de usuario. */
    public int getDiasMaximosPrestamo() { return 0; }

    /** Devuelve el nombre del tipo de usuario. */
    public String getTipoUsuario() { return "Persona"; }

    /** Indica si puede realizar un préstamo según su límite y suspensión. */
    public boolean puedePrestar() {
        return getDiasSuspension() == 0 && prestamosActivos < getlimiteLibros();
    }

    /** Indica si tiene al menos un préstamo activo. */
    public boolean tienePrestamosPendientes() { return prestamosActivos > 0; }

    /** Aumenta en uno la cantidad de préstamos activos. */
    public void incrementarPrestamos() { prestamosActivos++; }

    /** Reduce en uno la cantidad de préstamos activos si es mayor que cero. */
    public void decrementarPrestamos() {
        if (prestamosActivos > 0) prestamosActivos--;
    }

    /** Agrega días de suspensión cuando la cantidad recibida es positiva. */
    public void aplicarMulta(int dias) {
        if (dias > 0) {
            diasSuspension = getDiasSuspension() + dias;
            fechaInicioSuspension = LocalDate.now();
        }
    }

    /** Imprime los datos y el estado de préstamos de la persona. */
    public void mostrarInformacion() {
        System.out.println("Nombre: " + nombre);
        System.out.println("Documento: " + documento);
        System.out.println("Préstamos activos: " + prestamosActivos);
        System.out.println("Días de suspensión pendientes: " + getDiasSuspension());
    }
}
