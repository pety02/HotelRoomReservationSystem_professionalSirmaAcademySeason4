package controllers;

import interfaces.IHotelMangeable;
import models.*;
import readersWriters.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HotelController implements IHotelMangeable {
    private final static String usersFilename = "users.txt";
    private final static String roomsFilename = "rooms.txt";
    private final static String reservationsFilename = "reservations.txt";
    private final static String hotelsFilename = "hotels.txt";
    private final static String debitCardsFilename = "debitCards.txt";
    public final static double fixedCancellationFee = 100.0;

    private <T extends Comparable<T>>void rewrite(ArrayList<T> objs, String filename, Class<T> cl) throws IOException {
        ArrayList<T> readObjs = new ArrayList<>();
        File file = new File(filename);
        ReaderWriter<T> readerWriter;

        // Determine the appropriate ReaderWriter
        if (cl == Room.class) {
            readerWriter = (ReaderWriter<T>) new RoomReaderWriter();
        } else if (cl == Hotel.class) {
            readerWriter = (ReaderWriter<T>) new HotelReaderWriter();
        } else if (cl == User.class) {
            readerWriter = (ReaderWriter<T>) new UserReaderWriter();
        } else if (cl == DebitCard.class) {
            readerWriter = (ReaderWriter<T>) new DebitCardReaderWriter();
        } else if (cl == Reservation.class) {
            readerWriter = (ReaderWriter<T>) new ReservationReaderWriter();
        } else {
            throw new RuntimeException("Unknown abstract T type.");
        }

        // Read the existing objects from the file
        try (FileReader fileReader = new FileReader(file)) {
            readObjs = readerWriter.read(fileReader, file);
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }

        // Delete the existing file
        if (file.delete()) {
            // Merge and update the objects
            for (T obj : objs) {
                boolean found = false;
                for (int i = 0; i < readObjs.size(); i++) {
                    if (readObjs.get(i).compareTo(obj) == 0) {
                        readObjs.set(i, obj); // Update existing object
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    readObjs.add(obj); // Add new object
                }
            }

            // Write all objects back to the file
            try (FileWriter fileWriter = new FileWriter(filename)) {
                for (T readObj : readObjs) {
                    readerWriter.write(readObj, filename);
                }
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        } else {
            throw new IOException("Failed to delete the file.");
        }
    }

    private void rewriteFiles(Room readRoom, Reservation reservation, Hotel currentHotel, User currentUser) throws IOException {
        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(readRoom);
        rewrite(rooms, HotelController.roomsFilename, Room.class);

        ArrayList<Hotel> hotels = new ArrayList<>();
        hotels.add(currentHotel);
        rewrite(hotels, HotelController.hotelsFilename, Hotel.class);

        reservation.setBookedBy(currentUser.getId());
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        rewrite(reservations, HotelController.reservationsFilename, Reservation.class);

        HashMap<Integer, Double> userReservations = new HashMap<>();
        userReservations.put(reservation.getId(), reservation.getTotalPrice());
        currentUser.setReservations(userReservations);
        currentUser.setDebitCard(Map.entry(currentUser.getDebitCard().getKey(), currentUser.getDebitCard().getValue() - reservation.getTotalPrice()));
        ArrayList<User> users = new ArrayList<>();
        users.add(currentUser);
        rewrite(users, HotelController.usersFilename, User.class);

        ArrayList<DebitCard> cards = new ArrayList<>();
        DebitCardReaderWriter drw = new DebitCardReaderWriter();
        File cardsFile = new File(HotelController.debitCardsFilename);
        try(FileReader fileReader = new FileReader(cardsFile)) {
            cards = drw.read(fileReader, cardsFile);
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }
        ArrayList<DebitCard> toBeReWrittenCards = new ArrayList<>();
        for(DebitCard dc : cards) {
            if(dc.getId() == currentUser.getDebitCard().getKey()) {
                dc.setBalance(currentUser.getDebitCard().getValue());
                toBeReWrittenCards.add(dc);
            }
        }
        rewrite(toBeReWrittenCards, HotelController.debitCardsFilename, DebitCard.class);
    }

    /**
     *
     * @param currentHotel
     */
    @Override
    public void viewAllRooms(Hotel currentHotel) {
        System.out.printf("%s's all FREE rooms%n", currentHotel.getName());
        System.out.printf("address: %s%n", currentHotel.getAddress());
        System.out.println("----------------");
        System.out.println();
        RoomReaderWriter rrw = new RoomReaderWriter();
        File file = new File(HotelController.roomsFilename);
        ArrayList<Room> readRooms = new ArrayList<>();
        try (FileReader fr = new FileReader(file)) {
            readRooms = rrw.read(fr, file);
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }
        for (Room room : readRooms) {
            if(currentHotel.getAllRooms().contains(room.getId())) {
                System.out.printf("Room type: %s | Max persons: %d | Price per night (per person): %.2f$ | " +
                                "Total price per night: %.2f$%n", room.getType(),
                        room.getMaximumOccupancy(), room.getPricePerNight(),
                        room.getTotalPrice());
            }
        }
    }

    /**
     *
     * @param fromDate
     * @param toDate
     * @param guestsCount
     * @param currentHotel
     * @throws IllegalArgumentException
     */
    @Override
    public void recommendRooms(LocalDateTime fromDate, LocalDateTime toDate, int guestsCount, Hotel currentHotel)
            throws IllegalArgumentException {
        RoomReaderWriter rrw = new RoomReaderWriter();
        ArrayList<Room> readRooms;
        File file = new File(HotelController.roomsFilename);
        try (FileReader fr = new FileReader(file)) {
            readRooms = rrw.read(fr, file);
            for (Room room : readRooms) {
                if(currentHotel.getBookedRooms().contains(room.getId())) {
                    System.out.println("Sorry but this room is currently booked!");
                    break;
                }
                if (currentHotel.getAllRooms().contains(room.getId())) {
                    for (var isCurrentlyAvailable : room.getBookingAvailability().entrySet()) {
                        if(!isCurrentlyAvailable.getKey()) {
                            continue;
                        }
                        if (room.getMaximumOccupancy() == guestsCount) {
                            if (isCurrentlyAvailable.getValue().getFirst().isBefore(fromDate)
                                    && isCurrentlyAvailable.getValue().getLast().isAfter(toDate)
                                    && isCurrentlyAvailable.getKey()) {
                                System.out.printf("ID: %d | Room type: %s | Max persons: %d | Price per night (per person): %.2f$ | " +
                                                "Total price per night: %.2f$ | Status: %s%n", room.getId(), room.getType(),
                                        room.getMaximumOccupancy(), room.getPricePerNight(),
                                        room.getTotalPrice(), "free");
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }
    }

    /**
     *
     * @param id
     * @param currentHotel
     * @param fromDate
     * @param toDate
     * @param bookedBy
     * @param cancellationFees
     * @throws IOException
     */
    @Override
    public void bookRoom(int id, Hotel currentHotel, LocalDateTime fromDate, LocalDateTime toDate, User bookedBy, double cancellationFees) throws IOException {
        RoomReaderWriter rrw = new RoomReaderWriter();
        File file = new File(HotelController.roomsFilename);
        ArrayList<Room> readRooms = new ArrayList<>();
        try (FileReader fr = new FileReader(file);) {
            readRooms = rrw.read(fr, file);
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }
        boolean isBooked = false;
        for (Integer currentRoomId : currentHotel.getAllRooms()) {
            if(isBooked) {
                break;
            }
            for (Room room : readRooms) {
                if (room.getId() == currentRoomId && currentRoomId == id && !room.isBooked()) {
                    // not currently available room
                    room.getBookingAvailability().get(true).remove(fromDate);
                    room.getBookingAvailability().get(true).remove(toDate);
                    room.getBookingAvailability().get(false).add(fromDate);
                    room.getBookingAvailability().get(false).add(toDate);
                    // booked room
                    room.setBooked(true);
                    currentHotel.getBookedRooms().add(room.getId());

                    Reservation reservation = new Reservation(fromDate, toDate, cancellationFees, false);
                    reservation.getRooms().put(room.getId(), room.getTotalPrice());
                    int days = toDate.getDayOfYear() - fromDate.getDayOfYear();
                    reservation.setTotalPrice(reservation.calculateTotalPrice(days));
                    room.getInReservations().add(reservation.getId());

                    if(bookedBy.getDebitCard().getValue() < reservation.getTotalPrice()) {
                        System.out.println("Sorry, but you haven't enough money! Try again later...");
                        return;
                    }
                    TransactionController tc = new TransactionController(currentHotel);
                    if(tc.makeTransaction(bookedBy.getDebitCard().getKey(), reservation.getTotalPrice())) {

                        rewriteFiles(room, reservation, currentHotel, bookedBy);

                        System.out.printf("Congratulations! You've just booked room with ID: %d%n", room.getId());
                        isBooked = true;
                        break;
                    }
                }
            }
        }

        if(!isBooked) {
            System.out.println("Sorry but the reservation was not successful!");
        }
    }

    /**
     *
     * @param currentHotel
     * @throws IOException
     */
    @Override
    public void freeRooms(Hotel currentHotel) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        RoomReaderWriter rrw = new RoomReaderWriter();
        File file = new File(HotelController.roomsFilename);
        ArrayList<Room> readRooms = new ArrayList<>();
        try (FileReader fr = new FileReader(file)) {
            readRooms = rrw.read(fr, file);
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }

        for (Room room : readRooms) {
            for (var currentStatus : room.getBookingAvailability().entrySet()) {
                if (!currentStatus.getKey() && currentStatus.getValue().contains(now)) {
                    currentStatus.getValue().add(now); // available for this period
                    room.setBooked(false); // not booked room

                    rewrite(readRooms, HotelController.roomsFilename, Room.class);

                    ArrayList<Hotel> hotels = new ArrayList<>();
                    hotels.add(currentHotel);
                    rewrite(hotels, HotelController.hotelsFilename, Hotel.class);

                    ArrayList<Reservation> reservations = new ArrayList<>();
                    ReservationReaderWriter reservrw = new ReservationReaderWriter();
                    try(FileReader fr = new FileReader(HotelController.reservationsFilename)) {
                        reservations = reservrw.read(fr, file);
                    } catch (IOException ex) {
                        ex.fillInStackTrace();
                        System.out.printf("Cannot read from file with name %s!%n", HotelController.roomsFilename);
                    }

                    ArrayList<Reservation> toBeReWritten = new ArrayList<>();
                    for (Integer hotelReservationId : currentHotel.getBookedRooms()) {
                        for (Reservation currReservation : reservations) {
                            if (hotelReservationId == currReservation.getId()) {
                                toBeReWritten.add(currReservation);
                            }
                        }
                    }
                    rewrite(toBeReWritten, HotelController.reservationsFilename, Reservation.class);
                }
            }
        }
    }

    /**
     *
     * @param reservationId
     * @param currentUser
     * @param currentHotel
     * @throws RuntimeException
     */
    @Override
    public void cancelReservation(int reservationId, User currentUser, Hotel currentHotel) throws RuntimeException {
        for (Map.Entry<Integer, Double> currentReservation : currentUser.getReservations().entrySet()) {
            if (currentReservation.getKey() == reservationId) {
                double totalPrice = HotelController.fixedCancellationFee;
                double userTotalMoney = currentUser.getDebitCard().getValue();
                if (userTotalMoney < totalPrice) {
                    throw new RuntimeException("Sorry, you have enough money! You cannot cancel your reservation!");
                }

                ReservationReaderWriter rrw = new ReservationReaderWriter();
                ArrayList<Reservation> readReservations;
                File file = new File(HotelController.reservationsFilename);
                try (FileReader fr = new FileReader(file)) {
                    readReservations = rrw.read(fr, file);
                    if (readReservations.isEmpty()) {
                        System.out.println("There is no read reservations.");
                    } else {
                        RoomReaderWriter roomrw = new RoomReaderWriter();
                        ArrayList<Room> readRooms = new ArrayList<>();

                        File roomsFile = new File(HotelController.roomsFilename);
                        try (FileReader roomfr = new FileReader(roomsFile)) {
                            readRooms = roomrw.read(roomfr, roomsFile);
                        } catch (IOException ex) {
                            ex.fillInStackTrace();
                        }
                        for (Reservation reservation : readReservations) {
                            for (Integer cr : reservation.getRooms().keySet()) {
                                if (readRooms.isEmpty()) {
                                    System.out.println("There is no read rooms.");
                                } else {
                                    for (Room room : readRooms) {
                                        for (Map.Entry<Boolean, ArrayList<LocalDateTime>> isAvailable : room.getBookingAvailability().entrySet()) {
                                            if (reservation.getId() == cr
                                                    && cr == reservationId
                                                    && reservation.getBookedBy() == currentUser.getId()
                                                    && isAvailable.getValue() == isAvailable.getValue()) {
                                                room.setBooked(false);
                                                reservation.setCancelled(true);

                                                TransactionController tc = new TransactionController(currentHotel);
                                                if(tc.makeTransaction(currentUser.getDebitCard().getKey(), totalPrice)) {
                                                    rewriteFiles(room, reservation, currentHotel, currentUser);
                                                    System.out.println("Successful cancelled booking!");
                                                    return;
                                                }
                                            }
                                        }
                                    }

                                    System.out.println("Sorry, but booking cancellation was not successful!");
                                }
                            }
                        }
                    }
                } catch (IOException ex) {
                    ex.fillInStackTrace();
                    System.out.printf("Cannot read from file with name %s!%n", file.getName());
                }
            }
        }
    }
}