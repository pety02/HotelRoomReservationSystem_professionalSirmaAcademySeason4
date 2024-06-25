package controllers;

import interfaces.IHotelMangeable;
import models.Hotel;
import models.Reservation;
import models.Room;
import models.User;
import readersWriters.HotelReaderWriter;
import readersWriters.ReservationReaderWriter;
import readersWriters.RoomReaderWriter;
import readersWriters.UserReaderWriter;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
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
        for(Room currentRoom : currentHotel.getAllRooms()) {
            System.out.printf("Room type: %s | Max persons: %d | Price per night (per person): %.2f$ | " +
                            "Total price per night: %.2f$%n", currentRoom.getType(),
                    currentRoom.getMaximumOccupancy(), currentRoom.getPricePerNight(),
                    currentRoom.getTotalPrice());
        }
    }

    @Override
    public void recommendRooms(LocalDateTime fromDate, LocalDateTime toDate, int guestsCount, Hotel currentHotel) {
        // TODO: to think period of stay calculations better
        int periodOfStay = toDate.getDayOfMonth() - fromDate.getDayOfMonth();
        if(periodOfStay < 0) {
            throw new IllegalArgumentException("Invalid dates of stay! The final date should be after the beginning date!");
        }

        for(Room currentRoom : currentHotel.getAllRooms()) {
            for(Map.Entry<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> isCurrentlyAvailable : currentRoom.isCurrentlyAvailable().entrySet()) {
                if (isCurrentlyAvailable.getKey().getKey().equals(fromDate)
                        && isCurrentlyAvailable.getKey().getValue().equals(toDate)
                        && isCurrentlyAvailable.getValue()) {
                    System.out.printf("ID: %d | Room type: %s | Max persons: %d | Price per night (per person): %.2f$ | " +
                                    "Total price per night: %.2f$ | Status: %b%n", currentRoom.getId(), currentRoom.getType(),
                            currentRoom.getMaximumOccupancy(), currentRoom.getPricePerNight(),
                            currentRoom.getTotalPrice(), isCurrentlyAvailable.getValue());
                }
            }
        }
    }

    @Override
    public void bookRoom(int id, Hotel currentHotel, LocalDateTime fromDate, LocalDateTime toDate, User bookedBy, double cancellationFees) {
        for (Room currentRoom : currentHotel.getAllRooms()) {
            if(currentRoom.getId() == id) {
                if(!currentRoom.isBooked()) {
                    Map.Entry<LocalDateTime, LocalDateTime> periodOfBooking = new AbstractMap.SimpleEntry<>(fromDate, toDate);
                    currentRoom.isCurrentlyAvailable().put(periodOfBooking, false); // not currently available room
                    currentRoom.setBooked(true); // booked room
                    currentHotel.getBookedRooms().add(currentRoom); // adds to booked rooms

                    HotelReaderWriter hotelRW = new HotelReaderWriter();
                    hotelRW.write(currentHotel, HotelController.hotelsFilename);
                    RoomReaderWriter roomRW = new RoomReaderWriter();
                    roomRW.write(currentRoom, HotelController.roomsFilename);
                    Reservation reservation = new Reservation(fromDate, toDate, new ArrayList<>(), bookedBy, cancellationFees, false);
                    reservation.getRooms().add(currentRoom);
                    ReservationReaderWriter reservationRW = new ReservationReaderWriter();
                    reservationRW.write(reservation, HotelController.reservationsFilename);
                    UserReaderWriter userRW = new UserReaderWriter();
                    userRW.write(bookedBy, HotelController.usersFilename);
                } else {
                    System.out.println("Sorry but this room is not currently available!");
                }
            }
        }
    }

    @Override
    public void freeRooms(Hotel currentHotel) {
        LocalDateTime now = LocalDateTime.now();
        for (Room currentRoom : currentHotel.getBookedRooms()) {
            for(Map.Entry<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> currentStatus : currentRoom.isCurrentlyAvailable().entrySet()) {
                if(currentStatus.getKey().getValue().isBefore(now)) {
                    currentStatus.setValue(true); // available for this period
                    currentRoom.setBooked(false); // not booked room

                    // TODO: to think how to rewrite the info without loosing data
                    /*HotelReaderWriter hotelRW = new HotelReaderWriter();
                    hotelRW.write(currentHotel, HotelController.hotelsFilename);
                    RoomReaderWriter roomRW = new RoomReaderWriter();
                    roomRW.write(currentRoom, HotelController.roomsFilename);*/
                }
            }
        }
    }

    @Override
    public void makePayment(Reservation reservation) throws RuntimeException {
        double reservedByTotalMoney = reservation.getBookedBy().getDebitCard().getBalance(),
                reservationTotalPrice = reservation.getTotalPrice();
        if(reservedByTotalMoney < reservationTotalPrice) {
            throw new RuntimeException("Sorry, but you haven't enough money!");
        }

        double currentUserNewBalance = reservedByTotalMoney - reservationTotalPrice;
        reservation.getBookedBy().getDebitCard().setBalance(currentUserNewBalance);
        Hotel currentHotel = reservation.getRooms().getFirst().getHotel();
        double currentIncomes = currentHotel.getIncomes() + reservationTotalPrice;
        currentHotel.setIncomes(currentIncomes);
        System.out.printf("You successfully pay for reservation: %s%n", reservation);

        for(Room currentRoom : reservation.getRooms()) {
            for (Map.Entry<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> currentStatus : currentRoom.isCurrentlyAvailable().entrySet()) {
                currentStatus.setValue(true); // available for this period
                currentRoom.setBooked(false); // not booked room
            }
        }

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
        for(Reservation currentReservation : currentUser.getReservations()) {
            if(currentReservation.getId() == reservationId) {
                double totalPrice = currentReservation.getCancellationFees();
                double userTotalMoney = currentUser.getDebitCard().getBalance();
                if(userTotalMoney < totalPrice) {
                    throw new RuntimeException("Sorry, you have enough money! You cannot cancel your reservation!");
                }

                currentReservation.setCancelled(true);
                for(Room currentRoom : currentReservation.getRooms()) {
                    currentRoom.setBooked(false); // not booked room
                    for(Map.Entry<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> isCurrentlyAvailable : currentRoom.isCurrentlyAvailable().entrySet()) {
                        if(isCurrentlyAvailable.getKey().getKey().equals(currentReservation.getFromDate())
                                && isCurrentlyAvailable.getKey().getValue().equals(currentReservation.getToDate())) {
                            isCurrentlyAvailable.setValue(true); // currently available room
                        }
                    }
                }
                Hotel currentHotel = currentReservation.getRooms().getFirst().getHotel();
                double currentIncomes = currentHotel.getIncomes() + totalPrice;
                currentHotel.setIncomes(currentIncomes);

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