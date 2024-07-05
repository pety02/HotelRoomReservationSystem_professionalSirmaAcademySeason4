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
 *
 */
@JsonPropertyOrder({"id", "hotel", "type", "amenities", "maximumOccupancy",
        "pricePerNight", "totalPrice", "isBooked", "bookingAvailability", "inReservations"})
@JsonRootName("Room")
public class Room implements Comparable<Room> {
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

    /**
     *
     */
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

    /**
     *
     * @param hotel
     * @param type
     * @param amenities
     * @param maximumOccupancy
     * @param isBooked
     * @param bookingAvailability
     * @param pricePerNight
     * @param inReservations
     */
    public Room(int hotel, RoomType type, ArrayList<String> amenities, int maximumOccupancy,
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

    /**
     *
     * @param id
     * @param hotel
     * @param type
     * @param amenities
     * @param maximumOccupancy
     * @param isBooked
     * @param bookingAvailability
     * @param pricePerNight
     * @param inReservations
     */
    public Room(int id, int hotel, RoomType type, ArrayList<String> amenities, int maximumOccupancy,
                boolean isBooked, Map<Boolean, ArrayList<LocalDateTime>> bookingAvailability,
                double pricePerNight, ArrayList<Integer> inReservations) {
        this.setId(id);
        this.setHotel(hotel);
        this.setType(type);
        this.setAmenities(amenities);
        this.setMaximumOccupancy(maximumOccupancy);
        this.setPricePerNight(pricePerNight);
        this.setTotalPrice(pricePerNight * maximumOccupancy);
        this.setBooked(isBooked);
        this.setBookingAvailability(bookingAvailability);
        this.setInReservations(inReservations);
    }

    /**
     *
     * @param id
     * @param hotel
     * @param type
     * @param amenities
     * @param maximumOccupancy
     * @param pricePerNight
     * @param totalPrice
     * @param isBooked
     * @param bookingAvailability
     * @param inReservations
     */
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
    public Integer getHotel() {
        return hotel;
    }

    /**
     *
     * @param hotel
     */
    @JsonSetter("hotel")
    public void setHotel(Integer hotel) {
        this.hotel = hotel;
    }

    /**
     *
     * @return
     */
    public RoomType getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    @JsonSetter("type")
    public void setType(RoomType type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getAmenities() {
        return amenities;
    }

    /**
     *
     * @param amenities
     */
    @JsonSetter("amenities")
    public void setAmenities(ArrayList<String> amenities) {
        this.amenities = amenities;
    }

    /**
     *
     * @return
     */
    public int getMaximumOccupancy() {
        return maximumOccupancy;
    }

    /**
     *
     * @param maximumOccupancy
     */
    @JsonSetter("maximumOccupancy")
    public void setMaximumOccupancy(int maximumOccupancy) {
        this.maximumOccupancy = maximumOccupancy;
    }

    /**
     *
     * @return
     */
    public double getPricePerNight() {
        return pricePerNight;
    }

    /**
     *
     * @param pricePerNight
     */
    @JsonSetter("pricePerNight")
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    /**
     *
     * @return
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     *
     * @param totalPrice
     */
    @JsonSetter("totalPrice")
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     *
     * @return
     */
    public boolean isBooked() {
        return isBooked;
    }

    /**
     *
     * @param booked
     */
    @JsonSetter("isBooked")
    public void setBooked(boolean booked) {
        this.isBooked = booked;
    }

    /**
     *
     * @return
     */
    public Map<Boolean, ArrayList<LocalDateTime>> getBookingAvailability() {
        return bookingAvailability;
    }

    /**
     *
     * @param bookingAvailability
     */
    @JsonSetter("bookingAvailability")
    public void setBookingAvailability(Map<Boolean, ArrayList<LocalDateTime>> bookingAvailability) {
        this.bookingAvailability = bookingAvailability;
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> getInReservations() {
        return inReservations;
    }

    /**
     *
     * @param inReservations
     */
    @JsonSetter("inReservations")
    public void setInReservations(ArrayList<Integer> inReservations) {
        this.inReservations = inReservations;
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

        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return "{}";
        }
    }

    /**
     *
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Room o) {
        int firstCondition = 0, secondCondition = 0, thirdCondition = 0, fourthCondition = 0;
        if(this.getId() < o.getId() && this.getType().compareTo(o.getType()) < 0
            && this.getMaximumOccupancy() < o.getMaximumOccupancy() && this.getHotel() < o.getHotel()
            && this.getPricePerNight() < o.getPricePerNight() && this.getTotalPrice() < o.getTotalPrice()) {
            firstCondition = -1;
        } else if (this.getId() == o.getId() && this.getType().compareTo(o.getType()) == 0
                && this.getMaximumOccupancy() == o.getMaximumOccupancy() && this.getHotel().compareTo(o.getHotel()) == 0
                && this.getPricePerNight() == o.getPricePerNight() && this.getTotalPrice() == o.getTotalPrice()) {
            firstCondition = 0;
        } else {
            firstCondition = 1;
        }

        for(String currAmenity : this.getAmenities()) {
            for(String otherAmenity : o.getAmenities()) {
                if (currAmenity.compareTo(otherAmenity) < 0) {
                    secondCondition = -1;
                } else if(currAmenity.compareTo(otherAmenity) == 0) {
                    secondCondition = 0;
                } else {
                    secondCondition = 1;
                }
            }
        }

        for(int currReservationId : this.getInReservations()) {
            for(int otherReservationId : o.getInReservations()) {
                if(currReservationId < otherReservationId) {
                    thirdCondition = -1;
                } else if (currReservationId == otherReservationId) {
                    thirdCondition = 0;
                } else {
                    thirdCondition = 1;
                }
            }
        }

        for(Map.Entry<Boolean, ArrayList<LocalDateTime>> currRoomBookingAvailability : this.getBookingAvailability().entrySet()) {
            for(Map.Entry<Boolean, ArrayList<LocalDateTime>> otherRoomBookingAvailability : o.getBookingAvailability().entrySet()) {
                for(LocalDateTime currDT : currRoomBookingAvailability.getValue()) {
                    for(LocalDateTime otherDT : otherRoomBookingAvailability.getValue()) {
                        if (currRoomBookingAvailability.getKey().compareTo(otherRoomBookingAvailability.getKey()) < 0
                            && currDT.isBefore(otherDT)) {
                            fourthCondition = -1;
                        } else if (currRoomBookingAvailability.getKey().compareTo(otherRoomBookingAvailability.getKey()) == 0
                            && currDT.equals(otherDT)) {
                            fourthCondition = 0;
                        } else {
                            fourthCondition = 1;
                        }
                    }
                }
            }
        }

        if(firstCondition < 0 || secondCondition < 0 || thirdCondition < 0 || fourthCondition < 0) {
            return -1;
        } else if (firstCondition == 0 && secondCondition == 0 && thirdCondition == 0 && fourthCondition == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}