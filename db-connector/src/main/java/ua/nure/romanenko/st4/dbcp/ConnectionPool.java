package ua.nure.romanenko.st4.dbcp;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

class ConnectionPool {

    private final static ConnectionPool instance = new ConnectionPool();

    private BasicDataSource connectionPool = new BasicDataSource();

    private ConnectionPool() {
        ResourceBundle rb = ResourceBundle.getBundle("db");

        connectionPool.setDriverClassName(rb.getString("db.driver"));
        connectionPool.setUrl(rb.getString("db.url"));
        connectionPool.setUsername(rb.getString("db.name"));
        connectionPool.setPassword(rb.getString("db.password"));
        connectionPool.setInitialSize(Integer.parseInt(rb.getString("db.count.pools")));
    }

    public static Connection getConnection() throws SQLException {
        return instance.connectionPool.getConnection();
    }

}