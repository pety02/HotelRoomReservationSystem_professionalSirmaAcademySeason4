package controllers;

import interfaces.IAdminManageable;
import models.Hotel;
import models.Reservation;
import models.Room;
import readersWriters.HotelReaderWriter;
import readersWriters.ReservationReaderWriter;
import readersWriters.RoomReaderWriter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AdminController implements IAdminManageable {
    private static final String hotelsFilename = "hotels.txt";
    private static final String reservationsFilename = "reservations.txt";
    private static final String roomsFilename = "rooms.txt";

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
}