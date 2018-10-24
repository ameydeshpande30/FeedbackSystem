package asdsoft;

import com.mysql.cj.protocol.Resultset;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;

import static asdsoft.ApiToken.createJsonWebToken;

public class DataBase {

    // JDBC driver name and database URL
   // static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
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
                ld.setToken(createJsonWebToken(rs.getString("ID"),(long)1));
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
    public ArrayList<SyncData> dayFetch(){
        ArrayList<SyncData> syncDataArrayList;
        ArrayList<Integer> idList = new ArrayList<>();
        syncDataArrayList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM records WHERE callisdone = 0 ORDER BY priority DESC LIMIT 15 ";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                SyncData syncData = new SyncData();
                syncData.setCust_id(rs.getString("cust_id"));
                syncData.setFirst_name(rs.getString("first_name"));
                syncData.setLast_name(rs.getString("last_name"));
                syncData.setPhone("0" + rs.getString("pnumber").trim());
                syncData.setVehical_number(rs.getString("vehicle_number"));
                syncData.setModel(rs.getString("model"));
                syncData.setService_id(rs.getString("service_id"));
                syncData.setL_service(rs.getString("date_of_service"));
                syncData.setN_service(rs.getString("next_date_of_service"));
                syncData.setKms(rs.getInt("kms"));
                syncDataArrayList.add(syncData);
                idList.add(rs.getInt("service_id"));
            }
            updateData(idList);
            return syncDataArrayList;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return syncDataArrayList;
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
    public void addUser2(String fname, String lname, String phone , String dob, int sal, String email,String uname, String pass){
        try {
            String sql = String.format("INSERT INTO `employee`(`username`, `password`, `first_name`, `last_name`, `dob`, `salary`, `contact`, `is_admin`, `rating`, `email`) VALUES ('%s', '%s' , '%s', '%s', '%s', '%s', '%s', %d, %d, '%s')", uname,
                    PassHash.createHash(pass), fname, lname, dob, sal, phone, 0, 5, email);
            System.out.println(sql);
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void submitData(String time, String Date, int a1, int a2, int a3, int a4, int a5, int a6, int cust_rat, int cust_id, int service_id, int emp_id ){
        try {
            String sql = String.format("INSERT INTO submit(cust_id, emp_id, service_id, a1, a2, a3, a4, a5, a6, cust_rating, dateofcall, time) VALUES " +
                    "(%d, %d , %d, %d, %d, %d, %d, %d, %d, %d, '%s', '%s')",cust_id ,emp_id , service_id, a1, a2, a3, a4, a5, a6, cust_rat, Date, time);
            System.out.println(sql);
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            updatesddata(service_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateData(ArrayList<Integer> idList){
        String out = "( ";
        for(int i : idList){
            out += String.valueOf(i) + ", ";

        }
        out += String.valueOf(idList.get(0)) + " )";

        String sql = String.format("UPDATE service SET callisdone = 2 where service_id in %s", out);
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updatesddata(int service_id){


        String sql = String.format("UPDATE service SET callisdone = 1 where service_id = %d", service_id);
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int a1(){

        int a = 0;
        String sql = String.format("select avg(a1) from submit;");
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                a = (int) rs.getFloat("avg(a1)");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
    public int a3(){

        int a = 0;
        String sql = String.format("select avg(a3) from submit;");
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                a = (int) rs.getFloat("avg(a3)");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
    public int a5(){

        int a = 0;
        String sql = String.format("select avg(a5) from submit;");
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                a = (int) rs.getFloat("avg(a5)");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
    public int a2(){

        int a = 0;
        String sql = String.format("select count(a2) from submit where a2=1;");
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                a = (int) rs.getInt("count(a2)");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
    public int a6(){

        int a = 0;
        String sql = String.format("select count(a6) from submit where a6=1;");
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                a = (int) rs.getInt("count(a6)");
                System.out.println(a);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(a);
        return a;
    }
    public int a4(){

        int a = 0;
        String sql = String.format("select count(a4) from submit where a4=1;");
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                a = (int) rs.getInt("count(a4)");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
    public int count(){

        int a = 0;
        String sql = String.format("select count(service_id) from submit;");
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                a = (int) rs.getInt("count(service_id)");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }

        @Override
    protected void finalize() throws Throwable {
        conn.close();
    }
    public boolean Check(){
        return true;
    }
}
