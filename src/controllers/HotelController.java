package controllers;

import interfaces.IHotelMangeable;
import models.*;
import readersWriters.*;

import javax.imageio.IIOException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HotelController implements IHotelMangeable {
    private final static String usersFilename = "users.txt";
    private final static String roomsFilename = "rooms.txt";
    private final static String reservationsFilename = "reservations.txt";
    private final static String hotelsFilename = "hotels.txt";
    private final static String debitCardsFilename = "debitCards.txt";

    @Override
    public void viewAllRooms(Hotel currentHotel) {
        System.out.printf("%s's all FREE rooms%n", currentHotel.getName());
        System.out.printf("address: %s%n", currentHotel.getAddress());
        System.out.println("----------------");
        System.out.println();
        for(Integer currentRoom : currentHotel.getAllRooms()) {
            RoomReaderWriter rrw = new RoomReaderWriter();
            File file = new File(HotelController.roomsFilename);
            Room readRoom;
            try(FileReader fr = new FileReader(file)) {
                readRoom = rrw.read(fr, file);
                System.out.printf("Room type: %s | Max persons: %d | Price per night (per person): %.2f$ | " +
                                "Total price per night: %.2f$%n", readRoom.getType(),
                        readRoom.getMaximumOccupancy(), readRoom.getPricePerNight(),
                        readRoom.getTotalPrice());
            } catch (IOException ex) {
                ex.fillInStackTrace();
            }
        }
    }

    @Override
    public void recommendRooms(LocalDateTime fromDate, LocalDateTime toDate, int guestsCount, Hotel currentHotel)
            throws IllegalArgumentException{
        // TODO: to think period of stay calculations better
        int periodOfStay = toDate.getDayOfMonth() - fromDate.getDayOfMonth();
        if(periodOfStay < 0) {
            throw new IllegalArgumentException("Invalid dates of stay! The final date should be after the beginning date!");
        }

        RoomReaderWriter rrw = new RoomReaderWriter();
        Room currentRoom;
        File file = new File(HotelController.roomsFilename);
        try(FileReader fr = new FileReader(file)) {
            for (Integer currentRoomId : currentHotel.getAllRooms()) {
                currentRoom = rrw.read(fr, file);
                if(currentRoom.getId() == currentRoomId) {
                    for (var isCurrentlyAvailable : currentRoom.getBookingAvailability().entrySet()) {
                        if (currentRoom.getMaximumOccupancy() == guestsCount) {
                            int days = (toDate.getDayOfMonth() - fromDate.getDayOfMonth());
                            // TODO: to think for better validation of period of stay.
                            if(days < 0) {
                                throw new IIOException("Invalid period of stay!");
                            }
                            for (int i = 0; i < days; i++) {
                                LocalDateTime currDate = LocalDateTime.of(fromDate.getYear(), fromDate.getMonthValue(), (fromDate.getDayOfMonth() + i), fromDate.getHour(), fromDate.getMinute(), fromDate.getSecond());
                                if(isCurrentlyAvailable.getValue().contains(currDate) && isCurrentlyAvailable.getKey()) {
                                    System.out.printf("ID: %d | Room type: %s | Max persons: %d | Price per night (per person): %.2f$ | " +
                                                    "Total price per night: %.2f$ | Status: %s%n", currentRoom.getId(), currentRoom.getType(),
                                            currentRoom.getMaximumOccupancy(), currentRoom.getPricePerNight(),
                                            currentRoom.getTotalPrice(), "free");
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }
    }

    @Override
    public void bookRoom(int id, Hotel currentHotel, LocalDateTime fromDate, LocalDateTime toDate, User bookedBy, double cancellationFees) {
        for (Integer currentRoomId : currentHotel.getAllRooms()) {
            if(currentRoomId == id) {
                RoomReaderWriter rrw = new RoomReaderWriter();
                File file = new File(HotelController.roomsFilename);
                Room currentRoom;
                try (FileReader fr = new FileReader(file);) {
                    currentRoom = rrw.read(fr, file);
                    if (!currentRoom.isBooked()) {
                        ArrayList<LocalDateTime> periodOfBooking = new ArrayList<>();
                        currentRoom.getBookingAvailability().put(false, periodOfBooking); // not currently available room
                        currentRoom.setBooked(true); // booked room
                        currentHotel.getBookedRooms().add(currentRoom.getId()); // adds to booked rooms

                        Reservation reservation = new Reservation(fromDate, toDate, cancellationFees, false);
                        reservation.getRooms().put(currentRoom.getId(), currentRoom.getTotalPrice());
                        reservation.setTotalPrice(reservation.calculateTotalPrice());
                        ReservationReaderWriter reservationRW = new ReservationReaderWriter();
                        reservationRW.write(reservation, HotelController.reservationsFilename);
                        System.out.printf("Congratulations! You've just booked room with ID: %d%n", currentRoom.getId());
                    } else {
                        System.out.println("Sorry but this room is not currently available!");
                    }
                } catch (IOException ex) {
                    ex.fillInStackTrace();
                }
            }
        }
    }

    @Override
    public void freeRooms(Hotel currentHotel) {
        LocalDateTime now = LocalDateTime.now();
        for (Integer currentRoomId : currentHotel.getBookedRooms()) {
            RoomReaderWriter rrw = new RoomReaderWriter();
            File file = new File(HotelController.roomsFilename);
            Room currentRoom;
            try (FileReader fr = new FileReader(file)) {
                currentRoom = rrw.read(fr, file);
                for (var currentStatus : currentRoom.getBookingAvailability().entrySet()) {
                    if (!currentStatus.getKey() && currentStatus.getValue().contains(now)) {
                        currentStatus.getValue().add(now); // available for this period
                        currentRoom.setBooked(false); // not booked room

                        // TODO: to think how to rewrite the info without loosing data
                    /*HotelReaderWriter hotelRW = new HotelReaderWriter();
                    hotelRW.write(currentHotel, HotelController.hotelsFilename);
                    RoomReaderWriter roomRW = new RoomReaderWriter();
                    roomRW.write(currentRoom, HotelController.roomsFilename);*/
                    }
                }
            } catch (IOException ex) {
                ex.fillInStackTrace();
            }
        }
    }

    @Override
    public void makePayment(Hotel currentHotel, Reservation reservation, double totalMoney, String IBAN) throws RuntimeException {
        double reservationTotalPrice = reservation.getTotalPrice();
        if(totalMoney < reservationTotalPrice) {
            throw new RuntimeException("Sorry, but you haven't enough money!");
        }

        double currentUserNewBalance = totalMoney - reservationTotalPrice;
        DebitCardReaderWriter dcrw = new DebitCardReaderWriter();
        DebitCard readCard;

        File file = new File(HotelController.debitCardsFilename);
        ArrayList<DebitCard> currentUserCards = new ArrayList<>();
        try(FileReader fr = new FileReader(file)) {
            readCard = dcrw.read(fr, file);
            if (readCard == null) {
                System.out.println("There is no read reservations.");
            } else {
                if (readCard.getOwner() == reservation.getBookedBy() && readCard.getIban().endsWith(IBAN)) {
                    currentUserCards.add(readCard);
                }
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }

        if(!currentUserCards.isEmpty()) {
            currentUserCards.getFirst().setBalance(currentUserNewBalance);
            double currentIncomes = currentHotel.getIncomes() + reservationTotalPrice;
            currentHotel.setIncomes(currentIncomes);
            System.out.printf("You successfully pay for reservation: %s%n", reservation);

            for(Integer currentRoom : reservation.getRooms().keySet()) {
                // make all rooms reserved
                Room readRoom;
                RoomReaderWriter rrw = new RoomReaderWriter();
                File roomsFile = new File(HotelController.roomsFilename);
                try(FileReader fr = new FileReader(roomsFile)) {
                    readRoom = rrw.read(fr, file);
                    if (readRoom == null) {
                        System.out.println("There is no read rooms.");
                    } else {
                        if(!readRoom.isBooked()) {
                            readRoom.setBooked(true);
                            for(var entry : readRoom.getBookingAvailability().entrySet()) {
                                if(!entry.getKey()) {
                                    // TODO: to add all date between startDate and EndDate in the list.
                                    entry.getValue().add(reservation.getFromDate());
                                    entry.getValue().add(reservation.getToDate());
                                }
                            }
                        }
                    }
                } catch (IOException ex) {
                    ex.fillInStackTrace();
                }
            }
        }
        double currentIncomes = currentHotel.getIncomes() + reservationTotalPrice;
        currentHotel.setIncomes(currentIncomes);
        System.out.printf("You successfully pay for reservation: %s%n", reservation);

        // TODO: to think how to rewrite the info without loosing data
        /*HotelReaderWriter hotelRW = new HotelReaderWriter();
        hotelRW.write(currentHotel, HotelController.hotelsFilename);
        ReservationReaderWriter reservationRW = new ReservationReaderWriter();
        reservationRW.write(reservation, HotelController.reservationsFilename);
        UserReaderWriter userRW = new UserReaderWriter();
        userRW.write(reservation.getBookedBy(), HotelController.usersFilename);*/
    }

    @Override
    public void cancelReservation(int reservationId, User currentUser) throws RuntimeException {
        LocalDateTime now = LocalDateTime.now();
        for(Map.Entry<Integer, Double> currentReservation : currentUser.getReservations().entrySet()) {
            if(currentReservation.getKey() == reservationId) {
                double totalPrice = currentReservation.getValue();
                double userTotalMoney = currentUser.getDebitCard().getValue();
                if(userTotalMoney < totalPrice) {
                    throw new RuntimeException("Sorry, you have enough money! You cannot cancel your reservation!");
                }

                ReservationReaderWriter rrw = new ReservationReaderWriter();
                Reservation readReservation;

                File file = new File(HotelController.reservationsFilename);
                try(FileReader fr = new FileReader(file)) {
                    readReservation = rrw.read(fr, file);
                    if (readReservation == null) {
                        System.out.println("There is no read reservations.");
                    } else {
                        for(Integer cr : readReservation.getRooms().keySet()) {
                            RoomReaderWriter roomrw = new RoomReaderWriter();
                            Room readRoom;

                            File roomsFile = new File(HotelController.roomsFilename);
                            try(FileReader roomfr = new FileReader(roomsFile)) {
                                readRoom = roomrw.read(roomfr, roomsFile);
                                if (readRoom == null) {
                                    System.out.println("There is no read rooms.");
                                } else {
                                    // TODO: to implement it
                                    /*for(Map.Entry<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> isAvailable : readRoom.getBookingAvailability().entrySet()) {
                                        if (readReservation.getId() == reservationId
                                                && readReservation.getBookedBy() == currentUser.getId()
                                                && isAvailable.getValue() == isAvailable.getValue()) {
                                            readRoom.setBooked(false);
                                            readReservation.setCancelled(true);
                                        }
                                    }*/
                                }
                            } catch (IOException ex) {
                                ex.fillInStackTrace();
                            }
                        }
                    }
                } catch (IOException ex) {
                    ex.fillInStackTrace();
                }

                // TODO: to think how to rewrite the info without loosing data
                /*HotelReaderWriter hotelRW = new HotelReaderWriter();
                hotelRW.write(currentHotel, HotelController.hotelsFilename);
                ReservationReaderWriter reservationRW = new ReservationReaderWriter();
                reservationRW.write(currentReservation, HotelController.reservationsFilename);
                UserReaderWriter userRW = new UserReaderWriter();
                userRW.write(currentReservation.getBookedBy(), HotelController.usersFilename);*/
            }
        }
    }
}