package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@JsonPropertyOrder({"id", "username", "email", "password"})
@JsonRootName("User")
public class User implements Comparable<User> {
    private static int userNo = 0;
    private int id;
    private String username;
    private String email;
    private String password;
    private Map<Integer, Double> reservations;
    private Map.Entry<Integer, Double> debitCard;

    private int generateId() {
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
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    // Default constructor for Jackson
    public User() {
        this("", "", "", new ArrayList<>(), new DebitCard());
    }
    public User(String username, String email, String password) {
        this(username, email, password, new ArrayList<>(), null);
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
        Map<Integer, Double> rs = new HashMap<>();
        for(Reservation r : reservations) {
            rs.put(r.getId(), r.getTotalPrice());
        }
        this.setReservations(rs);
        if(debitCard != null) {
            this.setDebitCard(Map.entry(debitCard.getId(), debitCard.getBalance()));
        } else {
            this.setDebitCard(Map.entry(0, 0.0));
        }
        this.setId(this.generateId());
    }

    @JsonCreator
    public User(@JsonProperty("id") int id,
                @JsonProperty("username") String username,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password,
                @JsonProperty("reservations") Map<Integer, Double> reservations,
                @JsonProperty("debitCard") Map.Entry<Integer, Double> debitCard) {
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

    public User(int id,
                String username,
                String email,
                String password) {
        this.setId(id);
        this.setUsername(username);
        this.setEmail(email);
        try {
            this.setPassword(password, false);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.fillInStackTrace();
            this.password = null;
        }
        this.setReservations(new HashMap<>());
        Map.Entry<Integer, Double> dce = Map.entry(0,0.0);
        this.setDebitCard(dce);
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

    public Map<Integer, Double> getReservations() {
        return reservations;
    }

    @JsonSetter("reservations")
    public void setReservations(Map<Integer, Double> reservations) {
        this.reservations = reservations;
    }

    public Map.Entry<Integer, Double> getDebitCard() {
        return debitCard;
    }

    @JsonSetter("debitCard")
    public void setDebitCard(Map.Entry<Integer, Double> debitCard) {
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

    private static User parse(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User obj = mapper.reader().readValue(json, User.class);
        if(obj == null) {
            System.out.println("Cannot parse object!");
        }
        return obj;
    }

    public static void main(String[] args) {
        User u =  new User("petya123", "petyata@abv.bg", "petya123");
        System.out.println(u);
    }

    @Override
    public int compareTo(User o) {
        return Integer.compare(this.getId(), o.getId());
    }
}