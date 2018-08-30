package asdsoft;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import jdk.nashorn.internal.parser.Token;
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.JsonTokenParser;
import net.oauth.jsontoken.crypto.HmacSHA256Signer;
import net.oauth.jsontoken.crypto.HmacSHA256Verifier;
import net.oauth.jsontoken.crypto.SignatureAlgorithm;
import net.oauth.jsontoken.crypto.Verifier;
import net.oauth.jsontoken.discovery.VerifierProvider;
import net.oauth.jsontoken.discovery.VerifierProviders;
import org.joda.time.DateTime;

import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.List;

public class ApiToken {
    private static final String AUDIENCE = "EMP";

    private static final String ISSUER = "ASDSOFT";

    private static  String SIGNING_KEY = "?3v5u3=+emB8aAg^RpePmtJmu68NX@fJFztC&_u2bEq!HTRzPKbm2zJgT&%u_!hzUH&?xqCe$8=+w&&2EVse94AXF_g%G_Lh_5Rucr8+X5z_TMWrPEDA2*y=$CqK&TbukD68NH?S3!ueqt!@TxjXVE@FP2kGs=-VnSCKXv+bn$5G&qJ5PpCx2x!ZC6Ve-m9uZ$65tJcs8pQM4%CN5+Wr*dS%YLGdLf!u9JvNvUt9Rym=Cu_2TenP%9x@4ZJ9*kvZ";

    public static String createJsonWebToken(String userId, Long durationDays)    {
        //Current time and signing algorithm
        Calendar cal = Calendar.getInstance();
        HmacSHA256Signer signer;
        try {
            signer = new HmacSHA256Signer(ISSUER, "jwt", SIGNING_KEY.getBytes());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        //Configure JSON token
        JsonToken token = new net.oauth.jsontoken.JsonToken(signer);
        token.setAudience(AUDIENCE);
        token.setIssuedAt(new org.joda.time.Instant(cal.getTimeInMillis()));
        token.setExpiration(new org.joda.time.Instant(cal.getTimeInMillis() + 1000L * 60L * 60L * 24L * durationDays));

        //Configure request object, which provides information of the item
        JsonObject request = new JsonObject();
        request.addProperty("userId", userId);

        JsonObject payload = token.getPayloadAsJsonObject();
        payload.add("info", request);

        try {
            String token1 = token.serializeAndSign();

            return token1;
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Verifies a json web token's validity and extracts the user id and other information from it.
     * @param token
     * @return
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    public static TokenInfo verifyToken(String token)
    {

        try {
            final Verifier hmacVerifier = new HmacSHA256Verifier(SIGNING_KEY.getBytes());

            VerifierProvider hmacLocator = new VerifierProvider() {

                @Override
                public List<Verifier> findVerifier(String id, String key){
                    return Lists.newArrayList(hmacVerifier);
                }
            };
            VerifierProviders locators = new VerifierProviders();
            locators.setVerifierProvider(SignatureAlgorithm.HS256, hmacLocator);
            net.oauth.jsontoken.Checker checker = new net.oauth.jsontoken.Checker(){

                @Override
                public void check(JsonObject payload) throws SignatureException {
                    // don't throw - allow anything
                }

            };
            //Ignore Audience does not mean that the Signature is ignored
            JsonTokenParser parser = new JsonTokenParser(locators,
                    checker);
            JsonToken jt;
            try {
                jt = parser.verifyAndDeserialize(token);
            } catch (SignatureException e) {
                return null;
            }
            JsonObject payload = jt.getPayloadAsJsonObject();
            TokenInfo t = new TokenInfo();
            String issuer = payload.getAsJsonPrimitive("iss").getAsString();
            String userIdString =  payload.getAsJsonObject("info").getAsJsonPrimitive("userId").getAsString();
            if (issuer.equals(ISSUER) && !userIdString.isEmpty())
            {
                t.setUserId(userIdString);
                t.setIssued(new DateTime(payload.getAsJsonPrimitive("iat").getAsLong()));
                t.setExpires(new DateTime(payload.getAsJsonPrimitive("exp").getAsLong()));
                return t;
            }
            else
            {
                return null;
            }
        } catch (InvalidKeyException e1) {
            throw new RuntimeException(e1);
        }
    }

    public static Boolean checkToken(String token){
        TokenInfo t = verifyToken(token);
        try {

            if (t.getExpires().isAfter(t.getIssued())) {
                return true;
            }
        }
        catch (Exception e) {

        }
        return false;
    }
}
