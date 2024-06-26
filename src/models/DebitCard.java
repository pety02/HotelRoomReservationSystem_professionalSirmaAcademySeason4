package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Random;

@JsonPropertyOrder({"id", "iban", "creationDate", "expirationDate", "balance"})
@JsonRootName("DebitCard")
public class DebitCard {
    private static int debitCardNo = 0;
    private int id;
    private String iban;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
    private double balance;
    @JsonIgnore
    private User owner;

    private int generateId() {
        // It is the good way only for learning purposes because
        // it may mak the program to generate not unique ids.
        return ++DebitCard.debitCardNo;
    }

    private String generateIBAN() {
        StringBuilder iban = new StringBuilder("BGN");
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            iban.append(rand.nextInt(10));
        }

        return iban.toString();
    }

    public DebitCard() {
        this.setIban(this.generateIBAN());
        this.setBalance(0.0);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterFourYears = LocalDateTime.of(
                now.getYear() + 4, now.getMonth(), now.getDayOfMonth(),
                now.getHour(), now.getMinute(), now.getSecond());
        this.setCreationDate(now);
        this.setExpirationDate(afterFourYears);
        this.setOwner(null);
        this.setId(this.generateId());
    }

    public DebitCard(String iban, double balance, User owner) {
        this.setIban(iban);
        this.setBalance(balance);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterFourYears = LocalDateTime.of(
                now.getYear() + 4, now.getMonth(), now.getDayOfMonth(),
                now.getHour(), now.getMinute(), now.getSecond());
        this.setCreationDate(now);
        this.setExpirationDate(afterFourYears);
        this.setOwner(owner);
        this.setId(this.generateId());
    }

    @JsonCreator
    public DebitCard(@JsonProperty("id") int id,
                     @JsonProperty("iban") String iban,
                     @JsonProperty("creationDate") LocalDateTime creationDate,
                     @JsonProperty("expirationDate") LocalDateTime expirationDate,
                     @JsonProperty("balance") double balance,
                     User owner) {
        this.setId(id);
        this.setIban(iban);
        this.setBalance(balance);
        this.setCreationDate(creationDate);
        this.setExpirationDate(expirationDate);
        this.setOwner(owner);
    }

    public int getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    @JsonSetter("iban")
    public void setIban(String iban) {
        this.iban = iban;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @JsonSetter("creationDate")
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    @JsonSetter("expirationDate")
    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public double getBalance() {
        return balance;
    }

    @JsonSetter("balance")
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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