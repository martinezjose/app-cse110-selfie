package database;

/**
 * Custom made exception for errors when deleting from the database.
 * Created by edwinmo on 6/1/14.
 */
public class DeleteFromDatabaseException extends Exception {
    public DeleteFromDatabaseException(){}

    public DeleteFromDatabaseException(String message){
        super(message);
    }
}
