package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import types.RoomType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Room class represents a hotel room with attributes such as ID, type, amenities, price, occupancy,
 * booking availability, and reservation IDs.
 */
@JsonPropertyOrder({"id", "hotelId", "type", "amenities", "maximumOccupancy",
        "pricePerNight", "totalPrice", "isBooked", "bookingAvailability", "inReservationsIds"})
@JsonRootName("Room")
public class Room implements Comparable<Room> {
    private static int roomNo = 0;
    private int id; // Unique identifier for the room
    private Integer hotelId; // ID of the hotel that owns the room
    private RoomType type; // Type of the room (e.g., single, double, suite)
    private ArrayList<String> amenities; // List of amenities available in the room
    private int maximumOccupancy; // Maximum number of occupants allowed in the room
    private double pricePerNight; // Price per night per person
    private double totalPrice; // Total price for the room
    private boolean isBooked; // Flag indicating if the room is currently booked
    private Map<Boolean, ArrayList<LocalDateTime>> bookingAvailability; // Availability of the room for booking
    private ArrayList<Integer> inReservationsIds; // List of reservation IDs associated with the room

    // Static method to generate a unique ID for each room instance
    private int generateId() {
        return ++Room.roomNo;
    }

    /**
     * Default constructor initializes a room with default values.
     */
    public Room() {
        this.setType(RoomType.UNKNOWN);
        this.setHotelId(0);
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
        this.setInReservationsIds(new ArrayList<>());
        this.setId(this.generateId());
    }

    /**
     * Constructor initializes a room with specified attributes.
     *
     * @param id                 ID of the current room.
     * @param hotel              ID of the hotel that owns the room.
     * @param type               Type of the room.
     * @param amenities          List of amenities available in the room.
     * @param maximumOccupancy   Maximum number of occupants allowed in the room.
     * @param isBooked           Flag indicating if the room is currently booked.
     * @param bookingAvailability Availability of the room for booking.
     * @param pricePerNight      Price per night per person.
     * @param inReservations     List of reservation IDs associated with the room.
     */
    public Room(int id, int hotel, RoomType type, ArrayList<String> amenities, int maximumOccupancy,
                boolean isBooked, Map<Boolean, ArrayList<LocalDateTime>> bookingAvailability,
                double pricePerNight, ArrayList<Integer> inReservations) {
        this.setId(id);
        this.setHotelId(hotel);
        this.setType(type);
        this.setAmenities(amenities);
        this.setMaximumOccupancy(maximumOccupancy);
        this.setPricePerNight(pricePerNight);
        this.setTotalPrice(pricePerNight * maximumOccupancy);
        this.setBooked(isBooked);
        this.setBookingAvailability(bookingAvailability);
        this.setInReservationsIds(inReservations);
    }

    /**
     * Constructor initializes a room with specified attributes.
     *
     * @param hotel              ID of the hotel that owns the room.
     * @param type               Type of the room.
     * @param amenities          List of amenities available in the room.
     * @param maximumOccupancy   Maximum number of occupants allowed in the room.
     * @param isBooked           Flag indicating if the room is currently booked.
     * @param bookingAvailability Availability of the room for booking.
     * @param pricePerNight      Price per night per person.
     * @param inReservations     List of reservation IDs associated with the room.
     */
    public Room(int hotel, RoomType type, ArrayList<String> amenities, int maximumOccupancy,
                boolean isBooked, Map<Boolean, ArrayList<LocalDateTime>> bookingAvailability,
                double pricePerNight, ArrayList<Integer> inReservations) {
        this.setHotelId(hotel);
        this.setType(type);
        this.setAmenities(amenities);
        this.setMaximumOccupancy(maximumOccupancy);
        this.setPricePerNight(pricePerNight);
        this.setTotalPrice(pricePerNight * maximumOccupancy);
        this.setBooked(isBooked);
        this.setBookingAvailability(bookingAvailability);
        this.setInReservationsIds(inReservations);
        this.setId(this.generateId());
    }

    /**
     * Constructor initializes a room with specified attributes including an ID.
     *
     * @param id                 Unique identifier for the room.
     * @param hotel              ID of the hotel that owns the room.
     * @param type               Type of the room.
     * @param amenities          List of amenities available in the room.
     * @param maximumOccupancy   Maximum number of occupants allowed in the room.
     * @param isBooked           Flag indicating if the room is currently booked.
     * @param bookingAvailability Availability of the room for booking.
     * @param pricePerNight      Price per night per person.
     * @param inReservations     List of reservation IDs associated with the room.
     */
    @JsonCreator
    public Room(@JsonProperty("id") int id,
                @JsonProperty("hotelId") Integer hotel,
                @JsonProperty("type") RoomType type,
                @JsonProperty("amenities") ArrayList<String> amenities,
                @JsonProperty("maximumOccupancy") int maximumOccupancy,
                @JsonProperty("pricePerNight") double pricePerNight,
                @JsonProperty("totalPrice") double totalPrice,
                @JsonProperty("isBooked") boolean isBooked,
                @JsonProperty("bookingAvailability") Map<Boolean, ArrayList<LocalDateTime>> bookingAvailability,
                @JsonProperty("inReservationsIds") ArrayList<Integer> inReservations) {
        this.setId(id);
        this.setHotelId(hotel);
        this.setType(type);
        this.setAmenities(amenities);
        this.setMaximumOccupancy(maximumOccupancy);
        this.setPricePerNight(pricePerNight);
        this.setTotalPrice(totalPrice);
        this.setBooked(isBooked);
        this.setBookingAvailability(bookingAvailability);
        this.setInReservationsIds(inReservations);
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    @JsonSetter("hotelId")
    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
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
        isBooked = booked;
    }

    public Map<Boolean, ArrayList<LocalDateTime>> getBookingAvailability() {
        return bookingAvailability;
    }

    @JsonSetter("bookingAvailability")
    public void setBookingAvailability(Map<Boolean, ArrayList<LocalDateTime>> bookingAvailability) {
        this.bookingAvailability = bookingAvailability;
    }

    public ArrayList<Integer> getInReservationsIds() {
        return inReservationsIds;
    }

    @JsonSetter("inReservationsIds")
    public void setInReservationsIds(ArrayList<Integer> inReservationsIds) {
        this.inReservationsIds = inReservationsIds;
    }

    // Other Methods

    /**
     * Converts the room object to a JSON string representation.
     *
     * @return The JSON string representation of the room.
     */
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

    /**
     * Compares this room to another room for ordering based on ID, type, amenities, occupancy, and availability.
     *
     * @param o The other room to compare to.
     * @return A negative integer, zero, or a positive integer as this room is less than, equal to, or greater than the specified room.
     */
    @Override
    public int compareTo(Room o) {
        // Compare ID, type, maximum occupancy, hotel ID, price per night, and total price
        int firstCondition = Integer.compare(this.getId(), o.getId());
        if (firstCondition == 0) {
            firstCondition = this.getType().compareTo(o.getType());
            if (firstCondition == 0) {
                firstCondition = Integer.compare(this.getMaximumOccupancy(), o.getMaximumOccupancy());
                if (firstCondition == 0) {
                    firstCondition = Integer.compare(this.getHotelId(), o.getHotelId());
                    if (firstCondition == 0) {
                        firstCondition = Double.compare(this.getPricePerNight(), o.getPricePerNight());
                        if (firstCondition == 0) {
                            firstCondition = Double.compare(this.getTotalPrice(), o.getTotalPrice());
                        }
                    }
                }
            }
        }

        // Compare amenities
        if (firstCondition == 0) {
            ArrayList<String> thisAmenities = this.getAmenities();
            ArrayList<String> otherAmenities = o.getAmenities();
            for (int i = 0; i < Math.min(thisAmenities.size(), otherAmenities.size()); i++) {
                firstCondition = thisAmenities.get(i).compareTo(otherAmenities.get(i));
                if (firstCondition != 0) {
                    break;
                }
            }
            if (firstCondition == 0) {
                firstCondition = Integer.compare(thisAmenities.size(), otherAmenities.size());
            }
        }

        // Compare inReservationsIds
        if (firstCondition == 0) {
            ArrayList<Integer> thisReservations = this.getInReservationsIds();
            ArrayList<Integer> otherReservations = o.getInReservationsIds();
            for (int i = 0; i < Math.min(thisReservations.size(), otherReservations.size()); i++) {
                firstCondition = Integer.compare(thisReservations.get(i), otherReservations.get(i));
                if (firstCondition != 0) {
                    break;
                }
            }
            if (firstCondition == 0) {
                firstCondition = Integer.compare(thisReservations.size(), otherReservations.size());
            }
        }

        // Compare bookingAvailability
        if (firstCondition == 0) {
            Map<Boolean, ArrayList<LocalDateTime>> thisBooking = this.getBookingAvailability();
            Map<Boolean, ArrayList<LocalDateTime>> otherBooking = o.getBookingAvailability();
            firstCondition = Integer.compare(thisBooking.size(), otherBooking.size());
            if (firstCondition == 0) {
                for (Map.Entry<Boolean, ArrayList<LocalDateTime>> thisEntry : thisBooking.entrySet()) {
                    Boolean key = thisEntry.getKey();
                    ArrayList<LocalDateTime> thisDates = thisEntry.getValue();
                    ArrayList<LocalDateTime> otherDates = otherBooking.get(key);
                    firstCondition = key.compareTo(otherBooking.containsKey(key));
                    if (firstCondition == 0) {
                        for (int i = 0; i < Math.min(thisDates.size(), otherDates.size()); i++) {
                            firstCondition = thisDates.get(i).compareTo(otherDates.get(i));
                            if (firstCondition != 0) {
                                break;
                            }
                        }
                        if (firstCondition == 0) {
                            firstCondition = Integer.compare(thisDates.size(), otherDates.size());
                        }
                    }
                    if (firstCondition != 0) {
                        break;
                    }
                }
            }
        }

        return firstCondition;
    }
}