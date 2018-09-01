package asdsoft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://db4free.net/sparkdatabase";

    //  Database credentials
    static final String USER = "sparkdb";
    static final String PASS = "Amey1234";
    Connection conn = null;
    Statement stmt = null;
    DataBase(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addUser(String uname, String pass){
        try {
            String sql = String.format("INSERT into user_table (uname, pass, is_admin) VALUES ('%s', '%s', %b);",uname, pass, false);
            System.out.println(sql);
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        conn.close();
    }
}
