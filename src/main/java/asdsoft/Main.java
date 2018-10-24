package asdsoft;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;

import static asdsoft.ApiToken.createJsonWebToken;
import static asdsoft.ApiToken.getUserId;
import static asdsoft.ApiToken.checkToken;

import static spark.Spark.*;
public class Main {
    public static void main(String[] args) {
        port(8000);


//        String keyStoreLocation = "/home/amey/IdeaProjects/feedback/src/keystore.jks";
//        String keyStorePassword = "password";
//        secure(keyStoreLocation, keyStorePassword, null, null);

        staticFiles.location("/static");
        DataBase db = new DataBase();
        System.out.println("Now the output is redirected!");

        get("/test",(req, res) -> {
            res.type("application/json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject out = new JsonObject();
            out.addProperty("id", 1);
            out.addProperty("fname", "Amey");
            out.addProperty("lname", "Deshpande");
            out.addProperty("vno", "MH09-AS-1234");
            out.addProperty("phone","+91-7588758032");
            out.addProperty("service_id", 2);
            out.addProperty("D_Service", "2018-05-20");
            out.addProperty("N_Service","2018-05-20");
            out.addProperty("kms",3000);
            return gson.toJson(out);
        });
        post("/login", (req,res) -> {
            res.type("application/json");
            String uname = req.queryParams("uname");
            String passwd = req.queryParams("pass");
            LoginData ld = db.check(uname,passwd);
            Gson gson = new GsonBuilder().create();
            return gson.toJson(ld);

        });
        get("/token", (req,res) -> {
            res.type("application/json");
//            String uname = req.queryParams("uname");
//            String passwd = req.queryParams("pass");
            db.addUser("admin","admin");
            JsonArray array = new JsonArray();
            JsonObject person = new JsonObject();
            person.addProperty("a", 4);
            person.addProperty("b",2);
            person.addProperty("token", createJsonWebToken("admin", (long) 1));
            array.add(person);
            return array.toString();
        });

        post("/verify", (req,res) -> {
            Boolean m;
            String token = req.queryParams("token");
            try {
                m = checkToken(token);
            }
            catch (Exception e){
                m = false;
            }
            return String.valueOf(m);
        });
        post("/dayFetch", (req,res) -> {
            String token = req.queryParams("token");
            res.type("application/json");
            Boolean m;
            try {
                m = checkToken(token);
            }
            catch (Exception e){
                m = false;
            }
            ArrayList<SyncData> arrayList = db.dayFetch();
            JsonArray jsonArray = new JsonArray();
            if(m){

            for (SyncData syncData : arrayList){
                Gson gson = new GsonBuilder().create();
                jsonArray.add(gson.toJsonTree(syncData));
            }}
            return jsonArray.toString();
        });
        post("/submitData",(req,res) -> {
            String token = req.queryParams("token");
            res.type("application/json");
            Boolean m;
            try {
                m = checkToken(token);
            }
            catch (Exception e){
                m = false;
            }
            if(m){
                Integer userid = getUserId(token);
                Integer cust_id = Integer.valueOf(req.queryParams("cust_id"));
                Integer service_id = Integer.valueOf(req.queryParams("service_id"));
                int a1 = Integer.valueOf(req.queryParams("a1"));
                int a2 = Integer.valueOf(req.queryParams("a2"));
                int a3 = Integer.valueOf(req.queryParams("a3"));
                int a4 = Integer.valueOf(req.queryParams("a4"));
                int a5 = Integer.valueOf(req.queryParams("a5"));
                int a6 = Integer.valueOf(req.queryParams("a6"));
                String date = req.queryParams("date");
                String time = req.queryParams("time");
                int cust_rat = Integer.valueOf(req.queryParams("cust_rat"));
                db.submitData(time,date,a1,a2,a3,a4,a5,a6,cust_rat,cust_id,service_id,userid);
                //System.out.println(time + date + a1 + a2 + a3 +a4 +a5 +a6 + " " +cust_id + " " + cust_rat + " " + service_id + " " + userid);

            }
            return "ok";
        });
        post("/adminSubmit",(req,res) -> {
            String fname = req.queryParams("fname");
            String lname = req.queryParams("lname");
            String uname = req.queryParams("user");
            String pass = req.queryParams("password");
            String dob = req.queryParams("dob");
            int salary = Integer.parseInt(req.queryParams("sal"));
            String email = req.queryParams("email");
            String phone = req.queryParams("no");
            db.addUser2(fname,lname,phone,dob,salary,email,uname,pass);
            return "done";
        });
        get("/newUser",(req,res) -> {
            return new ModelAndView(null, "login.mustache"); // hello.mustache file is in resources/templates directory
        }, new MustacheTemplateEngine());
//        Map map = new HashMap();
//        map.put("name", "Sam");
//
//        // hello.mustache file is in resources/templates directory
//        get("/hello", (rq, rs) -> new ModelAndView(map, "login.mustache"), new MustacheTemplateEngine());
//
        get("/bi2",(req,res) -> {
            int count = db.count();
            int a1 = db.a1()*20;
            int a2= (db.a2())*100;
            a2 = a2/count;
            int a3 = db.a3()*20;
            int a4 = (db.a4())*100;
            a4 = a4/count;
            int a5 = db.a5()*20;
            int a6 = (db.a6());
            a6 = a6*100;
            a6 = a6/count;
            System.out.println(a6);
            Map map = new HashMap();
            map.put("a1", a1);
            map.put("a2", a2);
            map.put("a3", a3);
            map.put("a4", a4);
            map.put("a5", a5);
            map.put("a6", a6);
            return new ModelAndView(map, "bargraph.mustache"); // hello.mustache file is in resources/templates directory
        }, new MustacheTemplateEngine());


        get("/bi",(req,res) -> {
            int count = db.count();
            int a1 = db.a1()*20;
            int a2= (db.a2())*100;
            a2 = a2/count;
            int a3 = db.a3()*20;
            int a4 = (db.a4())*100;
            a4 = a4/count;
            int a5 = db.a5()*20;
            int a6 = (db.a6());
            a6 = a6*100;
            a6 = a6/count;
            System.out.println(a6);
            Map map = new HashMap();
            map.put("a1", a1);
            map.put("a2", a2);
            map.put("a3", a3);
            map.put("a4", a4);
            map.put("a5", a5);
            map.put("a6", a6);
            return new ModelAndView(map, "Statistics.mustache"); // hello.mustache file is in resources/templates directory
        }, new MustacheTemplateEngine());

    }



}
