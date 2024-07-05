package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import utils.LocalDateTimeMapDeserializer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * The Reservation class represents a hotel reservation with fields for ID, dates, booked rooms,
 * booking user ID, cancellation fees, total price, and cancellation status.
 * It supports JSON serialization/deserialization and implements Comparable for sorting.
 */
@JsonPropertyOrder({"id", "fromDate", "toDate", "roomsIds", "bookedById", "cancellationFees", "totalPrice", "isCancelled"})
@JsonRootName("Reservation")
public class Reservation implements Comparable<Reservation> {
    private static int reservationNo = 0; // Static counter for generating unique reservation IDs
    private int id; // Unique identifier for the reservation
    private LocalDateTime fromDate; // Start date of the reservation
    private LocalDateTime toDate; // End date of the reservation
    private Map<Integer, Double> roomsIds; // Map of room IDs to booking prices
    private int bookedById; // ID of the user who booked the reservation
    private double cancellationFees; // Fees charged if the reservation is cancelled
    private double totalPrice; // Total price of the reservation
    private boolean isCancelled; // Flag indicating if the reservation is cancelled

    /**
     * Generates a unique ID for the reservation.
     * Note: This method is for learning purposes and may not ensure unique IDs in a real-world scenario.
     *
     * @return A unique ID.
     */
    private int generateId() {
        return ++Reservation.reservationNo;
    }

    /**
     * Default constructor that initializes a new reservation with default values.
     */
    public Reservation() {
        this.setFromDate(LocalDateTime.now());
        this.setToDate(LocalDateTime.now());
        this.setRoomsIds(new HashMap<>());
        this.setBookedById(0);
        this.setCancellationFees(0.0);
        this.setTotalPrice(0.0);
        this.setCancelled(false);
        this.setId(this.generateId());
    }

    /**
     * Constructor that initializes a new reservation with specified dates, booked rooms, booking user,
     * cancellation fees, and cancellation status.
     *
     * @param fromDate         The start date of the reservation.
     * @param toDate           The end date of the reservation.
     * @param rooms            The map of room IDs to booking prices.
     * @param bookedBy         The user who booked the reservation.
     * @param cancellationFees The fees charged if the reservation is cancelled.
     * @param isCancelled      The cancellation status of the reservation.
     */
    public Reservation(LocalDateTime fromDate,
                       LocalDateTime toDate,
                       Map<Integer, Double> rooms,
                       User bookedBy,
                       double cancellationFees,
                       boolean isCancelled) {
        this.setFromDate(fromDate);
        this.setToDate(toDate);
        this.setRoomsIds(rooms);
        this.setBookedById(bookedBy.getId());
        this.setCancellationFees(cancellationFees);
        int days = toDate.getDayOfYear() - fromDate.getDayOfYear();
        this.setTotalPrice(this.calculateTotalPrice(days));
        this.setCancelled(isCancelled);
        this.setId(this.generateId());
    }

    /**
     * Constructor that initializes a new reservation with specified dates, cancellation fees, and cancellation status.
     *
     * @param fromDate         The start date of the reservation.
     * @param toDate           The end date of the reservation.
     * @param cancellationFees The fees charged if the reservation is cancelled.
     * @param isCancelled      The cancellation status of the reservation.
     */
    public Reservation(LocalDateTime fromDate,
                       LocalDateTime toDate,
                       double cancellationFees,
                       boolean isCancelled) {
        this.setId(this.generateId());
        this.setFromDate(fromDate);
        this.setToDate(toDate);
        this.setRoomsIds(new HashMap<>());
        this.setBookedById(0);
        this.setCancellationFees(cancellationFees);
        int days = toDate.getDayOfYear() - fromDate.getDayOfYear();
        this.setTotalPrice(this.calculateTotalPrice(days));
        this.setCancelled(isCancelled);
    }

    /**
     * Constructor for deserializing a Reservation object from JSON.
     *
     * @param id                The unique ID of the reservation.
     * @param fromDate          The start date of the reservation.
     * @param toDate            The end date of the reservation.
     * @param rooms             The map of room IDs to booking prices.
     * @param bookedBy          The ID of the user who booked the reservation.
     * @param cancellationFees  The cancellation fees.
     * @param totalPrice        The total price of the reservation.
     * @param isCancelled       The cancellation status of the reservation.
     */
    @JsonCreator
    public Reservation(@JsonProperty("id") int id,
                       @JsonProperty("fromDate") LocalDateTime fromDate,
                       @JsonProperty("toDate") LocalDateTime toDate,
                       @JsonProperty("roomsIds") Map<Integer, Double> rooms,
                       @JsonProperty("bookedById") int bookedBy,
                       @JsonProperty("cancellationFees") double cancellationFees,
                       @JsonProperty("totalPrice") double totalPrice,
                       @JsonProperty("isCancelled") boolean isCancelled) {
        this.setId(id);
        this.setFromDate(fromDate);
        this.setToDate(toDate);
        this.setRoomsIds(rooms);
        this.setBookedById(bookedBy);
        this.setCancellationFees(cancellationFees);
        this.setTotalPrice(totalPrice);
        this.setCancelled(isCancelled);
    }

    /**
     * Gets the unique ID of the reservation.
     *
     * @return The ID of the reservation.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the reservation.
     *
     * @param id The ID to set.
     */
    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the start date of the reservation.
     *
     * @return The start date of the reservation.
     */
    public LocalDateTime getFromDate() {
        return fromDate;
    }

    /**
     * Sets the start date of the reservation.
     *
     * @param fromDate The start date to set.
     */
    @JsonSetter("fromDate")
    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Gets the end date of the reservation.
     *
     * @return The end date of the reservation.
     */
    public LocalDateTime getToDate() {
        return toDate;
    }

    /**
     * Sets the end date of the reservation.
     *
     * @param toDate The end date to set.
     */
    @JsonSetter("toDate")
    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    /**
     * Gets the map of room IDs to booking prices.
     *
     * @return The map of room IDs to booking prices.
     */
    @JsonSetter("roomsIds")
    public Map<Integer, Double> getRoomsIds() {
        return roomsIds;
    }

    /**
     * Sets the map of room IDs to booking prices.
     *
     * @param roomsIds The map of room IDs to booking prices to set.
     */
    public void setRoomsIds(Map<Integer, Double> roomsIds) {
        this.roomsIds = roomsIds;
    }

    /**
     * Gets the ID of the user who booked the reservation.
     *
     * @return The ID of the user who booked the reservation.
     */
    public int getBookedById() {
        return bookedById;
    }

    /**
     * Sets the ID of the user who booked the reservation.
     *
     * @param bookedById The ID of the user who booked the reservation to set.
     */
    @JsonSetter("bookedById")
    public void setBookedById(int bookedById) {
        this.bookedById = bookedById;
    }

    /**
     * Gets the cancellation fees of the reservation.
     *
     * @return The cancellation fees of the reservation.
     */
    public double getCancellationFees() {
        return cancellationFees;
    }

    /**
     * Sets the cancellation fees of the reservation.
     *
     * @param cancellationFees The cancellation fees to set.
     */
    @JsonSetter("cancellationFees")
    public void setCancellationFees(double cancellationFees) {
        this.cancellationFees = cancellationFees;
    }

    /**
     * Gets the total price of the reservation.
     *
     * @return The total price of the reservation.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the total price of the reservation.
     *
     * @param totalPrice The total price to set.
     */
    @JsonSetter("totalPrice")
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Checks if the reservation is cancelled.
     *
     * @return True if the reservation is cancelled, false otherwise.
     */
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * Sets the cancellation status of the reservation.
     *
     * @param cancelled True to cancel the reservation, false otherwise.
     */
    @JsonSetter("isCancelled")
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    /**
     * Calculates the total price of the reservation based on the number of days.
     *
     * @param days The number of days of the reservation.
     * @return The total price of the reservation.
     */
    public double calculateTotalPrice(int days) {
        double total = 0.0;
        for (Map.Entry<Integer, Double> bookedRoom : this.roomsIds.entrySet()) {
            total += (bookedRoom.getValue() * days);
        }
        return total;
    }

    /**
     * Converts the reservation to a JSON string representation.
     *
     * @return The JSON string representation of the reservation.
     */
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

    /**
     * Compares this reservation to another reservation for ordering.
     *
     * @param o The other reservation to compare to.
     * @return A negative integer, zero, or a positive integer as this reservation is less than, equal to, or greater than the specified reservation.
     */
    @Override
    public int compareTo(Reservation o) {
        int firstCondition, secondCondition = 0;

        // Compare ID, dates, total price, cancellation fees, and bookedById
        if (this.getId() < o.getId() && this.getFromDate().isBefore(o.getFromDate())
                && this.getToDate().isBefore(o.getToDate()) && this.getTotalPrice() < o.getTotalPrice()
                && this.getCancellationFees() < o.getCancellationFees() && this.getBookedById() < o.getBookedById()) {
            firstCondition = -1;
        } else if (this.getId() == o.getId() && this.getFromDate().equals(o.getFromDate())
                && this.getToDate().equals(o.getToDate()) && this.getTotalPrice() == o.getTotalPrice()
                && this.getCancellationFees() == o.getCancellationFees() && this.getBookedById() == o.getBookedById()) {
            firstCondition = 0;
        } else {
            firstCondition = 1;
        }

        // Compare roomsIds map
        for (Map.Entry<Integer, Double> currentRoomId : this.getRoomsIds().entrySet()) {
            for (Map.Entry<Integer, Double> otherRoomId : o.getRoomsIds().entrySet()) {
                if (currentRoomId.getKey().compareTo(otherRoomId.getKey()) < 0
                        && Double.compare(currentRoomId.getValue(), otherRoomId.getValue()) < 0) {
                    secondCondition = -1;
                } else if (currentRoomId.getKey().compareTo(otherRoomId.getKey()) == 0
                        && Double.compare(currentRoomId.getValue(), otherRoomId.getValue()) == 0) {
                    secondCondition = 0;
                } else {
                    secondCondition = 1;
                }
            }
        }

        if (firstCondition < 0 || secondCondition < 0) {
            return -1;
        } else if (firstCondition == 0 && secondCondition == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}