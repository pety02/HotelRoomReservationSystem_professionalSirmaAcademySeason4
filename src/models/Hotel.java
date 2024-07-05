package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

/**
 * The Hotel class represents a hotel with fields for ID, name, address, all room IDs, booked room IDs, and incomes.
 * It supports JSON serialization/deserialization and implements Comparable for sorting.
 */
@JsonPropertyOrder({"id", "name", "address", "allRoomsIds", "bookedRoomsIds", "incomes"})
@JsonRootName("Hotel")
public class Hotel implements Comparable<Hotel> {
    private static int hotelNo; // Static counter for generating unique IDs
    private int id; // Unique identifier for the hotel
    private String name; // Name of the hotel
    private String address; // Address of the hotel
    private ArrayList<Integer> allRoomsIds; // List of all room IDs in the hotel
    private ArrayList<Integer> bookedRoomsIds; // List of booked room IDs in the hotel
    private double incomes; // Total incomes of the hotel

    /**
     * Generates a unique ID for the hotel.
     * Note: This method is for learning purposes and may not ensure unique IDs in a real-world scenario.
     *
     * @return A unique ID.
     */
    private int generateId() {
        return ++Hotel.hotelNo;
    }

    /**
     * Default constructor that initializes a new hotel with default values.
     */
    public Hotel() {
        this.setId(this.generateId());
        this.setName("");
        this.setAddress("");
        this.setIncomes(0.0);
        this.setAllRoomsIds(new ArrayList<>());
        this.setBookedRoomsIds(new ArrayList<>());
    }

    /**
     * Constructor that initializes a new hotel with the specified name, address, and list of all rooms.
     *
     * @param name     The name of the hotel.
     * @param address  The address of the hotel.
     * @param allRooms The list of all rooms in the hotel.
     */
    public Hotel(String name, String address, ArrayList<Room> allRooms) {
        this.setId(this.generateId());
        this.setName(name);
        this.setAddress(address);
        ArrayList<Integer> rids = new ArrayList<>();
        for (Room room : allRooms) {
            rids.add(room.getId());
        }
        this.setAllRoomsIds(rids);
        this.setBookedRoomsIds(new ArrayList<>());
        this.setIncomes(0.0);
    }

    /**
     * Constructor for deserializing a Hotel object from JSON.
     *
     * @param id            The unique ID of the hotel.
     * @param name          The name of the hotel.
     * @param address       The address of the hotel.
     * @param allRooms      The list of all room IDs in the hotel.
     * @param bookedRooms   The list of booked room IDs in the hotel.
     * @param incomes       The total incomes of the hotel.
     */
    @JsonCreator
    public Hotel(@JsonProperty("id") int id,
                 @JsonProperty("name") String name,
                 @JsonProperty("address") String address,
                 @JsonProperty("allRoomsIds") ArrayList<Integer> allRooms,
                 @JsonProperty("bookedRoomsIds") ArrayList<Integer> bookedRooms,
                 @JsonProperty("incomes") double incomes) {
        this.setId(id);
        this.setName(name);
        this.setAddress(address);
        this.setAllRoomsIds(allRooms);
        this.setBookedRoomsIds(bookedRooms);
        this.setIncomes(incomes);
    }

    /**
     * Gets the unique ID of the hotel.
     *
     * @return The ID of the hotel.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the hotel.
     *
     * @param id The ID to set.
     */
    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the hotel.
     *
     * @return The name of the hotel.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the hotel.
     *
     * @param name The name to set.
     */
    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the address of the hotel.
     *
     * @return The address of the hotel.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the hotel.
     *
     * @param address The address to set.
     */
    @JsonSetter("address")
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the list of all room IDs in the hotel.
     *
     * @return The list of all room IDs.
     */
    public ArrayList<Integer> getAllRoomsIds() {
        return allRoomsIds;
    }

    /**
     * Sets the list of all room IDs in the hotel.
     *
     * @param allRoomsIds The list of all room IDs to set.
     */
    @JsonSetter("allRoomsIds")
    public void setAllRoomsIds(ArrayList<Integer> allRoomsIds) {
        this.allRoomsIds = allRoomsIds;
    }

    /**
     * Gets the list of booked room IDs in the hotel.
     *
     * @return The list of booked room IDs.
     */
    public ArrayList<Integer> getBookedRoomsIds() {
        return bookedRoomsIds;
    }

    /**
     * Sets the list of booked room IDs in the hotel.
     *
     * @param bookedRoomsIds The list of booked room IDs to set.
     */
    @JsonSetter("bookedRoomsIds")
    public void setBookedRoomsIds(ArrayList<Integer> bookedRoomsIds) {
        this.bookedRoomsIds = bookedRoomsIds;
    }

    /**
     * Gets the total incomes of the hotel.
     *
     * @return The incomes of the hotel.
     */
    public double getIncomes() {
        return incomes;
    }

    /**
     * Sets the total incomes of the hotel.
     *
     * @param incomes The incomes to set.
     */
    @JsonSetter("incomes")
    public void setIncomes(double incomes) {
        this.incomes = incomes;
    }

    /**
     * Converts the hotel to a JSON string representation.
     *
     * @return The JSON string representation of the hotel.
     */
    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.fillInStackTrace();
            return "{}";
        }
    }

    /**
     * Compares this hotel to another hotel for ordering.
     *
     * @param o The other hotel to compare to.
     * @return A negative integer, zero, or a positive integer as this hotel is less than, equal to, or greater than the specified hotel.
     */
    @Override
    public int compareTo(Hotel o) {
        int firstCondition, secondCondition = 0, thirdCondition = 0;
        if (this.getId() < o.getId() && this.getName().compareTo(o.getName()) < 0
                && this.getAddress().compareTo(o.getAddress()) < 0 && this.getIncomes() < o.getIncomes()) {
            firstCondition = -1;
        } else if (this.getId() == o.getId() && this.getName().compareTo(o.getName()) == 0
                && this.getAddress().compareTo(o.getAddress()) == 0 && this.getIncomes() == o.getIncomes()) {
            firstCondition = 0;
        } else {
            firstCondition = 1;
        }

        for (int currRoomId : this.allRoomsIds) {
            for (int currORoomId : o.getAllRoomsIds()) {
                secondCondition = Integer.compare(currRoomId, currORoomId);
            }
        }

        for (int currRoomId : this.bookedRoomsIds) {
            for (int currORoomId : o.getBookedRoomsIds()) {
                thirdCondition = Integer.compare(currRoomId, currORoomId);
            }
        }

        if (firstCondition < 0 || secondCondition < 0 || thirdCondition < 0) {
            return -1;
        } else if (firstCondition == 0 && secondCondition == 0 && thirdCondition == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}