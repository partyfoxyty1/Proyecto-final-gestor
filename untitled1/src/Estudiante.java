/** Representa a un estudiante con programa y reglas propias de préstamo. */
public class Estudiante extends Persona{
    private String programa;

    /** Crea un estudiante con nombre, documento y programa académico. */
    public Estudiante(String nombre, int documento, String programa){
        super(nombre,documento);
        this.programa = programa;
    }

    /** Devuelve el programa académico del estudiante. */
    public String getPrograma(){
        return this.programa;
    }

    /** Devuelve el tipo de usuario. */
    @Override
    public String getTipoUsuario(){ return "Estudiante"; }

    /** Devuelve el máximo de libros que puede tener prestados. */
    @Override
    public int getlimiteLibros(){
        return 1;
    }

    /** Devuelve el plazo máximo de préstamo en días. */
    @Override
    public int getDiasMaximosPrestamo(){
        return 10;
    }
}
