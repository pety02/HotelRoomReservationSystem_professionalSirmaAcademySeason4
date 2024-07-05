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
import java.util.HashMap;
import java.util.Map;

/**
 * The User class represents a user entity with attributes such as ID, username, email, hashed password,
 * reservations, and debit card information.
 */
@JsonPropertyOrder({"id", "username", "email", "password", "reservations", "debitCard"})
@JsonRootName("User")
public class User implements Comparable<User> {
    private static int userNo = 0; // Static counter for generating unique user IDs
    private int id; // Unique identifier for the user
    private String username; // User's username
    private String email; // User's email address
    private String password; // User's hashed password
    private Map<Integer, Double> reservations; // User's reservations mapped by reservation ID and total price
    private Map.Entry<Integer, Double> debitCard; // User's debit card information

    /**
     * Generates a unique ID for each user instance.
     *
     * @return The generated unique ID.
     */
    private int generateId() {
        return ++User.userNo;
    }

    /**
     * Static method to hash a given password using PBKDF2 algorithm with HMAC SHA-1.
     *
     * @param password The password to hash.
     * @return The hashed password as a hexadecimal string.
     * @throws InvalidKeySpecException If there's an issue with the provided key specification.
     * @throws NoSuchAlgorithmException If the requested cryptographic algorithm is not available.
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
     * Default constructor initializes a user with empty/default values.
     */
    public User() {
        this("", "", "", new ArrayList<>(), new DebitCard());
    }

    /**
     * Constructor initializes a user with basic credentials and default values for reservations and debit card.
     *
     * @param username The user's username.
     * @param email    The user's email address.
     * @param password The user's password (plaintext).
     */
    public User(String username, String email, String password) {
        this(username, email, password, new ArrayList<>(), null);
    }

    /**
     * Constructor initializes a user with all provided attributes.
     *
     * @param username     The user's username.
     * @param email        The user's email address.
     * @param password     The user's password (plaintext).
     * @param reservations The user's reservations.
     * @param debitCard    The user's debit card information.
     */
    public User(String username, String email, String password, ArrayList<Reservation> reservations, DebitCard debitCard) {
        this.setUsername(username);
        this.setEmail(email);
        try {
            this.setPassword(password, false); // Hash the password if needed
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.fillInStackTrace();
            this.password = null; // In case of hashing failure
        }
        // Convert reservations to a map of ID to total price
        Map<Integer, Double> rs = new HashMap<>();
        for (Reservation r : reservations) {
            rs.put(r.getId(), r.getTotalPrice());
        }
        this.setReservations(rs);
        // Set debit card entry if provided, otherwise default to zero values
        if (debitCard != null) {
            this.setDebitCard(Map.entry(debitCard.getId(), debitCard.getBalance()));
        } else {
            this.setDebitCard(Map.entry(0, 0.0));
        }
        this.setId(this.generateId()); // Generate a unique ID
    }

    /**
     * Constructor for JSON deserialization.
     *
     * @param id           The user's ID.
     * @param username     The user's username.
     * @param email        The user's email address.
     * @param password     The user's password (hashed).
     * @param reservations The user's reservations.
     * @param debitCard    The user's debit card information.
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
            this.setPassword(password, false); // Hash the password if needed
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.fillInStackTrace();
            this.password = null; // In case of hashing failure
        }
        this.setReservations(reservations); // Set reservations
        this.setDebitCard(debitCard); // Set debit card information
    }

    /**
     * Constructor initializes a user with specific attributes including a hashed password.
     *
     * @param id       The user's ID.
     * @param username The user's username.
     * @param email    The user's email address.
     * @param password The user's password (plaintext).
     */
    public User(int id,
                String username,
                String email,
                String password) {
        this.setId(id);
        this.setUsername(username);
        this.setEmail(email);
        try {
            this.setPassword(password, false); // Hash the password if needed
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.fillInStackTrace();
            this.password = null; // In case of hashing failure
        }
        this.setReservations(new HashMap<>()); // Initialize reservations
        // Initialize debit card entry with default values
        Map.Entry<Integer, Double> dce = Map.entry(0, 0.0);
        this.setDebitCard(dce);
    }

    // Getters and Setters

    /**
     * Retrieves the user's ID.
     *
     * @return The user's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user's ID.
     *
     * @param id The user's ID.
     */
    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the user's username.
     *
     * @return The user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username.
     *
     * @param username The user's username.
     */
    @JsonSetter("username")
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the user's email address.
     *
     * @return The user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email The user's email address.
     */
    @JsonSetter("email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the user's hashed password.
     *
     * @return The user's hashed password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password, optionally hashing it.
     *
     * @param password   The user's password (plaintext or hashed).
     * @param toBeHashed Flag indicating if the password should be hashed.
     * @throws InvalidKeySpecException If there's an issue with the provided key specification.
     * @throws NoSuchAlgorithmException If the requested cryptographic algorithm is not available.
     */
    @JsonSetter("password")
    public void setPassword(String password, boolean toBeHashed) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.password = toBeHashed ? User.hashPassword(password) : password;
    }

    /**
     * Retrieves the user's reservations.
     *
     * @return The user's reservations.
     */
    public Map<Integer, Double> getReservations() {
        return reservations;
    }

    /**
     * Sets the user's reservations.
     *
     * @param reservations The user's reservations.
     */
    @JsonSetter("reservations")
    public void setReservations(Map<Integer, Double> reservations) {
        this.reservations = reservations;
    }

    /**
     * Retrieves the user's debit card information.
     *
     * @return The user's debit card information.
     */
    public Map.Entry<Integer, Double> getDebitCard() {
        return debitCard;
    }

    /**
     * Sets the user's debit card information.
     *
     * @param debitCard The user's debit card information.
     */
    @JsonSetter("debitCard")
    public void setDebitCard(Map.Entry<Integer, Double> debitCard) {
        this.debitCard = debitCard;
    }

    // Other Methods

    /**
     * Returns a JSON representation of the user object.
     *
     * @return A JSON string representing the user object.
     */
    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.fillInStackTrace();
            return "{}"; // Return empty object if serialization fails
        }
    }

    /**
     * Compares this user object with another user object based on their attributes.
     *
     * @param o The user object to compare.
     * @return An integer representing the comparison result (-1, 0, or 1).
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