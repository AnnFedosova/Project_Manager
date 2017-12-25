package dbService;

/**
 * @author Evgeny Levin
 */
public class DBException extends Exception {
    public DBException(Throwable throwable) {
        super(throwable);
    }

    public DBException(String message)  {
        super(message);
    }
}
