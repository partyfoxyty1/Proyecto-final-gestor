/** Representa a un investigador con línea de investigación propia. */
public class Investigador extends Persona{
    private String lineaInvestigacion;

    /** Crea un investigador con nombre, documento y línea de investigación. */
    public Investigador(String nombre, int documento, String lineaInvestigacion){
        super(nombre, documento);
        this.lineaInvestigacion = lineaInvestigacion;
    }
    /** Devuelve la línea de investigación. */
    public String getLineaInvestigacion(){
        return this.lineaInvestigacion;
    }

    /** Devuelve el tipo de usuario. */
    @Override
    public String getTipoUsuario(){ return "Investigador"; }

    /** Devuelve el máximo de libros que puede tener prestados. */
    @Override
    public int getlimiteLibros(){
        return 5;
    }

    /** Devuelve el plazo máximo de préstamo en días. */
    @Override
    public int getDiasMaximosPrestamo(){
        return 30;
    }

}
