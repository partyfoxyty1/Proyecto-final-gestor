/** Representa a un directivo con departamento y reglas de préstamo. */
public class Directivo extends Persona{
    private String departamento;

    /** Crea un directivo con nombre, documento y departamento. */
    public Directivo(String nombre, int documento, String departamento){
        super(nombre,documento);
        this.departamento = departamento;
    }

    /** Devuelve el departamento del directivo. */
    public String getDepartamento(){
        return this.departamento;
    }

    /** Devuelve el tipo de usuario. */
    @Override
    public String getTipoUsuario(){ return "Directivo"; }

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
