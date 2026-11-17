/** Representa a un docente con área y reglas propias de préstamo. */
public class Docente extends Persona{
    private String area;

    /** Crea un docente con nombre, documento y área. */
    public Docente(String nombre, int documento, String area){
        super(nombre,documento);
        this.area = area;
    }

    /** Devuelve el área del docente. */
    public String getArea(){
        return this.area;
    }

    /** Devuelve el tipo de usuario. */
    @Override
    public String getTipoUsuario(){ return "Docente"; }

    /** Devuelve el máximo de libros que puede tener prestados. */
    @Override
    public int getlimiteLibros(){
        return 3;
    }

    /** Devuelve el plazo máximo de préstamo en días. */
    @Override
    public int getDiasMaximosPrestamo(){
        return 30;
    }

}
