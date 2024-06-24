package interfaces;

import models.Reservation;
import models.User;

import java.time.LocalDateTime;

public interface IHotelMangeable {
    void viewAllRooms();
    void recommendRooms(LocalDateTime fromDate, LocalDateTime toDate, int guestsCount);
    void bookRoom(int id);
    void freeRooms();
    void makePayment(User currentUser, Reservation reservation);
    void cancelReservation(int id);
}