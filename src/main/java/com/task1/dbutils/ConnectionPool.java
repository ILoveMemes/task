/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.task1.dbutils;

import java.sql.Connection;

/**
 *
 * @author ILoveMemes
 */
public interface ConnectionPool {
    Connection getConnection();
    void releaseConnection(Connection connection);
}
