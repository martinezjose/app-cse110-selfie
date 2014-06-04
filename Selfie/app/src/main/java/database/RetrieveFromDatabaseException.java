package database;

/**
 * Custom made Exception for errors retrieving from the database.
 * Created by edwinmo on 5/29/14.
 */
public class RetrieveFromDatabaseException extends Exception {
    public RetrieveFromDatabaseException(){}

    public RetrieveFromDatabaseException(String message){
        super(message);
    }
}
