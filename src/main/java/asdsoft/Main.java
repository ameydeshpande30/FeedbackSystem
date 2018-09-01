package asdsoft;
import com.google.common.collect.Lists;
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
        System.out.println("Now the output is redirected!");
        get("/", (req, res) -> (createJsonWebToken("asd",(long)3)) );
        post("/token", (req,res) -> {
            String uname = req.queryParams("uname");
            String passwd = req.queryParams("pass");
            DataBase db = new DataBase();
            db.addUser(uname,passwd);
            JsonArray array = new JsonArray();
            JsonObject person = new JsonObject();
            person.addProperty("a", 4);
            person.addProperty("b",2);
            person.addProperty("token", createJsonWebToken(uname, (long) 1));
            array.add(person);
            return array.toString();
        });
    }



}
