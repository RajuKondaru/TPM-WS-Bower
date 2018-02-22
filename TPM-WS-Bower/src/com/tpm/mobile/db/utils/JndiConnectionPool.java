package com.tpm.mobile.db.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;


import org.apache.commons.dbcp.BasicDataSource;

public class JndiConnectionPool {
    private static BasicDataSource dataSource;

    static {
        try {
            dataSource = (BasicDataSource) new InitialContext().lookup("jndifordbconc");
        }
        catch (NamingException e) { 
            throw new ExceptionInInitializerError("'jndifordbconc' not found in JNDI"+ e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}