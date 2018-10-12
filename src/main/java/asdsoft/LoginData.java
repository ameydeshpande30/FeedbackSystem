package asdsoft;

public class LoginData {
    String username;
    String first_name;
    String last_name;
    String contact;
    String email;
    String token;
    int rating;

    LoginData(){
        this.username = "asd";
        this.first_name = "Amey";
        this.last_name = "Deshpande";
        this.contact = "917588758032";
        this.email = "asd@gmail.com";
        this.rating = 5;

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
