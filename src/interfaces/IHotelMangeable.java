package interfaces;

import models.Hotel;
import models.Reservation;
import models.User;

import java.time.LocalDateTime;

public interface IHotelMangeable {
    void viewAllRooms(Hotel currentHotel);
    void recommendRooms(LocalDateTime fromDate, LocalDateTime toDate, int guestsCount, Hotel currentHotel);
    void bookRoom(int id, Hotel currentHotel, LocalDateTime fromDate, LocalDateTime toDate, User bookedBy, double cancellationFees);
    void freeRooms(Hotel currentHotel);
    void cancelReservation(int reservationId, User currentUser, Hotel currentHotel) throws RuntimeException;
}