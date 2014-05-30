package database;

/**
 * Custom-created Exception. Very basic!!!
 * Created by edwinmo on 5/27/14.
 */
public class InsertToDatabaseException extends Exception {
    public InsertToDatabaseException(){}

    public InsertToDatabaseException(String message){
        super(message);
    }
}
