package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import types.RoomType;

import java.util.ArrayList;

@JsonPropertyOrder({"id", "type", "amenities", "maximumOccupancy",
        "pricePerNight", "totalPrice", "isBooked", "inReservations"})
@JsonRootName("models.Room")
public class Room {
    private static int roomNo = 0;
    private int id;
    private RoomType type;
    private ArrayList<String> amenities;
    private int maximumOccupancy;
    private double pricePerNight; // price per person
    private double totalPrice;
    private boolean isBooked;
    private ArrayList<Reservation> inReservations;

    private int generateId() {
        // It is the good way only for learning purposes because
        // it may mak the program to generate not unique ids.
        return ++Room.roomNo;
    }

    public Room() {
        this.setType(RoomType.UNKNOWN);
        this.setAmenities(new ArrayList<>());
        this.setMaximumOccupancy(0);
        this.setPricePerNight(0.0);
        this.setTotalPrice(0.0);
        this.setBooked(false);
        this.setInReservations(new ArrayList<>());
        this.setId(this.generateId());
    }

    public Room(RoomType type, ArrayList<String> amenities, int maximumOccupancy,
                boolean isBooked, double pricePerNight, ArrayList<Reservation> inReservations) {
        this.setType(type);
        this.setAmenities(amenities);
        this.setMaximumOccupancy(maximumOccupancy);
        this.setPricePerNight(pricePerNight);
        this.setTotalPrice(pricePerNight * maximumOccupancy);
        this.setBooked(isBooked);
        this.setInReservations(inReservations);
        this.setId(this.generateId());
    }

    @JsonCreator
    public Room(@JsonProperty("id") int id,
                @JsonProperty("type") RoomType type,
                @JsonProperty("amenities") ArrayList<String> amenities,
                @JsonProperty("maximumOccupancy") int maximumOccupancy,
                @JsonProperty("pricePerNight") double pricePerNight,
                @JsonProperty("totalPrice") double totalPrice,
                @JsonProperty("isBooked") boolean isBooked,
                @JsonProperty("inReservations") ArrayList<Reservation> inReservations) {
        this.setId(id);
        this.setType(type);
        this.setAmenities(amenities);
        this.setMaximumOccupancy(maximumOccupancy);
        this.setPricePerNight(pricePerNight);
        this.setTotalPrice(totalPrice);
        this.setBooked(isBooked);
        this.setInReservations(inReservations);
    }

    public int getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    public RoomType getType() {
        return type;
    }

    @JsonSetter("type")
    public void setType(RoomType type) {
        this.type = type;
    }

    public ArrayList<String> getAmenities() {
        return amenities;
    }

    @JsonSetter("amenities")
    public void setAmenities(ArrayList<String> amenities) {
        this.amenities = amenities;
    }

    public int getMaximumOccupancy() {
        return maximumOccupancy;
    }

    @JsonSetter("maximumOccupancy")
    public void setMaximumOccupancy(int maximumOccupancy) {
        this.maximumOccupancy = maximumOccupancy;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    @JsonSetter("pricePerNight")
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @JsonSetter("totalPrice")
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isBooked() {
        return isBooked;
    }

    @JsonSetter("booked")
    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public ArrayList<Reservation> getInReservations() {
        return inReservations;
    }

    @JsonSetter("inReservations")
    public void setInReservations(ArrayList<Reservation> inReservations) {
        this.inReservations = inReservations;
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