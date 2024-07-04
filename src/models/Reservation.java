package models;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.*;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import readersWriters.ReservationReaderWriter;
import utils.LocalDateTimeMapDeserializer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@JsonPropertyOrder({"id", "fromDate", "toDate", "rooms", "bookedBy", "cancellationFees", "totalPrice", "isCancelled"})
@JsonRootName("Reservation")
public class Reservation implements Comparable<Reservation> {
    private static int reservationNo = 0;
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fromDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime toDate;
    private Map<Integer, Double> rooms;
    private int bookedBy;
    private double cancellationFees;
    private double totalPrice;
    private boolean isCancelled;

    private int generateId() {
        // It is the good way only for learning purposes because
        // it may make the program to generate not unique ids.
        return ++Reservation.reservationNo;
    }

    public Reservation() {
        this.setFromDate(LocalDateTime.now());
        this.setToDate(LocalDateTime.now());
        this.setRooms(new HashMap<>());
        this.setBookedBy(0);
        this.setCancellationFees(0.0);
        this.setTotalPrice(0.0);
        this.setCancelled(false);
        this.setId(this.generateId());
    }

    public Reservation(LocalDateTime fromDate,
                       LocalDateTime toDate,
                       Map<Integer, Double> rooms,
                       User bookedBy,
                       double cancellationFees,
                       boolean isCancelled) {
        this.setFromDate(fromDate);
        this.setToDate(toDate);
        this.setRooms(rooms);
        this.setBookedBy(bookedBy.getId());
        this.setCancellationFees(cancellationFees);
        int days = toDate.getDayOfYear() - fromDate.getDayOfYear();
        this.setTotalPrice(this.calculateTotalPrice(days));
        this.setCancelled(isCancelled);
        this.setId(this.generateId());
    }

    public Reservation(LocalDateTime fromDate,
                       LocalDateTime toDate,
                       double cancellationFees,
                       boolean isCancelled) {
        this.setId(this.generateId());
        this.setFromDate(fromDate);
        this.setToDate(toDate);
        this.setRooms(new HashMap<>());
        this.setBookedBy(0);
        this.setCancellationFees(cancellationFees);
        int days = toDate.getDayOfYear() - fromDate.getDayOfYear();
        this.setTotalPrice(this.calculateTotalPrice(days));
        this.setCancelled(isCancelled);
    }

    @JsonCreator
    public Reservation(@JsonProperty("id") int id,
                       @JsonProperty("fromDate") LocalDateTime fromDate,
                       @JsonProperty("toDate") LocalDateTime toDate,
                       @JsonProperty("rooms") Map<Integer, Double> rooms,
                       @JsonProperty("bookedBy") int bookedBy,
                       @JsonProperty("cancellationFees") double cancellationFees,
                       @JsonProperty("totalPrice") double totalPrice,
                       @JsonProperty("isCancelled") boolean isCancelled) {
        this.setId(id);
        this.setFromDate(fromDate);
        this.setToDate(toDate);
        this.setRooms(rooms);
        this.setBookedBy(bookedBy);
        this.setCancellationFees(cancellationFees);
        this.setTotalPrice(totalPrice);
        this.setCancelled(isCancelled);
    }

    public int getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    @JsonSetter("fromDate")
    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    @JsonSetter("toDate")
    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    @JsonSetter("rooms")
    public Map<Integer, Double> getRooms() {
        return rooms;
    }

    public void setRooms(Map<Integer, Double> rooms) {
        this.rooms = rooms;
    }

    public int getBookedBy() {
        return bookedBy;
    }

    @JsonSetter("bookedBy")
    public void setBookedBy(int bookedBy) {
        this.bookedBy = bookedBy;
    }

    public double getCancellationFees() {
        return cancellationFees;
    }

    @JsonSetter("cancellationFees")
    public void setCancellationFees(double cancellationFees) {
        this.cancellationFees = cancellationFees;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @JsonSetter("totalPrice")
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    @JsonSetter("isCancelled")
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public double calculateTotalPrice(int days) {
        double total = 0.0;
        for (Map.Entry<Integer, Double> bookedRoom : this.rooms.entrySet()) {
            total += (bookedRoom.getValue() * days);
        }

        return total;
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
        Reservation r = new Reservation(LocalDateTime.of(2024,7,2,10,30), LocalDateTime.of(2024,7,5,12,30), 100.00, false);
        System.out.println(r);
        ReservationReaderWriter rrw=  new ReservationReaderWriter();
        rrw.write(r, "reservations.txt");

        ArrayList<Reservation> ls = new ArrayList<>();
        File f = new File("reservations.txt");
        try(FileReader fr = new FileReader(f)) {
            ls = rrw.read(fr, f);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            ex.printStackTrace();
        }
        for(Reservation c : ls) {
            System.out.println(c);
        }
    }

    @Override
    public int compareTo(Reservation o) {
        return Integer.compare(this.getId(), o.getId());
    }
}
