package bull.exception;

/**
 * Class extending the RuntimeException class of java allows to handle the exception
 * by including the criticality level.
 * @author Bull/Atos
 */
public class MyRuntimeException extends RuntimeException {
    private final CriticityLevelEnum criticity;

    /**
     * Build an object to handle the exception
     * @param criticity Enum defined beforehand, it is the criticality level
     * @param message The error message to display
     * @param e The exception
     */
    public MyRuntimeException(CriticityLevelEnum criticity, String message, Exception e){
        super(message, e);
        this.criticity = criticity;
    }

    /**
     * @return Criticality level
     */
    public CriticityLevelEnum getCriticity() {
        return criticity;
    }
}
