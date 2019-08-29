/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.task1.repositories;

import com.task1.dbutils.ConcurrentConnectionPool;
import com.task1.dbutils.ConnectionPool;
import com.task1.entities.AuthorEntity;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import static jdk.nashorn.internal.objects.NativeFunction.function;

/**
 *
 * @author ILoveMemes
 */
public class AuthorRepository {

    private ConnectionPool connectionPool;

    public AuthorRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public AuthorEntity create() {
        //
        return new AuthorEntity();
    }

    public void update(AuthorEntity authorEntity) {
        //
    }

    public void delete(AuthorEntity authorEntity) {
        //
    }

    private void indb(Consumer<Connection> function) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            function.accept(connection);
        } finally {
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
    }

    public AuthorEntity getById(int id) throws SQLException {
        AuthorEntity result = new AuthorEntity();
        indb((connection) -> {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM authors WHERE id = " + id);
                result.setId(resultSet.getInt("id"));
                result.setBirthYear(resultSet.getInt("birthYear"));
                result.setFirstName(resultSet.getString("firstName"));
                result.setLastName(resultSet.getString("lastName"));
            } catch (SQLException ex) {
                //Logger.getLogger(AuthorRepository.class.getName()).log(Level.SEVERE, null, ex);
                // throw some shit on propeller
            }
        });
        return result;
    }

}
