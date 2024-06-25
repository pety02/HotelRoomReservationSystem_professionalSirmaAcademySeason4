package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import types.RoomType;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@JsonPropertyOrder({"id", "hotel", "type", "amenities", "maximumOccupancy",
        "pricePerNight", "totalPrice", "isBooked", "bookingAvailability"})
@JsonRootName("models.Room")
public class Room {
    private static int roomNo = 0;
    private int id;
    private Hotel hotel;
    private RoomType type;
    private ArrayList<String> amenities;
    private int maximumOccupancy;
    private double pricePerNight; // price per person
    private double totalPrice;
    private boolean isBooked;
    private Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> bookingAvailability;
    @JsonIgnore
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
        Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> isBooked = new HashMap<>();
        Map.Entry<LocalDateTime, LocalDateTime> dates = new AbstractMap.SimpleEntry<>(
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        isBooked.put(dates, false);
        this.setBookingAvailability(isBooked);
        this.setInReservations(new ArrayList<>());
        this.setId(this.generateId());
    }

    public Room(Hotel hotel, RoomType type, ArrayList<String> amenities, int maximumOccupancy,
                boolean isBooked, Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> bookingAvailability,
                double pricePerNight, ArrayList<Reservation> inReservations) {
        this.setHotel(hotel);
        this.setType(type);
        this.setAmenities(amenities);
        this.setMaximumOccupancy(maximumOccupancy);
        this.setPricePerNight(pricePerNight);
        this.setTotalPrice(pricePerNight * maximumOccupancy);
        this.setBooked(isBooked);
        this.setBookingAvailability(bookingAvailability);
        this.setInReservations(inReservations);
        this.setId(this.generateId());
    }

    @JsonCreator
    public Room(@JsonProperty("id") int id,
                @JsonProperty("hotel") Hotel hotel,
                @JsonProperty("type") RoomType type,
                @JsonProperty("amenities") ArrayList<String> amenities,
                @JsonProperty("maximumOccupancy") int maximumOccupancy,
                @JsonProperty("pricePerNight") double pricePerNight,
                @JsonProperty("totalPrice") double totalPrice,
                @JsonProperty("isBooked") boolean isBooked,
                @JsonProperty("bookingAvailability") Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> bookingAvailability) {
        this.setId(id);
        this.setHotel(hotel);
        this.setType(type);
        this.setAmenities(amenities);
        this.setMaximumOccupancy(maximumOccupancy);
        this.setPricePerNight(pricePerNight);
        this.setTotalPrice(totalPrice);
        this.setBooked(isBooked);
        this.setBookingAvailability(bookingAvailability);
        this.setInReservations(new ArrayList<>());
    }

    public Room(int id,
                Hotel hotel,
                RoomType type,
                ArrayList<String> amenities,
                int maximumOccupancy,
                double pricePerNight,
                double totalPrice,
                boolean isBooked,
                Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> bookingAvailability,
                ArrayList<Reservation> inReservations) {
        this.setId(id);
        this.setHotel(hotel);
        this.setType(type);
        this.setAmenities(amenities);
        this.setMaximumOccupancy(maximumOccupancy);
        this.setPricePerNight(pricePerNight);
        this.setTotalPrice(totalPrice);
        this.setBooked(isBooked);
        this.setBookingAvailability(bookingAvailability);
        this.setInReservations(inReservations);
    }

    public int getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    @JsonSetter("hotel")
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
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

    @JsonSetter("isBooked")
    public void setBooked(boolean booked) {
        this.isBooked = booked;
    }

    public Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> isCurrentlyAvailable() {
        return bookingAvailability;
    }

    @JsonSetter("bookingAvailability")
    public void setBookingAvailability(Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> booked) {
        bookingAvailability = booked;
    }

    public ArrayList<Reservation> getInReservations() {
        return inReservations;
    }

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