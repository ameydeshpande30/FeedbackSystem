package asdsoft;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.corba.se.spi.ior.ObjectId;
import com.sun.deploy.util.StringUtils;
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.JsonTokenParser;
import net.oauth.jsontoken.crypto.HmacSHA256Signer;
import net.oauth.jsontoken.crypto.HmacSHA256Verifier;
import net.oauth.jsontoken.crypto.SignatureAlgorithm;
import net.oauth.jsontoken.crypto.Verifier;
import net.oauth.jsontoken.discovery.VerifierProvider;
import net.oauth.jsontoken.discovery.VerifierProviders;
import org.joda.time.DateTime;
import sun.rmi.runtime.Log;

import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.List;

import static asdsoft.ApiToken.createJsonWebToken;
import static asdsoft.ApiToken.checkToken;
import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;
import static spark.Spark.*;
public class Main {
    public static void main(String[] args) {
        port(8000);
        DataBase db = new DataBase();
        System.out.println("Now the output is redirected!");
        get("/", (req, res) -> {
            res.type("application/json");
            LoginData ld = db.check("root","root1");

            Gson gson = new GsonBuilder().create();
            return gson.toJson(ld);
            //System.out.println(PassHash.validatePassword("root","1000:bd417ce87a0050284e415254c0738a9fe1fbd9c543b770ab:6217d95786d75bd13df8b53502477c431804165d00a610b6"));
            //return createJsonWebToken("asd",(long)3);
        } );
        get("/profile",(req, res) -> {
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
            String uname = req.queryParams("uname");
            String passwd = req.queryParams("pass");
            System.out.println(uname);
            System.out.println(passwd);
            LoginData ld = db.check(uname,passwd);
            Gson gson = new GsonBuilder().create();
            return gson.toJson(ld);

        });
        post("/token", (req,res) -> {
            res.type("application/json");
            String uname = req.queryParams("uname");
            String passwd = req.queryParams("pass");

            db.addUser(uname,passwd);
            JsonArray array = new JsonArray();
            JsonObject person = new JsonObject();
            person.addProperty("a", 4);
            person.addProperty("b",2);
            person.addProperty("token", createJsonWebToken(uname, (long) 1));
            array.add(person);
            return array.toString();
        });
        post("/getCalls", (req,res) -> {
            String token = req.queryParams("token");
            String  asd = req.queryParams("pass");
            return "asd";
        });
    }



}
