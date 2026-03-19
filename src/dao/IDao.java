package dao;

import java.util.List;

/**
 * IDao - Generic DAO interface demonstrating ABSTRACTION.
 * All DAO classes will implement this interface.
 *
 * T  = the model type (User, Room, Booking, Payment)
 * ID = the primary key type (typically Integer)
 */
public interface IDao<T, ID> {

    /**
     * Insert a new record into the database.
     * @return true if successful, false otherwise
     */
    boolean add(T entity);

    /**
     * Retrieve a record by its primary key.
     */
    T getById(ID id);

    /**
     * Retrieve all records from the table.
     */
    List<T> getAll();

    /**
     * Update an existing record.
     * @return true if successful, false otherwise
     */
    boolean update(T entity);

    /**
     * Delete a record by primary key.
     * @return true if successful, false otherwise
     */
    boolean delete(ID id);
}
