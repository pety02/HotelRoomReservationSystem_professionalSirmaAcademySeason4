package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import readersWriters.DebitCardReaderWriter;
import utils.LocalDateTimeMapDeserializer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 *
 */
@JsonPropertyOrder({"id", "iban", "creationDate", "expirationDate", "balance", "owner"})
@JsonRootName("DebitCard")
public class DebitCard implements Comparable<DebitCard> {
    private static int debitCardNo = 0;
    private int id;
    private String iban;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
    private double balance;
    private Integer owner;

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

    /**
     *
     */
    public DebitCard() {
        this.setIban(this.generateIBAN());
        this.setBalance(0.0);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterFourYears = LocalDateTime.of(
                now.getYear() + 4, now.getMonth(), now.getDayOfMonth(),
                now.getHour(), now.getMinute(), now.getSecond());
        this.setCreationDate(now);
        this.setExpirationDate(afterFourYears);
        this.setOwner(0);
        this.setId(this.generateId());
    }

    /**
     *
     * @param balance
     * @param owner
     */
    public DebitCard(double balance, Integer owner) {
        this.setIban(this.generateIBAN());
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

    /**
     *
     * @param iban
     * @param balance
     * @param owner
     */
    public DebitCard(String iban, double balance, Integer owner) {
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

    /**
     *
     * @param id
     * @param iban
     * @param creationDate
     * @param expirationDate
     * @param balance
     * @param owner
     */
    @JsonCreator
    public DebitCard(@JsonProperty("id") int id,
                     @JsonProperty("iban") String iban,
                     @JsonProperty("creationDate") LocalDateTime creationDate,
                     @JsonProperty("expirationDate") LocalDateTime expirationDate,
                     @JsonProperty("balance") double balance,
                     @JsonProperty("owner") Integer owner) {
        this.setId(id);
        this.setIban(iban);
        this.setBalance(balance);
        this.setCreationDate(creationDate);
        this.setExpirationDate(expirationDate);
        this.setOwner(owner);
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
    public String getIban() {
        return iban;
    }

    /**
     *
     * @param iban
     */
    @JsonSetter("iban")
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     *
     * @param creationDate
     */
    @JsonSetter("creationDate")
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    /**
     *
     * @param expirationDate
     */
    @JsonSetter("expirationDate")
    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     *
     * @return
     */
    public double getBalance() {
        return balance;
    }

    /**
     *
     * @param balance
     */
    @JsonSetter("balance")
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     *
     * @return
     */
    public Integer getOwner() {
        return owner;
    }

    /**
     *
     * @param owner
     */
    @JsonSetter("owner")
    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    /**
     *
     * @return
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
     *
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(DebitCard o) {
        if(this.getId() < (o.getId()) && this.getCreationDate().isBefore(o.getCreationDate())
                && this.getExpirationDate().isBefore(o.getExpirationDate())
                && this.getIban().compareTo(o.getIban()) < 0 && this.getBalance() < o.getBalance()
                && this.getOwner().compareTo(o.getOwner()) < 0) {
            return -1;
        } else if(this.getId() == o.getId() && this.getCreationDate().equals(o.getCreationDate())
                && this.getExpirationDate().equals(o.getExpirationDate()) && this.getIban().equals(o.getIban())
                && this.getBalance() == o.getBalance() && this.getOwner().compareTo(o.getOwner()) == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        // DebitCard dc = new DebitCard(1000.0, 1);
        DebitCardReaderWriter drw = new DebitCardReaderWriter();
        //drw.write(dc, "debitCards.txt");

        ArrayList<DebitCard> ls = new ArrayList<>();
        File f = new File("debitCards.txt");
        try(FileReader fr = new FileReader(f)) {
            ls = drw.read(fr, f);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            ex.printStackTrace();
        }
        for(DebitCard c : ls) {
            System.out.println(c);
        }
    }
}