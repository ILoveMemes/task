/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.task1.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ILoveMemes
 */
public class ConcurrentConnectionPool implements ConnectionPool {
    
    private String url;
    private String user;
    private String password;
    private BlockingDeque<Connection> connectionPool;
    private CopyOnWriteArrayList<Connection> usedConnections;
    private static int InitialPoolSize = 10;
    
    private void addConnection() throws SQLException {
        connectionPool.add(DriverManager.getConnection(this.url, this.user, this.password));
    }
    
    private void removeConnection() throws InterruptedException {
        connectionPool.take();
    }
    
    public void create(String url, String user, String password, int initialPoolSize) throws SQLException, InterruptedException {
        this.url = url;
        this.user = user;
        this.password = password;
        resize(initialPoolSize);
    }
    
    public void create(String url, String user, String password) throws SQLException, InterruptedException {
        create(url, user, password, InitialPoolSize);
    }
    
    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }
    
    public void resize(int newSize) throws SQLException, InterruptedException {
        if (newSize > 0) {
            while (this.getSize() <= newSize) {
                addConnection();
            }
            while (this.getSize() > newSize) {
                removeConnection();
            }
        }
    }
    
    @Override
    public Connection getConnection() {
        try {
            Connection taken = connectionPool.take();
            usedConnections.add(taken);
            return taken;
        } catch (InterruptedException ex) {
            Logger.getLogger(ConcurrentConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void releaseConnection(Connection connection) {
        if (usedConnections.contains(connection)) {
            try {
                connectionPool.put(connection);
                usedConnections.remove(connection);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConcurrentConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new IllegalArgumentException("wrong connection to return");           
        }
    }
    
}
