/** Representa un libro y la cantidad de ejemplares disponibles. */
public class Libro {
    private String codigo;
    private String titulo;
    private String autor;
    private int cantidadDisponible;

    /** Crea un libro con su código, título, autor y ejemplares iniciales. */
    public Libro(String codigo, String titulo, String autor, int cantidadDisponible) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.cantidadDisponible = cantidadDisponible;
    }

    /** Devuelve el código del libro. */
    public String getCodigo() { return codigo; }

    /** Devuelve el título del libro. */
    public String getTitulo() { return titulo; }

    /** Devuelve el autor del libro. */
    public String getAutor() { return autor; }

    /** Devuelve cuántos ejemplares están disponibles. */
    public int getCantidadDisponible() { return cantidadDisponible; }

    /** Indica si queda al menos un ejemplar disponible. */
    public boolean estaDisponible() { return cantidadDisponible > 0; }

    /** Suma un ejemplar disponible al registrar una devolución. */
    public void marcarComoDisponible() { cantidadDisponible++; }

    /** Resta un ejemplar disponible únicamente si hay existencias. */
    public void marcarComoPrestado() {
        if (cantidadDisponible > 0) {
            cantidadDisponible--;
        }
    }

    /** Imprime los datos principales del libro. */
    public void mostrarInformacion() {
        System.out.println("Código: " + codigo);
        System.out.println("Título: " + titulo);
        System.out.println("Autor: " + autor);
        System.out.println("Disponibles: " + cantidadDisponible);
    }
}
