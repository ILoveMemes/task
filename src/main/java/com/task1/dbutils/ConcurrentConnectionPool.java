/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.task1.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
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
    private ConcurrentHashMap usedConnectionsMap = new ConcurrentHashMap<>();
    private Set usedConnections;
    private static int InitialPoolSize = 10;
    
    public ConcurrentConnectionPool(String url, String user, String password, int initialPoolSize) throws SQLException, InterruptedException {
        create(url, user, password, initialPoolSize);
    }
    
    public ConcurrentConnectionPool(String url, String user, String password) throws SQLException, InterruptedException {
        create(url, user, password, InitialPoolSize);
    }
    
    private void create(String url, String user, String password, int initialPoolSize) throws SQLException, InterruptedException {
        if (initialPoolSize > 0) {
            this.url = url;
            this.user = user;
            this.password = password;
            connectionPool = new LinkedBlockingDeque<>(initialPoolSize);
            usedConnections = usedConnectionsMap.keySet();
            for (int i = 0; i < initialPoolSize; i++) {
                addConnection();
            }
        } else {
            throw new IllegalArgumentException("size cannot be negative or zero");
        }
    }
    
    private void addConnection() throws SQLException {
        connectionPool.add(DriverManager.getConnection(this.url, this.user, this.password));
    }
    
    private void removeConnection() throws InterruptedException {
        connectionPool.take();
    }
    
    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }
    
    @Deprecated
    public void resize(int newSize) throws SQLException, InterruptedException {
        if (newSize > 0) {
            while (this.getSize() <= newSize) {
                addConnection();
            }
            while (this.getSize() > newSize) {
                removeConnection();
            }
        } else {
            throw new IllegalArgumentException("size cannot be negative or zero");
        }
    }
    
    @Override
    public Connection getConnection() {
        try {
            Connection taken = connectionPool.take();
            usedConnections.add(taken);
            return taken;
        } catch (InterruptedException ex) {
            throw new IllegalStateException("error while getting connection");
        }
    }

    @Override
    public void releaseConnection(Connection connection) {
        if (usedConnections.contains(connection)) {
            try {
                connectionPool.put(connection);
                usedConnections.remove(connection);
            } catch (InterruptedException ex) {
                throw new IllegalStateException("error while releasing connection");
            }
        } else {
            throw new IllegalArgumentException("wrong connection to return");           
        }
    }

    @Override
    public Connection getConnection(long timeout) {
        try {
            Connection taken = connectionPool.poll(timeout, TimeUnit.MILLISECONDS);
            usedConnections.add(taken);
            return taken;
        } catch (InterruptedException ex) {
            throw new IllegalStateException("error while getting connection");
        }   
    }
    
}
