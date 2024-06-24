package controllers;

import interfaces.IHotelMangeable;
import models.Hotel;
import models.Reservation;
import models.User;

import java.time.LocalDateTime;

public class HotelController implements IHotelMangeable {

    @Override
    public void viewAllRooms(Hotel currentHotel) {

    }

    @Override
    public void recommendRooms(LocalDateTime fromDate, LocalDateTime toDate, int guestsCount, Hotel currentHotel) {

    }

    @Override
    public void bookRoom(int id, Hotel currentHotel) {

    }

    @Override
    public void freeRooms(Hotel currentHotel) {

    }

    @Override
    public void makePayment(User currentUser, Reservation reservation) {

    }

    @Override
    public void cancelReservation(int id, Hotel currentHotel) {

    }
}