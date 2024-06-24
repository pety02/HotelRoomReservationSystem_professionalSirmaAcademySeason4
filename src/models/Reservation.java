package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;

@JsonPropertyOrder({"id", "fromDate", "toDate", "rooms", "bookedBy",
    "cancellationFees", "totalPrice", "isCancelled"})
@JsonRootName("models.Reservation")
public class Reservation {
    private static int reservationNo = 0;
    private int id;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private ArrayList<Room> rooms;
    private User bookedBy;
    private double cancellationFees;
    private double totalPrice;
    private boolean isCancelled;

    private int generateId() {
        // It is the good way only for learning purposes because
        // it may mak the program to generate not unique ids.
        return ++Reservation.reservationNo;
    }

    public Reservation() {
        this.setFromDate(LocalDateTime.now());
        this.setToDate(LocalDateTime.now());
        this.setRooms(new ArrayList<>());
        this.setBookedBy(null);
        this.setCancellationFees(0.0);
        this.setTotalPrice(0.0);
        this.setCancelled(false);
        this.setId(this.generateId());
    }

    public Reservation(LocalDateTime fromDate,
                       LocalDateTime toDate,
                       ArrayList<Room> rooms,
                       User bookedBy,
                       double cancellationFees,
                       boolean isCancelled) {
        this.setFromDate(fromDate);
        this.setToDate(toDate);
        this.setRooms(rooms);
        this.setBookedBy(bookedBy);
        this.setCancellationFees(cancellationFees);
        this.setTotalPrice(this.calculateTotalPrice());
        this.setCancelled(isCancelled);
        this.setId(this.generateId());
    }

    @JsonCreator
    public Reservation(@JsonProperty("id") int id,
                       @JsonProperty("fromDate") LocalDateTime fromDate,
                       @JsonProperty("toDate") LocalDateTime toDate,
                       @JsonProperty("rooms") ArrayList<Room> rooms,
                       @JsonProperty("bookedBy") User bookedBy,
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

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    @JsonSetter("rooms")
    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public User getBookedBy() {
        return bookedBy;
    }

    @JsonSetter("bookedBy")
    public void setBookedBy(User bookedBy) {
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

    public double calculateTotalPrice() {
        double total = 0.0;
        for(Room bookedRoom : this.rooms) {
            total += bookedRoom.getTotalPrice();
        }
        if(this.isCancelled) {
            total += this.cancellationFees;
        }

        return total;
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