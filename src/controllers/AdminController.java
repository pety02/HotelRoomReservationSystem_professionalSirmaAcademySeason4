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
 *
 */
public class AdminController implements IAdminManageable {
    private static final String hotelsFilename = "hotels.txt";
    private static final String reservationsFilename = "reservations.txt";
    private static final String roomsFilename = "rooms.txt";

    /**
     *
     */
    @Override
    public void showAllHotels() {
        File file = new File(AdminController.hotelsFilename);
        HotelReaderWriter hrw = new HotelReaderWriter();
        ArrayList<Hotel> readHotels = new ArrayList<>();
        try (FileReader fr = new FileReader(file)) {
            readHotels = hrw.read(fr, file);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from file with name %s!%n", AdminController.hotelsFilename);
        }

        System.out.println("All Hotels Listed:");
        System.out.println("------------------");
        for(Hotel currHotel : readHotels) {
            System.out.printf("ID: %d | Name: %s | Address: %s | All Rooms: %d | Booked Rooms: %d | Incomes: %.2f%n",
                    currHotel.getId(), currHotel.getName(), currHotel.getAddress(), currHotel.getAllRooms().size(),
                    currHotel.getBookedRooms().size(), currHotel.getIncomes());
        }
    }

    /**
     *
     * @param hotelId
     */
    @Override
    public void viewAllBookings(int hotelId) {
        File reservationFile = new File(AdminController.reservationsFilename);
        ReservationReaderWriter reservationRW = new ReservationReaderWriter();
        ArrayList<Reservation> readReservations = new ArrayList<>();
        try (FileReader fr = new FileReader(reservationFile)) {
            readReservations = reservationRW.read(fr, reservationFile);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from file with name %s!%n", reservationFile.getName());
        }

        File roomsFile = new File(AdminController.roomsFilename);
        RoomReaderWriter roomRW = new RoomReaderWriter();
        ArrayList<Room> readRooms = new ArrayList<>();
        try (FileReader fr = new FileReader(roomsFile)) {
            readRooms = roomRW.read(fr, roomsFile);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from file with name %s!%n", reservationFile.getName());
        }

        for(Reservation currReservation : readReservations) {
            for(Integer currReservationRoomId : currReservation.getRooms().keySet()) {
                for (Room currRoom : readRooms) {
                    if (currRoom.getId() == currReservationRoomId && currRoom.getHotel() == hotelId) {
                        System.out.printf("ID: %d | From: %s | To: %s | Rooms: %d | Total Price: %.2f$ | Cancellation Fees: %.2f$ | Status: %s%n",
                                currReservation.getId(), currReservation.getFromDate(), currReservation.getToDate(),
                                currRoom.getInReservations().size(), currReservation.getTotalPrice(), currReservation.getCancellationFees(),
                                currReservation.isCancelled() ? "cancelled" : "active");
                    }
                }
            }
        }
    }

    /**
     *
     * @param hotelId
     * @return
     * @throws RuntimeException
     */
    @Override
    public double getTotalIncome(int hotelId) throws RuntimeException {
        File file = new File(AdminController.hotelsFilename);
        HotelReaderWriter hrw = new HotelReaderWriter();
        ArrayList<Hotel> readHotels = new ArrayList<>();
        try (FileReader fr = new FileReader(file)) {
            readHotels = hrw.read(fr, file);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from file with name %s!%n", AdminController.hotelsFilename);
        }

        for(Hotel currentHotel : readHotels) {
            if(currentHotel.getId() == hotelId) {
                return currentHotel.getIncomes();
            }
        }

        throw new RuntimeException("Hotel not found!");
    }

    /**
     *
     * @param hotelId
     * @return
     */
    @Override
    public double getCancellationFees(int hotelId) {
        File reservationFile = new File(AdminController.reservationsFilename);
        ReservationReaderWriter reservationRW = new ReservationReaderWriter();
        ArrayList<Reservation> readReservations = new ArrayList<>();
        try (FileReader fr = new FileReader(reservationFile)) {
            readReservations = reservationRW.read(fr, reservationFile);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from file with name %s!%n", reservationFile.getName());
        }

        File roomsFile = new File(AdminController.roomsFilename);
        RoomReaderWriter roomRW = new RoomReaderWriter();
        ArrayList<Room> readRooms = new ArrayList<>();
        try (FileReader fr = new FileReader(roomsFile)) {
            readRooms = roomRW.read(fr, roomsFile);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from file with name %s!%n", reservationFile.getName());
        }

        int cancelledReservationsCount = 0;
        for(Reservation currReservation : readReservations) {
            for(Integer currReservationRoomId : currReservation.getRooms().keySet()) {
                for (Room currRoom : readRooms) {
                    if (currRoom.getId() == currReservationRoomId && currRoom.getHotel() == hotelId && currReservation.isCancelled()) {
                        cancelledReservationsCount++;
                    }
                }
            }
        }

        return cancelledReservationsCount * HotelController.fixedCancellationFee;
    }

    /**
     *
     * @param roomId
     * @param hotelId
     * @param type
     * @param amenities
     * @param maximumOccupancy
     * @param pricePerNight
     * @return
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
     *
     * @param hotelId
     */
    @Override
    public void showAllHotelRooms(int hotelId) {
        File roomsFile = new File(AdminController.roomsFilename);
        RoomReaderWriter roomRW = new RoomReaderWriter();
        ArrayList<Room> readRooms = new ArrayList<>();
        try (FileReader fr = new FileReader(roomsFile)) {
            readRooms = roomRW.read(fr, roomsFile);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            System.out.printf("Cannot read from file with name %s!%n", roomsFile.getName());
        }

        for(Room currRoom : readRooms) {
            if(currRoom.getHotel() == hotelId) {
                System.out.printf("ID: %d | Hotel ID: %d | Type: %s | Amenities: %s | Max Occupancy: %d | Price Per Night (per person): %.2f$ | Total Price: %.2f$ | Status: %s%n",
                        currRoom.getId(), currRoom.getHotel(), currRoom.getType(), Arrays.toString(currRoom.getAmenities().toArray()),
                        currRoom.getMaximumOccupancy(), currRoom.getPricePerNight(), currRoom.getTotalPrice(), currRoom.isBooked() ? "booked" : "free");
            }
        }
    }

    /**
     *
     * @param roomId
     * @return
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
        for(Room currRoom : readRooms) {
            if(currRoom.getId() == roomId) {
                found = true;
                continue;
            }
            toBeWritten.add(currRoom);
        }

        if(found && roomsFile.delete()) {
            roomsFile = new File(AdminController.roomsFilename);
            for(Room currRoom : toBeWritten) {
                roomRW.write(currRoom, roomsFile.getName());
            }
        }

        return found;
    }

    /**
     *
     * @param roomId
     * @param hotelId
     * @param type
     * @param amenities
     * @param maximumOccupancy
     * @param pricePerNight
     * @return
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

        for(Room currRoom : readRooms) {
            if(currRoom.getId() == roomId) {
                found = true;
                currRoom.setHotel(hotelId);
                currRoom.setType(type);
                currRoom.setAmenities(amenities);
                currRoom.setMaximumOccupancy(maximumOccupancy);
                currRoom.setPricePerNight(pricePerNight);
                double total = currRoom.getPricePerNight() * currRoom.getMaximumOccupancy();
                currRoom.setTotalPrice(total);
            }
        }

        if(found && roomsFile.delete()) {
            roomsFile = new File(AdminController.roomsFilename);
            for(Room currRoom : readRooms) {
                roomRW.write(currRoom, roomsFile.getName());
            }
        }

        return found;
    }
}