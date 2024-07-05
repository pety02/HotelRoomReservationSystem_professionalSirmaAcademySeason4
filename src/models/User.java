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

/**
 *
 */
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

    /**
     *
     * @param password
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
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

    /**
     *
     */
    public User() {
        this("", "", "", new ArrayList<>(), new DebitCard());
    }

    /**
     *
     * @param username
     * @param email
     * @param password
     */
    public User(String username, String email, String password) {
        this(username, email, password, new ArrayList<>(), null);
    }

    /**
     *
     * @param username
     * @param email
     * @param password
     * @param reservations
     * @param debitCard
     */
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

    /**
     *
     * @param id
     * @param username
     * @param email
     * @param password
     * @param reservations
     * @param debitCard
     */
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

    /**
     *
     * @param id
     * @param username
     * @param email
     * @param password
     */
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

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    @JsonSetter("username")
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    @JsonSetter("email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * @param toBeHashed
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    @JsonSetter("password")
    public void setPassword(String password, boolean toBeHashed) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.password = toBeHashed ? User.hashPassword(password) : password;
    }

    /**
     *
     * @return
     */
    public Map<Integer, Double> getReservations() {
        return reservations;
    }

    /**
     *
     * @param reservations
     */
    @JsonSetter("reservations")
    public void setReservations(Map<Integer, Double> reservations) {
        this.reservations = reservations;
    }

    /**
     *
     * @return
     */
    public Map.Entry<Integer, Double> getDebitCard() {
        return debitCard;
    }

    /**
     *
     * @param debitCard
     */
    @JsonSetter("debitCard")
    public void setDebitCard(Map.Entry<Integer, Double> debitCard) {
        this.debitCard = debitCard;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.fillInStackTrace();
            return "{}";
        }
    }

    /**
     *
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(User o) {
        int firstCondition = 0, secondCondition = 0;
        if(this.getId() < o.getId() && this.getUsername().compareTo(o.getUsername()) < 0
            && this.getEmail().compareTo(o.getEmail()) < 0 && this.getPassword().compareTo(o.getPassword()) < 0
            && this.getDebitCard().getKey().compareTo(o.getDebitCard().getKey()) < 0
            && this.getDebitCard().getValue().compareTo(o.getDebitCard().getValue()) < 0) {
            firstCondition = -1;
        } else if (this.getId() == o.getId() && this.getUsername().compareTo(o.getUsername()) == 0
                && this.getEmail().compareTo(o.getEmail()) == 0 && this.getPassword().compareTo(o.getPassword()) == 0
                && this.getDebitCard().getKey().compareTo(o.getDebitCard().getKey()) == 0
                && this.getDebitCard().getValue().compareTo(o.getDebitCard().getValue()) == 0) {
            firstCondition = 0;
        } else {
            firstCondition = 1;
        }

        for(Map.Entry<Integer, Double> currReservationId : this.getReservations().entrySet()) {
            for(Map.Entry<Integer, Double> otherReservationId : o.getReservations().entrySet()) {
                if (currReservationId.getKey().compareTo(otherReservationId.getKey()) < 0
                    && currReservationId.getValue().compareTo(otherReservationId.getValue()) < 0) {
                    secondCondition = -1;
                } else if (currReservationId.getKey().compareTo(otherReservationId.getKey()) == 0
                        && currReservationId.getValue().compareTo(otherReservationId.getValue()) == 0) {
                    secondCondition = 0;
                } else {
                    secondCondition = 1;
                }
            }
        }

        if(firstCondition < 0 || secondCondition < 0) {
            return -1;
        } else if (firstCondition == 0 && secondCondition == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}