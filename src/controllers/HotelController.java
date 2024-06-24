package controllers;

import interfaces.IHotelMangeable;
import models.Hotel;
import models.Reservation;
import models.User;

import java.time.LocalDateTime;

public class HotelController implements IHotelMangeable {
    private Hotel hotel;

    public HotelController(Hotel hotel) {
        this.setHotel(hotel);
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public void viewAllRooms() {

    }

    @Override
    public void recommendRooms(LocalDateTime fromDate, LocalDateTime toDate, int guestsCount) {

    }

    @Override
    public void bookRoom(int id) {

    }

    @Override
    public void freeRooms() {

    }

    @Override
    public void makePayment(User currentUser, Reservation reservation) {

    }

    @Override
    public void cancelReservation(int id) {

    }
}