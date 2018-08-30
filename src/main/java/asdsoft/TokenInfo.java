package asdsoft;

import com.sun.corba.se.spi.ior.ObjectId;
import org.joda.time.DateTime;
import org.omg.CORBA_2_3.portable.OutputStream;

public class TokenInfo {
    private String userId;
    private DateTime issued;
    private DateTime expires;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public DateTime getIssued() {
        return issued;
    }
    public void setIssued(DateTime issued) {
        this.issued = issued;
    }
    public DateTime getExpires() {
        return expires;
    }
    public void setExpires(DateTime expires) {
        this.expires = expires;
    }
}
