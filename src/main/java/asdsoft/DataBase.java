package asdsoft;

import com.mysql.cj.protocol.Resultset;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;

import static asdsoft.ApiToken.createJsonWebToken;

public class DataBase {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://db4free.net/sdl_project";

    //  Database credentials
    static final String USER = "iamroot";
    static final String PASS = "sdl_project";
    static Connection conn = null;
    Statement stmt = null;
    DataBase(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public LoginData check(String uname, String pass){
        LoginData ld = new LoginData();
        try {
            String Sql = String.format("SELECT * FROM `employee` WHERE username = '%s' ;", uname);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(Sql);

            while (rs.next()){
            if(PassHash.validatePassword(pass,rs.getString("password"))){
                ld.setUsername(rs.getString("username"));
                ld.setFirst_name(rs.getString("first_name"));
                ld.setLast_name(rs.getString("last_name"));
                ld.setContact(rs.getString("contact"));
                ld.setEmail(rs.getString("email"));
                ld.setRating(rs.getInt("rating"));
                ld.setToken(createJsonWebToken(ld.getUsername(),(long)3));
                return ld;
            }
            ld.setToken("-1");
                return ld;}

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        ld.setToken("-2");
        return ld;
    }
    public void addUser(String uname, String pass){
        try {
            String sql = String.format("INSERT INTO `employee`(`username`, `password`, `first_name`, `last_name`, `dob`, `salary`, `contact`, `is_admin`, `rating`) VALUES ('%s', '%s' , '%s', '%s', '%s', '%s', '%s', %d, %d)", uname,
                    PassHash.createHash(pass), "Amey", "Deshpande", "1998-04-30", 50000, "917588758032", 1, 5);
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
    public boolean Check(){
        return true;
    }
}
