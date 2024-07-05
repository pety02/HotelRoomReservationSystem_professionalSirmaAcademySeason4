package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import utils.LocalDateTimeMapDeserializer;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

/**
 * The DebitCard class represents a debit card with fields for ID, IBAN, creation and expiration dates, balance, and owner ID.
 * It supports JSON serialization/deserialization and implements Comparable for sorting.
 */
@JsonPropertyOrder({"id", "iban", "creationDate", "expirationDate", "balance", "ownerId"})
@JsonRootName("DebitCard")
public class DebitCard implements Comparable<DebitCard> {
    private static int debitCardNo = 0; // Static counter for generating unique IDs
    private int id; // Unique identifier for the debit card
    private String iban; // International Bank Account Number
    private LocalDateTime creationDate; // Date and time when the card was created
    private LocalDateTime expirationDate; // Date and time when the card will expire
    private double balance; // Current balance of the card
    private Integer ownerId; // Identifier of the card owner

    /*
     Generates a unique ID for the debit card.
     Note: This method is for learning purposes and may not ensure unique IDs in a real-world scenario.
     */
    private int generateId() {
        return ++DebitCard.debitCardNo;
    }

    /**
     * Generates a random IBAN for the debit card.
     *
     * @return A randomly generated IBAN.
     */
    private String generateIBAN() {
        StringBuilder iban = new StringBuilder("BGN");
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            iban.append(rand.nextInt(10));
        }
        return iban.toString();
    }

    /**
     * Default constructor that initializes a new debit card with default values.
     */
    public DebitCard() {
        this.setIban(this.generateIBAN());
        this.setBalance(0.0);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterFourYears = now.plusYears(4);
        this.setCreationDate(now);
        this.setExpirationDate(afterFourYears);
        this.setOwnerId(0);
        this.setId(this.generateId());
    }

    /**
     * Constructor that initializes a new debit card with the specified balance and owner ID.
     *
     * @param balance The initial balance of the debit card.
     * @param owner The ID of the card owner.
     */
    public DebitCard(double balance, Integer owner) {
        this.setIban(this.generateIBAN());
        this.setBalance(balance);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterFourYears = now.plusYears(4);
        this.setCreationDate(now);
        this.setExpirationDate(afterFourYears);
        this.setOwnerId(owner);
        this.setId(this.generateId());
    }

    /**
     * Constructor that initializes a new debit card with the specified IBAN, balance, and owner ID.
     *
     * @param iban The IBAN of the debit card.
     * @param balance The initial balance of the debit card.
     * @param owner The ID of the card owner.
     */
    public DebitCard(String iban, double balance, Integer owner) {
        this.setIban(iban);
        this.setBalance(balance);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterFourYears = now.plusYears(4);
        this.setCreationDate(now);
        this.setExpirationDate(afterFourYears);
        this.setOwnerId(owner);
        this.setId(this.generateId());
    }

    /**
     * Constructor for deserializing a DebitCard object from JSON.
     *
     * @param id The unique ID of the debit card.
     * @param iban The IBAN of the debit card.
     * @param creationDate The creation date of the debit card.
     * @param expirationDate The expiration date of the debit card.
     * @param balance The current balance of the debit card.
     * @param owner The ID of the card owner.
     */
    @JsonCreator
    public DebitCard(@JsonProperty("id") int id,
                     @JsonProperty("iban") String iban,
                     @JsonProperty("creationDate") LocalDateTime creationDate,
                     @JsonProperty("expirationDate") LocalDateTime expirationDate,
                     @JsonProperty("balance") double balance,
                     @JsonProperty("ownerId") Integer owner) {
        this.setId(id);
        this.setIban(iban);
        this.setBalance(balance);
        this.setCreationDate(creationDate);
        this.setExpirationDate(expirationDate);
        this.setOwnerId(owner);
    }

    /**
     * Gets the unique ID of the debit card.
     *
     * @return The ID of the debit card.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the debit card.
     *
     * @param id The ID to set.
     */
    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the IBAN of the debit card.
     *
     * @return The IBAN of the debit card.
     */
    public String getIban() {
        return iban;
    }

    /**
     * Sets the IBAN of the debit card.
     *
     * @param iban The IBAN to set.
     */
    @JsonSetter("iban")
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * Gets the creation date of the debit card.
     *
     * @return The creation date of the debit card.
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date of the debit card.
     *
     * @param creationDate The creation date to set.
     */
    @JsonSetter("creationDate")
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the expiration date of the debit card.
     *
     * @return The expiration date of the debit card.
     */
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the expiration date of the debit card.
     *
     * @param expirationDate The expiration date to set.
     */
    @JsonSetter("expirationDate")
    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Gets the current balance of the debit card.
     *
     * @return The balance of the debit card.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the debit card.
     *
     * @param balance The balance to set.
     */
    @JsonSetter("balance")
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Gets the owner ID of the debit card.
     *
     * @return The owner ID of the debit card.
     */
    public Integer getOwnerId() {
        return ownerId;
    }

    /**
     * Sets the owner ID of the debit card.
     *
     * @param ownerId The owner ID to set.
     */
    @JsonSetter("ownerId")
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Converts the debit card to a JSON string representation.
     *
     * @return The JSON string representation of the debit card.
     */
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Map.class, new LocalDateTimeMapDeserializer());
        mapper.registerModule(module);

        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.fillInStackTrace();
            return "{}";
        }
    }

    /**
     * Compares this debit card to another debit card for ordering.
     *
     * @param o The other debit card to compare to.
     * @return A negative integer, zero, or a positive integer as this debit card is less than, equal to, or greater than the specified debit card.
     */
    @Override
    public int compareTo(DebitCard o) {
        if (this.getId() < o.getId() &&
                this.getCreationDate().isBefore(o.getCreationDate()) &&
                this.getExpirationDate().isBefore(o.getExpirationDate()) &&
                this.getIban().compareTo(o.getIban()) < 0 &&
                this.getBalance() < o.getBalance() &&
                this.getOwnerId().compareTo(o.getOwnerId()) < 0) {
            return -1;
        } else if (this.getId() == o.getId() &&
                this.getCreationDate().equals(o.getCreationDate()) &&
                this.getExpirationDate().equals(o.getExpirationDate()) &&
                this.getIban().equals(o.getIban()) &&
                this.getBalance() == o.getBalance() &&
                this.getOwnerId().compareTo(o.getOwnerId()) == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}