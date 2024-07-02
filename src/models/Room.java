package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import types.RoomType;
import utils.LocalDateTimeMapDeserializer;
import utils.LocalDateTimeMapEntryDeserializer;
import utils.LocalDateTimeMapEntrySerializer;
import utils.LocalDateTimeMapSerializer;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@JsonPropertyOrder({"id", "hotel", "type", "amenities", "maximumOccupancy",
        "pricePerNight", "totalPrice", "isBooked", "bookingAvailability", "inReservations"})
@JsonRootName("Room")
public class Room {
    private static int roomNo = 0;
    private int id;
    private Integer hotel;
    private RoomType type;
    private ArrayList<String> amenities;
    private int maximumOccupancy;
    private double pricePerNight; // price per person
    private double totalPrice;
    private boolean isBooked;
    private Map<Boolean, ArrayList<LocalDateTime>> bookingAvailability;
    private ArrayList<Integer> inReservations;

    private int generateId() {
        return ++Room.roomNo;
    }

    public Room() {
        this.setType(RoomType.UNKNOWN);
        this.setHotel(0);
        this.setAmenities(new ArrayList<>());
        this.setMaximumOccupancy(0);
        this.setPricePerNight(0.0);
        this.setTotalPrice(0.0);
        this.setBooked(false);
        Map<Boolean, ArrayList<LocalDateTime>> isBooked = new HashMap<>();
        ArrayList<LocalDateTime> dates = new ArrayList<>();
        dates.add(LocalDateTime.now());
        isBooked.put(false, dates);
        this.setBookingAvailability(isBooked);
        this.setInReservations(new ArrayList<>());
        this.setId(this.generateId());
    }

    public Room(Integer hotel, RoomType type, ArrayList<String> amenities, int maximumOccupancy,
                boolean isBooked, Map<Boolean, ArrayList<LocalDateTime>> bookingAvailability,
                double pricePerNight, ArrayList<Integer> inReservations) {
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
                @JsonProperty("hotel") Integer hotel,
                @JsonProperty("type") RoomType type,
                @JsonProperty("amenities") ArrayList<String> amenities,
                @JsonProperty("maximumOccupancy") int maximumOccupancy,
                @JsonProperty("pricePerNight") double pricePerNight,
                @JsonProperty("totalPrice") double totalPrice,
                @JsonProperty("isBooked") boolean isBooked,
                @JsonProperty("bookingAvailability") Map<Boolean, ArrayList<LocalDateTime>> bookingAvailability,
                @JsonProperty("inReservations") ArrayList<Integer> inReservations) {
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

    public Integer getHotel() {
        return hotel;
    }

    @JsonSetter("hotel")
    public void setHotel(Integer hotel) {
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

    public Map<Boolean, ArrayList<LocalDateTime>> getBookingAvailability() {
        return bookingAvailability;
    }

    @JsonSetter("bookingAvailability")
    public void setBookingAvailability(Map<Boolean, ArrayList<LocalDateTime>> bookingAvailability) {
        this.bookingAvailability = bookingAvailability;
    }

    public ArrayList<Integer> getInReservations() {
        return inReservations;
    }

    @JsonSetter("inReservations")
    public void setInReservations(ArrayList<Integer> inReservations) {
        this.inReservations = inReservations;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));

        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return "{}";
        }
    }

    public static void main(String[] args) {
        Room r = new Room();
        System.out.println(r);
    }
}