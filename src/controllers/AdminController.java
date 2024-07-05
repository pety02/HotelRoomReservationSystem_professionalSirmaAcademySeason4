package controllers;

import interfaces.IAdminManageable;
import models.Hotel;
import models.Reservation;
import models.Room;
import readersWriters.HotelReaderWriter;
import readersWriters.ReservationReaderWriter;
import readersWriters.RoomReaderWriter;
import types.RoomType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The AdminController class implements the IAdminManageable interface and provides methods for managing hotels, reservations, and rooms.
 * This class handles reading and writing operations for hotels, reservations, and rooms from/to text files.
 */
public class AdminController implements IAdminManageable {
    private static final String hotelsFilename = "hotels.txt";
    private static final String reservationsFilename = "reservations.txt";
    private static final String roomsFilename = "rooms.txt";

    private static ArrayList<Room> readAllRooms() {
        File roomsFile = new File(AdminController.roomsFilename);
        RoomReaderWriter roomRW = new RoomReaderWriter();
        ArrayList<Room> readRooms = new ArrayList<>();
        try (FileReader fr = new FileReader(roomsFile)) {
            readRooms = roomRW.read(fr, roomsFile);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from file with name %s!%n", roomsFile.getName());
        }
        return readRooms;
    }

    private static ArrayList<Reservation> readAllReservations() {
        File reservationFile = new File(AdminController.reservationsFilename);
        ReservationReaderWriter reservationRW = new ReservationReaderWriter();
        ArrayList<Reservation> readReservations = new ArrayList<>();
        try (FileReader fr = new FileReader(reservationFile)) {
            readReservations = reservationRW.read(fr, reservationFile);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from file with name %s!%n", reservationFile.getName());
        }
        return readReservations;
    }

    /**
     * Displays all hotels by reading from the hotels text file.
     */
    @Override
    public void showAllHotels() {
        ArrayList<Hotel> readHotels = readAllHotels();

        System.out.println("All Hotels Listed:");
        System.out.println("------------------");
        for (Hotel currHotel : readHotels) {
            System.out.printf("ID: %d | Name: %s | Address: %s | All Rooms: %d | Booked Rooms: %d | Incomes: %.2f%n",
                    currHotel.getId(), currHotel.getName(), currHotel.getAddress(), currHotel.getAllRoomsIds().size(),
                    currHotel.getBookedRoomsIds().size(), currHotel.getIncomes());
        }
    }

    /**
     * Displays all bookings for a given hotel by reading from the reservations and rooms text files.
     *
     * @param hotelId the ID of the hotel
     */
    @Override
    public void viewAllBookings(int hotelId) {
        ArrayList<Reservation> readReservations = readAllReservations();
        ArrayList<Room> readRooms = readAllRooms();

        for (Reservation currReservation : readReservations) {
            for (Integer currReservationRoomId : currReservation.getRoomsIds().keySet()) {
                for (Room currRoom : readRooms) {
                    if (currRoom.getId() == currReservationRoomId && currRoom.getHotelId() == hotelId) {
                        System.out.printf("ID: %d | From: %s | To: %s | Rooms: %d | Total Price: %.2f$ | Cancellation Fees: %.2f$ | Status: %s%n",
                                currReservation.getId(), currReservation.getFromDate(), currReservation.getToDate(),
                                currRoom.getInReservationsIds().size(), currReservation.getTotalPrice(), currReservation.getCancellationFees(),
                                currReservation.isCancelled() ? "cancelled" : "active");
                    }
                }
            }
        }
    }

    /**
     * Retrieves the total income for a given hotel by reading from the hotels text file.
     *
     * @param hotelId the ID of the hotel
     * @return the total income of the hotel
     * @throws RuntimeException if the hotel is not found
     */
    @Override
    public double getTotalIncome(int hotelId) throws RuntimeException {
        ArrayList<Hotel> readHotels = readAllHotels();

        for (Hotel currentHotel : readHotels) {
            if (currentHotel.getId() == hotelId) {
                return currentHotel.getIncomes();
            }
        }

        throw new RuntimeException("Hotel not found!");
    }

    private ArrayList<Hotel> readAllHotels() {
        File file = new File(AdminController.hotelsFilename);
        HotelReaderWriter hrw = new HotelReaderWriter();
        ArrayList<Hotel> readHotels = new ArrayList<>();
        try (FileReader fr = new FileReader(file)) {
            readHotels = hrw.read(fr, file);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from file with name %s!%n", AdminController.hotelsFilename);
        }

        return readHotels;
    }

    /**
     * Retrieves the total cancellation fees for a given hotel by reading from the reservations and rooms text files.
     *
     * @param hotelId the ID of the hotel
     * @return the total cancellation fees for the hotel
     */
    @Override
    public double getCancellationFees(int hotelId) {
        ArrayList<Reservation> readReservations = readAllReservations();
        ArrayList<Room> readRooms = readAllRooms();

        int cancelledReservationsCount = 0;
        for (Reservation currReservation : readReservations) {
            for (Integer currReservationRoomId : currReservation.getRoomsIds().keySet()) {
                for (Room currRoom : readRooms) {
                    if (currRoom.getId() == currReservationRoomId && currRoom.getHotelId() == hotelId && currReservation.isCancelled()) {
                        cancelledReservationsCount++;
                    }
                }
            }
        }

        return cancelledReservationsCount * HotelController.fixedCancellationFee;
    }

    /**
     * Adds a new room to a given hotel by writing to the rooms text file.
     *
     * @param roomId           the ID of the room
     * @param hotelId          the ID of the hotel
     * @param type             the type of the room
     * @param amenities        the amenities provided in the room
     * @param maximumOccupancy the maximum occupancy of the room
     * @param pricePerNight    the price per night for the room
     * @return true if the room is successfully added
     */
    @Override
    public boolean addRoom(int roomId, int hotelId, RoomType type, ArrayList<String> amenities, int maximumOccupancy, double pricePerNight) {
        HashMap<Boolean, ArrayList<LocalDateTime>> bookingAvailability = new HashMap<>();
        Room room = new Room(roomId, hotelId, type, amenities, maximumOccupancy, false, bookingAvailability, pricePerNight, new ArrayList<>());
        File roomsFile = new File(AdminController.roomsFilename);
        RoomReaderWriter roomRW = new RoomReaderWriter();
        roomRW.write(room, roomsFile.getName());
        return true;
    }

    /**
     * Displays all rooms for a given hotel by reading from the rooms text file.
     *
     * @param hotelId the ID of the hotel
     */
    @Override
    public void showAllHotelRooms(int hotelId) {
        ArrayList<Room> readRooms = readAllRooms();

        for (Room currRoom : readRooms) {
            if (currRoom.getHotelId() == hotelId) {
                System.out.printf("ID: %d | Hotel ID: %d | Type: %s | Amenities: %s | Max Occupancy: %d | Price Per Night (per person): %.2f$ | Total Price: %.2f$ | Status: %s%n",
                        currRoom.getId(), currRoom.getHotelId(), currRoom.getType(), Arrays.toString(currRoom.getAmenities().toArray()),
                        currRoom.getMaximumOccupancy(), currRoom.getPricePerNight(), currRoom.getTotalPrice(), currRoom.isBooked() ? "booked" : "free");
            }
        }
    }

    /**
     * Removes a room by its ID by modifying the rooms text file.
     *
     * @param roomId the ID of the room
     * @return true if the room is found and removed, false otherwise
     */
    @Override
    public boolean removeRoom(int roomId) {
        boolean found = false;
        File roomsFile = new File(AdminController.roomsFilename);
        RoomReaderWriter roomRW = new RoomReaderWriter();
        ArrayList<Room> readRooms = new ArrayList<>();
        try (FileReader fr = new FileReader(roomsFile)) {
            readRooms = roomRW.read(fr, roomsFile);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from file with name %s!%n", roomsFile.getName());
        }

        ArrayList<Room> toBeWritten = new ArrayList<>();
        for (Room currRoom : readRooms) {
            if (currRoom.getId() == roomId) {
                found = true;
                continue;
            }
            toBeWritten.add(currRoom);
        }

        if (found && roomsFile.delete()) {
            roomsFile = new File(AdminController.roomsFilename);
            for (Room currRoom : toBeWritten) {
                roomRW.write(currRoom, roomsFile.getName());
            }
        }

        return found;
    }

    /**
     * Updates the details of a room by its ID by modifying the rooms text file.
     *
     * @param roomId           the ID of the room
     * @param hotelId          the ID of the hotel
     * @param type             the type of the room
     * @param amenities        the amenities provided in the room
     * @param maximumOccupancy the maximum occupancy of the room
     * @param pricePerNight    the price per night for the room
     * @return true if the room is found and updated, false otherwise
     */
    @Override
    public boolean updateRoom(int roomId, int hotelId, RoomType type, ArrayList<String> amenities, int maximumOccupancy,
                              double pricePerNight) {
        boolean found = false;
        File roomsFile = new File(AdminController.roomsFilename);
        RoomReaderWriter roomRW = new RoomReaderWriter();
        ArrayList<Room> readRooms = new ArrayList<>();
        try (FileReader fr = new FileReader(roomsFile)) {
            readRooms = roomRW.read(fr, roomsFile);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from file with name %s!%n", roomsFile.getName());
        }

        for (Room currRoom : readRooms) {
            if (currRoom.getId() == roomId) {
                found = true;
                currRoom.setHotelId(hotelId);
                currRoom.setType(type);
                currRoom.setAmenities(amenities);
                currRoom.setMaximumOccupancy(maximumOccupancy);
                currRoom.setPricePerNight(pricePerNight);
                double total = currRoom.getPricePerNight() * currRoom.getMaximumOccupancy();
                currRoom.setTotalPrice(total);
            }
        }

        if (found && roomsFile.delete()) {
            roomsFile = new File(AdminController.roomsFilename);
            for (Room currRoom : readRooms) {
                roomRW.write(currRoom, roomsFile.getName());
            }
        }

        return found;
    }
}