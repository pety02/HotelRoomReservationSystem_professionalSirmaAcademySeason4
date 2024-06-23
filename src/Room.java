import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

@JsonPropertyOrder()
@JsonRootName("Room")
public class Room {
    private static int roomNo = 0;
    private String id;
    private RoomType type;
    private ArrayList<String> amenities;
    private int maximumOccupancy;
    private double pricePerNight; // price per person
    private double totalPrice;
    private boolean isBooked;
    private User booker;

    private String generateId() {
        if(this.booker == null) {
            return "Room" + (++Room.roomNo) + "not_booked";
        }
        return "Room" + (++Room.roomNo) + this.booker.getId();
    }

    public Room() {
    }

    public Room(RoomType type, ArrayList<String> amenities, int maximumOccupancy,
                boolean isBooked, double pricePerNight, User booker) {
        this.setId(this.generateId());
        this.setType(type);
        this.setAmenities(amenities);
        this.setMaximumOccupancy(maximumOccupancy);
        this.setPricePerNight(pricePerNight);
        this.setTotalPrice(pricePerNight * maximumOccupancy);
        this.setBooked(isBooked);
        this.setBooker(booker);
    }

    @JsonCreator
    public Room(@JsonProperty("id") String id,
                @JsonProperty("type") RoomType type,
                @JsonProperty("amenities") ArrayList<String> amenities,
                @JsonProperty("maximumOccupancy") int maximumOccupancy,
                @JsonProperty("pricePerNight") double pricePerNight,
                @JsonProperty("totalPrice") double totalPrice,
                @JsonProperty("isBooked") boolean isBooked,
                @JsonProperty("booker") User booker) {
        this.setId(id);
        this.setType(type);
        this.setAmenities(amenities);
        this.setMaximumOccupancy(maximumOccupancy);
        this.setPricePerNight(pricePerNight);
        this.setTotalPrice(totalPrice);
        this.setBooked(isBooked);
        this.setBooker(booker);
    }

    public String getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(String id) {
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

    public User getBooker() {
        return booker;
    }

    @JsonSetter("booker")
    public void setBooker(User booker) {
        this.booker = booker;
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