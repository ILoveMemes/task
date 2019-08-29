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

/**
 *
 * @author ILoveMemes
 */
public class AuthorRepository {
    
    private ConnectionPool connectionPool;
    
    public AuthorRepository() {
        try {
            connectionPool = new ConcurrentConnectionPool("jdbc:postgresql:5432/task1", "task1", "7355608");
        } catch (SQLException ex) {
            //Logger.getLogger(AuthorRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            //Logger.getLogger(AuthorRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    public AuthorEntity getById(int id) throws SQLException {
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM authors WHERE id = " + id);
        AuthorEntity result = new AuthorEntity();
        result.setId(resultSet.getInt("id"));
        result.setBirthYear(resultSet.getInt("birthYear"));
        result.setFirstName(resultSet.getString("firstName"));
        result.setLastName(resultSet.getString("lastName"));
        return result;
    }
    
}
