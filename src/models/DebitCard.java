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

@JsonPropertyOrder({"id", "iban", "creationDate", "expirationDate", "balance", "owner"})
@JsonRootName("DebitCard")
public class DebitCard implements Comparable<DebitCard> {
    private static int debitCardNo = 0;
    private int id;
    private String iban;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
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

    public Integer getOwner() {
        return owner;
    }

    @JsonSetter("owner")
    public void setOwner(Integer owner) {
        this.owner = owner;
    }

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

    @Override
    public int compareTo(DebitCard o) {
        return Integer.compare(this.getId(), o.getId());
    }
}