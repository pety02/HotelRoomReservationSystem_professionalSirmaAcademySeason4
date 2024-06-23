import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@JsonPropertyOrder({"id", "username", "email", "password"})
@JsonRootName("User")
public class User {
    private static int userNo = 0;
    private String id;
    private String username;
    private String email;
    private String password;

    public static String hashPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = factory.generateSecret(spec).getEncoded();
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b)); // Change to hexadecimal string representation
        }

        return sb.toString();
    }

    // No @JsonCreator annotation here
    public User() {
        this.setUsername("");
        this.setId("User0 ");
        this.setEmail("");
        try {
            this.setPassword("", false);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.fillInStackTrace();
            this.password = null;
        }
    }

    public User(String username, String email, String password) {
        this.setUsername(username);
        this.setId(this.generateIdNo());
        this.setEmail(email);
        try {
            this.setPassword(password, false);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.fillInStackTrace();
            this.password = null;
        }
    }

    @JsonCreator
    public User(@JsonProperty("id") String id,
                @JsonProperty("username") String username,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password) {
        this.setUsername(username);
        this.setId(id);
        this.setEmail(email);
        try {
            this.setPassword(password, false);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.fillInStackTrace();
            this.password = null;
        }
    }

    public String getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(String id) {
        this.id = id;
    }

    public String generateIdNo() {
        // It is the good way only for learning purposes because
        // it may mak the program to generate not unique ids.
        return "User" + (++User.userNo) + this.username;
    }

    public String getUsername() {
        return username;
    }

    @JsonSetter("username")
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    @JsonSetter("email")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    @JsonSetter("password")
    public void setPassword(String password, boolean toBeHashed) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.password = toBeHashed ? User.hashPassword(password) : password;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.fillInStackTrace();
            return "null";
        }
    }
}