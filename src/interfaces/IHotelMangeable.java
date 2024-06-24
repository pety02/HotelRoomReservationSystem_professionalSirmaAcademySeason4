package interfaces;

import models.Hotel;
import models.Reservation;
import models.User;

import java.time.LocalDateTime;

public interface IHotelMangeable {
    void viewAllRooms(Hotel currentHotel);
    void recommendRooms(LocalDateTime fromDate, LocalDateTime toDate, int guestsCount, Hotel currentHotel);
    void bookRoom(int id, Hotel currentHotel);
    void freeRooms(Hotel currentHotel);
    void makePayment(User currentUser, Reservation reservation);
    void cancelReservation(int id, Hotel currentHotel);
}