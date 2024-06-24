package models;

import com.fasterxml.jackson.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@JsonPropertyOrder({})
@JsonRootName("Hotel")
public class Hotel {
    private static int hotelNo;
    private int id;
    private String name;
    private String address;
    private ArrayList<Room> allRooms;
    private ArrayList<Room> bookedRooms;
    private double incomes;

    private int generateId() {
        return ++Hotel.hotelNo;
    }

    public Hotel() {
    }

    public Hotel(String name, String address, ArrayList<Room> allRooms) {
        this.name = name;
        this.address = address;
        this.allRooms = allRooms;
    }

    @JsonCreator
    public Hotel(@JsonProperty("") int id,
                 @JsonProperty("") String name,
                 @JsonProperty("") String address,
                 @JsonProperty("") ArrayList<Room> allRooms,
                 @JsonProperty("") ArrayList<Room> bookedRooms,
                 @JsonProperty("") double incomes) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.allRooms = allRooms;
        this.bookedRooms = bookedRooms;
        this.incomes = incomes;
    }

    public int getId() {
        return id;
    }

    @JsonSetter("")
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @JsonSetter("")
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    @JsonSetter("")
    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Room> getAllRooms() {
        return allRooms;
    }

    @JsonSetter("")
    public void setAllRooms(ArrayList<Room> allRooms) {
        this.allRooms = allRooms;
    }

    public ArrayList<Room> getBookedRooms() {
        return bookedRooms;
    }

    @JsonSetter("")
    public void setBookedRooms(ArrayList<Room> bookedRooms) {
        this.bookedRooms = bookedRooms;
    }

    public double getIncomes() {
        return incomes;
    }

    @JsonSetter("")
    public void setIncomes(double incomes) {
        this.incomes = incomes;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", allRooms=" + allRooms +
                ", bookedRooms=" + bookedRooms +
                ", incomes=" + incomes +
                '}';
    }
}