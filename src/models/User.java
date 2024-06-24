package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;

@JsonPropertyOrder({"id", "username", "email", "password", "reservations", "debitCard"})
@JsonRootName("models.User")
public class User {
    private static int userNo = 0;
    private int id;
    private String username;
    private String email;
    private String password;
    private ArrayList<Reservation> reservations;
    private DebitCard debitCard;

    private int generateId() {
        // It is the good way only for learning purposes because
        // it may mak the program to generate not unique ids.
        return ++User.userNo;
    }

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

    public User() {
        this.setUsername("");
        this.setEmail("");
        try {
            this.setPassword("", false);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.fillInStackTrace();
            this.password = null;
        }
        this.setReservations(new ArrayList<>());
        this.setDebitCard(new DebitCard());
        this.setId(this.generateId());
    }

    public User(String username, String email, String password, ArrayList<Reservation> reservations, DebitCard debitCard) {
        this.setUsername(username);
        this.setEmail(email);
        try {
            this.setPassword(password, false);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.fillInStackTrace();
            this.password = null;
        }
        this.setReservations(reservations);
        this.setDebitCard(debitCard);
        this.setId(this.generateId());
    }

    @JsonCreator
    public User(@JsonProperty("id") int id,
                @JsonProperty("username") String username,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password,
                @JsonProperty("reservations") ArrayList<Reservation> reservations,
                @JsonProperty("debitCard") DebitCard debitCard) {
        this.setId(id);
        this.setUsername(username);
        this.setEmail(email);
        try {
            this.setPassword(password, false);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.fillInStackTrace();
            this.password = null;
        }
        this.setReservations(reservations);
        this.setDebitCard(debitCard);
    }

    public int getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
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

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    @JsonSetter("reservations")
    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }

    @JsonSetter("debitCard")
    public void setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.fillInStackTrace();
            return "{}";
        }
    }
}