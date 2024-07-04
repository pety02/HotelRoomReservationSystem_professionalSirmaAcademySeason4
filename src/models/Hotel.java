package models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

@JsonPropertyOrder({"id", "name", "address", "allRooms", "bookedRooms", "incomes"})
@JsonRootName("Hotel")
public class Hotel implements Comparable<Hotel> {
    private static int hotelNo;
    private int id;
    private String name;
    private String address;
    private ArrayList<Integer> allRooms;
    private ArrayList<Integer> bookedRooms;
    private double incomes;

    private int generateId() {
        return ++Hotel.hotelNo;
    }

    public Hotel() {
    }

    public Hotel(String name, String address, ArrayList<Room> allRooms) {
        this.setId(this.generateId());
        this.setName(name);
        this.setAddress(address);
        ArrayList<Integer> rids = new ArrayList<>();
        for(Room room : allRooms) {
            rids.add(room.getId());
        }
        this.setAllRooms(rids);
        this.setBookedRooms(new ArrayList<>());
        this.setIncomes(0.0);
    }

    @JsonCreator
    public Hotel(@JsonProperty("id") int id,
                 @JsonProperty("name") String name,
                 @JsonProperty("address") String address,
                 @JsonProperty("allRooms") ArrayList<Integer> allRooms,
                 @JsonProperty("bookedRooms") ArrayList<Integer> bookedRooms,
                 @JsonProperty("incomes") double incomes) {
        this.setId(id);
        this.setName(name);
        this.setAddress(address);
        this.setAllRooms(allRooms);
        this.setBookedRooms(bookedRooms);
        this.setIncomes(incomes);
    }

    public int getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    @JsonSetter("address")
    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Integer> getAllRooms() {
        return allRooms;
    }

    @JsonSetter("allRooms")
    public void setAllRooms(ArrayList<Integer> allRooms) {
        this.allRooms = allRooms;
    }

    public ArrayList<Integer> getBookedRooms() {
        return bookedRooms;
    }

    @JsonSetter("bookedRooms")
    public void setBookedRooms(ArrayList<Integer> bookedRooms) {
        this.bookedRooms = bookedRooms;
    }

    public double getIncomes() {
        return incomes;
    }

    @JsonSetter("incomes")
    public void setIncomes(double incomes) {
        this.incomes = incomes;
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

    public static void main(String[] args) {
        Hotel h = new Hotel();
        System.out.println(h);
    }

    @Override
    public int compareTo(Hotel o) {
        int firstCondition = 0, secondCondition = 0, thirdCondition = 0;
        if(this.getId() < o.getId() && this.getName().compareTo(o.getName()) < 0
            && this.getAddress().compareTo(o.getAddress()) < 0 && this.getIncomes() < o.getIncomes()) {
            firstCondition = -1;
        } else if(this.getId() == o.getId() && this.getName().compareTo(o.getName()) == 0
                && this.getAddress().compareTo(o.getAddress()) == 0 && this.getIncomes() == o.getIncomes()) {
            firstCondition = 0;
        } else {
            firstCondition = 1;
        }

        for(int currRoomId : this.allRooms) {
            for(int currORoomId : o.getAllRooms()) {
                secondCondition = Integer.compare(currRoomId, currORoomId);
            }
        }

        for(int currRoomId : this.bookedRooms) {
            for(int currORoomId : o.getBookedRooms()) {
                thirdCondition = Integer.compare(currRoomId, currORoomId);
            }
        }

        if(firstCondition < 0 || secondCondition < 0 || thirdCondition < 0) {
            return -1;
        } else if (firstCondition == 0 && secondCondition == 0 && thirdCondition == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}