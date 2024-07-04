package interfaces;

import types.RoomType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public interface IAdminManageable {
    void showAllHotels();
    void viewAllBookings(int hotelId);
    double getTotalIncome(int hotelId) throws RuntimeException;
    double getCancellationFees(int hotelId);
    boolean addRoom(int roomId, int hotelId, RoomType type, ArrayList<String> amenities, int maximumOccupancy,
                    double pricePerNight);
    void showAllHotelRooms(int hotelId);
    boolean removeRoom(int roomId);
    boolean updateRoom(int roomId, int hotelId, RoomType type, ArrayList<String> amenities, int maximumOccupancy,
                       double pricePerNight);
}